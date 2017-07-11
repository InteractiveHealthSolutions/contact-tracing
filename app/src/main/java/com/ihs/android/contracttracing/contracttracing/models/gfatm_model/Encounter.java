package com.ihs.android.contracttracing.contracttracing.models.gfatm_model;

/**
 * Created by Rabbia on 1/9/2017.
 */

import android.content.Context;


import com.ihs.android.contracttracing.contracttracing.App;
import com.ihs.android.contracttracing.contracttracing.utils.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

public class Encounter extends AbstractModel {
    private String encounterType;
    private String encounterDatetime;
    private String encounterLocation;
    private String patientId;
    private String dateCreated;
    private ArrayList<Obs> obsGroup;

    public Encounter(String uuid, String encounterType, String encounterDatetime, ArrayList<Obs> obsGroup, String encounterLocation, String dateCreated) {
        super(uuid);
        this.encounterType = encounterType;
        this.encounterDatetime = encounterDatetime;
        this.obsGroup = obsGroup;
        this.encounterLocation = encounterLocation;
        this.dateCreated = dateCreated;
    }

    public static Encounter parseJSONObject(JSONObject json, Context context) {
        Encounter encounter = null;
        String uuid = "";
        String encounterType = "";
        String encounterDatetime = "";
        String encounterLocation = "";
        String dateCreated = "";
        ArrayList<Obs> obsGroup = new ArrayList<>();
        try {
            uuid = json.getString("uuid");
            String display = json.getString("display");
            encounterDatetime = display.substring(display.length() - 10, display.length());
            encounterType = display.replace(" " + encounterDatetime, "");
           // JSONObject locationObject = json.getJSONObject("location");
            //encounterLocation = locationObject.getString("display");
            JSONObject patientObject = json.getJSONObject("patient");
            String patientUuid = patientObject.getString("uuid");
            if(json.has("auditInfo"))
                dateCreated  = json.getJSONObject("auditInfo").getString("dateCreated");
            else
                dateCreated = App.getSqlDateTime(new Date());
            JSONArray obsArray = json.getJSONArray("obs");
            for (int i = 0; i < obsArray.length(); i++) {
                JSONObject jsonObject = obsArray.getJSONObject(i);
                Obs obs = Obs.parseJSONObject(jsonObject);
                if (obs != null)
                    obsGroup.add(obs);
                else {
                    if (jsonObject.has("groupMembers")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("groupMembers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonA = jsonArray.getJSONObject(j);
                            Obs obs1 = Obs.parseJSONObject(jsonA);
                            if (obs1 != null)
                                obsGroup.add(obs1);
                        }
                    } else { //post returns doesn't indlude group members :/
                        HttpGet httpGet = new HttpGet(App.getIp(), App.getPort(), context);
                        JSONArray linkJsonArray = jsonObject.getJSONArray("links");
                        JSONObject linkObject = linkJsonArray.getJSONObject(0);
                        String link = linkObject.getString("uri");
                        String[] linkArray = link.split("/");
                        JSONObject observation = httpGet.getObservationByUuid(linkArray[linkArray.length - 1]);
                        JSONArray jsonArray = observation.getJSONArray("groupMembers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonA = jsonArray.getJSONObject(j);
                            Obs obs1 = Obs.parseJSONObject(jsonA);
                            if (obs1 != null)
                                obsGroup.add(obs1);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            encounter = null;
        }
        encounter = new Encounter(uuid, encounterType, encounterDatetime, obsGroup, encounterLocation, dateCreated);
        return encounter;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", super.getUuid());
            jsonObject.put("encounterType", encounterType);
            jsonObject.put("encounterDatetime", encounterDatetime);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(String encounterType) {
        this.encounterType = encounterType;
    }

    public String getEncounterLocation() {
        return encounterLocation;
    }

    public void setEncounterLocation(String encounterLocation) {
        this.encounterLocation = encounterLocation;
    }

    public String getEncounterDatetime() {
        return encounterDatetime;
    }

    public void setEncounterDatetime(String encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }

    public ArrayList<Obs> getObsGroup() {
        return obsGroup;
    }

    public void setObsGroup(ArrayList<Obs> obsGroup) {
        this.obsGroup = obsGroup;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + encounterType;
    }
}