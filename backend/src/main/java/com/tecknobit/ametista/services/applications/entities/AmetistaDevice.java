package com.tecknobit.ametista.services.applications.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import static com.tecknobit.ametistacore.ConstantsKt.*;
import static com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem.IDENTIFIER_KEY;

/**
 * The {@code AmetistaDevice} class is useful to represent a device where the applications monitored by the Ametista Engine
 * are running
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 */
@Entity
@Table(name = DEVICES_KEY)
@AttributeOverride(
        name = IDENTIFIER_KEY,
        column = @Column(
                name = DEVICE_IDENTIFIER_KEY
        )
)
public class AmetistaDevice extends EquinoxItem {

    /**
     * {@code brand} the brand of the device
     */
    @Column(name = BRAND_KEY)
    private final String brand;

    /**
     * {@code model} the model of the device
     */
    @Column(name = MODEL_KEY)
    private final String model;

    /**
     * {@code os} the operating system of the device
     */
    @Column(name = OS_KEY)
    private final String os;

    /**
     * {@code osVersion} the operating system version of the device
     */
    @Column(name = OS_VERSION_KEY)
    private final String osVersion;

    /**
     * Constructor to init the {@link AmetistaDevice} class
     *
     * @apiNote empty constructor required
     */
    public AmetistaDevice() {
        this(null, null, null, null, null);
    }

    /**
     * Constructor to init the {@link AmetistaDevice} class
     *
     * @param id        The identifier of the device
     * @param brand     The brand of the device
     * @param model     The model of the device
     * @param os        The operating system of the device
     * @param osVersion The operating system version of the device
     */
    public AmetistaDevice(String id, String brand, String model, String os, String osVersion) {
        super(id);
        this.brand = brand;
        this.model = model;
        this.os = os;
        this.osVersion = osVersion;
    }

    /**
     * Method to get {@link #brand} instance
     *
     * @return {@link #brand} instance as {@link String}
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Method to get {@link #model} instance
     *
     * @return {@link #model} instance as {@link String}
     */
    public String getModel() {
        return model;
    }

    /**
     * Method to get {@link #os} instance
     *
     * @return {@link #os} instance as {@link String}
     */
    public String getOs() {
        return os;
    }

    /**
     * Method to get {@link #osVersion} instance
     *
     * @return {@link #osVersion} instance as {@link String}
     */
    @JsonGetter(OS_VERSION_KEY)
    public String getOsVersion() {
        return osVersion;
    }

}