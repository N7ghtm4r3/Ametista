package com.tecknobit.ametista.services.applications.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametista.services.collector.entities.issues.IssueAnalytic;
import com.tecknobit.ametista.services.collector.entities.performance.PerformanceAnalytic;
import com.tecknobit.ametista.shared.data.AmetistaItem;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.equinoxbackend.annotations.EmptyConstructor;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tecknobit.ametistacore.ConstantsKt.*;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.IDENTIFIER_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.NAME_KEY;

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
    @EmptyConstructor
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

}
