package com.example.sherifawad.hyperencryption;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;

import com.example.sherifawad.hyperencryption.PIN.CreatePIN;
import com.example.sherifawad.hyperencryption.PIN.EnterPIN;
import com.example.sherifawad.hyperencryption.Password.PasswordActivity;

import javax.crypto.Cipher;

import Util.Uty;
import algorithm.encryption.AESencryption;

import static Util.Uty.encryptDecrypt;

public class MainActivity extends AppCompatActivity implements CreatePIN.Callback {
    private static SharedPreferences preferences;
    private static final Object mLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                public void run() {
                                    if (preferences.getString("PIN", "").equals("")) {
//                                        Intent intentCoreActivity = new Intent(getApplicationContext(), PasswordActivity.class);
//                                        startActivity(intentCoreActivity);
                                        CreatePIN dialog = new CreatePIN();
                                        dialog.show(ft, dialog.TAG);


                                    } else {
                                        EnterPIN dialog = new EnterPIN();
                                        dialog.show(ft, dialog.TAG);
                                    }
                                }
                            }, 5
        );
    }


    public static void setPreference(String Name, String value) {
        synchronized (mLock) {
            preferences.edit().putString(Name, encryptDecrypt(value, preferences.getString("PIN", "")) ).commit();

        }


    }
    public static String getPreference(String Name) {

        synchronized (mLock) {
            if (preferences.contains(Name)) {
                String decrypt = encryptDecrypt(preferences.getString(Name, ""), preferences.getString("PIN", ""));
                return decrypt;
            } else {
                return null;
            }
        }


    }

    @Override
    public void setData(String PIN) {
        preferences.edit().putString("PIN", PIN).apply();
        System.out.println("PIN is" + preferences.getString("PIN", ""));
        Intent intentCoreActivity = new Intent(getApplicationContext(), PasswordActivity.class);
        startActivity(intentCoreActivity);
    }
}
