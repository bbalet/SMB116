package balet.benjamin.cinephoria.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TicketListResponse {
    @SerializedName("hydra:member")
    public List<TicketResponse> tickets;

    public List<TicketResponse> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketResponse> tickets) {
        this.tickets = tickets;
    }
}
