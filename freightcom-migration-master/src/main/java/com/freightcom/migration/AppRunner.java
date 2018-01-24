package com.freightcom.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class AppRunner implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final MigrationService migrationService;

    public AppRunner(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @Override
    public void run(String... args) throws Exception
    {
        if (args.length == 0) {
            logger.info("RUNNING COPY");

            migrationService.migrate();

            String[] migrations = new String[] {
                "migrations/migrations0.sql",
                "migrations/migrations1.sql",
                "migrations/migrations2.sql",
                "migrations/migrations3.sql",
                "migrations/migrations4.sql",
                "migrations/migrations5.sql",
                "migrations/migrations6.sql",
                "migrations/migrations7.sql",
                "migrations/migrations8.sql",
                "migrations/migrations9.sql",
                "migrations/migrations10.sql",
                "migrations/migrations11.sql",
                "migrations/create-payables.sql",
                "migrations/fix-missing-customer.sql"
            };

            migrationService.runScripts(migrations);
        } else {
            logger.info("RUNNING ARGS " + String.join(", ",args));
            migrationService.runScripts(args)  ;
        }

        logger.info("\n");
        logger.info("DONE");
    }
}
