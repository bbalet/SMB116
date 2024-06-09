package balet.benjamin.cinephoria.model;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {
    @SerializedName("firstName")
    public String firstname;
    @SerializedName("lastName")
    public String lastname;
    @SerializedName("role")
    public String role;
}
