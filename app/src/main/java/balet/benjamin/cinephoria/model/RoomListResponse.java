package balet.benjamin.cinephoria.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomListResponse {
    @SerializedName("hydra:member")
    public List<RoomResponse> roomResponses;

    public List<RoomResponse> getRooms() {
        return roomResponses;
    }

    public void setRooms(List<RoomResponse> roomResponses) {
        this.roomResponses = roomResponses;
    }
}
