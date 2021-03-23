package com.aot.pms;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.aot.pms.listener.OnExitListener;
import com.aot.pms.listener.OnExplainListener;
import com.aot.pms.listener.OnPermissionCallbackListener;
import com.aot.pms.permission.ConcretePermission;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class




PermissionHelper implements OnPermissionCallbackListener {

    private PermissionHelper() {
    }

    private static class Holder {
        private static PermissionHelper instance = new PermissionHelper();
    }

    public static PermissionHelper getInstance() {
        return Holder.instance;
    }

    /**
     * 判断是否权限申请成功
     * 每次只会申请一个权限，所以grantResults[0]数组取0下标
     * ggrant
     *
     * @param grantResults
     * @return
     */
    public static boolean requestIsSuccess(int[] grantResults) {
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 用于接收申请权限的结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        final Activity activity = weakRefActivity.get();
        if (activity != null && curPermission != null) {

        }
    }

    //    curPermission = curPermission.getNextPermission();
    //记录第一个权限
    private static ConcretePermission curPermission = null;

    /**
     * 构建所有的权限后，调用该方法开始申请权限
     */
    public void requestPermissions() {
        //构建完所有的权限后，请求第一个权限；
        Activity activity = weakRefActivity.get();
        if (activity == null) {
            throw new IllegalArgumentException("请设置Activity上下文");
        }
        if (activity != null && curPermission != null) {
            int i = ActivityCompat.checkSelfPermission(activity, curPermission.getPermissionName());
            if (i == PackageManager.PERMISSION_DENIED) {
                boolean showExplain = ActivityCompat.shouldShowRequestPermissionRationale(activity, curPermission.getPermissionName());
                if (showExplain) {
                    if (explainListener != null)
                        explainListener.explainReason();
                } else {
                    if (explainListener != null)
                        explainListener.handAuth();
                }
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                curPermission.getPermissionName(),
                        }, curPermission.getRequestCode());
            }
        } else {
            throw new IllegalArgumentException("请检查你要申请的权限");
        }
    }

    /**
     * 进入设置界面回来后，判断用户是否还需要弹出对应的权限申请的界面
     *
     * @return false 不需要弹出界面提示，true需要弹出界面提示
     */


    private static WeakReference<Activity> weakRefActivity;
    private static WeakReference<Fragment> weakRefFragment;

    private static OnExitListener exitListener;
    private static OnExplainListener explainListener;


    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Builder() {
        }

        private final ArrayList<ConcretePermission> permissions = new ArrayList<>();


        /**
         * @param permissionName 申请的 权限，必须是Manifest.permission中定义的
         * @return
         */
        public Builder addPermission(@NonNull String permissionName) {
            i++;
            permissions.add(new ConcretePermission(permissionName, i + resultCode, false));
            return this;
        }

        /**
         * @param permissionName 申请的 权限，必须是Manifest.permission中定义的
         * @param tip            如果用户拒绝给该权限的提示
         * @param isForce        默认为true，权限必须要给，false 权限可忽略
         * @return
         */
        int i = 0;
        int resultCode = 1000;

        public Builder addPermission(@NonNull String permissionName, boolean isForce) {
            i++;
            permissions.add(new ConcretePermission(permissionName, i + resultCode, isForce));
            return this;
        }

        /**
         * 设置退出的逻辑
         *
         * @param listener
         * @return
         */
        public Builder setOnExitListener(OnExitListener listener) {
            exitListener = listener;
            return this;
        }

        public Builder setOnExplainListener(OnExplainListener listener) {
            explainListener = listener;
            return this;
        }

        /**
         * 将多个权限以链表的方式连接在一起
         */
        public Builder build() {
            int i = 0;
            ConcretePermission prePermission = null;//上一个权限
            for (ConcretePermission item : permissions) {

                if (i == 0) {
                    PermissionHelper.curPermission = item;
                }
                if (prePermission != null) {
                    prePermission.setNextPermission(item);
                }
                prePermission = item;
                i++;
            }
            return this;
        }

        public Builder with(Activity activity) {
            weakRefActivity = new WeakReference(activity);
            return this;
        }
    }

}
