package com.example.moiz_ihs.contracttracing.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.moiz_ihs.contracttracing.App;
import com.example.moiz_ihs.contracttracing.utils.CommonUtils;
import com.example.moiz_ihs.contracttracing.views.activities.R;
import com.example.moiz_ihs.contracttracing.views.activities.databinding.ActivityContactTraceBinding;
import com.example.moiz_ihs.contracttracing.models.ContactDetail;
import com.example.moiz_ihs.contracttracing.models.response.contacts.ContactFormResponse;
import com.example.moiz_ihs.contracttracing.models.response.index_user.IndexUserResponse;
import com.example.moiz_ihs.contracttracing.utils.Constants;
import com.example.moiz_ihs.contracttracing.utils.HttpGet;
import com.example.moiz_ihs.contracttracing.utils.ServerService;
import com.example.moiz_ihs.contracttracing.views.adapters.ContactsAdapter;
import com.example.moiz_ihs.contracttracing.views.fragments.LoaderFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContactTraceActivity extends AppCompatActivity {

    private ActivityContactTraceBinding binding;
    private List<ContactDetail> list = new ArrayList<ContactDetail>();
    private ContactsAdapter adapter;
    private Boolean isSearched = false;

    private Gson gson;
    private String id;
   // DatabaseUtil dbUtils;
    private IndexUserResponse indexUser;
    private com.example.moiz_ihs.contracttracing.models.gfatm_model.Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_trace);



        gson = new Gson();





        // recyclerView =  binding.recycler;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        binding.recycler.setLayoutManager(layoutManager);
        adapter = new ContactsAdapter(list);
        binding.recycler.setAdapter(adapter);

/**
 * comment these three lines
 */
//        isSearched = true;
//        adapter = new ContactsAdapter(list);
//        binding.recycler.setAdapter(adapter);

        setListeners();

    }

    private void updateDataModel(String name, String cid, String age, String gender,boolean isFormCompleted) {
        ContactDetail detail = new ContactDetail();
        detail.setContactName(name);
        detail.setContactId(cid);

        detail.setAge(age);
        detail.setGender(gender);
        detail.setIndexName(patient.getPerson().getGivenName());
        detail.setIndexId(id);
        detail.setContactFormCompleted(isFormCompleted);
        list.add(detail);


        //ContentProvider provider = new Con
    }

    private void setListeners() {
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //binding.indexId.clearFocus();
                adapter.notifyDataSetChanged();
                startLoader();

                if(binding.indexId.getText().equals(""))
                {
                    Toast.makeText(ContactTraceActivity.this, "Please enter Index Id", Toast.LENGTH_SHORT).show();
                }
                else {

                    id = binding.indexId.getText().toString();
                    list.clear();
                    GetUserFromServer getUserFromServer = new GetUserFromServer();
                    getUserFromServer.execute("");
                }
            }
        });
    }


    private boolean checkSavedData()
    {
        boolean isPatientFound = false;
        ServerService serverService = new ServerService(ContactTraceActivity.this);

        patient = serverService.getPatientByIdentifierFromLocalDB(id);
        if(patient != null)
        {
            isPatientFound =  true;

            binding.indexLayout.parentLayout.setVisibility(View.VISIBLE);
            binding.indexLayout.contactName.setText("Index Name : "+patient.getPerson().getGivenName() +" "+ patient.getPerson().getFamilyName());
            binding.indexLayout.indexId.setText("Gender : "+patient.getPerson().getGender());
            binding.indexLayout.contactId.setText(" Age : "+patient.getPerson().getAge());
        }
        else {
            binding.indexLayout.parentLayout.setVisibility(View.GONE);
        }

        String intid = serverService.getPatientSystemIdByIdentifierLocalDB(id);
        String encounterType = "CONTACT INFORMATION";
        Object[][] encounters = serverService.getLatestEncounter(intid,encounterType);
        Object[][] obs = new Object[0][];

        if (encounters != null || encounters.length == 0) {

            for (int i = 0; i < encounters.length; i++) {
                obs = (Object[][]) serverService.getAllObsFromEncounterId(Integer.parseInt((String) encounters[i][1]));
                if(obs != null) {
                    filterAdapterData(obs);
                }
            }


        }
        else
        {
            Toast.makeText(ContactTraceActivity.this, "No records found", Toast.LENGTH_SHORT).show();
        }
        return isPatientFound;
    }



    private void filterAdapterData(Object[][] obs) {
        String name = "";
        String contactId = "";
        String gender = "";
        String age = "";

        for(int i=0;i<obs.length;i++)
        {
            if(obs[i][1].toString().contains("NAME"))
            {
              //  name = obs[i][1].toString().replace("CONTACT","");   //0 is value and 1 is display of ob
                name = obs[i][0].toString();
            }

            if(obs[i][1].toString().contains("AGE"))
            {
               // age = obs[i][1].toString().replace("CONTACT","");
                age =  obs[i][0].toString();
            }
            if(obs[i][1].toString().contains("ID"))
            {
               // contactId = obs[i][1].toString().replace("CONTACT","");
                contactId = obs[i][0].toString();
            }
            if(obs[i][1].toString().contains("GENDER"))
            {
                //gender = obs[i][1].toString().replace("CONTACT","");
                gender = obs[i][0].toString();

            }


        }

        ServerService serverService = new ServerService(this);
        com.example.moiz_ihs.contracttracing.models.gfatm_model.Patient localPatient = serverService.getPatientByIdentifierFromLocalDB(contactId);
        //String uuid = serverService.getPatientIdentifierBySystemIdLocalDB(contactId);

        boolean isFormCompleted = false;
      if(localPatient == null)
      {
          isFormCompleted =false;
      }
      else
      {
          if(localPatient.getPerson() == null)
              isFormCompleted = false;
          else
              isFormCompleted = true;

      }


        if(!isFormCompleted)
        {

           String message = serverService.getPatient(contactId,false);
            if(message != null ) {
                if (message.equalsIgnoreCase("SUCCESS")) {

                    String firstName, lastName = "";
                    if (name.contains(" ")) {
                        String[] splited = name.split("\\s+");
                        firstName = splited[0];
                        lastName = splited[1];
                    } else {
                        firstName = name;
                        lastName = "test";
                    }

                    Calendar now = Calendar.getInstance();

                    int yearOfBirth = (int) Double.parseDouble(age);

                    now.add(Calendar.YEAR, -yearOfBirth);

                    serverService.savePatientLocally(contactId, firstName, lastName, gender, App.getSqlDate(now.getTime()));
                    isFormCompleted = true;
                }
            }

        }

            updateDataModel(name, contactId, age, gender,isFormCompleted);
        if(list.size()> 0) {
            isSearched = true;
            adapter.notifyDataSetChanged();
           // binding.recycler.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(this, "No Contacts Available", Toast.LENGTH_SHORT).show();
        }
       // }

    }


    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.SEARCH_CONTACT_LOADER_TAG);
        if(fragment !=null && fragment.isVisible()) {
            removeLoader();
        }
        else {
            super.onBackPressed();
        }
    }


    public void updateListItem(int position) {
        ((ContactsAdapter) adapter).data.get(position).setContactFormCompleted(true);
        adapter.notifyDataSetChanged();
    }
    


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                updateListItem(data.getIntExtra("result", 0));
        }
    }


    private class GetUserFromServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            ServerService serverService = new ServerService(ContactTraceActivity.this);
            startLoader();
            return serverService.getPatient(id, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            removeLoader();
            if(result == null || !result.equals("SUCCESS"))
            {
                if(CommonUtils.isNetworkConnected(ContactTraceActivity.this))
                    Toast.makeText(ContactTraceActivity.this, "Patient not found", Toast.LENGTH_SHORT).show();
            }

            if(!checkSavedData()) {
                if (result == null || result.equals("")) {
                    Toast.makeText(ContactTraceActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    // return;
                } else if (result.equals("FAIL")) {
                    Toast.makeText(ContactTraceActivity.this, "Patient not Found", Toast.LENGTH_SHORT).show();
                }
            }
//
//            else if(result.equals("SUCCESS"))
//            {
//                checkSavedData();
//            }
        }
    }


    private void startLoader() {
        LoaderFragment loaderFragment = new LoaderFragment("Searching for contacts Please Wait...");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contact_trace_containers,loaderFragment, Constants.SEARCH_CONTACT_LOADER_TAG).commit();
    }

    private void removeLoader()
    {

        try
        {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.SEARCH_CONTACT_LOADER_TAG);
            if(fragment !=null && fragment.isVisible()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }

        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            // Log.e("crash", "removeLoader:"  + e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }




}