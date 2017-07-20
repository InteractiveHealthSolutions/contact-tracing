package com.ihs.android.contracttracing.contracttracing.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihs.android.contracttracing.contracttracing.App;

import com.ihs.android.contracttracing.contracttracing.utils.ServerService;
import com.ihs.android.contracttracing.contracttracing.views.fragments.LoaderFragment;
import com.ihs.android.contracttracing.views.activities.R;
import com.ihs.android.contracttracing.contracttracing.models.gfatm_model.PersonAttributeType;


import com.ihs.android.contracttracing.contracttracing.utils.Constants;
import com.ihs.android.contracttracing.contracttracing.utils.DatabaseUtil;
import com.ihs.android.contracttracing.contracttracing.utils.DevicePreference;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {



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

        serverService = new ServerService(this);
        if(DevicePreference.getInstance().getServerAddress().equals(""))
        {
            App.setIp(Constants.SERVER_IP);
            App.setPort(Constants.PORT);
            DevicePreference.getInstance().setServerAddress(Constants.SERVER_IP);
            DevicePreference.getInstance().setServerPort(Constants.PORT);
        }
        else
        {
            App.setIp(DevicePreference.getInstance().getServerAddress());
            App.setPort(DevicePreference.getInstance().getServerPort());
        }

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
                AuthenticateUser authenticateUser = new AuthenticateUser();

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
                  authenticateUser.execute("");

                        }
                    }
//            else
//            {
//                Toast.makeText(LoginActivity.this, "User not found in offline mode,Kindly connect to internet for login ", Toast.LENGTH_SHORT).show();
//            }

                    else {
                        App.setUsername(mEmailView.getText().toString());
                        App.setPassword(mPasswordView.getText().toString());
                        authenticateUser.execute("");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
//            case R.id.action_refresh:
//                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
//                        .show();
//                break;
//            // action with ID action_settings was selected
            case R.id.action_settings:
               showCustomDialog();
                break;
            default:
                break;
        }

        return true;
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Select Ip Address");

        // set the custom dialog components - text, image and button
//        TextView text = (TextView) dialog.findViewById(R.id.text);
//        text.setText("Android custom dialog example!");

        final EditText ip = (EditText) dialog.findViewById(R.id.web_ip);
        final EditText port = (EditText) dialog.findViewById(R.id.web_port);


        ip.setText(App.getIp());
        port.setText(App.getPort());

        Button dialogButton = (Button) dialog.findViewById(R.id.save_web_address);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                App.setIp(ip.getText().toString());
                App.setPort(port.getText().toString());
                DevicePreference.getInstance().setServerAddress(ip.getText().toString());
                DevicePreference.getInstance().setServerPort(port.getText().toString());
                serverService = new ServerService(LoginActivity.this);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private class AuthenticateUser extends  AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            startLoader();
            ServerService serverService = new ServerService(LoginActivity.this);
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
                    dbUtil.doesDatabaseExist();
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

