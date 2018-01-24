package com.freightcom.migration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.ResultSetMetaData;

@Component
public class MigrationService
{

    private final static Logger log = LoggerFactory.getLogger(MigrationService.class);

    private final JdbcTemplate destinationTemplate;
    private final JdbcTemplate sourceTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Mapper mapper;
    private final List<String> foreign_keys = new ArrayList<String>();
    private final DataSource destinationDataSource;

    public MigrationService(JdbcTemplate sourceTemplate, JdbcTemplate destinationTemplate, Mapper mapper,
            BCryptPasswordEncoder passwordEncoder,
                            DataSource secondaryDataSource )
    {
        this.destinationTemplate = destinationTemplate;
        this.sourceTemplate = sourceTemplate;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.destinationDataSource = secondaryDataSource;
    }

    @Transactional
    public void migrate() throws Exception
    {
        int i = 0;

        for (String tableName : mapper.getTableNames()) {
            i++;
            log.info("COPY TABLE " + i + " " + tableName);
            buildForeignKeys(tableName, mapper.getTableMap(tableName));
            copy(tableName, mapper.getTableMap(tableName));
        }

        for (@SuppressWarnings("unused") String addKeyCommand: foreign_keys) {
            // destinationTemplate.execute(addKeyCommand);
        }
    }

    private void buildForeignKeys(final String tableName, final TableMap map)
    {
        for (FieldSpecification specification : map.getFields()) {
            String foreignClass = mapper.getKey(specification.getMapped());

            if (foreignClass != null) {
                foreign_keys.add("alter table `" + map.getMappedTableName() + "` add foreign key (`" + specification.getMapped() + "`) references `" + foreignClass + "` (id)" );
            }
        }
    }

    private String buildQuery(final String tableName, final TableMap map, final int offset, final int page_size)
    {
        StringBuilder query = new StringBuilder("select ");
        int count = 0;

        for (FieldSpecification specification : map.getFields()) {
            if (specification.getColumnName()
                .length() < 4
                || !specification.getColumnName()
                .substring(0, 4)
                .equals("*new")) {
                if (count > 0) {
                    query.append(",");
                }

                if (specification.hasValueExpression()) {
                    query.append(specification.getValueExpression())
                        .append(" as '")
                        .append(specification.getColumnName())
                        .append("'");
                } else {
                    query.append("`")
                        .append(specification.getColumnName())
                        .append("`");
                }

                count++;
            }
        }

        return query.append(" from `")
            .append(tableName)
            .append("`")
            .append(" limit ")
            .append(offset)
            .append(",")
            .append(page_size)
            .toString();
    }

    public void copy(final String tableName, final TableMap map) throws Exception
    {
        final RowData data = new RowData();
        final ResultSetMetaData[] metaData = new ResultSetMetaData[1];
        final int page_size = 100000;


        for (int offset = 0; data.live; offset += page_size) {
            final String query = buildQuery(tableName, map, offset, page_size);
            log.info("COPY QUERY " + query);

            data.live = false;
            metaData[0] = null;
            data.rows = new ArrayList<Map<String, Object>>();

            sourceTemplate.setFetchSize(100);
            sourceTemplate.query(query, resultSet -> {
                    Map<String, Object> item = new HashMap<String, Object>();

                    if (metaData[0] == null) {
                        metaData[0] = (ResultSetMetaData) resultSet.getMetaData();
                    }

                    for (int column = 1; column <= metaData[0].getColumnCount(); column++) {
                        String columnName = metaData[0].getColumnName(column);
                        FieldSpecification specification = map.getColumnSpecification(columnName);

                        if (specification.specialIs("bcrypt")) {
                            String fieldValue = resultSet.getString(columnName);

                            if (fieldValue == null) {
                                item.put(columnName, null);
                            } else {
                                item.put(columnName, passwordEncoder.encode(fieldValue));
                            }
                        } else {
                            switch (metaData[0].getColumnType(column)) {
                            case java.sql.Types.BIT:
                                item.put(columnName, resultSet.getBoolean(columnName));
                                break;

                            case java.sql.Types.BIGINT:
                                item.put(columnName, resultSet.getLong(columnName));
                                break;

                            case java.sql.Types.INTEGER:
                            case java.sql.Types.SMALLINT:
                                item.put(columnName, resultSet.getInt(columnName));
                                break;

                            case java.sql.Types.DECIMAL:
                                item.put(columnName, resultSet.getBigDecimal(columnName));
                                break;

                            case java.sql.Types.REAL:
                            case java.sql.Types.DOUBLE:
                            case java.sql.Types.FLOAT:
                                item.put(columnName, resultSet.getDouble(columnName));
                                break;

                            default:
                                try {
                                    item.put(columnName, resultSet.getString(columnName));
                                } catch (Exception e) {
                                    item.put(columnName, null);
                                }
                                break;
                            }
                        }
                    }

                    data.rows.add(item);

                    if (data.rows.size() > 10000) {
                        try {
                            log.info("INSERT BATCH " + tableName);
                            insertRows(tableName, map, data.rows, metaData[0]);
                        } catch (Exception e) {
                            log.error("INSERT ERROR " + tableName);
                            e.printStackTrace();
                        }

                        data.rows = new ArrayList<Map<String, Object>>();
                        data.live = true;
                    }
                });

            if (data.rows.size() > 0) {
                try {
                    log.info("FINAL INSERT " + tableName);
                    insertRows(tableName, map, data.rows, metaData[0]);
                    data.live = true;
                } catch (Exception e) {
                    log.error("INSERT ERROR " + tableName);
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertRows(final String tableName, final TableMap map, final List<Map<String, Object>> rows,
            final ResultSetMetaData metaData) throws Exception
    {
        StringBuilder query = new StringBuilder("insert into `").append(map.getMappedTableName())
                .append("` (");
        StringBuilder valueList = new StringBuilder();
        StringBuilder updates = new StringBuilder();
        int idColumn = 0;

        for (int column = 1; column <= metaData.getColumnCount(); column++) {
            String columnName = metaData.getColumnName(column);
            FieldSpecification specification = map.getColumnSpecification(columnName);

            log.info("BUILD QUERY " + map + " " + specification + " " + metaData.getColumnTypeName(column));

            if (column > 1) {
                query.append(",");
                valueList.append(",");
            }

            if (specification.getMapped()
                    .equals("id")) {
                idColumn = column;
            } else {
                if (updates.length() > 0) {
                    updates.append(",");
                }

                updates.append("`")
                        .append(specification.getMapped())
                        .append("` = ?");
            }

            query.append("`")
                    .append(specification.getMapped())
                    .append("`");

            valueList.append("?");
        }

        final int checkIdColumn = idColumn;

        query.append(") VALUES (")
                .append(valueList)
                .append(")")
                .append(" ON DUPLICATE KEY UPDATE ")
                .append(updates);

        log.info("USING QUERY " + query);

        destinationTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                int index = 1;

                for (int column = 1; column <= metaData.getColumnCount(); column++) {
                    String columnName = metaData.getColumnName(column);

                    if (column == checkIdColumn && rows.get(i)
                            .get(columnName)
                            .toString()
                            .equals("0")) {
                        ps.setInt(index, -1);
                    } else {
                        ps.setObject(index, rows.get(i)
                                .get(columnName));
                    }

                    index++;
                }

                for (int column = 1; column <= metaData.getColumnCount(); column++) {
                    String columnName = metaData.getColumnName(column);

                    if (column != checkIdColumn) {
                        ps.setObject(index, rows.get(i)
                                .get(columnName));

                        index++;
                    }
                }
            }

            @Override
            public int getBatchSize()
            {
                return rows.size();
            }
        });
    }

    public List<String> findAllBookings()
    {
        return destinationTemplate.query("select name from customer", (rs, rowNum) -> rs.getString("name"));
    }

    public void runScripts(String[] fileNames) throws Exception
    {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

        for (String fileName: fileNames) {
            populator.addScript(new InputStreamResource((new ClassPathResource(fileName)).getInputStream()));
        }

        populator.populate(destinationDataSource.getConnection());
    }

    public void executeFile(String fileName) throws Exception
    {
        @SuppressWarnings("unused")
        String[] lines = readFile(fileName);
    }

    public String[] readFile(String fileName) throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader()
                              .getResource(fileName).toURI());

        StringBuilder data = new StringBuilder();
        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> data.append(line).append("\n"));
        lines.close();

        return data.toString().split(";");
    }

    private class RowData
    {
        public List<Map<String, Object>> rows;
        public boolean live = true;
    }
}
