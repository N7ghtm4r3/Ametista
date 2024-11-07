package com.tecknobit.ametistacore.models.analytics.issues;

import com.tecknobit.ametistacore.models.AmetistaDevice;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tecknobit.ametistacore.models.AmetistaDevice.DEVICE_IDENTIFIER_KEY;
import static com.tecknobit.ametistacore.models.AmetistaDevice.DEVICE_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.ISSUE;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUES_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUE_KEY;

@Entity
@Table(name = ISSUES_KEY)
@DiscriminatorValue(ISSUE_KEY)
public class IssueAnalytic extends AmetistaAnalytic {

    public static final String ISSUES_KEY = "issues";

    public static final String ISSUE_KEY = "issue";

    public static final String DATE_FILTERS_KEY = "dates";

    public static final String VERSION_FILTERS_KEY = "versions";

    public static final String BRAND_FILTERS_KEY = "brands";

    public static final String MODEL_FILTERS_KEY = "models";

    @Column(
            name = ISSUE_KEY,
            columnDefinition = "MEDIUMTEXT"
    )
    protected final String issue;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = DEVICE_IDENTIFIER_KEY)
    protected final AmetistaDevice device;

    public IssueAnalytic() {
        this(null, null, -1, null, null, null, null);
    }

    public IssueAnalytic(String id, String name, long creationDate, String appVersion, AmetistaDevice device, String issue,
                         Platform platform) {
        super(id, name, creationDate, appVersion, ISSUE, platform);
        this.issue = issue;
        this.device = device;
        this.name = name;
    }

    public IssueAnalytic(JSONObject jIssue) {
        super(jIssue, ISSUE);
        issue = hItem.getString(ISSUE_KEY);
        device = new AmetistaDevice(hItem.getJSONObject(DEVICE_KEY));
    }

    // TODO: 07/11/2024 TO USE WHEN INSERT IN DATABASE
    private String getIssueName() {
        Pattern pattern = Pattern.compile("\\b(\\w+Exception)\\b");
        Matcher matcher = pattern.matcher(issue);
        if (matcher.find())
            return name = matcher.group(1);
        return name;
    }

    public String getIssue() {
        return issue;
    }

    public AmetistaDevice getDevice() {
        return device;
    }

}
