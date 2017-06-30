package com.example.moiz_ihs.contracttracing.dagger;

import android.app.Application;
import android.content.SharedPreferences;


/**
 * Created by Moiz-IHS on 6/8/2017.
 */

//@Module
public class AppModule {

    private Application mApplication;
    private final String PREF = "prefs";

    public AppModule(Application application)
    {
        mApplication = application;
    }

   // @Provides
  //  @Singleton
    Application provideApplication()
    {
        return mApplication;
    }

   // @Provides
   // @Singleton
    public SharedPreferences getAppPreferences()
    {
        return mApplication.getSharedPreferences(PREF,mApplication.MODE_PRIVATE);
    }

}
