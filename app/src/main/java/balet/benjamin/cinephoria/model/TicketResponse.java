package balet.benjamin.cinephoria.model;

import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class TicketResponse {
    @SerializedName("ticketId")
    public String ticketId;
    @SerializedName("imdbId")
    public String imdbId;
    @SerializedName("movieTitle")
    public String movieTitle;
    @SerializedName("day")
    public String day;
    @SerializedName("roomNumber")
    public String roomNumber;
    @SerializedName("startDate")
    public Date startDate;
    @SerializedName("endDate")
    public Date endDate;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
