package ma.ensa.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfigLoader {

    public static DBConfig load(String filePath) {
        DBConfig config = new DBConfig();
        Properties props = new Properties();

        try (InputStream input = DBConfigLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (input == null) {
                throw new IOException("Fichier " + filePath + " introuvable !");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try  {

            config.setType(props.getProperty("db.type"));
            config.setUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            config.setHost(props.getProperty("db.host"));
            config.setPort(props.getProperty("db.port"));
            config.setName(props.getProperty("db.name"));
        } catch (Exception e) {
            e.getMessage();
        }

        return config;
    }

    /**utilisée avec les paramètres
     * @param getProperty donne la propriété recherche dans le fichier
     * @param filename
     * @param getProperty peut avoir les valeurs suivantes :
     *      db.type           : type de base de données exple : SQLSERVER, MYSQL, POSTGRESQL
     *      db.host           : nom de l'hébergeur exple : localhost
     *      db.port           : numéro de port exple : 3306, 1433
     *      db.name           : nom de la base de données exple : ma_bd
     *      db.user           : nom de l'utilisateur de la base de données exple : sa, root
     *      db.password       : mot de passe de la base de données
     * */
    public static String get(String getProperty, String filename) throws IOException {
        Properties props = new Properties();
        props.load(DBConfigLoader.class.getClassLoader().getResourceAsStream(filename));
        return props.getProperty(getProperty);
    }

}
