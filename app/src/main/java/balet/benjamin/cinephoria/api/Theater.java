package balet.benjamin.cinephoria.api;

import com.google.gson.annotations.SerializedName;

public class Theater {
    @SerializedName("theaterId")
    public int theaterId;
    @SerializedName("city")
    public String city;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
}
