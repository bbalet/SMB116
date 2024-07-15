package balet.benjamin.cinephoria.model;

import com.google.gson.annotations.SerializedName;

/**
 * Corps de la réponse de la demande de détails sur l'utilisateur
 */
public class UserInfoResponse {
    @SerializedName("firstName")
    public String firstname;
    @SerializedName("lastName")
    public String lastname;
    @SerializedName("role")
    public String role;
}
