package com.iamverycute.air;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.IConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;

public class SetAirModeActivity extends AppCompatActivity implements Shizuku.OnRequestPermissionResultListener, CompoundButton.OnCheckedChangeListener {

    private final IConnectivityManager icm = IConnectivityManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.CONNECTIVITY_SERVICE)));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Switch aSwitch = findViewById(R.id.switch2);
        aSwitch.setOnCheckedChangeListener(this);
        if (Shizuku.pingBinder()) {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            Method Airplane = icm.getClass().getMethod("setAirplaneMode", boolean.class);
            Airplane.invoke(icm, isChecked);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored) {
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, int grantResult) {
    }
}