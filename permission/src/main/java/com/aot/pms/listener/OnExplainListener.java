package com.aot.pms.listener;

public interface OnExplainListener {
    /**
     * 说明申请权限的原因
     */
    void explainReason();

    /**
     * 设置中心授权
     */
    void handAuth();
}
