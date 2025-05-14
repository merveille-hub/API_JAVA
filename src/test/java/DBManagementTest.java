import ma.ensa.db.DatabaseManager;
import ma.ensa.db.MySQLDatabaseManager;
import ma.ensa.util.DBConfig;
import ma.ensa.util.DBConfigLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DBManagementTest {
    private DatabaseManager dbManager;

    @BeforeEach
    public void setUp() {
        DBConfig config = DBConfigLoader.load("db.properties");
        dbManager = new MySQLDatabaseManager(config);
    }

    @Test
    public void testConnection() {
        assertDoesNotThrow(() -> {
            dbManager.connect();
            assertTrue(dbManager.isConnected(), "La connexion doit être établie");
        });
    }

    @Test
    public void testExecuteQuery() {
        dbManager.connect();

        List<Map<String, Object>> result = dbManager.executeQuery("SELECT * FROM users");

        assertNotNull(result);
        assertFalse(result.isEmpty(), "Le résultat ne doit pas être vide");

        Map<String, Object> firstRow = result.get(0);
        assertTrue(firstRow.containsKey("id"), "Le champ 'id' doit exister");
        assertTrue(firstRow.containsKey("nom"), "Le champ 'nom' doit exister");
        assertTrue(firstRow.containsKey("email"), "Le champ 'email' doit exister");
        assertTrue(firstRow.containsKey("age"), "Le champ 'age' doit exister");
        afficherPremiereLigne(firstRow);
        dbManager.disconnect();
    }

    public void afficherPremiereLigne(Map<String, Object> firstRow) {
        for (Object key : firstRow.keySet()) {
            System.out.println(key + ": " + firstRow.get(key));
        }
    }

    @Test
    public void testExecuteUpdate() {
        dbManager.connect();

        int rows = dbManager.executeUpdate("INSERT INTO users (nom, email, age) VALUES ('Test', 'test@example.com', 9)");
        assertEquals(1, rows, "Une ligne doit être insérée");

        dbManager.disconnect();
    }
}
