package balet.benjamin.cinephoria.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JWTTokenUtils {

    /**
     * Méthode pour vérifier si un token JWT a expiré ou si il est invalide
     * Return true si le token est invalide, null, vide, ou expiré
     * @param token
     * @return true si le token est valide et qu'il n'a pas expiré
     */
    public static boolean isTokenExpired(String token) {
        if(token != null && !token.trim().isEmpty()) {
            try {
                DecodedJWT jwt = JWT.decode(token);
                Date expiration = jwt.getExpiresAt();
                return expiration.before(new Date());
            } catch (Exception e) {
                e.printStackTrace();
                return true; // Si une exception est levée, on considère que le token est expiré ou invalide
            }
        } else {
            return true; // Si le token est null ou vide, on considère que le token est expiré ou invalide
        }
    }
}
