package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.json.JSONObject;

@Entity
@Table(name = AmetistaDevice.DEVICES_KEY)
public class AmetistaDevice extends EquinoxItem {

    public static final String DEVICE_KEY = "device";

    public static final String DEVICES_KEY = "devices";

    public static final String BRAND_KEY = "brand";

    public static final String MODEL_KEY = "model";

    public static final String OS_KEY = "os";

    public static final String OS_VERSION_KEY = "os_version";

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

    @JsonGetter(OS_VERSION_KEY)
    public String getOsVersion() {
        return osVersion;
    }

}