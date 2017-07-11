package com.ihs.android.contracttracing.contracttracing.views.activities;

import android.app.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.ihs.android.contracttracing.contracttracing.App;
import com.ihs.android.contracttracing.contracttracing.utils.CommonUtils;
import com.ihs.android.contracttracing.contracttracing.utils.ServerService;
import com.ihs.android.contracttracing.contracttracing.views.fragments.LoaderFragment;
import com.ihs.android.contracttracing.views.activities.R;

import com.ihs.android.contracttracing.contracttracing.models.ContactDetail;
import com.ihs.android.contracttracing.contracttracing.utils.Constants;
import com.ihs.android.contracttracing.contracttracing.utils.Validator;
import com.google.gson.Gson;
import com.ihs.android.contracttracing.views.activities.databinding.FragmentContactFormBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;


public class ContactFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private FragmentContactFormBinding binding;
    private int contactPosition;
    private ContactDetail contactDetail;
    private   Calendar screenCalendar;
    private int age  = 0;
    private ArrayList<String[]> observations;
    private static String RELATIONSHIP_TYPE = "Index";
    private boolean isFormSubmitting = false;
    private String firstName,lastName  =  "";
    private Calendar now;


    public ContactFormActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.fragment_contact_form);

        String contactDetailJson = getIntent().getStringExtra("contactDetail");
        Gson gson = new Gson();
        contactDetail = gson.fromJson(contactDetailJson,ContactDetail.class);
        contactPosition = getIntent().getIntExtra("position",0);
        age = (int) Double.parseDouble(contactDetail.getAge());
        updateFormAccordingToAge();
        initHeader();
        initSpinners();
        setListeners();
    }

    private void updateFormAccordingToAge() {
        if(age >14)
        {
            binding.weightLossLayout.setVisibility(View.GONE);
            binding.weightLossPeriodLayout.setVisibility(View.GONE);
        }
        else {
            binding.weightLossAdultLayout.setVisibility(View.GONE);
        }

    }

    private void initHeader() {
        binding.contactName.setText(contactDetail.getContactName());
        binding.contactId.setText(contactDetail.getContactId());
        binding.indexName.setText(contactDetail.getIndexName());
        binding.indexId.setText(contactDetail.getIndexId());


        if(contactDetail.getGender().equals("MALE"))
        {
            if(age >16)
            {
                binding.contactImage.setImageResource(R.drawable.boy);
            }
            else
            {
                binding.contactImage.setImageResource(R.drawable.boy);
            }
        }
        else
        {
            if(age >16)
            {
                binding.contactImage.setImageResource(R.drawable.girl);
            }
            else
            {
                binding.contactImage.setImageResource(R.drawable.girl);
            }

        }
    }

    private void initSpinners() {
        SpinnerAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.cough_duration_array));
        binding.coughDuration.setAdapter(adapter);


        SpinnerAdapter adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.weight_loss_array));
        binding.weightlossPeriod.setAdapter(adapter2);


    }

    private void setListeners() {
        binding.cough.setOnCheckedChangeListener(new CustomCheckedListener());
        binding.fever.setOnCheckedChangeListener(new CustomCheckedListener());
        binding.weightLossChild.setOnCheckedChangeListener(new CustomCheckedListener());
        binding.contactFound.setOnCheckedChangeListener(new CustomCheckedListener());
        binding.contactReferred.setOnCheckedChangeListener(new CustomCheckedListener());

        binding.saveForm.setOnClickListener(new CustomClickListener());
        binding.datePicker.setOnClickListener(new CustomClickListener());

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        screenCalendar = Calendar.getInstance();
        screenCalendar.set(Calendar.YEAR,year);
        screenCalendar.set(Calendar.MONTH,monthOfYear);
        screenCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);


       // String date = ""+year+"-"+dayOfMonth+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        String date = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        binding.date.setText(date);
    }



    private class CustomCheckedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
           if(group.equals(binding.cough))
           {
               RadioButton radio = (RadioButton)group.findViewById(checkedId);
                if(radio.isChecked())
               {
                   if(radio.getText().equals("No"))
                   {
                       binding.coughDetailLayout.setVisibility(View.GONE);
                   }
                   else
                   {
                       binding.coughDetailLayout.setVisibility(View.VISIBLE);
                   }

               }
           }

            if(group.equals(binding.fever))
            {
                RadioButton radio = (RadioButton)group.findViewById(checkedId);
                if(radio.isChecked())
                {
                    if(radio.getText().equals("No"))
                    {
                        binding.feverDuration.setVisibility(View.GONE);
                    }
                    else
                    {
                        binding.feverDuration.setVisibility(View.VISIBLE);
                    }

                }
            }

            if(group.equals(binding.weightLossChild))
            {
                RadioButton radio = (RadioButton)group.findViewById(checkedId);
                if(radio.isChecked())
                {
                    if(radio.getText().equals("No"))
                    {
                        binding.weightLossPeriodLayout.setVisibility(View.GONE);
                    }
                    else
                    {
                        binding.weightLossPeriodLayout.setVisibility(View.VISIBLE);
                    }

                }
            }

            if(group.equals(binding.contactFound))
            {
                RadioButton radio = (RadioButton)group.findViewById(checkedId);
                if(radio.isChecked())
                {
                    if(radio.getText().equals("No"))
                    {
                        binding.layoutForm.setVisibility(View.GONE);
                    }
                    else
                    {
                        binding.layoutForm.setVisibility(View.VISIBLE);
                    }

                }
            }

            if(group.equals(binding.contactReferred))
            {
                RadioButton radio = (RadioButton)group.findViewById(checkedId);
                if(radio.isChecked())
                {
                    if(radio.getText().equals("No"))
                    {
                        binding.layoutReferralReason.setVisibility(View.GONE);
                    }
                    else
                    {
                        binding.layoutReferralReason.setVisibility(View.VISIBLE);
                    }

                }
            }


        }
    }


    private String getRadioGroupSelectedText(RadioGroup group)
    {
        RadioButton button = (RadioButton)findViewById(group.getCheckedRadioButtonId());

    return button.getText().toString();
    }

    private class CustomClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.equals(binding.saveForm)) {


                validateForm();
            }

            if(v.equals(binding.datePicker))
            {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(ContactFormActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)

                );
                dpd.setMaxDate(Calendar.getInstance());
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }

           //
        }
    }

    private void validateForm() {
        boolean allClear = true;

        Validator validator = new Validator();

        if(validator.validateRadioGroupEmpty(binding.contactFound))
        {
            allClear = false;
            Toast.makeText(this, "Contact found is empty", Toast.LENGTH_SHORT).show();
        }
        else if(getRadioGroupSelectedText(binding.contactFound).equals("Yes")){
            if (validator.validateRadioGroupEmpty(binding.cough) || validator.validateRadioGroupEmpty(binding.fever) || validator.validateRadioGroupEmpty(binding.nightSweat) || validator.validateRadioGroupEmpty(binding.swelling) || validator.validateRadioGroupEmpty(binding.tbBefore) || validator.validateRadioGroupEmpty(binding.hivDone) || validator.validateRadioGroupEmpty(binding.hivResult) || validator.validateRadioGroupEmpty(binding.contactReferred) || validator.validateRadioGroupEmpty(binding.symptomScreen)) {
                allClear = false;
                Toast.makeText(this, "Some fields are Empty", Toast.LENGTH_SHORT).show();
            }

            if (!validator.validateRadioGroupEmpty(binding.cough)) {
                if (getRadioGroupSelectedText(binding.cough).equals("Yes")) {
                    if (validator.validateRadioGroupEmpty(binding.coughBlood)) {
                        allClear = false;
                        Toast.makeText(this, "Cough Blood is Empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (!validator.validateRadioGroupEmpty(binding.cough)) {
                if (getRadioGroupSelectedText(binding.cough).equals("Yes")) {
                    if (validator.validateRadioGroupEmpty(binding.coughBlood)) {
                        allClear = false;
                        Toast.makeText(this, "Cough Blood is Empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            if (!validator.validateRadioGroupEmpty(binding.contactReferred)) {
                if (getRadioGroupSelectedText(binding.contactReferred).equals("Yes")) {
                    if (!binding.ipt.isChecked() && !binding.furtherTbAssessment.isChecked() && !binding.hivTesting.isChecked()) {
                        allClear = false;
                        Toast.makeText(this, "Select atleast one referral reason", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (!validator.validateRadioGroupEmpty(binding.fever)) {
                if (getRadioGroupSelectedText(binding.fever).equals("Yes")) {
                    if (binding.feverDuration.getText().toString().equals("")) {
                        allClear = false;
                        Toast.makeText(this, "Fever duration is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        int duration = Integer.parseInt(binding.feverDuration.getText().toString());
                        if (duration > 30) {
                            allClear = false;
                            Toast.makeText(this, "Fever duration must be less than 30", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            if (binding.date.getText().toString().equals("")) {
                allClear = false;
                Toast.makeText(this, "Please select Date", Toast.LENGTH_SHORT).show();
            }


            if (age > 14) {
                if (validator.validateRadioGroupEmpty(binding.weightLoss)) {
                    allClear = false;
                    Toast.makeText(this, "Weight loss is empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (validator.validateRadioGroupEmpty(binding.weightLossChild)) {
                    allClear = false;
                    Toast.makeText(this, "Weight loss is empty", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if(allClear)
        {
            if(!CommonUtils.isNetworkConnected(ContactFormActivity.this))
                   App.setMode("OFFLINE");
            CreatePatientAndSaveEncounterOnServer patientAndSaveEncounterOnServer = new CreatePatientAndSaveEncounterOnServer();
            patientAndSaveEncounterOnServer.execute("");
//            saveEncounter();
        }
    }

    private String saveEncounter() {
       observations = new ArrayList<String[]>();
            Calendar calendar = Calendar.getInstance();




        observations.add(new String[]{"CONTACT FOUND",getRadioGroupSelectedText(binding.contactFound)});
        if(getRadioGroupSelectedText(binding.contactFound).equals("Yes")) {
            observations.add(new String[]{"DATE CONTACT SCREENED", App.getSqlDateTime(screenCalendar)});
            observations.add(new String[]{"HAVE COUGH", getRadioGroupSelectedText(binding.cough)});
            if (getRadioGroupSelectedText(binding.cough).equals("Yes")) {
                observations.add(new String[]{"COUGHING HOW LONG", binding.coughDuration.getSelectedItem().toString()});
                observations.add(new String[]{"COUGHING BLOOD", getRadioGroupSelectedText(binding.coughBlood)});
            }
            observations.add(new String[]{"HAVE FEVER", getRadioGroupSelectedText(binding.fever)});
            if (getRadioGroupSelectedText(binding.fever).equals("Yes")) {
                observations.add(new String[]{"FEVER HOW LONG", binding.feverDuration.getText().toString()});
            }
            observations.add(new String[]{"ABNORMAL NIGHTS SWEATS", getRadioGroupSelectedText(binding.nightSweat)});

            if (age > 14) {
                observations.add(new String[]{"WEIGHT LOSS 3KG PER MONTH", getRadioGroupSelectedText(binding.weightLoss)});
            } else {
                observations.add(new String[]{"WEIGHT LOSS OR FAILURE TO GAIN WEIGHT", getRadioGroupSelectedText(binding.weightLossChild)});
                observations.add(new String[]{"WEIGHT MEASURING PERIOD", binding.weightlossPeriod.getSelectedItem().toString()});
            }

            observations.add(new String[]{"SWELLING OR LUMPS", getRadioGroupSelectedText(binding.swelling)});
            observations.add(new String[]{"HAD TB BEFORE", getRadioGroupSelectedText(binding.tbBefore)});
            observations.add(new String[]{"HIV TEST DONE", getRadioGroupSelectedText(binding.hivDone)});
            observations.add(new String[]{"HIV RESULT", getRadioGroupSelectedText(binding.hivResult)});
            observations.add(new String[]{"CONTACT REFERRED FOR EVALUATION", getRadioGroupSelectedText(binding.contactReferred)});
            observations.add(new String[]{"SYMPTOM SCREEN", getRadioGroupSelectedText(binding.symptomScreen)});

            if (getRadioGroupSelectedText(binding.contactReferred).equals("Yes")) {

             String referralObsGroup = "";
                referralObsGroup = (binding.ipt.isChecked())? "IPT ; " : "";
                referralObsGroup += (binding.furtherTbAssessment.isChecked())? "FURTHER TB ASSESSMENT ; " : "";
                referralObsGroup += (binding.hivTesting.isChecked())? "HIV TESTING ; " : "";
                observations.add(new String[]{"REASON FOR REFERRAL",referralObsGroup});
            }


        }






        Calendar formDateCalendar = App.getCalendar(calendar.getTime());






        ServerService serverService = new ServerService(this);

        String returnMessage =  serverService.createPatient(getPatientContent());
       // Toast.makeText(this, "Patient Message: "+patientMessage, Toast.LENGTH_SHORT).show();


        if(returnMessage.equalsIgnoreCase("SUCCESS")) {
            String contactUuid = serverService.getPatientUuid(contactDetail.getContactId());
            String personAttribute = serverService.savePersonAttribute("Contact Index Case Number", contactDetail.getIndexId(), contactUuid);


            String indexUuid = serverService.getPatientUuid(contactDetail.getIndexId());

            String relationMessage = serverService.saveRelationBetweenPatient(indexUuid, RELATIONSHIP_TYPE, contactUuid);

             returnMessage = serverService.saveEncounterAndObservation("CONTACT INVESTIGATION", null, formDateCalendar, observations.toArray(new String[][]{}), false, contactUuid, indexUuid);
        }


        return returnMessage;

    }


    public ContentValues getPatientContent() {
        ContentValues values = new ContentValues();
        String given =  contactDetail.getContactName();


        if(given.contains(" "))
        {
            String[] splited = given.split("\\s+");
            firstName = splited[0];
            lastName = splited[1];
        }
        else
        {
            firstName = given;
            lastName = "test";
        }

         now = Calendar.getInstance();

        int age = (int) Double.parseDouble(contactDetail.getAge());

        now.add(Calendar.YEAR,-age);


        values.put("patientId", contactDetail.getContactId());
        values.put("firstName",firstName);
        values.put("lastName", lastName);
        values.put("gender", contactDetail.getGender());
        values.put("dob", App.getSqlDate(now.getTime()));
        values.put("externalId", "" );
        values.put("indexId",contactDetail.getIndexId());
        //App.setPatient(patient);



        return values;
    }


    private class CreatePatientAndSaveEncounterOnServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            startLoader();
            isFormSubmitting = true;
            return saveEncounter();
        }

        @Override
        protected void onPostExecute(String returnMessage) {
            super.onPostExecute(returnMessage);
            removeLoader();

            if(returnMessage.equalsIgnoreCase("SUCCESS"))
            {

                if(App.getMode().equals("OFFLINE"))
                    Toast.makeText(ContactFormActivity.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(ContactFormActivity.this, "Submit Successfully", Toast.LENGTH_SHORT).show();
                    ServerService serverService = new ServerService(ContactFormActivity.this);
                    serverService.savePatientLocally(contactDetail.getContactId(),firstName,lastName,contactDetail.getGender(),App.getSqlDate(now.getTime()),"");
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",contactPosition);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
            else if (returnMessage.equals("CONNECTION_ERROR"))
            {
                Toast.makeText(ContactFormActivity.this, "Check your Internet connection,\n To save it offline turn off Internet connection completely", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(ContactFormActivity.this, "Sending Failed : Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

//    private void saveOffline() {
//        InvestigationForm form = new InvestigationForm();
//        form.setContactDetail(contactDetail);
//        form.setObservations(observations);
//
//        Toast.makeText(this, "Offline Saved", Toast.LENGTH_SHORT).show();
//    }


    private void startLoader() {
        LoaderFragment loaderFragment = new LoaderFragment("Submitting Form\n Please Wait...");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.index_form_container,loaderFragment, Constants.INVESTIGATION_FORM_LOADER_TAG).commit();
    }


    private void removeLoader()
    {

        try
        {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.INVESTIGATION_FORM_LOADER_TAG);
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
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.CONTACT_FORM_TAG);
        if(!isFormSubmitting)
            super.onBackPressed();
    }

}
