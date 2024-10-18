package com.tecknobit.ametistacore.models.analytics;

import com.tecknobit.ametistacore.models.AmetistaItem;
import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import org.json.JSONObject;

@Structure
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

    protected final String appVersion;

    protected final AnalyticType type;

    protected final AmetistaDevice device;

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

    public static class AmetistaDevice extends EquinoxItem {

        private final String brand;

        private final String model;

        private final String os;

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
