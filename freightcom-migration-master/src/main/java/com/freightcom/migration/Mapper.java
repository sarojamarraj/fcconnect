package com.freightcom.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.stereotype.Component;

@Component
public class Mapper
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final InputStreamReader mapStream;
    private final DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    private final Map<String, TableMap> tableMap = new HashMap<String, TableMap>();
    private final Map<String, TableMap> newTableMap = new HashMap<String, TableMap>();
    private final Map<String, String> keys = new HashMap<String, String>();

    public Mapper(InputStreamReader mapStream)
    {
        this.mapStream = mapStream;
    }

    public void load() throws IOException
    {
        String line;
        String tableName = null;
        String newTableName = null;
        TableMap map = null;
        TableMap newTable = null;
        BufferedReader in = new BufferedReader(mapStream);

        while ((line = in.readLine()) != null) {
            String[] fields = tokenizer.tokenize(line)
                    .getValues();

            if (newTable == null && fields.length == 3 && fields[0].equals("key")) {
                keys.put(fields[1], fields[2]);
            } else if (fields.length >= 3) {
                if (fields[0].equals("table")) {
                    tableName = fields[1];
                    newTableName = fields[2];
                    map = new TableMap(tableName, newTableName);
                    tableMap.put(tableName, map);

                    if (newTableMap.containsKey(newTableName)) {
                        newTable = newTableMap.get(newTableName);
                    } else {
                        newTable = new TableMap(tableName, newTableName);
                        newTableMap.put(newTableName, newTable);
                    }
                } else if (map != null) {
                    map.add(fields);
                    newTable.addNew(fields);
                }
            }
        }
    }

    public Set<String> getTableNames()
    {
        Set<String> filtered = new HashSet<String>();

        for(String name: tableMap.keySet()) {
            if (! (name.length() > 4 && name.substring(0, 4).equals("*new"))) {
                filtered.add(name);
            }
        }

        return filtered;
    }

    public Set<String> getNewTableNames()
    {
        return newTableMap.keySet();
    }

    public TableMap getTableMap(String tableName)
    {
        return tableMap.get(tableName);
    }

    public TableMap getNewTableMap(String tableName)
    {
        return newTableMap.get(tableName);
    }

    public String getNewSchema()
    {
        StringBuilder builder = new StringBuilder();

        for (String tableName : getNewTableNames()) {
            TableMap map = getNewTableMap(tableName);

            builder.append("DROP TABLE IF EXISTS `" + map.getMappedTableName() + "`;\n")
                    .append("CREATE TABLE `" + map.getMappedTableName() + "` (\n")
                    .append("id bigint(20) NOT NULL AUTO_INCREMENT,\n");

            for (String columnName : map.getColumnNames()) {
                FieldSpecification field = map.getColumnSpecification(columnName);

                if (!field.getMapped()
                        .equals("id")) {
                    builder.append("`")
                            .append(field.getMapped())
                            .append("` ")
                            .append(field.getNewType())
                            .append(",\n");
                }
            }

            builder.append("PRIMARY KEY(`id`)\n")
                    .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n");
        }

        log.info("SCHEMA\n" + builder);

        return builder.toString();
    }

    public String getKey(String columnName)
    {
        if (keys.containsKey(columnName)) {
            return keys.get(columnName);
        } else {
            return null;
        }
    }

}
