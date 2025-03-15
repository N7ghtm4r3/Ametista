package com.tecknobit.ametistacore.enums

import kotlinx.serialization.Serializable

/**
 * List of available analytic types
 *
 * @property tabTitle The related tab title
 */
@Serializable
enum class AnalyticType(
    val tabTitle: String,
) {

    /**
     * `ISSUE` analytic
     */
    ISSUE("Issues"),

    /**
     * `PERFORMANCE` analytic
     */
    PERFORMANCE("Performance");

}