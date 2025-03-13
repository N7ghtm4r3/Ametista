package com.tecknobit.ametista.services.collector.entities.issues;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.tecknobit.ametista.services.applications.entities.AmetistaDevice;
import com.tecknobit.ametista.services.collector.entities.AmetistaAnalytic;
import com.tecknobit.ametista.shared.data.AmetistaItem;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.json.JSONObject;

import static com.tecknobit.ametistacore.ConstantsKt.BROWSER_KEY;
import static com.tecknobit.ametistacore.ConstantsKt.BROWSER_VERSION_KEY;

/**
 * The {@code IssueAnalytic} class is useful to represent an issue occurred in a web environment
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see IssueAnalytic
 * @see AmetistaAnalytic
 * @see AmetistaItem
 * @see EquinoxItem
 */
@Entity
@Table(name = WebIssueAnalytic.WEB_ISSUES_KEY)
@DiscriminatorValue(WebIssueAnalytic.WEB_ISSUE_KEY)
public class WebIssueAnalytic extends IssueAnalytic {

    /**
     * {@code browser} the browser where the issue occurred
     */
    @Column(name = BROWSER_KEY)
    private final String browser;

    /**
     * {@code browser} the version of the browser
     */
    @Column(name = BROWSER_VERSION_KEY)
    private final String browserVersion;

    /**
     * Constructor to init the {@link WebIssueAnalytic} class
     *
     * @apiNote empty constructor required
     */
    public WebIssueAnalytic() {
        this(null, null, -1, null, null, null, null, null, null);
    }

    /**
     * Constructor to init the {@link IssueAnalytic} class
     *
     * @param id             The identifier of the issue
     * @param name           The name of the issue
     * @param creationDate   The date when the issue has been inserted in the system
     * @param appVersion     The application version where the issue is attached
     * @param device         The device where the issue occurred
     * @param issue          The cause message of the issue occurred
     * @param platform       The platform target of the application where the issue is attached
     * @param browser        The browser where the issue occurred
     * @param browserVersion The version of the browser
     */
    public WebIssueAnalytic(String id, String name, long creationDate, String appVersion, AmetistaDevice device,
                            String issue, Platform platform, String browser, String browserVersion) {
        super(id, name, creationDate, appVersion, device, issue, platform);
        this.browser = browser;
        this.browserVersion = browserVersion;
    }

    /**
     * Constructor to init the {@link IssueAnalytic} class
     *
     * @param jWebIssue Web issue details formatted as JSON
     */
    // TODO: 13/03/2025 CHECK TO REMOVE
    public WebIssueAnalytic(JSONObject jWebIssue) {
        super(jWebIssue);
        browser = hItem.getString(BROWSER_KEY);
        browserVersion = hItem.getString(BROWSER_VERSION_KEY);
    }

    /**
     * Method to get {@link #browser} instance
     *
     * @return {@link #browser} instance as {@link String}
     */
    public String getBrowser() {
        return browser;
    }

    /**
     * Method to get {@link #browserVersion} instance
     *
     * @return {@link #browserVersion} instance as {@link String}
     */
    @JsonGetter(BROWSER_VERSION_KEY)
    public String getBrowserVersion() {
        return browserVersion;
    }

}
