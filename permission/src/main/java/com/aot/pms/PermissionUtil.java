package com.aot.pms;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.aot.pms.abs.AbsBasePermission;
import com.aot.pms.abs.CustomPermission;
import com.aot.pms.abs.IExitListener;
import com.aot.pms.abs.IPermission;
import com.aot.pms.abs.PmsLocal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class PermissionUtil implements IPermission {


    private PermissionUtil() {
    }

    private static class Holder {
        private static PermissionUtil instance = new PermissionUtil();
    }

    public static PermissionUtil getInstance() {
        return Holder.instance;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Activity activity = weakRefActivity.get();
        if (firstPermission != null && activity != null) {
            firstPermission.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
        }
    }


    //记录第一个权限
    private CustomPermission firstPermission = null;

    public void requestPermissions() {
        int i = 0;
        int resultCode = 1000;
        CustomPermission curPermission = null;//当前的权限
        CustomPermission prePermission = null;//上一个权限
        for (Item item : permissions) {
            curPermission = new CustomPermission(item.pmsName, item.pmsDesc, i, resultCode + i);
            if (i == 0) {
                firstPermission = curPermission;
            }

            if (prePermission != null) {
                prePermission.setNextPermission(curPermission);
            }
            prePermission = curPermission;
            i++;
        }
        //构建完所有的权限后，请求第一个权限；
        Activity activity = weakRefActivity.get();
        if (activity != null) {
            firstPermission.requestPermissions(activity);
        }
    }

    /**
     * 进入设置界面回来后，判断用户是否还需要弹出对应的权限申请的界面
     *
     * @return false 不需要弹出界面提示，true需要弹出界面提示
     */
    private AbsBasePermission nextPms = null;

    public boolean enterSettingPage() {
        if (firstPermission == null) return false;
        PmsLocal pmsLocal = new PmsLocal(firstPermission);
        int index = 0;
        if (pmsLocal.getCurPmsId() == index) {
            return firstPermission.isEnterSettingPage == 1000;
        }
        for (nextPms = firstPermission.getNextPermission(); nextPms != null; ) {
            ++index;
            pmsLocal = new PmsLocal(nextPms);
            if (pmsLocal.getCurPmsId() == index) {
                return nextPms.isEnterSettingPage == 1000;
            }
            nextPms = nextPms.getNextPermission();
        }
        return false;
    }

    private static WeakReference<Activity> weakRefActivity;
    private static ArrayList<Item> permissions = new ArrayList<>();
    private static IExitListener exitListener;

    public IExitListener getExitListener() {
        return exitListener;
    }

    public static class Builder {
        public Builder setActivity(Activity activity) {
            weakRefActivity = new WeakReference(activity);
            return this;
        }

        /**
         * @param permissionName 申请的 权限，必须是Manifest.permission中定义的
         * @param tip            如果用户拒绝给该权限的提示
         * @return
         */
        public Builder addPermission(@NonNull String permissionName, @NonNull String tip) {
            for (Item i : permissions) {
                if (i.equals(permissionName)) {
                    return this;
                }
            }
            permissions.add(new Item(permissionName, null == tip ? permissionName : tip));
            return this;
        }

        /**
         * 设置退出游戏的逻辑
         *
         * @param listener
         * @return
         */
        public Builder setExitListener(IExitListener listener) {
            exitListener = listener;
            return this;
        }

        public void build() {
            PermissionUtil.getInstance().requestPermissions();
        }
    }

    private static class Item {
        public String pmsName;
        public String pmsDesc;

        public Item(String pmsName, String pmsDesc) {
            this.pmsName = pmsName;
            this.pmsDesc = pmsDesc;
        }

        public boolean equals(String pmsName) {
            if (pmsName.equals(pmsName)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
