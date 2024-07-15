package balet.benjamin.cinephoria.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IssueListResponse {
    @SerializedName("hydra:member")
    public List<IssueResponse> issueResponses;

    public List<IssueResponse> getIssues() {
        return issueResponses;
    }

    public void setIssues(List<IssueResponse> issueResponses) {
        this.issueResponses = issueResponses;
    }
}
