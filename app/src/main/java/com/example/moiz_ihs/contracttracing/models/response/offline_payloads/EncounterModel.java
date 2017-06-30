package com.example.moiz_ihs.contracttracing.models.response.offline_payloads;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/23/2017.
 */

public class EncounterModel {
    private String encounterDatetime;
    private String patient;
    private String encounterType;
    private List<Ob> obs = null;
    private Boolean voided;
    private List<Object> encounterProviders = null;

    public String getEncounterDatetime() {
        return encounterDatetime;
    }

    public void setEncounterDatetime(String encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(String encounterType) {
        this.encounterType = encounterType;
    }

    public List<Ob> getObs() {
        return obs;
    }

    public void setObs(List<Ob> obs) {
        this.obs = obs;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public List<Object> getEncounterProviders() {
        return encounterProviders;
    }

    public void setEncounterProviders(List<Object> encounterProviders) {
        this.encounterProviders = encounterProviders;
    }
}
