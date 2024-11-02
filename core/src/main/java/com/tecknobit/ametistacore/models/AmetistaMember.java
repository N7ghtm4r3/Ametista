package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.tecknobit.ametistacore.models.AmetistaUser.Role;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import org.json.JSONObject;

import static com.tecknobit.equinox.environment.records.EquinoxUser.PROFILE_PIC_KEY;

public class AmetistaMember extends EquinoxItem {

    public static final String MEMBER_IDENTIFIER_KEY = "member_id";

    private final String profilePic;

    private final String name;

    private final String surname;

    private final String email;

    private final Role role;

    public AmetistaMember(String id, String profilePic, String name, String surname, String email, Role role) {
        super(id);
        this.profilePic = profilePic;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
    }

    public AmetistaMember(JSONObject jMember) {
        super(jMember);
        // TODO: 02/11/2024 TO INIT CORRECTLY
        profilePic = null;
        name = null;
        surname = null;
        email = null;
        role = null;
    }

    @JsonGetter(PROFILE_PIC_KEY)
    public String getProfilePic() {
        return profilePic;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

}
