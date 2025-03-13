package com.tecknobit.ametista.services.collector.entities.issues;

import com.tecknobit.ametista.services.applications.entities.AmetistaDevice;
import com.tecknobit.ametista.services.collector.entities.AmetistaAnalytic;
import com.tecknobit.ametista.shared.data.AmetistaItem;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.*;
import org.json.JSONObject;

import static com.tecknobit.ametistacore.ConstantsKt.*;
import static com.tecknobit.ametistacore.enums.AnalyticType.ISSUE;

/**
 * The {@code IssueAnalytic} class is useful to represent an issue
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see AmetistaAnalytic
 * @see AmetistaItem
 * @see EquinoxItem
 */
@Entity
@Table(name = ISSUES_KEY)
@DiscriminatorValue(ISSUE_KEY)
public class IssueAnalytic extends AmetistaAnalytic {

    /**
     * {@code application} the cause message of the issue occurred
     */
    @Column(
            name = ISSUE_KEY,
            columnDefinition = "MEDIUMTEXT"
    )
    protected final String issue;

    /**
     * {@code device} the device where the issue occurred
     */
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = DEVICE_IDENTIFIER_KEY)
    protected final AmetistaDevice device;

    /**
     * Constructor to init the {@link IssueAnalytic} class
     *
     * @apiNote empty constructor required
     */
    public IssueAnalytic() {
        this(null, null, -1, null, null, null, null);
    }

    /**
     * Constructor to init the {@link IssueAnalytic} class
     *
     * @param id           The identifier of the issue
     * @param name         The name of the issue
     * @param creationDate The date when the issue has been inserted in the system
     * @param appVersion   The application version where the issue is attached
     * @param device       The device where the issue occurred
     * @param issue        The cause message of the issue occurred
     * @param platform     The platform target of the application where the issue is attached
     */
    public IssueAnalytic(String id, String name, long creationDate, String appVersion, AmetistaDevice device, String issue,
                         Platform platform) {
        super(id, name, creationDate, appVersion, ISSUE, platform);
        this.issue = issue;
        this.device = device;
        this.name = name;
    }

    /**
     * Constructor to init the {@link IssueAnalytic} class
     *
     * @param jIssue Issue details formatted as JSON
     */
    // TODO: 13/03/2025 CHECK TO REMOVE
    public IssueAnalytic(JSONObject jIssue) {
        super(jIssue, ISSUE);
        issue = hItem.getString(ISSUE_KEY);
        device = new AmetistaDevice(hItem.getJSONObject(DEVICE_KEY));
    }

    /**
     * Method to get {@link #issue} instance
     *
     * @return {@link #issue} instance as {@link String}
     */
    public String getIssue() {
        return issue;
    }

    /**
     * Method to get {@link #device} instance
     *
     * @return {@link #device} instance as {@link AmetistaDevice}
     */
    public AmetistaDevice getDevice() {
        return device;
    }

}
