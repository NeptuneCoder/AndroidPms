package com.aot.pms.abs;

public class PmsLocal {
    private final AbsBasePermission abp;

    public PmsLocal(AbsBasePermission abp) {
        this.abp = abp;
    }

    public int curPmsId;

    public int getCurPmsId() {
        return abp.curPermissionIndex;
    }
}
