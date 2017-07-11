package com.ihs.android.contracttracing.contracttracing.models.response.contacts;

/**
 * Created by Moiz-IHS on 6/17/2017.
 */

public class Ob {

    private String uuid;
    private String display;
    private String obsDatetime;
    private Object value;

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

    public String getObsDatetime() {
        return obsDatetime;
    }

    public void setObsDatetime(String obsDatetime) {
        this.obsDatetime = obsDatetime;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
