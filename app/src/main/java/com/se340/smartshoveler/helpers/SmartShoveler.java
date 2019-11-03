package com.se340.smartshoveler.helpers;
import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class SmartShoveler  extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        SmartShoveler.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return SmartShoveler.context;
    }
}
