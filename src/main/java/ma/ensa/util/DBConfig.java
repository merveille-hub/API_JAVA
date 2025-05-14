package ma.ensa.util;

import lombok.Data;

@Data
public class DBConfig {
    private String type;
    private String host;
    private String port;
    private String url;
    private String username;
    private String password;
    private String name;

}
