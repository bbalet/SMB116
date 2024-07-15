package balet.benjamin.cinephoria.api;

import balet.benjamin.cinephoria.model.IssueRequest;
import balet.benjamin.cinephoria.model.IssueResponse;
import balet.benjamin.cinephoria.model.LoginResponse;
import balet.benjamin.cinephoria.model.RoomListResponse;
import balet.benjamin.cinephoria.model.TicketDetailResponse;
import balet.benjamin.cinephoria.model.TicketListResponse;
import balet.benjamin.cinephoria.model.UserInfoResponse;
import balet.benjamin.cinephoria.model.TheaterListResponse;
import balet.benjamin.cinephoria.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CinephoriaAPI {
    /**
     * Obtenir la liste des cinémas proches des coordonnées GPS données
     * @param latitude
     * @param longitude
     * @return
     */
    //@Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("api/theaters")
    Call<TheaterListResponse> getTheaters(@Query("latitude") double latitude, @Query("longitude") double longitude);

    /**
     * Obtenir la liste de tous les cinémas
     * @return
     */
    @GET("api/theaters")
    Call<TheaterListResponse> getTheaters();

    /**
     * Obtenir la liste des salles d'un cinéma
     * @param token JWT
     * @param theaterId identifiant du cinéma
     * @return
     */
    //@Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("api/rooms")
    Call<RoomListResponse> getRooms(@Header("Authorization") String token, @Query("theaterId") int theaterId);

    /**
     * Obtenir la liste des problèmes liés à une salle
     * @param token JWT
     * @param roomId identifiant de la salle
     * @return
     */
    //@Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("api/issues")
    Call<IssueResponse> getIssues(@Header("Authorization") String token, @Query("roomId") int roomId);

    /**
     * Obtenir un problème depuis son identifiant
     * @param token
     * @param issueId
     * @return
     */
    //@Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("api/issues/{issueId}")
    Call<IssueResponse> getIssue(@Header("Authorization") String token, @Path("issueId") int issueId);

    /**
     * Créer un nouveau problème
     * @param token JWT
     * @param issue problème à créer (en lien avec une salle)
     * @return Problème créé (avec son nouvel identifiant)
     */
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("api/issues")
    Call<IssueResponse> createIssue(@Header("Authorization") String token, @Body IssueRequest issue);

    /**
     * Modifier un problème
     * @param token JWT
     * @param roomId identifiant de la salle
     * @param issue problème à modifier
     * @return Problème modifié
     */
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PATCH("api/issues/{roomId}")
    Call<IssueResponse> updateIssue(@Header("Authorization") String token, @Path("roomId") int roomId, @Body IssueRequest issue);

    /**
     * Obtenir les détails d'un ticket (dont son QR code)
     * @param token JWT
     * @param ticketId identifiant obfuscé du ticket
     * @return
     */
    @GET("api/tickets/{ticketId}")
    Call<TicketDetailResponse> getTicket(@Header("Authorization") String token, @Path("ticketId") String ticketId);

    /**
     * Obtenir la liste des tickets d'un utilisateur
     * @param token JWT
     * @return
     */
    @GET("api/tickets")
    Call<TicketListResponse> getTickets(@Header("Authorization") String token);

    /**
     * Obtenir les détails de l'utilisateur connecté
     * @param token JWT
     * @return
     */
    @GET("api/whoami")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String token);

    /**
     * Se connecter à l'API
     * @param loginRequest JSON contenant l'identifiant et le mot de passe
     * @return
     */
    @POST("auth")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
