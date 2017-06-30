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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author owais.hussain@irdresearch.org
 */
public class PersonAttributeType extends AbstractModel {
    public static final String FIELDS = "uuid,display";
    private String name = "";

    public PersonAttributeType(String uuid, String name) {
        super(uuid);
        this.name = name;
    }

    public static PersonAttributeType parseJSONObject(JSONObject json) {
        PersonAttributeType encounterType = null;
        String uuid = "";
        String name = "";
        try {
            uuid = json.getString("uuid");
            name = json.getString("display");
        } catch (JSONException e) {
            e.printStackTrace();
            encounterType = null;
        }
        encounterType = new PersonAttributeType(uuid, name);
        return encounterType;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", super.getUuid());
            jsonObject.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + name;
    }

    /**
     * Created by MaazAhmad on 6/18/2017.
     */

    public static class UserCredentials {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}