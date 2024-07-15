package balet.benjamin.cinephoria.model;

import com.google.gson.annotations.SerializedName;

public class IssueRequest {
    @SerializedName("issueId")
    public int issueId;
    @SerializedName("roomId")
    public int roomId;
    @SerializedName("title")
    public String title;
    @SerializedName("status")
    public String status;
    @SerializedName("description")
    public String description;

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
