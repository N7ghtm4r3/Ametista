package com.tecknobit.ametistacore.models.analytics;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.tecknobit.ametistacore.models.AmetistaApplication;
import com.tecknobit.ametistacore.models.AmetistaDevice;
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

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    protected final AmetistaDevice device;

    @Enumerated(EnumType.STRING)
    @Column(name = PLATFORM_KEY)
    protected final Platform platform;

    public AmetistaAnalytic() {
        this(null, null, -1, null, null, null, null);
    }

    public AmetistaAnalytic(String id, String name, long creationDate, String appVersion, AnalyticType type,
                            AmetistaDevice device, Platform platform) {
        super(id, name, creationDate);
        this.appVersion = appVersion;
        this.type = type;
        this.device = device;
        this.platform = platform;
    }

    public AmetistaAnalytic(JSONObject jAnalytic) {
        super(jAnalytic);
        // TODO: 18/10/2024 TO INIT CORRECTLY
        type = null;
        appVersion = null;
        device = null;
        platform = null;
    }

    @JsonGetter(APP_VERSION_KEY)
    public String getAppVersion() {
        return appVersion;
    }

    public AmetistaDevice getDevice() {
        return device;
    }

    public Platform getPlatform() {
        return platform;
    }

}
