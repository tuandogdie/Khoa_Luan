package com.example.shippermanager.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Date;

public class HelperUtils {
    private static String MY_PREFS_NAME = "MyPrefsFile";

    public static void AddSharedObject(Context context, String json, String key)
    {
        SharedPreferences mPrefs = context.getSharedPreferences(MY_PREFS_NAME, 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("Shipper", json);
        prefsEditor.commit();
    }

    public static String GetSharedObject(Context context,String key)
    {
        SharedPreferences mPrefs = context.getSharedPreferences(MY_PREFS_NAME,0);
        return mPrefs.getString(key, "");
    }

    public static NumberFormat GetNumberFormat()
    {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        return format;
    }


}
