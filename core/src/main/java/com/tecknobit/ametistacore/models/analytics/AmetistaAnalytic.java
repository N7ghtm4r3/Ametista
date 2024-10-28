package com.tecknobit.ametistacore.models.analytics;

import com.tecknobit.ametistacore.models.AmetistaApplication;
import com.tecknobit.ametistacore.models.AmetistaItem;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

@Structure
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

    public static final String ANALYTIC_KEY = "analytic";

    public static final String APP_VERSION_KEY = "app_version";

    public static final String ANALYTIC_TYPE_KEY = "type";

    public static final String PLATFORM_KEY = "platform";

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AmetistaApplication application;

    @Column(name = APP_VERSION_KEY)
    protected final String appVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = ANALYTIC_TYPE_KEY)
    protected final AnalyticType type;

    @OneToOne(
            mappedBy = ANALYTIC_KEY
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

    public String getAppVersion() {
        return appVersion;
    }

    public AmetistaDevice getDevice() {
        return device;
    }

    public Platform getPlatform() {
        return platform;
    }

    @Entity
    @Table(name = AmetistaDevice.DEVICES_KEY)
    public static class AmetistaDevice extends EquinoxItem {

        public static final String DEVICE_KEY = "device";

        public static final String DEVICES_KEY = "devices";

        public static final String BRAND_KEY = "brand";

        public static final String MODEL_KEY = "model";

        public static final String OS_KEY = "os";

        public static final String OS_VERSION_KEY = "osVersion";

        @OneToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        private AmetistaAnalytic analytic;

        @Column(name = BRAND_KEY)
        private final String brand;

        @Column(name = MODEL_KEY)
        private final String model;

        @Column(name = OS_KEY)
        private final String os;

        @Column(name = OS_VERSION_KEY)
        private final String osVersion;

        public AmetistaDevice() {
            this(null, null, null, null, null);
        }

        public AmetistaDevice(String id, String brand, String model, String os, String osVersion) {
            super(id);
            this.brand = brand;
            this.model = model;
            this.os = os;
            this.osVersion = osVersion;
        }

        public AmetistaDevice(JSONObject jDevice) {
            super(jDevice);
            // TODO: 18/10/2024 TO INIT CORRECTLY
            brand = null;
            model = null;
            os = null;
            osVersion = null;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public String getOs() {
            return os;
        }

        public String getOsVersion() {
            return osVersion;
        }

    }

}
