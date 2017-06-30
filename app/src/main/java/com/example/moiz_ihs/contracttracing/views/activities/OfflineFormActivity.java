package com.example.moiz_ihs.contracttracing.views.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.moiz_ihs.contracttracing.App;
import com.example.moiz_ihs.contracttracing.models.ContactDetail;
import com.example.moiz_ihs.contracttracing.models.gfatm_model.Patient;
import com.example.moiz_ihs.contracttracing.models.response.offline_payloads.EncounterModel;
import com.example.moiz_ihs.contracttracing.models.response.offline_payloads.PatientModel;
import com.example.moiz_ihs.contracttracing.models.response.offline_payloads.PersonModel;
import com.example.moiz_ihs.contracttracing.utils.DatabaseUtil;
import com.example.moiz_ihs.contracttracing.utils.ServerService;
import com.example.moiz_ihs.contracttracing.views.activities.databinding.ActivitySaveOfflineBinding;
import com.example.moiz_ihs.contracttracing.views.adapters.OfflineFormAdapter;
import com.google.gson.Gson;

import org.joda.time.LocalDate;
import org.joda.time.Years;

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
        List<PersonModel> personList = new ArrayList<PersonModel>();

        Gson gson = new Gson();
        for(int i= 0 ; i < offlinePerson.length;i++)
        {

            PersonModel personDetails =  gson.fromJson(offlinePerson[i][0].toString(), PersonModel.class);
            PatientModel patientDetails = gson.fromJson(offlinePatient[i][0].toString(),PatientModel.class);

            personDetails.setIdentifier(patientDetails.getIdentifiers().get(0).getIdentifier());
            personList.add(personDetails);

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

            for(int i= 0; i< offlinePatient.length;i++)
            {
                serverService.submitOfflineForm(offlinePatient[i][0].toString());
            }

            addPatientsToEncounters();

            for(int i = 0;i< ids.length;i++)
            {
                contactIdentifier   = serverService.getPatientIdentifierBySystemIdLocalDB(ids[i][0].toString());
                indexIdentifier = contactIdentifier.substring(0,10);
                createRelationAndAttributes(contactIdentifier,indexIdentifier);
            }



            for(int i= 0; i< formEncounter.length;i++)
            {
               result = serverService.submitOfflineForm(formEncounter[i][0].toString());
            }

            if(result.equals("SUCCESS"))
            {
                Toast.makeText(OfflineFormActivity.this, "All forms are submitted successfully", Toast.LENGTH_SHORT).show();
                finish();
            }


            if(result.equals("CONNECTION_ERROR"))
                Toast.makeText(OfflineFormActivity.this, "Kindly connect to internet first", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(OfflineFormActivity.this, ""+result, Toast.LENGTH_SHORT).show();

            adapter.notifyDataSetChanged();
          

        }
    }

    private void addPatientsToEncounters() {
        List<EncounterModel> encounters = new ArrayList<>();
        Gson gson = new Gson();
        Object object[][] =   serverService.getOfflineFormContentAndPatientId(FORM_CONTACT_INVESTIGATION);
        for(int i= 0;i<object.length ; i++)
        {
            EncounterModel model = gson.fromJson(object[i][0].toString(),EncounterModel.class)  ;
            String patientId = object[i][1].toString();
            Patient patient = serverService.getPatientBySystemIdFromLocalDB(patientId);

            model.setPatient(serverService.getPatientUuid(patient.getPatientId()));
            String content = gson.toJson(model);
            serverService.updateEncounterContents(content,patientId);

        }
    }

    private void createRelationAndAttributes(String contactIdentifier,String indexIdentifier)
    {


        Object[][] ids = serverService.getOfflinePatientIds();

//        for(int i = 0;i< ids.length;i++)
//        {
//           contactIdentifier   = serverService.getPatientIdentifierBySystemIdLocalDB(ids[i][0].toString());
//           indexIdentifier = contactIdentifier.substring(0,10);
//
//
//        }

        String contactUuid = serverService.getPatientUuid(contactIdentifier);
        String personAttribute = serverService.savePersonAttribute("Index Case Number", indexIdentifier, contactUuid);


        String indexUuid = serverService.getPatientUuid(indexIdentifier);

        String relationMessage = serverService.saveRelationBetweenPatient(indexUuid, RELATIONSHIP_TYPE, contactUuid);


    }

}
