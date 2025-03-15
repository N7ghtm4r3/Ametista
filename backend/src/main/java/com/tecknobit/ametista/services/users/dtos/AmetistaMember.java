package com.tecknobit.ametista.services.users.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.tecknobit.ametistacore.enums.Role;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import com.tecknobit.equinoxcore.annotations.DTO;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.PROFILE_PIC_KEY;

/**
 * The {@code AmetistaUser} class is useful to represent a base Ametista's system user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 */
@DTO
public class AmetistaMember extends EquinoxItem {

    /**
     * {@code profilePic} the profile picture of the user
     */
    private final String profilePic;

    /**
     * {@code name} the name of the user
     */
    private final String name;

    /**
     * {@code surname} the surname of the user
     */
    private final String surname;

    /**
     * {@code email} the email of the user
     */
    private final String email;

    /**
     * {@code role} the role of the user
     */
    private final Role role;

    /**
     * Constructor to init the {@link AmetistaMember} class
     *
     * @param id         The identifier of the user
     * @param name       The name of the user
     * @param surname    The surname of the user
     * @param email      The email of the user
     * @param profilePic The profile picture of the user
     * @param role       The role of the user
     */
    public AmetistaMember(String id, String profilePic, String name, String surname, String email, Role role) {
        super(id);
        this.profilePic = profilePic;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
    }

    /**
     * Method to get {@link #profilePic} instance <br>
     * No-any params required
     *
     * @return {@link #profilePic} instance as {@link String}
     */
    @JsonGetter(PROFILE_PIC_KEY)
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * Method to get {@link #name} instance <br>
     * No-any params required
     *
     * @return {@link #name} instance as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get {@link #surname} instance <br>
     * No-any params required
     *
     * @return {@link #surname} instance as {@link String}
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Method to get {@link #email} instance <br>
     * No-any params required
     *
     * @return {@link #email} instance as {@link String}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to get {@link #role} instance
     *
     * @return {@link #role} instance as {@link Role}
     */
    public Role getRole() {
        return role;
    }

}
