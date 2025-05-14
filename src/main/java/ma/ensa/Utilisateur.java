package ma.ensa;

import lombok.Data;

@Data
public class Utilisateur {
    private final int id;
    private String nom;
    private String email;
    private int age;
}
