package ma.ensa.db;

import ma.ensa.util.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLServerDatabaseManager implements DatabaseManager {
    private Connection connection;
    private DBConfig config;

    public SQLServerDatabaseManager(DBConfig config) {
        this.config = config;
    }

    @Override
    public void connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );
            System.out.println("Connexion SQL Server réussie.");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC SQL Server non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion SQL Server : " + e.getMessage());
        }
    }


    @Override
    public void disconnect() {
        // Ferme la connexion si elle est ouverte
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql) {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String column = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    row.put(column, value);
                }

                results.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
        }

        return results;
    }

    @Override
    public int executeUpdate(String sql) {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }
}
