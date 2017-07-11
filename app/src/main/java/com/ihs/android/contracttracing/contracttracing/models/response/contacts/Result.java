package com.ihs.android.contracttracing.contracttracing.models.response.contacts;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/17/2017.
 */

public class Result {
    private String uuid;
    private String display;
    private String encounterDatetime;
    private Patient patient;
    private Object location;
    private List<Ob> obs = null;

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

    public String getEncounterDatetime() {
        return encounterDatetime;
    }

    public void setEncounterDatetime(String encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public List<Ob> getObs() {
        return obs;
    }

    public void setObs(List<Ob> obs) {
        this.obs = obs;
    }
}
