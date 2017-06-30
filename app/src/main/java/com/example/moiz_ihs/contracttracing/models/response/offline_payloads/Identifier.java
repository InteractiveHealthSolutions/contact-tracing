package com.example.moiz_ihs.contracttracing.models.response.offline_payloads;

/**
 * Created by Moiz-IHS on 6/22/2017.
 */

public class Identifier {
    private String identifier;
    private String identifierType;
    private Boolean preferred;
    private Boolean voided;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public Boolean getPreferred() {
        return preferred;
    }

    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

}
