package com.example.moiz_ihs.contracttracing.models;

import java.util.ArrayList;

/**
 * Created by Moiz-IHS on 6/21/2017.
 */

public class InvestigationForm {
    private ContactDetail contactDetail;
    ArrayList<String[]> observations;

    public ContactDetail getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(ContactDetail contactDetail) {
        this.contactDetail = contactDetail;
    }

    public ArrayList<String[]> getObservations() {
        return observations;
    }

    public void setObservations(ArrayList<String[]> observations) {
        this.observations = observations;
    }
}
