package com.ihs.android.contracttracing.contracttracing.views.activities;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.PaintCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.ihs.android.contracttracing.contracttracing.App;
import com.ihs.android.contracttracing.contracttracing.models.ContactDetail;
import com.ihs.android.contracttracing.contracttracing.models.gfatm_model.Patient;
import com.ihs.android.contracttracing.contracttracing.models.gfatm_model.Person;
import com.ihs.android.contracttracing.contracttracing.models.response.offline_payloads.EncounterModel;
import com.ihs.android.contracttracing.contracttracing.models.response.offline_payloads.Name;
import com.ihs.android.contracttracing.contracttracing.models.response.offline_payloads.PatientModel;
import com.ihs.android.contracttracing.contracttracing.models.response.offline_payloads.PersonModel;
import com.ihs.android.contracttracing.contracttracing.utils.Constants;
import com.ihs.android.contracttracing.contracttracing.utils.Metadata;
import com.ihs.android.contracttracing.contracttracing.utils.ServerService;

import com.ihs.android.contracttracing.contracttracing.views.adapters.OfflineFormAdapter;
import com.google.gson.Gson;
import com.ihs.android.contracttracing.contracttracing.views.fragments.LoaderFragment;
import com.ihs.android.contracttracing.views.activities.R;
import com.ihs.android.contracttracing.views.activities.databinding.ActivitySaveOfflineBinding;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OfflineFormActivity extends AppCompatActivity {

    private ActivitySaveOfflineBinding binding;
    private List<ContactDetail> list = new ArrayList<ContactDetail>();

    private final String FORM_CONTACT_INVESTIGATION = "CONTACT INVESTIGATION";
    private final String FORM_CREATE_PATIENT = "CREATE PATIENT";
    private final String FORM_CREATE_PERSON = "CREATE PERSON";
    private ServerService serverService;
    private OfflineFormAdapter adapter;
    private static String RELATIONSHIP_TYPE = "Index";

    private boolean isFormSubmitting =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_save_offline);
        serverService = new ServerService(OfflineFormActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerSave.setLayoutManager(layoutManager);
        binding.uploadButton.setOnClickListener(new Uploader());
        adapter = new OfflineFormAdapter(list);
        binding.recyclerSave.setAdapter(adapter);


        fetchLocalData();

    }

    private void fetchLocalData() {
       // addPatientsToEncounters();

        Object[][] offlinePerson =   serverService.getOfflineFormContent(FORM_CREATE_PERSON);
        Object[][] offlinePatient =   serverService.getOfflineFormContent(FORM_CREATE_PATIENT);
        Object[][] offlineEncounters =   serverService.getOfflineFormContent(FORM_CONTACT_INVESTIGATION);

        List<PersonModel> personList = new ArrayList<PersonModel>();

        Gson gson = new Gson();

        if(offlineEncounters.length == offlinePerson.length) {
            for (int i = 0; i < offlinePerson.length; i++) {

                PersonModel personDetails = gson.fromJson(offlinePerson[i][0].toString(), PersonModel.class);
                PatientModel patientDetails = gson.fromJson(offlinePatient[i][0].toString(), PatientModel.class);

                personDetails.setIdentifier(patientDetails.getIdentifiers().get(0).getIdentifier());
                personList.add(personDetails);

            }
        }
        else
        {
            for (int i = 0; i < offlineEncounters.length; i++) {

                String json = offlineEncounters[i][0].toString();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                   String patientUuid =  jsonObject.get("patient").toString();
                  Patient patient = serverService.getPatientByUuidFromLocalDB(patientUuid);
//                    if(patient == null)
//                    {
//                        patient =  serverService.getPatientByIdentifierFromLocalDB(patientUuid);
//                    }
                    if(patient != null) {
                        PersonModel personModel = new PersonModel();
                        personModel.setBirthdate(patient.getPerson().getBirthdate());
                        personModel.setGender(patient.getPerson().getGender());
                        if (patient.getExternalId() == null) {
                            personModel.setIdentifier(patient.getPatientId());
                        } else {
                            personModel.setIdentifier(patient.getExternalId());
                        }
                        List<Name> name = new ArrayList<Name>();
                        Name names = new Name();
                        names.setGivenName(patient.getPerson().getGivenName());
                        names.setFamilyName(patient.getPerson().getFamilyName());
                        //names.setMiddleName(patient.getPerson().getName);
                        name.add(names);
                        personModel.setNames(name);
                        personList.add(personModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            for(int i = 0; i < offlinePatient.length; i++)
            {
                String json = offlinePatient[i][0].toString();

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String patientUuid =  jsonObject.get("person").toString();
                    if(patientUuid.equals("uuid-replacement-string"))
                    {

                        PersonModel personDetails = gson.fromJson(offlinePerson[i][0].toString(), PersonModel.class);
                        PatientModel patientDetails = gson.fromJson(offlinePatient[i][0].toString(), PatientModel.class);

                        personDetails.setIdentifier(patientDetails.getIdentifiers().get(0).getIdentifier());
                        personList.add(personDetails);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }






        for(PersonModel person : personList)
        {
            String name = person.getNames().get(0).getGivenName() +" "+ person.getNames().get(0).getFamilyName();
            String age = calculateAgeFromDob(person.getBirthdate());

            updateDataModel(name,person.getIdentifier(),age,person.getGender(),false);

        }

       adapter.notifyDataSetChanged();

    }

    private String calculateAgeFromDob(String birthdate) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = df.parse(birthdate);
          Calendar calendar = Calendar.getInstance();
           calendar.setTime(startDate);
            int year = calendar.get(Calendar.YEAR);

            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            LocalDate birthday = new LocalDate (year, month, day);
            LocalDate now = new LocalDate();
            Years age = Years.yearsBetween(birthday, now);

            return String.valueOf(age.getYears());

        } catch (ParseException e) {
            e.printStackTrace();
        }

return "";


    }


    private void updateDataModel(String name, String contactId, String age, String gender,boolean isFormCompleted) {
        ContactDetail detail = new ContactDetail();
        detail.setContactName(name);
        detail.setContactId(contactId);

        detail.setAge(age);
        detail.setGender(gender);
        //detail.setIndexName(patient.getPerson().getGivenName());
        //detail.setIndexId(id);
        detail.setContactFormCompleted(isFormCompleted);
        list.add(detail);


        //ContentProvider provider = new Con
    }
    private class Uploader implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            SubmitOffline offlinetask = new SubmitOffline();
            offlinetask.execute("");



        }
    }

    private String uploadForms() {
        App.setMode("ONLINE");
        String result = "";
        String contactIdentifier,indexIdentifier = "";

        Object[][] offlinePerson =   serverService.getOfflineFormIds(FORM_CREATE_PERSON);
        Object[][] offlinePatient =   serverService.getOfflineFormIds(FORM_CREATE_PATIENT);
        Object[][] formEncounter =   serverService.getOfflineFormIds(FORM_CONTACT_INVESTIGATION);
        Object[][] ids = serverService.getOfflinePatientIds();




        for(int i= 0; i< offlinePerson.length;i++)
        {
            serverService.submitOfflineForm(offlinePerson[i][0].toString());
        }

//        for(int i= 0; i< offlinePatient.length;i++)
//        {
//            serverService.submitOfflineForm(offlinePatient[i][0].toString());
//        }



        for(int i = 0;i< ids.length;i++)
        {
            contactIdentifier   = serverService.getPatientIdentifierBySystemIdLocalDB(ids[i][0].toString());
            if(contactIdentifier.length()>14)
            {
                contactIdentifier = serverService.getContactPatientIdentifierBySystemIdLocalDB(ids[i][0].toString());
            }
          //  contactIdentifier   = ids[i][0].toString();

            indexIdentifier = contactIdentifier.substring(0,10);
            createRelationAndAttributes(contactIdentifier,indexIdentifier);


        }


            addPatientsToEncounters();


        for(int i= 0; i< formEncounter.length;i++)
        {
            result = serverService.submitOfflineForm(formEncounter[i][0].toString());
            if(result.equals("SUCCESS"))
            {
//                if(offlinePerson[i][0] !=null)
//                    serverService.deleteOfflineUsers(offlinePerson[i][0].toString());
            }
        }

        return result;


    }

    private void addPatientsToEncounters() {
        List<EncounterModel> encounters = new ArrayList<>();
        Gson gson = new Gson();
        Object object[][] =   serverService.getOfflineFormContentAndPatientId(FORM_CONTACT_INVESTIGATION);
        for(int i= 0;i<object.length ; i++)
        {
            EncounterModel model = gson.fromJson(object[i][0].toString(),EncounterModel.class)  ;
            String patientId = object[i][1].toString();

            String patientUuid = serverService.getPatientUuid(model.getPatient());

            if( patientUuid != null) {

               //Patient patient = serverService.getPatientByIdentifierFromLocalDB(patientId); //PatientBySystemIdFromLocalDB(patientId);
                 model.setPatient(patientUuid);
                String content = gson.toJson(model);
                serverService.updateEncounterContents(content, patientId);
            }
//            else
//            {
//                serverService.getPatientByIdeni
//
//            }

        }
    }

    private void createRelationAndAttributes(String contactIdentifier,String indexIdentifier)
    {


     //   Object[][] ids = serverService.getOfflinePatientIds();

        //com.ihs.android.contracttracing.contracttracing.models.gfatm_model.Patient contact =  serverService.getIndexPatientByIdentifierFromLocalDB(contactIdentifier);
        // String contactUuid = serverService.getPatientUuid(contactDetail.getContactId());
        //String contactUuid = contact.getUuid();
        String contactUuid = serverService.getPatientUuid(contactIdentifier);
        String personAttribute = serverService.savePersonAttribute("Contact Index Case Number", indexIdentifier, contactUuid);

        com.ihs.android.contracttracing.contracttracing.models.gfatm_model.Patient index = serverService.getIndexPatientByIdentifierFromLocalDB(indexIdentifier);
        String indexUuid = index.getUuid();

        String relationMessage = serverService.saveRelationBetweenPatient(indexUuid, RELATIONSHIP_TYPE, contactUuid);

    }


    private class SubmitOffline extends AsyncTask<String ,String,String >
    {

        @Override
        protected String doInBackground(String... params) {
            startLoader();
            isFormSubmitting = true;
            return uploadForms();
        }

        @Override
        protected void onPostExecute(String result) {
            removeLoader();
            isFormSubmitting = false;
            if(result.equals("SUCCESS"))
            {
                Toast.makeText(OfflineFormActivity.this, "All forms are submitted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }


            if(result.equals("CONNECTION_ERROR"))
                Toast.makeText(OfflineFormActivity.this, "Kindly connect to internet first", Toast.LENGTH_SHORT).show();
            else
                if(result.equals(""))
                    Toast.makeText(OfflineFormActivity.this, "Nothing to post", Toast.LENGTH_SHORT).show();
                 else
                    Toast.makeText(OfflineFormActivity.this, ""+result, Toast.LENGTH_SHORT).show();

            adapter.notifyDataSetChanged();

        }
    }

    private void startLoader() {
        LoaderFragment loaderFragment = new LoaderFragment("Uploading Offline Forms\n Please Wait...");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.offline_form_container,loaderFragment, Constants.OFFLINE_FORM_LOADER_TAG).commit();
    }


    private void removeLoader()
    {


        try
        {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.OFFLINE_FORM_LOADER_TAG);
            if(fragment !=null && fragment.isVisible()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }

            isFormSubmitting = false;
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            isFormSubmitting = false;
            // Log.e("crash", "removeLoader:"  + e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            isFormSubmitting = false;
        }

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.OFFLINE_FORM_LOADER_TAG);
        if(!isFormSubmitting)
            super.onBackPressed();
    }

}
