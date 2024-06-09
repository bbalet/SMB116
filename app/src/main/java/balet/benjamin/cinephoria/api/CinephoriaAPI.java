package balet.benjamin.cinephoria.api;

import balet.benjamin.cinephoria.model.LoginResponse;
import balet.benjamin.cinephoria.model.TicketListResponse;
import balet.benjamin.cinephoria.model.TicketResponse;
import balet.benjamin.cinephoria.model.UserInfoResponse;
import balet.benjamin.cinephoria.model.TheaterResponse;
import balet.benjamin.cinephoria.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CinephoriaAPI {
    @GET("api/theaters")
    Call<TheaterResponse> getTheaters(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("api/theaters")
    Call<TheaterResponse> getTheaters();

    @GET("api/ticket")
    Call<TicketResponse> getTicket(@Header("Authorization") String token, @Query("ticketId") int ticketId);

    @GET("api/tickets")
    Call<TicketListResponse> getTickets(@Header("Authorization") String token);

    @GET("api/whoami")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String token);

    @POST("auth")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
