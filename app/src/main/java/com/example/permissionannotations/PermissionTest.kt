package com.example.permissionannotations

import android.Manifest
import android.content.Context
import android.util.Log
import com.leo.lib_permission.annotations.PermissionApply
import com.leo.lib_permission.annotations.PermissionRefused
import com.leo.lib_permission.annotations.PermissionRefusedForever
import com.leo.lib_permission.interfaces.IContext

class PermissionTest constructor(private val mContext: Context) : IContext {

    @PermissionApply(
            permissions = [Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO],
            requestCode = 1002
    )
    fun request() {
        Log.e("LEOTEST", "权限申请成功")
    }

    @PermissionRefused(requestCode = 1002)
    fun permissionFailed(refusedPermissions: Array<String?>) {
        Log.e("LEOTEST", "权限申请失败:")
        val builder: StringBuilder = StringBuilder()
        for (s in refusedPermissions) {
            builder.append("${s}\n")
        }
        Log.e("LEOTEST", builder.toString())
    }

    @PermissionRefusedForever(requestCode = 1002)
    fun permissionForever(refusedPermissions: Array<String?>) {
        Log.e("LEOTEST", "权限申请失败,被永久拒绝了:")
        val builder: StringBuilder = StringBuilder()
        for (s in refusedPermissions) {
            builder.append("${s}\n")
        }
        Log.e("LEOTEST", builder.toString())
    }

    override fun getContext(): Context {
        return mContext
    }
}