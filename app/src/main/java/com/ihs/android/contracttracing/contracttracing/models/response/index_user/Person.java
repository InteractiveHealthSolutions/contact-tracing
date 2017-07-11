package com.ihs.android.contracttracing.contracttracing.models.response.index_user;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/19/2017.
 */

public class Person {
    private String uuid;
    private String display;
    private String gender;
    private Integer age;
    private String birthdate;
    private Boolean birthdateEstimated;
    private Boolean dead;
    private Object deathDate;
    private Object causeOfDeath;
    private Object preferredAddress;
    private List<Name> names = null;
    private List<Object> addresses = null;
    private List<Object> attributes = null;
    private Boolean voided;

    private Boolean deathdateEstimated;
    private Object birthtime;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getBirthdateEstimated() {
        return birthdateEstimated;
    }

    public void setBirthdateEstimated(Boolean birthdateEstimated) {
        this.birthdateEstimated = birthdateEstimated;
    }

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    public Object getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Object deathDate) {
        this.deathDate = deathDate;
    }

    public Object getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(Object causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public Object getPreferredAddress() {
        return preferredAddress;
    }

    public void setPreferredAddress(Object preferredAddress) {
        this.preferredAddress = preferredAddress;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public List<Object> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Object> addresses) {
        this.addresses = addresses;
    }

    public List<Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public Boolean getDeathdateEstimated() {
        return deathdateEstimated;
    }

    public void setDeathdateEstimated(Boolean deathdateEstimated) {
        this.deathdateEstimated = deathdateEstimated;
    }

    public Object getBirthtime() {
        return birthtime;
    }

    public void setBirthtime(Object birthtime) {
        this.birthtime = birthtime;
    }
}
