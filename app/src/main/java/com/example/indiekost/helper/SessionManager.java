package com.example.indiekost.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.indiekost.activity.LoginActivity;
import com.example.indiekost.activity.MainActivity;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;

    public static final String PREF_NAME = "LOGIN";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";
    public static final String EMAIL = "EMAIL";
    public static final String NAMA_PENGGUNA = "NAMA_PENGGUNA";
    public static final String ID_PENGGUNA = "ID_PENGGUNA";
    public static final String ID_AKSES = "ID_AKSES";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String email, String nama_pengguna, String id_pengguna, String id_akses) {
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putString(EMAIL, email);
        editor.putString(NAMA_PENGGUNA, nama_pengguna);
        editor.putString(ID_PENGGUNA, id_pengguna);
        editor.putString(ID_AKSES, id_akses);
        editor.apply();
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean(LOGIN_STATUS, false);
    }

    public void logout(){
        editor.clear();
        editor.commit();

        Intent login = new Intent(context, LoginActivity.class);
        context.startActivity(login);
        ((MainActivity)context).finish();
    }

    public String getEMAIL() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public String getNamaPengguna() {
        return sharedPreferences.getString(NAMA_PENGGUNA, null);
    }

    public String getIdPengguna() {
        return sharedPreferences.getString(ID_PENGGUNA, null);
    }

    public String getIdAkses() {
        return sharedPreferences.getString(ID_AKSES, null);
    }
}
