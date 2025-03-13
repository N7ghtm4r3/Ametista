package com.tecknobit.ametistacore.enums

import kotlinx.serialization.Serializable

/**
 * The list of tha available platforms
 */
@Serializable
enum class Platform {

    /**
     * **ANDROID** -> the Android target platform
     */
    ANDROID,

    /**
     * **IOS** -> the iOs target platform
     */
    IOS,

    /**
     * **DESKTOP** -> the desktop target platform
     */
    DESKTOP,

    /**
     * **WEB** -> the web target platform
     */
    WEB

}

