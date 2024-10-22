package com.tecknobit.ametistacore.models.analytics.performance;

import com.tecknobit.ametistacore.models.Platform;
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic;
import com.tecknobit.apimanager.annotations.Structure;
import org.json.JSONObject;

import java.util.Random;

import static com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.AnalyticType.PERFORMANCE;

@Structure
public class PerformanceAnalytic extends AmetistaAnalytic {

    public enum PerformanceAnalyticType {

        LAUNCH_TIME,

        NETWORK_REQUESTS,

        TOTAL_ISSUES,

        ISSUES_PER_SESSION

    }

    protected final double value;

    protected final PerformanceAnalyticType performanceAnalyticType;

    // TODO: 21/10/2024 TO REMOVE
    public PerformanceAnalytic(double value) {
        this(null, System.currentTimeMillis() - (86400000L * new Random().nextInt(90)), null, null, value, null, null);
    }

    public PerformanceAnalytic() {
        this(null, -1, null, null, 0, null, null);
    }

    public PerformanceAnalytic(String id, long creationDate, String appVersion, AmetistaDevice device, double value,
                               Platform platform, PerformanceAnalyticType performanceAnalyticType) {
        super(id, null, creationDate, appVersion, PERFORMANCE, device, platform);
        this.value = value;
        this.performanceAnalyticType = performanceAnalyticType;
    }

    public PerformanceAnalytic(JSONObject jPerformance) {
        super(jPerformance);
        // TODO: 18/10/2024 TO INIT CORRECTLY
        value = 0;
        performanceAnalyticType = null;
    }

    public double getValue() {
        return value;
    }

    public PerformanceAnalyticType getPerformanceAnalyticType() {
        return performanceAnalyticType;
    }

}
