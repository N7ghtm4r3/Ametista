package com.tecknobit.ametista.services.collector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametista.services.applications.entities.AmetistaApplication;
import com.tecknobit.ametista.shared.data.AmetistaItem;
import com.tecknobit.ametistacore.enums.AnalyticType;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static com.tecknobit.ametistacore.ConstantsKt.APP_VERSION_KEY;
import static com.tecknobit.ametistacore.ConstantsKt.PLATFORM_KEY;

/**
 * The {@code AmetistaAnalytic} class is useful to represent an Ametista's analytic
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see AmetistaItem
 * @see EquinoxItem
 */
@Structure
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AmetistaAnalytic extends AmetistaItem {

    /**
     * {@code application} the application where the analytic is attached
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AmetistaApplication application;

    /**
     * {@code appVersion} the application version where the analytic is attached
     */
    @Column(name = APP_VERSION_KEY)
    protected final String appVersion;

    /**
     * {@code type} the type of the analytic
     */
    @Transient
    protected final AnalyticType type;

    /**
     * {@code icon} the platform target of the application where the analytic is attached
     */
    @Enumerated(EnumType.STRING)
    @Column(name = PLATFORM_KEY)
    protected final Platform platform;

    /**
     * Constructor to init the {@link AmetistaAnalytic} class
     *
     * @apiNote empty constructor required
     */
    public AmetistaAnalytic() {
        this(null, null, -1, null, null, null);
    }

    /**
     * Constructor to init the {@link AmetistaAnalytic} class
     *
     * @param id           The identifier of the analytic
     * @param name         The name of the analytic
     * @param creationDate The date when the analytic has been inserted in the system
     * @param appVersion   The application version where the analytic is attached
     * @param type         The type of the analytic
     * @param platform     The platform target of the application where the analytic is attached
     */
    public AmetistaAnalytic(String id, String name, long creationDate, String appVersion, AnalyticType type,
                            Platform platform) {
        super(id, name, creationDate);
        this.appVersion = appVersion;
        this.type = type;
        this.platform = platform;
    }

    /**
     * Method to get {@link #appVersion} instance
     *
     * @return {@link #appVersion} instance as {@link String}
     */
    @JsonGetter(APP_VERSION_KEY)
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * Method to get {@link #platform} instance
     *
     * @return {@link #platform} instance as {@link Platform}
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Method to get {@link #application} instance
     *
     * @return {@link #application} instance as {@link AmetistaApplication}
     */
    @JsonIgnore
    public AmetistaApplication getApplication() {
        return application;
    }

}
