# AndroidPms

逐一处理申请的权限，获取到权限成功后，才能申请下一个权限。如果调用addPermission(@NonNull String permissionName, @NonNull String tip, boolean isForce)方法，
第三个参数传入false时，则当前权限不是必须。

## 相关方法说明

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

#### 默认条件下，权限是必须申请成功才能进行下一个权限申请

```groovy
  /**
         * @param permissionName 申请的 权限，必须是Manifest.permission中定义的
         * @param tip            如果用户拒绝给该权限的提示
         * @return
         */
        public Builder addPermission(@NonNull String permissionName, @NonNull String tip) {
            permissions.add(new Item(permissionName, null == tip ? permissionName : tip));
            return this;
        }
```

#### 默认条件下，全是都是必须要给的，才能进行下一个权限的申请。如果权限是非必须的，则对一下个权限进行申请。

```groovy

  /**
         * @param permissionName 申请的 权限，必须是Manifest.permission中定义的
         * @param tip            如果用户拒绝给该权限的提示
         * @param isForce        默认为true，权限必须要给，false 权限可忽略
         * @return
         */
        public Builder addPermission(@NonNull String permissionName, @NonNull String tip, boolean isForce) {
            permissions.add(new Item(permissionName, null == tip ? permissionName : tip, isForce));
            return this;
        }


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

## 使用事例

```java

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PermissionUtil.Builder()
                .setActivity(this)
                //添加退出的回调
                .setExitListener(new IExitListener() {
                    @Override
                    public void exit() {
                    }
                })
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA, Manifest.permission.CAMERA, false) //该权限可以不必须申请
                .addPermission(Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE)
                .addPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE)
                .addPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE)
                .addPermission(Manifest.permission.READ_SMS, Manifest.permission.READ_SMS)
                .build();
        PermissionUtil.getInstance().requestPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //是否选中了"不再询问"，如果选中了不再询问，进入了设置界面，返回到首页时，是否提示申请权限
        boolean b = PermissionUtil.getInstance().enterSettingPage();
        Log.i("onResume", "onResume = " + b);
        if (b) {
            PermissionUtil.getInstance().requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限的回调
        PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
```