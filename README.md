# AndroidPms

逐一处理申请的权限，获取到当前权限后，再申请下一个权限。

#### 配置需要申请的权限

```groovy
new PermissionUtil.Builder()
                .setActivity(this)//添加上下文
                //添加退出的回调
                .setExitListener(new IExitListener() {
                    @Override
                    public void exit() {
                    }
                })
//                添加需要申请的权限，添加的顺序也就是权限申请的顺序
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA, Manifest.permission.CAMERA)
                .addPermission(Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE)
                .addPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE)
                .addPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE)
                .addPermission(Manifest.permission.READ_SMS, Manifest.permission.READ_SMS)
                .build();
   
```

#### build之后，调用申请权限的方法

```groovy
             
        PermissionUtil.getInstance().requestPermissions();
```

#### 在onResume方法中，判断当点击不再询问后，进入设置界面再回到界面中时，是否需要继续弹出申请权限的框

```groovy
//在onResume方法中，判断当点击不再询问后，进入设置界面再回到界面中时，是否需要继续弹出申请权限的框
 @Override
    protected void onResume() {
        super.onResume();
        boolean b = PermissionUtil.getInstance().enterSettingPage();
        Log.i("onResume", "onResume = " + b);
        if (b) {
            PermissionUtil.getInstance().requestPermissions();
        }
    }
```

#### 配置申请权限的回调

```groovy
//配置申请权限的回调
  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

```