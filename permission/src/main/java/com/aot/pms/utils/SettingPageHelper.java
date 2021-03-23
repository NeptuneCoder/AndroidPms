package com.aot.pms.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class SettingPageHelper {
    public static void enterSettingPage(Activity activity) {
        //引导用户至设置页手动授权
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        if (activity != null) {
            activity.startActivityForResult(intent, 10001);
        }
    }
}
