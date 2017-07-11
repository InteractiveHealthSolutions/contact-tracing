package com.ihs.android.contracttracing.contracttracing.models.response.index_user;

/**
 * Created by Moiz-IHS on 6/19/2017.
 */

public class Name {
    private String display;
    private String uuid;
    private String givenName;
    private Object middleName;
    private String familyName;
    private Object familyName2;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Object getFamilyName2() {
        return familyName2;
    }

    public void setFamilyName2(Object familyName2) {
        this.familyName2 = familyName2;
    }
}
