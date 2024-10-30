package com.tecknobit.ametistacore.models.analytics.issues;

import com.tecknobit.ametistacore.models.AmetistaDevice;
import com.tecknobit.ametistacore.models.Platform;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.json.JSONObject;

import static com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic.WEB_ISSUES_KEY;
import static com.tecknobit.ametistacore.models.analytics.issues.WebIssueAnalytic.WEB_ISSUE_KEY;

@Entity
@Table(name = WEB_ISSUES_KEY)
@DiscriminatorValue(WEB_ISSUE_KEY)
public class WebIssueAnalytic extends IssueAnalytic {

    public static final String WEB_ISSUES_KEY = "web_issues";

    public static final String WEB_ISSUE_KEY = "web_issue";

    public static final String BROWSER_KEY = "browser";

    public static final String BROWSER_VERSION_KEY = "browser_version";

    @Column(name = BROWSER_KEY)
    private final String browser;

    @Column(name = BROWSER_VERSION_KEY)
    private final String browserVersion;

    public WebIssueAnalytic() {
        this(null, -1, null, null, null, null, null, null);
    }

    public WebIssueAnalytic(String id, long creationDate, String appVersion, AmetistaDevice device, String issue,
                            Platform platform, String browser, String browserVersion) {
        super(id, creationDate, appVersion, device, issue, platform);
        this.browser = browser;
        this.browserVersion = browserVersion;
    }

    public WebIssueAnalytic(JSONObject jWebIssue) {
        super(jWebIssue);
        // TODO: 19/10/2024 TO INIT CORRECTLY
        browser = null;
        browserVersion = null;
    }

    public String getBrowser() {
        return browser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

}
