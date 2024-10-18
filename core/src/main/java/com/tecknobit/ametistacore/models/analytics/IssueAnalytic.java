package com.tecknobit.ametistacore.models.analytics;

import org.json.JSONObject;

public class IssueAnalytic extends AmetistaAnalytic {

    private final String issue;

    public IssueAnalytic() {
        this(null, -1, null, null, null);
    }

    public IssueAnalytic(String id, long creationDate, String appVersion, AmetistaDevice device, String issue) {
        super(id, null, creationDate, appVersion, device);
        this.issue = issue;
        setIssueName();
    }

    public IssueAnalytic(JSONObject jIssue) {
        super(jIssue);
        // TODO: 18/10/2024 TO INIT CORRECTLY
        issue = null;
        setIssueName();
    }

    private void setIssueName() {
        // TODO: 18/10/2024 TO INIT CORRECTLY
        name = "toInit";
    }

    public String getIssue() {
        return issue;
    }

}
