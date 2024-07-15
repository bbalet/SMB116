package balet.benjamin.cinephoria.model;

import com.google.gson.annotations.SerializedName;

public class RoomResponse {
    @SerializedName("roomId")
    public int roomId;
    @SerializedName("number")
    public String number;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
