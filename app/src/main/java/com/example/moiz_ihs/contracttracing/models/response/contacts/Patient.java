package com.example.moiz_ihs.contracttracing.models.response.contacts;

/**
 * Created by Moiz-IHS on 6/17/2017.
 */


/**
 * This patient is contact
 */

public class Patient {
    private String uuid;
    private String display;

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
}


