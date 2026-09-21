package config;

public class DatabaseConfig {

    private static final String DB_HOST =
            System.getenv().getOrDefault("DB_HOST", "localhost");

    public static final String URL =
            "jdbc:postgresql://" + DB_HOST + ":5432/Hotil_Management";

    public static final String USER =
            "laa";

    public static final String PASSWORD =
            "laarif+osb2002";

    private DatabaseConfig() {
        // Prevent creating objects
    }
}