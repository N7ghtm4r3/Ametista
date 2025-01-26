package com.tecknobit.ametista.services.collector.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametista.services.applications.entities.AmetistaApplication;
import com.tecknobit.ametista.shared.data.AmetistaItem;
import com.tecknobit.ametistacore.enums.Platform;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

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
     * List of available analytic types
     */
    public enum AnalyticType {

        /**
         * {@code ISSUE} analytic
         */
        ISSUE("Issues"),

        /**
         * {@code PERFORMANCE} analytic
         */
        PERFORMANCE("Performance");

        /**
         * {@code tabTitle} related tab title
         */
        private final String tabTitle;

        /**
         * Constructor to init the {@link AnalyticType} enum class
         *
         * @param tabTitle The related tab title
         */
        AnalyticType(String tabTitle) {
            this.tabTitle = tabTitle;
        }

        /**
         * Method to get {@link #tabTitle} instance
         *
         * @return {@link #tabTitle} instance as {@link String}
         */
        public String getTabTitle() {
            return tabTitle;
        }

    }

    /**
     * {@code ANALYTIC_IDENTIFIER_KEY} the key for the <b>"analytic_id"</b> field
     */
    public static final String ANALYTIC_IDENTIFIER_KEY = "analytic_id";

    /**
     * {@code ANALYTIC_KEY} the key for the <b>"analytic"</b> field
     */
    public static final String ANALYTIC_KEY = "analytic";

    /**
     * {@code APP_VERSION_KEY} the key for the <b>"app_version"</b> field
     */
    public static final String APP_VERSION_KEY = "app_version";

    /**
     * {@code ANALYTIC_TYPE_KEY} the key for the <b>"type"</b> field
     */
    public static final String ANALYTIC_TYPE_KEY = "type";

    /**
     * {@code PLATFORM_KEY} the key for the <b>"platform"</b> field
     */
    public static final String PLATFORM_KEY = "platform";

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
     * Constructor to init the {@link AmetistaAnalytic} class
     *
     * @param jAnalytic Analytic details formatted as JSON
     * @param analyticType The type of the analytic
     */
    public AmetistaAnalytic(JSONObject jAnalytic, AnalyticType analyticType) {
        super(jAnalytic);
        type = analyticType;
        appVersion = hItem.getString(APP_VERSION_KEY);
        platform = Platform.valueOf(hItem.getString(PLATFORM_KEY));
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
