package config;

import org.flywaydb.core.Flyway;

public class DatabaseMigration {

    public static void migrate() {

        Flyway flyway = Flyway.configure()
                .dataSource(
                        DatabaseConfig.URL,
                        DatabaseConfig.USER,
                        DatabaseConfig.PASSWORD
                )
                .load();

        flyway.migrate();

        System.out.println("Database migration completed!");
    }
}