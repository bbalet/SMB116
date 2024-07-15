package balet.benjamin.cinephoria.model;

/**
 * Corps de requête pour la connexion
 */
public class LoginRequest {
    /**
     * Email (login)
     */
    public String email;
    /**
     * Mot de passe
     */
    public String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
