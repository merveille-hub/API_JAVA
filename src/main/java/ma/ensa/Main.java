package ma.ensa;

import ma.ensa.db.DatabaseManager;
import ma.ensa.db.MySQLDatabaseManager;
import ma.ensa.db.PostgreSQLDatabaseManager;
import ma.ensa.db.SQLServerDatabaseManager;
import ma.ensa.util.DBConfig;
import ma.ensa.util.DBConfigLoader;

import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        String fichierConfig;
        //fichierConfig = "db.properties";
        fichierConfig = "dbPostgreSql.properties";
        //fichierConfig = "dbSqlServer.properties";

        DBConfig config = DBConfigLoader.load(fichierConfig);
        DatabaseManager dbManager;

        switch (config.getType().toLowerCase()) {
            case "mysql":
                dbManager = new MySQLDatabaseManager(config);
                break;
            case "postgresql":
                dbManager = new PostgreSQLDatabaseManager(config);
                break;
            case "sqlserver":
                dbManager = new SQLServerDatabaseManager(config);
                break;
            default:
                throw new IllegalArgumentException("Unsupported DB type");
        }

        dbManager.connect();
        List<Map<String, Object>> result = dbManager.executeQuery("SELECT * FROM users");
        result.forEach(System.out::println);
        dbManager.disconnect();

    }
}