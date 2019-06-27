package com.aot.pms.abs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.aot.pms.PermissionUtil;
import com.foyoent.ossdk.agent.R;


public abstract class AbsBasePermission {

    /**
     * 当拒绝权限时，dialog提示的信息
     */
    protected final String desc;
    /**
     * 每一个权限，都有唯一的判断的code码，防止多个权限用同一个code导致的无法区分
     */
    protected final int resultCode;
    /**
     * 当前权限的id
     */
    protected final int pmsIndex;
    protected final boolean isForce;
    /**
     * 下一个权限
     */
    protected AbsBasePermission nextPermission;
    /**
     * 申请的权限名字
     */
    protected String permissionName;
    /**
     * 当前权限的id
     */
    static int curPermissionIndex = 0;

    protected AbsBasePermission(String permissionName, String desc, int pmsIndex, int resultCode, boolean isForce) {
        this.permissionName = permissionName;
        this.desc = desc;
        this.pmsIndex = pmsIndex;
        this.resultCode = resultCode;
        this.isForce = isForce;
    }

    public void setNextPermission(AbsBasePermission nextPermission) {
        this.nextPermission = nextPermission;
    }

    /**
     * 得到下一个权限
     *
     * @return
     */
    public AbsBasePermission getNextPermission() {
        return nextPermission;
    }

    protected boolean isNullOfNextPermission() {
        return nextPermission == null;
    }

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    //下面5个状态分别表示5申请5个权限有没有进入到设置界面中
    //0 ，1，2，3，4，分别对应5个权限,  如果赋值为1000，则表示进入了设置界面，当从设置界面回来后，重新调用申请权限框。
    public int isEnterSettingPage = 0;

    void exitApp(final Activity activity, int content, final onRequestPermission requestPermission) {
        if (activity != null) {
            exitApp(activity, activity.getResources().getString(content), requestPermission);
        }

    }

    void exitApp(final Activity activity, String content, final onRequestPermission requestPermission) {
        if (builder == null && activity != null) {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setMessage(content);
        builder.setTitle(R.string.aot_setting_again);
        createBuilder(activity, builder, requestPermission);
        builder.setNegativeButton(R.string.aot_exit_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                IExitListener exitListener = PermissionUtil.getInstance().getExitListener();
                if (exitListener != null) {
                    exitListener.exit();
                } else {
                    activity.finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }


    /**
     * 根据用户是否勾选了不再询问，修改弹出框上的文案，申请权限，去授权
     *
     * @param activity
     * @param builder
     * @param requestPermission
     */
    private void createBuilder(final Activity activity, AlertDialog.Builder builder, final onRequestPermission requestPermission) {
        final boolean b = shouldShowRequestPermissionRationale(activity, permissionName);
        builder.setPositiveButton(b ? R.string.aot_setting_again : R.string.aot_go_accredit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (b) {
                    if (requestPermission != null) {
                        requestPermission.reRequestPermission();
                    }
                } else {
                    //引导用户至设置页手动授权
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getApplicationContext().getPackageName(), null);
                    intent.setData(uri);
                    isEnterSettingPage = 1000;
                    Log.i("onResume", "isEnterSettingPage = " + isEnterSettingPage + "    name = " + permissionName);
                    if (activity != null) {
                        activity.startActivityForResult(intent, 10001);
                    }
                }
            }
        });
    }


    protected boolean shouldShowRequestPermissionRationale(Activity activity, String permissionStr) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionStr);
    }

    public interface onRequestPermission {
        void reRequestPermission();
    }

    public abstract void requestPermissions(Activity activity);

    protected abstract void onRequestPermissionsResult(final Activity activity, int requestCode,
                                                       @NonNull String[] permissions, @NonNull int[] grantResults);

    public boolean requestSuccess(int[] grantResults) {
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
