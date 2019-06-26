package com.aot.pms.abs;

import android.support.annotation.NonNull;

public interface IPermission {

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
