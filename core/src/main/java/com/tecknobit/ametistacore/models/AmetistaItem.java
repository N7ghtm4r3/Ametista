package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.json.JSONObject;

import static com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY;

@Structure
@MappedSuperclass
public abstract class AmetistaItem extends EquinoxItem {

    public static final String CREATION_DATE_KEY = "creation_date";

    @Column(name = NAME_KEY)
    protected String name;

    @Column(name = CREATION_DATE_KEY)
    protected final long creationDate;

    public AmetistaItem() {
        this(null, null, -1);
    }

    public AmetistaItem(String id, String name, long creationDate) {
        super(id);
        this.name = name;
        this.creationDate = creationDate;
    }

    public AmetistaItem(JSONObject jItem) {
        super(jItem);
        name = hItem.getString(NAME_KEY);
        creationDate = hItem.getLong(CREATION_DATE_KEY);
    }

    public String getName() {
        return name;
    }

    @JsonGetter(CREATION_DATE_KEY)
    public long getCreationTimestamp() {
        return creationDate;
    }

    @JsonIgnore
    public String getCreationDate() {
        return timeFormatter.formatAsString(creationDate);
    }

}
