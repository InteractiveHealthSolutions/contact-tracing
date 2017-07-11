package com.ihs.android.contracttracing.contracttracing.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.ihs.android.contracttracing.views.activities.R;

public class MainActivity extends AppCompatActivity {

    private LinearLayout contactForm;
    private LinearLayout offlineForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUI();
        initListener();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void initListener() {
        contactForm.setOnClickListener(new UIListener());
        offlineForm.setOnClickListener(new UIListener());
    }

    private void initUI() {
        contactForm = (LinearLayout) findViewById(R.id.contact_form);
        offlineForm = (LinearLayout) findViewById(R.id.offline_form);


    }

    private class UIListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.contact_form:
                //    startFormFragment();
                    startActivity(new Intent(MainActivity.this,ContactTraceActivity.class));
                    break;

                case R.id.offline_form:
                    startActivity(new Intent(MainActivity.this,OfflineFormActivity.class));
                    break;
            }
        }
    }




    @Override
    public void onBackPressed() {
      super.onBackPressed();
    }

}