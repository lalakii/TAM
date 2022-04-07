package com.iamverycute.air;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.IConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;

public class AirModeActivity extends AppCompatActivity implements Shizuku.OnRequestPermissionResultListener, CompoundButton.OnCheckedChangeListener {

    private final IConnectivityManager icm = IConnectivityManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.CONNECTIVITY_SERVICE)));
    private Method setAirplaneMode;

    @SuppressLint("BlockedPrivateApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SwitchCompat aSwitch = findViewById(R.id.switch2);
        aSwitch.setOnCheckedChangeListener(this);
        if (Shizuku.pingBinder()) {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0);
            }
        }
        try {
            setAirplaneMode = icm.getClass().getDeclaredMethod("setAirplaneMode", boolean.class);
        } catch (NoSuchMethodException ignored) {
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            setAirplaneMode.invoke(icm, isChecked);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, int grantResult) {
    }
}