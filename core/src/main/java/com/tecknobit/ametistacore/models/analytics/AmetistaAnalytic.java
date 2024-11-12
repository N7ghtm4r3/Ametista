package com.tecknobit.ametistacore.models.analytics;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import com.tecknobit.ametistacore.models.AmetistaItem;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.apimanager.annotations.Structure;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

@Structure
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AmetistaAnalytic extends AmetistaItem {

    public enum AnalyticType {

        ISSUE("Issues"),

        PERFORMANCE("Performance");

        private final String tabTitle;

        AnalyticType(String tabTitle) {
            this.tabTitle = tabTitle;
        }

        public String getTabTitle() {
            return tabTitle;
        }

    }

    public static final String ANALYTIC_IDENTIFIER_KEY = "analytic_id";

    public static final String ANALYTIC_KEY = "analytic";

    public static final String APP_VERSION_KEY = "app_version";

    public static final String ANALYTIC_TYPE_KEY = "type";

    public static final String PLATFORM_KEY = "platform";

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AmetistaApplication application;

    @Column(name = APP_VERSION_KEY)
    protected final String appVersion;

    @Transient
    protected final AnalyticType type;

    @Enumerated(EnumType.STRING)
    @Column(name = PLATFORM_KEY)
    protected final Platform platform;

    public AmetistaAnalytic() {
        this(null, null, -1, null, null, null);
    }

    public AmetistaAnalytic(String id, String name, long creationDate, String appVersion, AnalyticType type,
                            Platform platform) {
        super(id, name, creationDate);
        this.appVersion = appVersion;
        this.type = type;
        this.platform = platform;
    }

    public AmetistaAnalytic(JSONObject jAnalytic, AnalyticType analyticType) {
        super(jAnalytic);
        type = analyticType;
        appVersion = hItem.getString(APP_VERSION_KEY);
        platform = Platform.valueOf(hItem.getString(PLATFORM_KEY));
    }

    @JsonGetter(APP_VERSION_KEY)
    public String getAppVersion() {
        return appVersion;
    }

    public Platform getPlatform() {
        return platform;
    }

    @JsonIgnore
    public AmetistaApplication getApplication() {
        return application;
    }

}
