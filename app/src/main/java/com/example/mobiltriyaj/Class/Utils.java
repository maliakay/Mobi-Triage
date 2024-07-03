package com.example.mobiltriyaj.Class;

import android.content.Context;
import android.provider.Settings;

public class Utils {
    public static String getDeviceUID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
