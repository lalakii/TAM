package com.iamverycute.air;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;

import android.content.pm.IPackageManager;
import android.net.IConnectivityManager;


public class SetAirModeActivity extends Activity implements Shizuku.OnRequestPermissionResultListener {

    private final IConnectivityManager icm = IConnectivityManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService(Context.CONNECTIVITY_SERVICE)));
    private final IPackageManager ipm = IPackageManager.Stub.asInterface(new ShizukuBinderWrapper(SystemServiceHelper.getSystemService("package")));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (Shizuku.pingBinder()) {
                if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                    Shizuku.requestPermission(0);
                }
            }
            ipm.grantRuntimePermission(getPackageName(), android.Manifest.permission.WRITE_SECURE_SETTINGS, 0);
            boolean isClosed = Settings.Global.getInt(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON) == 0;
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE, 0);
            Method Airplane = icm.getClass().getMethod("setAirplaneMode", boolean.class);
            Airplane.invoke(icm, isClosed);
        } catch (Settings.SettingNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | RemoteException e) {
        } catch (Exception e) {
        } finally {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, int grantResult) {

    }
}