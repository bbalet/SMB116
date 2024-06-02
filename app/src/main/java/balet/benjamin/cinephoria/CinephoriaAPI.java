package balet.benjamin.cinephoria;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CinephoriaAPI {
    @GET("api/theaters")
    Call<TheaterResponse> getTheaters(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("api/theaters")
    Call<TheaterResponse> getTheaters();

}
