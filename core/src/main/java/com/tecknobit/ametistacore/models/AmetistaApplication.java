package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic;
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATIONS_KEY;
import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY;

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

    public static final String APPLICATION_IDENTIFIER_KEY = "application_id";

    public static final String APPLICATION_KEY = "application";

    public static final String APPLICATIONS_KEY = "applications";

    public static final String APPLICATION_ICON_KEY = "icon";

    public static final String DESCRIPTION_KEY = "description";

    public static final String PLATFORMS_KEY = "platforms";

    public static final int MAX_VERSION_SAMPLES = 3;

    @Column(name = APPLICATION_ICON_KEY)
    private final String icon;

    @Lob
    @Column(
            name = DESCRIPTION_KEY,
            columnDefinition = "MEDIUMTEXT"
    )
    private final String description;

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

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = APPLICATION_KEY
    )
    @OrderBy(value = CREATION_DATE_KEY + " DESC")
    private final List<IssueAnalytic> issues;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = APPLICATION_KEY
    )
    @OrderBy(value = CREATION_DATE_KEY + " DESC")
    private final List<PerformanceAnalytic> performanceAnalytics;

    public AmetistaApplication() {
        this(null, null, null, null, new HashSet<>(), -1, List.of(), List.of());
    }

    public AmetistaApplication(String id, String icon, String name, String description, Set<Platform> platforms,
                               long creationDate, List<IssueAnalytic> issues, List<PerformanceAnalytic> performanceAnalytics) {
        super(id, name, creationDate);
        this.icon = icon;
        this.description = description;
        this.platforms = platforms;
        this.issues = issues;
        this.performanceAnalytics = performanceAnalytics;
    }

    public AmetistaApplication(JSONObject jApplication) {
        super(jApplication);
        icon = hItem.getString(APPLICATION_ICON_KEY);
        description = hItem.getString(DESCRIPTION_KEY);
        platforms = new HashSet<>();
        loadPlatforms();
        issues = null;
        performanceAnalytics = null;
    }

    private void loadPlatforms() {
        List<String> jPlatforms = hItem.fetchList(PLATFORMS_KEY, new ArrayList<>());
        for (String platform : jPlatforms)
            platforms.add(Platform.valueOf(platform));
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    @JsonIgnore
    public List<IssueAnalytic> getIssues() {
        return issues;
    }

    @JsonIgnore
    public List<PerformanceAnalytic> getPerformanceAnalytics() {
        return performanceAnalytics;
    }

}
