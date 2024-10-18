package com.tecknobit.ametistacore.models.analytics;

import com.tecknobit.ametistacore.models.Platform;
import org.json.JSONObject;

import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.ISSUE;

public class IssueAnalytic extends AmetistaAnalytic {

    private final String issue;

    public IssueAnalytic() {
        this(null, -1, null, null, null, null);
    }

    public IssueAnalytic(String id, long creationDate, String appVersion, AmetistaDevice device, String issue,
                         Platform platform) {
        super(id, null, creationDate, appVersion, ISSUE, device, platform);
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
