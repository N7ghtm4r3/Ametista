package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class AmetistaApplication extends EquinoxItem {

    public enum Platform {

        ANDROID,

        IOS,

        DESKTOP,

        WEB

    }

    private final String icon;

    private final String name;

    private final String description;

    private final List<Platform> platforms;

    private final long creationDate;

    public AmetistaApplication() {
        this(null, null, null, null, List.of(), -1);
    }

    // TODO: 14/10/2024 TO REMOVE
    public AmetistaApplication(String name) {
        this(String.valueOf(new Random().nextInt()), "https://www.euroschoolindia.com/wp-content/uploads/2023/06/facts-about-space.jpg", name,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce consequat imperdiet accumsan. Quisque nisl mi, dignissim et mauris pharetra, laoreet dictum leo. Aenean efficitur lorem a nibh ullamcorper, a commodo lorem tincidunt. Integer cursus posuere tempor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas ullamcorper elit a varius placerat. Vivamus nec ex ultricies, accumsan est sed, facilisis ipsum. Vivamus condimentum lacinia mi, at ultrices purus tincidunt eget. Ut dictum mi augue, vitae maximus mi feugiat sit amet. Mauris ipsum arcu, fermentum non orci non, blandit bibendum dui. Sed nulla justo, posuere at lectus venenatis, ullamcorper porttitor odio. Cras sed dolor turpis. Duis eu varius mauris, at euismod enim. Nulla facilisi. Pellentesque consequat venenatis tortor id aliquam.",
                List.of(Platform.values()[new Random().nextInt(4)], Platform.values()[new Random().nextInt(4)]), 1);
    }

    public AmetistaApplication(String id, String icon, String name, String description, List<Platform> platforms,
                               long creationDate) {
        super(id);
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.platforms = platforms;
        this.creationDate = creationDate;
    }

    public AmetistaApplication(JSONObject jApplication) {
        super(jApplication);
        // TODO: 14/10/2024 TO INIT CORRECTLY
        icon = null;
        name = null;
        description = null;
        platforms = null;
        creationDate = 1;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public long getCreationTimestamp() {
        return creationDate;
    }

    @JsonIgnore
    public String getCreationDate() {
        return timeFormatter.formatAsString(creationDate);
    }

}
