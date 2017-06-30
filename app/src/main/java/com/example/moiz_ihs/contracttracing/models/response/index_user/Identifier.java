package com.example.moiz_ihs.contracttracing.models.response.index_user;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/19/2017.
 */

public class Identifier {
    private String display;
    private String uuid;
    private String identifier;
    private Boolean preferred;

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Boolean getPreferred() {
        return preferred;
    }

    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }
}
