package com.example.moiz_ihs.contracttracing.views.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moiz_ihs.contracttracing.App;
import com.example.moiz_ihs.contracttracing.views.activities.R;
import com.example.moiz_ihs.contracttracing.models.gfatm_model.OfflineForm;
import com.example.moiz_ihs.contracttracing.models.gfatm_model.PersonAttributeType;
import com.example.moiz_ihs.contracttracing.network.DataSender;


import com.example.moiz_ihs.contracttracing.network.Sendable;
import com.example.moiz_ihs.contracttracing.utils.CommonUtils;
import com.example.moiz_ihs.contracttracing.utils.Constants;
import com.example.moiz_ihs.contracttracing.utils.DatabaseUtil;
import com.example.moiz_ihs.contracttracing.utils.DevicePreference;
import com.example.moiz_ihs.contracttracing.utils.ServerService;
import com.example.moiz_ihs.contracttracing.views.fragments.LoaderFragment;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Sendable {



    private DatabaseUtil dbUtil;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox mRememberMe;
    ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DevicePreference.getInstance().init(this);


        App.setIp(Constants.SERVER_IP);
        App.setPort(Constants.PORT);
        serverService = new ServerService(this);

        // Set up the login fragment_contact_form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRememberMe = (CheckBox) findViewById( R.id.remember);

        init();


        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //   if(CommonUtils.isNetworkConnected(LoginActivity.this))
                App.setCommunicationMode("REST");
                App.setUsername(mEmailView.getText().toString());
                App.setPassword(mPasswordView.getText().toString());

                if (validateForm()) {

                    //   if(App.getMode().equals("OFFLINE")) {
                    if (DevicePreference.getInstance().getUser() != null) {
                        if (mEmailView.getText().toString().equals(DevicePreference.getInstance().getUser().getUsername()) && mPasswordView.getText().toString().equals(DevicePreference.getInstance().getUser().getPassword())) {


                            if(!mRememberMe.isChecked()) {
                                DevicePreference.getInstance().saveUser(null);
                            }

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        }
                        else
                        {
                            AuthenticateUser();
                        }
                    }
//            else
//            {
//                Toast.makeText(LoginActivity.this, "User not found in offline mode,Kindly connect to internet for login ", Toast.LENGTH_SHORT).show();
//            }

                    else {
                        App.setUsername(mEmailView.getText().toString());
                        App.setPassword(mPasswordView.getText().toString());
                        AuthenticateUser();
                    }
                }

            }


       //     }

        });

    }

    private void init() {
        if(!(DevicePreference.getInstance().getUser() == null))
        {
            mEmailView.setText(DevicePreference.getInstance().getUser().getUsername());
            mPasswordView.setText(DevicePreference.getInstance().getUser().getPassword());
            mRememberMe.setChecked(true);
        }
    }

    private boolean validateForm() {
      boolean  allValid = true;

        if(mEmailView.getText().toString().equals("") || mPasswordView.getText().toString().equals(""))
        {
            allValid = false;
            Toast.makeText(this, "Username or password can't be empty", Toast.LENGTH_SHORT).show();
        }

        return allValid;
    }

    private void startLoader() {
        LoaderFragment loaderFragment = new LoaderFragment("Login Please Wait...");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_container,loaderFragment, Constants.LOGIN_LOADER_TAG).commit();
    }



    private void AuthenticateUser() {
        AsyncTask<String,String,String> authenticate = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                startLoader();

                String result = serverService.getUser();
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("SUCCESS")) {

                    removeLoader();
                    PersonAttributeType.UserCredentials credentials = new PersonAttributeType.UserCredentials();
                    credentials.setUsername(App.getUsername());
                    credentials.setPassword(App.getPassword());

                    if(mRememberMe.isChecked()) {
                        DevicePreference.getInstance().saveUser(credentials);
                    }
                    else
                    {
                        DevicePreference.getInstance().saveUser(null);
                    }

                    dbUtil = new DatabaseUtil(getApplicationContext());
                    Boolean flag = dbUtil.doesDatabaseExist();
                    if (!flag) {
                        dbUtil.buildDatabase(false);            // build sql lite db in app memory
                    }
                    else{
                        dbUtil.getWritableDatabase();
                    }



                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            removeLoader();
                        }
                    });
                }
            }
        };

        authenticate.execute("");
    }


    @Override
    public void send(JSONArray data, int respId) {
        new DataSender(this, this, 0).execute(data);
    }

    @Override
    public void onResponseReceived(JSONObject resp, int respId) {
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.LOGIN_LOADER_TAG);
        if(fragment !=null && fragment.isVisible()) {
            removeLoader();
        }
        else {
             super.onBackPressed();
        }
    }


    private void removeLoader()
    {

        try
        {
             Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.LOGIN_LOADER_TAG);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();

        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
           // Log.e("crash", "removeLoader:"  + e.getMessage());
        }

    }

}

