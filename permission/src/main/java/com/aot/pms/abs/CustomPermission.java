package com.aot.pms.abs;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class CustomPermission extends AbsBasePermission {


    public CustomPermission(String permissionStr, String desc, int pmsIndex, int resultCode, boolean isForce) {
        super(permissionStr, desc, pmsIndex, resultCode, isForce);
    }

    //当前权限不是必须申请的时，界面出现在申请下一个权限进入到设置界面中，返回后提示上一个可以忽略的权限框；
    //处理业务逻辑是，第一次可以申请，后面不再申请；
    private boolean isFirstRequest = true;

    @Override
    public void requestPermissions(final Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, super.permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            if (isForce) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                super.permissionName,
                        }, resultCode);
                curPermissionIndex = pmsIndex;
                super.isEnterSettingPage = 0;
            } else {
                if (isFirstRequest) {
                    isFirstRequest = false;
                    ActivityCompat.requestPermissions(activity,
                            new String[]{
                                    super.permissionName,
                            }, resultCode);
                    curPermissionIndex = pmsIndex;
                    super.isEnterSettingPage = 0;
                } else {
                    if (super.nextPermission != null)
                        super.nextPermission.requestPermissions(activity);
                }

            }
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
                    if (isForce) {
                        exitApp(activity, desc, new onRequestPermission() {
                            @Override
                            public void reRequestPermission() {
                                requestPermissions(activity);
                            }
                        });
                    } else {
                        if (!isNullOfNextPermission())
                            super.nextPermission.requestPermissions(activity);
                    }
                }
            }
        }
    }
}
