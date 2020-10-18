package com.example.permissionannotations

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.leo.lib_permission.annotations.PermissionApply
import com.leo.lib_permission.annotations.PermissionRefused
import com.leo.lib_permission.annotations.PermissionRefusedForever

class SecondActivity : AppCompatActivity() {

    private var resultTv: TextView? = null
    private var permissionTest: PermissionTest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.cameraRequestBtn).setOnClickListener { requestPermission() }
        findViewById<View>(R.id.utilPermissionBtn).setOnClickListener {
            if (null == permissionTest) {
                permissionTest = PermissionTest(this@SecondActivity)
            }
            permissionTest!!.request()
        }
        resultTv = findViewById(R.id.resultTv)
    }

    @PermissionApply(
            permissions = [Manifest.permission.WRITE_CALENDAR,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO],
            requestCode = 1001
    )
    fun requestPermission() {
        resultTv!!.text = "权限申请成功"
    }

    @PermissionRefused(requestCode = 1001)
    fun permissionFailed(refusedPermissions: Array<String?>) {
        resultTv!!.text = "权限申请失败:\n"
        for (s in refusedPermissions) {
            resultTv!!.append(s)
            resultTv!!.append("\n")
        }
    }

    @PermissionRefusedForever(requestCode = 1001)
    fun permissionForever(refusedPermissions: Array<String?>) {
        resultTv!!.text = "权限申请失败,被永久拒绝了:\n"
        for (s in refusedPermissions) {
            resultTv!!.append(s)
            resultTv!!.append("\n")
        }
    }
}