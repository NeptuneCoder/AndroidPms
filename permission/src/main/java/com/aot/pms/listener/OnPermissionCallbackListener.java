package com.aot.pms.listener;


import androidx.annotation.NonNull;

public interface OnPermissionCallbackListener {
    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
