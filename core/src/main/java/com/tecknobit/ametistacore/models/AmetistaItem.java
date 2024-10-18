package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import org.json.JSONObject;

import static com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY;

@Structure
public abstract class AmetistaItem extends EquinoxItem {

    public static final String CREATION_DATE_KEY = "creation_date";

    protected String name;

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

    public long getCreationTimestamp() {
        return creationDate;
    }

    @JsonIgnore
    public String getCreationDate() {
        return timeFormatter.formatAsString(creationDate);
    }

}
