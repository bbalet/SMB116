package balet.benjamin.cinephoria.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TheaterListResponse {
    @SerializedName("hydra:member")
    public List<TheaterResponse> theaterResponses;

    public List<TheaterResponse> getTheaters() {
        return theaterResponses;
    }

    public void setTheaters(List<TheaterResponse> theaterResponses) {
        this.theaterResponses = theaterResponses;
    }
}
