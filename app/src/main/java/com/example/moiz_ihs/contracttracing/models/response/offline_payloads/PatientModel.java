package com.example.moiz_ihs.contracttracing.models.response.offline_payloads;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/22/2017.
 */

public class PatientModel {
    private String person;
    private List<Identifier> identifiers = null;
    private Boolean voided;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }
}
