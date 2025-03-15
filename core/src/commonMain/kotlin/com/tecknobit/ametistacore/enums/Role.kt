package com.tecknobit.ametistacore.enums

import kotlinx.serialization.Serializable

/**
 * List of the available roles
 */
@Serializable
enum class Role {

    /**
     * `VIEWER` this role allows the users to only see the data without the possibility to add/edit/remove them
     */
    VIEWER,

    /**
     * `ADMIN` this role allows the users to execute the basics action and add new [.VIEWER] in the system
     */
    ADMIN

}