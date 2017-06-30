package com.example.moiz_ihs.contracttracing.models.response.offline_payloads;

/**
 * Created by Moiz-IHS on 6/22/2017.
 */

public class Name {
    private String givenName;
    private String middleName;
    private String familyName;
    private Boolean voided;

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

}
