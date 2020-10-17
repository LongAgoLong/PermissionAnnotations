package com.leo.lib_permission.interfaces;

public interface PermissionCallback {
    /**
     * 权限申请成功
     */
    void permissionApplySuccess();

    /**
     * 权限申请失败,但非永久拒绝
     */
    void permissionApplyRefused(String[] refusedPermissions);

    /**
     * 权限申请被永久拒绝
     */
    void permissionApplyRefusedForever(String[] refusedForeverPermissions);
}
