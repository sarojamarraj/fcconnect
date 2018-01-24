package com.freightcom.migration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class Datasources
{

    @Value("classpath:new-schema.sql")
    private Resource schemaScript;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.old")
    public DataSource sourceDataSource()
    {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public JdbcTemplate sourceTemplate(@Qualifier("sourceDataSource") final DataSource sourceDataSource)
    {
        return new JdbcTemplate(sourceDataSource);
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.q")
    public DataSource secondaryDataSource()
    {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public PlatformTransactionManager txManager()
    {
        return new DataSourceTransactionManager(secondaryDataSource());
    }

    @Bean
    public JdbcTemplate destinationTemplate(@Qualifier("secondaryDataSource") final DataSource secondaryDataSource)
    {
        return new JdbcTemplate(secondaryDataSource);
    }

    @Bean
    public DataSourceInitializer secondaryDataSourceInitializer(
            @Qualifier("secondaryDataSource") final DataSource secondaryDataSource, final Mapper mapper)
            throws IOException
    {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(secondaryDataSource);
        initializer
                .setDatabasePopulator(databasePopulator(mapper, (new KeyDropper(secondaryDataSource)).getCommands()));

        return initializer;
    }

    private DatabasePopulator databasePopulator(final Mapper mapper, final StringBuilder dropCommands)
            throws IOException
    {
        mapper.load();
        final String commands = new StringBuilder(dropCommands).append(mapper.getNewSchema())
                .toString();
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                new ByteArrayResource(commands.getBytes(StandardCharsets.UTF_8)));
        return populator;
    }

    @Bean
    public InputStreamReader mapFile() throws IOException
    {
        return new InputStreamReader((new ClassPathResource("tableMap.csv")).getInputStream());
    }

    @Bean
    public Mapper mapper(InputStreamReader mapStream)
    {
        return new Mapper(mapStream);
    }

}
