package com.akturk.plugin.helper;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferenceEngine {

    public static final String LOCATIONS = "locations";

    private static SharedPreferences mPref;

    public static synchronized SharedPreferences getInstance(Context context) {
        if (mPref == null) {
            mPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }

        return mPref;
    }

}
