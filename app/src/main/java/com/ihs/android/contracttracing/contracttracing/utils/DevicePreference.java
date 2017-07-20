package com.ihs.android.contracttracing.contracttracing.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ihs.android.contracttracing.contracttracing.models.InvestigationForm;
import com.ihs.android.contracttracing.contracttracing.models.gfatm_model.PersonAttributeType;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaazAhmad on 6/18/2017.
 */

public class DevicePreference{

    private SharedPreferences preferences ;
    private final String PREF = "mpref";
    public static DevicePreference instance = null;
    private Context context;
    private final String userKey = "user";
    private final String serverAddressKey = "ip";
    private final String serverportKey = "port";
    private final String formKey = "user";
    private List<InvestigationForm> formList;



    private DevicePreference(){}

    public static DevicePreference getInstance()
    {
        if(instance == null)
        {
            instance = new DevicePreference();
        }

        return instance;
    }



    public void init(Context context)
    {
        this.context = context;
        preferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        formList = new ArrayList<InvestigationForm>();

    }

    public void saveUser(PersonAttributeType.UserCredentials credentials)
    {
        Gson gson = new Gson();
        String json = gson.toJson(credentials);
        preferences.edit().putString(userKey,json).commit();
    }

    public PersonAttributeType.UserCredentials getUser()
    {
        Gson gson = new Gson();
       String json = preferences.getString(userKey,"");
        return  gson.fromJson(json,PersonAttributeType.UserCredentials.class);
    }

    public String getServerAddress()
    {
        String address = preferences.getString(serverAddressKey,"");

        return address;
    }

    public void setServerPort(String serverAddress)
    {
        preferences.edit().putString(serverportKey,serverAddress).commit();
    }

    public String getServerPort()
    {
        String address = preferences.getString(serverportKey,"");

        return address;
    }

    public void setServerAddress(String serverAddress)
    {
        preferences.edit().putString(serverAddressKey,serverAddress).commit();
    }


//    public List<InvestigationForm> getFormList()
//    {
//        Gson gson = new Gson();
//        String json = preferences.getString(formKey,"");
//        //return  gson.fromJson(json,.class);
//    }
//
//    public void saveFormList(InvestigationForm form)
//    {
//        formList.add(form);
//        Gson gson = new Gson();
//       // gson.toJson(InvestigationForm,);
//
//    }

}
