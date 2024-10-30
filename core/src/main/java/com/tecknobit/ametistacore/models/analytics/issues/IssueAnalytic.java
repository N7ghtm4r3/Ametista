package com.tecknobit.ametistacore.models.analytics.issues;

import com.tecknobit.ametistacore.models.AmetistaDevice;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.json.JSONObject;

import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.ISSUE;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUES_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUE_KEY;

@Entity
@Table(name = ISSUES_KEY)
@DiscriminatorValue(ISSUE_KEY)
public class IssueAnalytic extends AmetistaAnalytic {

    public static final String ISSUES_KEY = "issues";

    public static final String ISSUE_KEY = "issue";

    @Column(
            name = ISSUE_KEY,
            columnDefinition = "MEDIUMTEXT"
    )
    protected final String issue;

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
