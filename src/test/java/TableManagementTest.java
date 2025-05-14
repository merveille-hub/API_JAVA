import ma.ensa.db.DatabaseManager;
import ma.ensa.db.MySQLDatabaseManager;
import ma.ensa.db.PostgreSQLDatabaseManager;
import ma.ensa.db.SQLServerDatabaseManager;
import ma.ensa.util.DBConfig;
import ma.ensa.util.DBConfigLoader;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TableManagementTest {

    private static DatabaseManager dbManager;

    @BeforeAll
    public static void init() {
        DBConfig config = DBConfigLoader.load("db.properties");
        dbManager = switch (config.getType().toUpperCase()) {
            case "MYSQL" -> new MySQLDatabaseManager(config);
            case "POSTGRESQL" -> new PostgreSQLDatabaseManager(config);
            case "SQLSERVER" -> new SQLServerDatabaseManager(config);
            default -> throw new RuntimeException("Type SGBD non supporté");
        };
        dbManager.connect();
    }

    @Test
    @Order(1)
    public void testCreateTable() {
        String createSql = """
            CREATE TABLE test_table (
                id INT PRIMARY KEY,
                name VARCHAR(100)
            )
            """;

        int result = dbManager.executeUpdate(createSql);

        // Pour CREATE TABLE, executeUpdate retourne 0 en général si succès
        assertEquals(0, result, "La table doit être créée sans erreur");

        // Vérifie que l'on peut exécuter une requête SELECT dessus
        List<Map<String, Object>> rows = dbManager.executeQuery("SELECT * FROM test_table");
        assertNotNull(rows, "La table doit exister et permettre une requête SELECT");
    }

    @Test
    @Order(2)
    public void testDropTable() {
        String dropSql = "DROP TABLE test_table";
        int result = dbManager.executeUpdate(dropSql);
        assertEquals(0, result, "La table doit être supprimée sans erreur");

        // Vérifie que SELECT * FROM test_table échoue
        Exception exception = assertThrows(RuntimeException.class, () -> {
            dbManager.executeQuery("SELECT * FROM test_table");
        });

        String msg = exception.getMessage().toLowerCase();
        assertTrue(msg.contains("test_table") || msg.contains("does not exist") || msg.contains("unknown table"),
                "L'erreur doit indiquer que la table n'existe plus");
    }

    @AfterAll
    public static void cleanUp() {
        try {
            if (dbManager != null) {
                dbManager.disconnect();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}
