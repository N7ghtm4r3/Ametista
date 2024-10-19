package com.tecknobit.ametistacore.models.analytics.issues;

import com.tecknobit.ametistacore.models.Platform;
import org.json.JSONObject;

public class WebIssueAnalytic extends IssueAnalytic {

    private final String browser;

    private final String browserVersion;

    public WebIssueAnalytic() {
        this(null, -1, null, null, null, null, null, null);
    }

    public WebIssueAnalytic(String id, long creationDate, String appVersion, AmetistaDevice device, String issue, Platform platform, String browser, String browserVersion) {
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
