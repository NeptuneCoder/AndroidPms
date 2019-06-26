package com.aot.pms.abs;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public class CustomPermission extends AbsBasePermission {
    public CustomPermission(String permissionStr, String desc, int pmsIndex, int resultCode) {
        super(permissionStr, desc, pmsIndex, resultCode);
    }

    @Override
    public void requestPermissions(final Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, super.permissionStr)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            super.permissionStr,
                    }, resultCode);
            curPermissionIndex = pmsIndex;
            super.isEnterSettingPage = 0;
        } else {
            if (super.nextPermission != null) super.nextPermission.requestPermissions(activity);
        }
    }

    public void onRequestPermissionsResult(final Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (nextPermission != null) {
            super.nextPermission.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
        }
        if (requestCode == resultCode) {
            if (requestSuccess(grantResults)) {
                if (!isNullOfNextPermission())
                    super.nextPermission.requestPermissions(activity);
            } else {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    exitApp(activity, desc, new onRequestPermission() {
                        @Override
                        public void reRequestPermission() {
                            requestPermissions(activity);
                        }
                    });
                }
            }
        }
    }
}
