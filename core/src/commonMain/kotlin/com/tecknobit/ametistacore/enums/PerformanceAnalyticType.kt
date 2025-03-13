package com.tecknobit.ametistacore.enums

import kotlinx.serialization.Serializable

/**
 * The performance analytics available
 */
@Serializable
enum class PerformanceAnalyticType {

    /**
     * **LAUNCH_TIME** -> Inherent measurement of application startup time
     */
    LAUNCH_TIME,

    /**
     * **NETWORK_REQUESTS** -> Inherent measure of the number of HTTP requests executed by each application daily
     */
    NETWORK_REQUESTS,

    /**
     * **TOTAL_ISSUES** -> Inherent measurement of the number of crashes or problems encountered while using the application
     */
    TOTAL_ISSUES,

    /**
     * **ISSUES_PER_SESSION** -> Inherent measurement of the average number of crashes or issues encountered while using the application for a single session
     */
    ISSUES_PER_SESSION

}