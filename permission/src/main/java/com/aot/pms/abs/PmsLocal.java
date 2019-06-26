package com.aot.pms.abs;

/**
 * 通过该类去获取当前的id
 */
public class PmsLocal {

    private final AbsBasePermission abp;

    public PmsLocal(AbsBasePermission abp) {
        this.abp = abp;
    }


    public int getCurPmsId() {
        return abp.curPermissionIndex;
    }
}
