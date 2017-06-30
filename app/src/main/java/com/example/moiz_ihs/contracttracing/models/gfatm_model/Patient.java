/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
/**
 *
 */

package com.example.moiz_ihs.contracttracing.models.gfatm_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author owais.hussain@irdresearch.org
 */
public class Patient extends AbstractModel {
    public static final String FIELDS = "uuid,name";
    private String patientId;
    private String externalId;
    private String enrs;
    private String endTBId;
    private Person person;
    private int pid;

    public Patient(String uuid, String patientId, String externalId, String enrs, String endTBId, Person person) {
        super(uuid);
        this.patientId = patientId;
        this.externalId = externalId;
        this.enrs = enrs;
        this.endTBId = endTBId;
        this.person = person;
    }

    public static Patient parseJSONObject(JSONObject json) {
        Patient patient = null;
        String uuid = "";
        String patientId = "";
        String externalId = "";
        String enrs = "";
        String endTBId = "";

        Person person = null;
        try {
            uuid = json.getString("uuid");
            JSONObject jsonobject = json.getJSONObject("person");
            person = Person.parseJSONObject(jsonobject);
            JSONArray identifiers = json.getJSONArray("identifiers");

            for (int i = 0; i < identifiers.length(); i++) {

                JSONObject object = identifiers.getJSONObject(i);
                String display = object.getString("display");

                if (display.contains("Index Case Number")) {
                    patientId = display.replace("Index Case Number = ", "");
                } else if (display.contains("External ID")) {
                    externalId = display.replace("External ID = ", "");
                } else if (display.contains("ENRS")) {
                    enrs = display.replace("ENRS = ", "");
                } else if (display.contains("endTB EMR ID")) {
                    endTBId = display.replace("endTB EMR ID = ", "");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            patient = null;
        }
        patient = new Patient(uuid, patientId, externalId, enrs, endTBId, person);
        return patient;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patientId", patientId);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getEnrs() {
        return enrs;
    }

    public void setEnrs(String enrs) {
        this.enrs = enrs;
    }

    public String getEndTBId() {
        return endTBId;
    }

    public void setEndTBId(String endTBId) {
        this.endTBId = endTBId;
    }


    @Override
    public String toString() {
        return super.toString() + ", " + patientId;
    }
}