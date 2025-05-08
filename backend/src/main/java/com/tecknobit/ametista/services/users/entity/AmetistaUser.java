package com.tecknobit.ametista.services.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecknobit.ametista.services.users.dtos.AmetistaMember;
import com.tecknobit.ametistacore.enums.Role;
import com.tecknobit.equinoxbackend.annotations.EmptyConstructor;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import com.tecknobit.equinoxbackend.environment.services.users.entity.EquinoxUser;
import com.tecknobit.equinoxcore.dtoutils.DTOConvertible;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import static com.tecknobit.ametistacore.ConstantsKt.ROLE_KEY;
import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.PASSWORD_KEY;


/**
 * The {@code AmetistaUser} class is useful to represent a base Ametista's system user
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 * @see DTOConvertible
 * 
 */
@Entity
@JsonIgnoreProperties({PASSWORD_KEY})
public class AmetistaUser extends EquinoxUser implements DTOConvertible<AmetistaMember> {

    /**
     * {@code role} the role of the user
     */
    @Enumerated(EnumType.STRING)
    @Column(name = ROLE_KEY)
    private final Role role;

    /**
     * Constructor to init the {@link AmetistaUser} class
     *
     * @apiNote empty constructor required
     */
    @EmptyConstructor
    public AmetistaUser() {
        this(null, null, null, null, null, null, null, null, null);
    }

    /**
     * Constructor to init the {@link EquinoxUser} class
     *
     * @param id The identifier of the user
     * @param token The token which the user is allowed to operate on server
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param profilePic The profile picture of the user
     * @param language The language of the user
     * @param role The role of the user
     */
    public AmetistaUser(String id, String token, String name, String surname, String email, String password,
                        String profilePic, String language, Role role) {
        super(id, token, name, surname, email, password, profilePic, language);
        this.role = role;
    }

    /**
     * Method to get {@link #role} instance
     *
     * @return {@link #role} instance as {@link Role}
     */
    public Role getRole() {
        return role;
    }

    /**
     * Method to get whether the user is a {@link Role#VIEWER}
     *
     * @return whether the user is a {@link Role#VIEWER} as {@code boolean}
     */
    @JsonIgnore
    public boolean isViewer() {
        return role == Role.VIEWER;
    }

    /**
     * Method to get whether the user is a {@link Role#ADMIN}
     *
     * @return whether the user is a {@link Role#ADMIN} as {@code boolean}
     */
    @JsonIgnore
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AmetistaMember convertToRelatedDTO() {
        return new AmetistaMember(
                id,
                profilePic,
                name,
                surname,
                email,
                role
        );
    }

}
