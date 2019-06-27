package com.aot.pms;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aot.pms.abs.IExitListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PermissionUtil.Builder()
                .setActivity(this)
                //添加退出的回调
                .setExitListener(new IExitListener() {
                    @Override
                    public void exit() {
                    }
                })
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA, Manifest.permission.CAMERA, false)
                .addPermission(Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE)
                .addPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE)
                .addPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE)
                .addPermission(Manifest.permission.READ_SMS, Manifest.permission.READ_SMS)
                .build();
        PermissionUtil.getInstance().requestPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean b = PermissionUtil.getInstance().enterSettingPage();
        Log.i("onResume", "onResume = " + b);
        if (b) {
            PermissionUtil.getInstance().requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
