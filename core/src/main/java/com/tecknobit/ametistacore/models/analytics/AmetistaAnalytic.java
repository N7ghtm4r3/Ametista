package com.tecknobit.ametistacore.models.analytics;

import com.tecknobit.ametistacore.models.AmetistaItem;
import com.tecknobit.apimanager.annotations.Structure;
import org.json.JSONObject;

@Structure
public abstract class AmetistaAnalytic extends AmetistaItem {

    private final String appVersion;

    private final AmetistaDevice device;

    public AmetistaAnalytic() {
        this(null, null, -1, null, null);
    }

    public AmetistaAnalytic(String id, String name, long creationDate, String appVersion, AmetistaDevice device) {
        super(id, name, creationDate);
        this.appVersion = appVersion;
        this.device = device;
    }

    public AmetistaAnalytic(JSONObject jAnalytic) {
        super(jAnalytic);
        // TODO: 18/10/2024 TO INIT CORRECTLY
        appVersion = null;
        device = null;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public AmetistaDevice getDevice() {
        return device;
    }

    public static class AmetistaDevice extends AmetistaItem {

        private final String brand;

        private final String os;

        private final String osVersion;

        public AmetistaDevice() {
            this(null, null, -1, null, null, null);
        }

        public AmetistaDevice(String id, String name, long creationDate, String brand, String os, String osVersion) {
            super(id, name, creationDate);
            this.brand = brand;
            this.os = os;
            this.osVersion = osVersion;
        }

        public AmetistaDevice(JSONObject jDevice) {
            super(jDevice);
            // TODO: 18/10/2024 TO INIT CORRECTLY
            brand = null;
            os = null;
            osVersion = null;
        }

        public String getBrand() {
            return brand;
        }

        public String getOs() {
            return os;
        }

        public String getOsVersion() {
            return osVersion;
        }

    }

}
