package com.tecknobit.ametistacore.models;

import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic;
import com.tecknobit.ametistacore.models.analytics.IssueAnalytic;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AmetistaApplication extends AmetistaItem {

    public static final String APPLICATION_KEY = "application";

    public static final String PLATFORM_KEY = "platform";

    private final String icon;

    private final String description;

    private final Set<Platform> platforms;

    private final List<IssueAnalytic> issues;

    public AmetistaApplication() {
        this(null, null, null, null, new HashSet<>(), -1, List.of());
    }

    // TODO: 14/10/2024 TO REMOVE
    public AmetistaApplication(String name) {
        super(String.valueOf(new Random().nextInt()), name, System.currentTimeMillis());
        this.icon = "https://www.euroschoolindia.com/wp-content/uploads/2023/06/facts-about-space.jpg";
        this.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce consequat imperdiet accumsan. Quisque nisl mi, dignissim et mauris pharetra, laoreet dictum leo. Aenean efficitur lorem a nibh ullamcorper, a commodo lorem tincidunt. Integer cursus posuere tempor. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas ullamcorper elit a varius placerat. Vivamus nec ex ultricies, accumsan est sed, facilisis ipsum. Vivamus condimentum lacinia mi, at ultrices purus tincidunt eget. Ut dictum mi augue, vitae maximus mi feugiat sit amet. Mauris ipsum arcu, fermentum non orci non, blandit bibendum dui. Sed nulla justo, posuere at lectus venenatis, ullamcorper porttitor odio. Cras sed dolor turpis. Duis eu varius mauris, at euismod enim. Nulla facilisi. Pellentesque consequat venenatis tortor id aliquam.";
        HashSet<Platform> platforms = new HashSet<>();
        for (Platform platform : Platform.values())
            if (new Random().nextBoolean())
                platforms.add(platform);
        this.platforms = platforms;
        issues = List.of(new IssueAnalytic(
                        String.valueOf(new Random().nextLong()),
                        System.currentTimeMillis(),
                        "1.0.0",
                        new AmetistaAnalytic.AmetistaDevice(
                                String.valueOf(new Random().nextInt()),
                                "Brand",
                                "XL",
                                "Android",
                                "12"
                        ),
                        "",
                        Platform.ANDROID
                ),
                new IssueAnalytic(
                        String.valueOf(new Random().nextLong()),
                        System.currentTimeMillis(),
                        "1.0.0",
                        new AmetistaAnalytic.AmetistaDevice(
                                String.valueOf(new Random().nextInt()),
                                "Brand",
                                "XL",
                                "Android",
                                "12"
                        ),
                        "",
                        Platform.ANDROID
                ));
    }

    public AmetistaApplication(String id, String icon, String name, String description, Set<Platform> platforms,
                               long creationDate, List<IssueAnalytic> issues) {
        super(id, name, creationDate);
        this.icon = icon;
        this.description = description;
        this.platforms = platforms;
        this.issues = issues;
    }

    public AmetistaApplication(JSONObject jApplication) {
        super(jApplication);
        // TODO: 14/10/2024 TO INIT CORRECTLY
        icon = null;
        description = null;
        platforms = null;
        issues = null;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public List<IssueAnalytic> getIssues() {
        return issues;
    }

}
