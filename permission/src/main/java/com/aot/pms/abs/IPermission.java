package com.aot.pms.abs;

import androidx.annotation.NonNull;

public interface IPermission {

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
