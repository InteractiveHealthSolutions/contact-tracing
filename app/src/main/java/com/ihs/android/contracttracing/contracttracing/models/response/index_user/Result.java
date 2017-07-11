package com.ihs.android.contracttracing.contracttracing.models.response.index_user;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/17/2017.
 */

public class Result {
    private String uuid;
    private String display;
    private List<Identifier> identifiers = null;
    private Person person;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
