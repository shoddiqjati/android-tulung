package com.onevest.dev.tulung.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PrefsManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "tulung";
    private static final String LOGIN_STATUS = "login";
    private static final String UUID = "uuid";
    private static final String PHONE = "phone";
    private static final String PANIC = "panic";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String TAG = PrefsManager.class.getSimpleName();

    public PrefsManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public void setLoginStatus(boolean status) {
        editor.putBoolean(LOGIN_STATUS, status);
        editor.commit();
        Log.d(TAG, "setLoginStatus");
    }

    public boolean getLoginStatus() {
        boolean status = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        return status;
    }

    public void setUuid(String uuid) {
        editor.putString(UUID, uuid);
        editor.commit();
        Log.d(TAG, "setUuid");
    }

    public String getUuid() {
        String uuid = sharedPreferences.getString(UUID, null);
        return uuid;
    }

    public void removeUuid() {
        editor.remove(UUID);
        editor.commit();
        Log.d(TAG, "removeUuid");
    }

    public void setPhone(String phone) {
        editor.putString(PHONE, phone);
        editor.commit();
        Log.d(TAG, "setPhone");
    }

    public String getPhone() {
        String phone = sharedPreferences.getString(PHONE, null);
        return phone;
    }

    public void removePhone() {
        editor.remove(PHONE);
        editor.commit();
        Log.d(TAG, "removePhone");
    }

    public void setPanic(String panic) {
        editor.putString(PANIC, panic);
        editor.commit();
        Log.d(TAG, "setPanic");
    }

    public String getPanic() {
        String panic = sharedPreferences.getString(PHONE, null);
        return panic;
    }

    public void removePanic() {
        editor.remove(PANIC);
        editor.commit();
        Log.d(TAG, "removePanic");
    }

    public void logout() {
        setLoginStatus(false);
        removeUuid();
        removePhone();
        removePanic();
        removeName();
        removeEmail();
        Log.d(TAG, "logout");
    }

    public void setName(String name) {
        editor.putString(NAME, name);
        editor.commit();
        Log.d(TAG, "setName");
    }

    public String getName() {
        String name = sharedPreferences.getString(NAME, null);
        return name;
    }

    public void removeName() {
        editor.remove(NAME);
        editor.commit();
        Log.d(TAG, "removeName");
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
        Log.d(TAG, "setEmail");
    }

    public String getEmail() {
        String email = sharedPreferences.getString(EMAIL, null);
        return email;
    }

    public void removeEmail() {
        editor.remove(EMAIL);
        editor.commit();
        Log.d(TAG, "removeEmail");
    }
}
