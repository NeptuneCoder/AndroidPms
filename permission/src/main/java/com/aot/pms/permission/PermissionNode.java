package com.aot.pms.permission;

public class PermissionNode {

    /**
     * 每一个权限，都有唯一的判断的code码，防止多个权限用同一个code导致的无法区分
     */
    protected final int requestCode;

    public int getRequestCode() {
        return requestCode;
    }

    /**
     * 当前权限的id
     */
    protected final boolean isMust;

    public boolean isMust() {
        return isMust;
    }


    /**
     * 下一个权限
     */
    protected PermissionNode nextPermission;
    /**
     * 申请的权限名字
     */
    protected String permissionName;

    public String getPermissionName() {
        return permissionName;
    }


    /**
     * @param permissionName
     * @param resultCode
     * @param isMust
     */
    public PermissionNode(String permissionName, int resultCode, boolean isMust) {
        this.permissionName = permissionName;
        this.requestCode = resultCode;
        this.isMust = isMust;
    }

    public void setNextPermission(PermissionNode nextPermission) {
        this.nextPermission = nextPermission;
    }

    /**
     * 得到下一个权限
     *
     * @return
     */
    public PermissionNode getNextPermission() {
        return nextPermission;
    }

    public boolean nextPermissionIsNull() {
        return nextPermission == null;
    }


}
