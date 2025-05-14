package ma.ensa.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseManager {

    void connect(); // Connexion à la BD
    void disconnect(); // Déconnexion

    List<Map<String, Object>> executeQuery(String sql); // SELECT
    int executeUpdate(String sql); // INSERT, UPDATE, DELETE

    boolean isConnected() throws SQLException; // Vérifie la connexion

    public static Connection connect(DBType type, String host, int port, String dbName, String user, String password) throws SQLException {

        if (type == null || host == null || dbName == null || user == null || password == null) {

            return null;
        }

        String url = "";

        switch (type) {
            case MYSQL:
                url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
                break;
            case POSTGRESQL:
                url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
                break;
            case SQLSERVER:
                url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbName;
                break;
            case ORACLE:
                url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
                break;
            default:
                throw new IllegalArgumentException("Type de base de données non supporté.");
        }
        return DriverManager.getConnection(url, user, password);
    }
}

