package com.ihs.android.contracttracing.contracttracing.models.response.offline_payloads;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/22/2017.
 */

public class PersonModel {


    private String gender;
    private String birthdate;
    private List<Name> names = null;
    private String identifier;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
