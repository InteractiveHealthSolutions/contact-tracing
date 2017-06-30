package com.example.moiz_ihs.contracttracing.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.moiz_ihs.contracttracing.models.InvestigationForm;
import com.example.moiz_ihs.contracttracing.models.gfatm_model.PersonAttributeType;
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
