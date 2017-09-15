package com.onevest.dev.tulung.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "tulung";
    private static final String LOGIN_STATUS = "login";
    private static final String UUID = "uuid";

    public PrefsManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public void setLoginStatus(boolean status) {
        editor.putBoolean(LOGIN_STATUS, status);
        editor.commit();
    }

    public boolean getLoginStatus() {
        boolean status = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        return status;
    }

    public void setUuid(String uuid) {
        editor.putString(UUID, uuid);
        editor.commit();
    }

    public String getUuid() {
        String uuid = sharedPreferences.getString(UUID, null);
        return uuid;
    }

    public void removeUuid() {
        editor.remove(UUID);
        editor.commit();
    }
}
