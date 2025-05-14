package ma.ensa.db;

import ma.ensa.util.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgreSQLDatabaseManager implements DatabaseManager{
    private Connection connection;
    private DBConfig config;

    public PostgreSQLDatabaseManager(DBConfig config) {
        this.config = config;
    }

    @Override
    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );
            System.out.println("Connexion PostgreSQL réussie.");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC PostgreSQL non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion PostgreSQL : " + e.getMessage());
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
