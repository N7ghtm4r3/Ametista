package com.tecknobit.ametistacore.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecknobit.ametistacore.helpers.annotations.DTO;
import com.tecknobit.ametistacore.models.AmetistaUser.Role;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import org.json.JSONObject;

import static com.tecknobit.ametistacore.models.AmetistaUser.ROLE_KEY;
import static com.tecknobit.equinox.environment.records.EquinoxUser.*;

@DTO
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
        profilePic = hItem.getString(PROFILE_PIC_KEY);
        name = hItem.getString(NAME_KEY);
        surname = hItem.getString(SURNAME_KEY);
        email = hItem.getString(EMAIL_KEY);
        role = Role.valueOf(hItem.getString(ROLE_KEY));
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

    @JsonIgnore
    public String getCompleteName() {
        return name + " " + surname;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

}
