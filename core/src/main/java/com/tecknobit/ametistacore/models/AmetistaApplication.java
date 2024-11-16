package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATIONS_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY;

/**
 * The {@code AmetistaApplication} class is useful to represent an application registered on the Ametista system
 * are running
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see AmetistaItem
 * @see EquinoxItem
 */
@Entity
@Table(
        name = APPLICATIONS_KEY,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {NAME_KEY}
                )
        }
)
public class AmetistaApplication extends AmetistaItem {

    /**
     * {@code APPLICATION_IDENTIFIER_KEY} the key for the <b>"application_id"</b> field
     */
    public static final String APPLICATION_IDENTIFIER_KEY = "application_id";

    /**
     * {@code APPLICATION_KEY} the key for the <b>"application"</b> field
     */
    public static final String APPLICATION_KEY = "application";

    /**
     * {@code APPLICATIONS_KEY} the key for the <b>"applications"</b> field
     */
    public static final String APPLICATIONS_KEY = "applications";

    /**
     * {@code APPLICATION_ICON_KEY} the key for the <b>"icon"</b> field
     */
    public static final String APPLICATION_ICON_KEY = "icon";

    /**
     * {@code DESCRIPTION_KEY} the key for the <b>"description"</b> field
     */
    public static final String DESCRIPTION_KEY = "description";

    /**
     * {@code PLATFORMS_KEY} the key for the <b>"platforms"</b> field
     */
    public static final String PLATFORMS_KEY = "platforms";

    /**
     * {@code MAX_VERSION_SAMPLES} the maximum version samples allowed for single time
     */
    public static final int MAX_VERSION_SAMPLES = 3;

    /**
     * {@code icon} the icon of the application
     */
    @Column(name = APPLICATION_ICON_KEY)
    private final String icon;

    /**
     * {@code description} the description of the application
     */
    @Lob
    @Column(
            name = DESCRIPTION_KEY,
            columnDefinition = "MEDIUMTEXT"
    )
    private final String description;

    /**
     * {@code platforms} the platforms target of the application
     */
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = PLATFORMS_KEY,
            joinColumns = @JoinColumn(name = APPLICATION_IDENTIFIER_KEY),
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (" + APPLICATION_IDENTIFIER_KEY + ") REFERENCES "
                            + APPLICATIONS_KEY + "(" + IDENTIFIER_KEY + ") ON DELETE CASCADE"
            )
    )
    @Column(name = PLATFORM_KEY)
    private final Set<Platform> platforms;

    /**
     * {@code brand} the issues occurred during the application runtime
     */
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = APPLICATION_KEY
    )
    @OrderBy(value = CREATION_DATE_KEY + " DESC")
    private final List<IssueAnalytic> issues;

    /**
     * {@code performanceAnalytics} the performance analytics of the application collected in the time
     */
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = APPLICATION_KEY
    )
    @OrderBy(value = CREATION_DATE_KEY + " DESC")
    private final List<PerformanceAnalytic> performanceAnalytics;

    /**
     * Constructor to init the {@link AmetistaApplication} class
     *
     * @apiNote empty constructor required
     */
    public AmetistaApplication() {
        this(null, null, null, null, new HashSet<>(), -1, List.of(), List.of());
    }

    /**
     * Constructor to init the {@link AmetistaApplication} class
     *
     * @param id                   The identifier of the application
     * @param icon                 The icon of the application
     * @param name                 The name of the application
     * @param description          The description of the application
     * @param platforms            The platforms target of the application
     * @param creationDate         The date when the application has been added in the system
     * @param issues               The issues occurred during the application runtime
     * @param performanceAnalytics The performance analytics of the application collected in the time
     */
    public AmetistaApplication(String id, String icon, String name, String description, Set<Platform> platforms,
                               long creationDate, List<IssueAnalytic> issues, List<PerformanceAnalytic> performanceAnalytics) {
        super(id, name, creationDate);
        this.icon = icon;
        this.description = description;
        this.platforms = platforms;
        this.issues = issues;
        this.performanceAnalytics = performanceAnalytics;
    }

    /**
     * Constructor to init the {@link AmetistaApplication} class
     *
     * @param jApplication Application details formatted as JSON
     */
    public AmetistaApplication(JSONObject jApplication) {
        super(jApplication);
        icon = hItem.getString(APPLICATION_ICON_KEY);
        description = hItem.getString(DESCRIPTION_KEY);
        platforms = new HashSet<>();
        loadPlatforms();
        issues = null;
        performanceAnalytics = null;
    }

    /**
     * Method to load the {@link #platforms} obtained by the response
     */
    private void loadPlatforms() {
        List<String> jPlatforms = hItem.fetchList(PLATFORMS_KEY, new ArrayList<>());
        for (String platform : jPlatforms)
            platforms.add(Platform.valueOf(platform));
    }

    /**
     * Method to get {@link #icon} instance
     *
     * @return {@link #icon} instance as {@link String}
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Method to get {@link #description} instance
     *
     * @return {@link #description} instance as {@link String}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to get {@link #platforms} instance
     *
     * @return {@link #platforms} instance as {@link Set} of {@link Platform}
     */
    public Set<Platform> getPlatforms() {
        return platforms;
    }

    /**
     * Method to get {@link #issues} instance
     *
     * @return {@link #issues} instance as {@link List} of {@link IssueAnalytic}
     */
    @JsonIgnore
    public List<IssueAnalytic> getIssues() {
        return issues;
    }

    /**
     * Method to get {@link #performanceAnalytics} instance
     *
     * @return {@link #performanceAnalytics} instance as {@link List} of {@link PerformanceAnalytic}
     */
    @JsonIgnore
    public List<PerformanceAnalytic> getPerformanceAnalytics() {
        return performanceAnalytics;
    }

}
