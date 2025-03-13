package com.tecknobit.ametista.shared.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.json.JSONObject;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.NAME_KEY;

/**
 * The {@code AmetistaItem} class is useful to create an Ametista's item giving the basis structure to work correctly
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see EquinoxItem
 */
@Structure
@MappedSuperclass
public abstract class AmetistaItem extends EquinoxItem {

    /**
     * {@code name} the name of the item
     */
    @Column(name = NAME_KEY)
    protected String name;

    /**
     * {@code creationDate} the timestamp when the item has been created
     */
    @Column(name = CREATION_DATE_KEY)
    protected final long creationDate;

    /**
     * Constructor to init the {@link AmetistaItem} class
     */
    public AmetistaItem() {
        this(null, null, -1);
    }

    /**
     * Constructor to init the {@link AmetistaItem} class
     *
     * @param id The identifier of the item
     * @param name The name of the item
     * @param creationDate The timestamp when the item has been created
     */
    public AmetistaItem(String id, String name, long creationDate) {
        super(id);
        this.name = name;
        this.creationDate = creationDate;
    }

    /**
     * Constructor to init the {@link AmetistaItem} class
     *
     * @param jItem: item formatted as JSON
     */
    public AmetistaItem(JSONObject jItem) {
        super(jItem);
        name = hItem.getString(NAME_KEY);
        creationDate = hItem.getLong(CREATION_DATE_KEY);
    }

    /**
     * Method to get {@link #name} instance
     *
     * @return {@link #name} instance as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get {@link #creationDate} instance
     *
     * @return {@link #creationDate} instance as {@code long}
     */
    @JsonGetter(CREATION_DATE_KEY)
    public long getCreationTimestamp() {
        return creationDate;
    }

    /**
     * Method to get {@link #creationDate} instance
     *
     * @return {@link #creationDate} instance as {@link String}
     */
    @JsonIgnore
    public String getCreationDate() {
        return timeFormatter.formatAsString(creationDate);
    }

}
