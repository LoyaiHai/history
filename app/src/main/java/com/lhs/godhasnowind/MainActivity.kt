package com.lhs.godhasnowind

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lhs.toollibrary.utils.DebugLogJava
import com.lhs.toollibrary.utils.HS_CoordinateUtils
import com.lhs.toollibrary.utils.HS_TimeUtils

class MainActivity : AppCompatActivity() {

    val REQUEST_PERMISSIONS_STORAGE = 0x100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DebugLogJava.e("这只是一个打印测试yyyy-MM-dd HH:mm:ss")

        val timeDifference = HS_TimeUtils.getTimeDifference("2023-04-17 10:03:00")
        DebugLogJava.e("获取当前的相差时间为：$timeDifference")


//        CoordinateTest()

        initData()

        initListener()

    }

    private fun initData() {

        // 请求动态权限
        requestDynamicPermission()

    }

    private fun initListener() {
        findViewById<Button>(R.id.btn_task_log).setOnClickListener {

            DebugLogJava.e("这里是跳转的界面")

            val intent = Intent()
            intent.setClass(this, TaskLogActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 申请动态权限
     */
    private fun requestDynamicPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,  // 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE  // 读取权限
        )

        if (!checkPermissionAllGranted(permissions)) {
            // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
            ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_PERMISSIONS_STORAGE
            )
        } else {
            DebugLogJava.e("所有权限通过")
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private fun checkPermissionAllGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission!!
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 只要有一个权限没有被授予, 则直接返回 false
                DebugLogJava.e("这里跑了一次权限是同意的")
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_STORAGE) {
            var ok = true
            var position = 0
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    ok = false
                    DebugLogJava.e("这里看下位置是第几个：$position")
                }
                position++
            }
            if (ok) {
                DebugLogJava.e("这里是所有权限都通过？？？")
            } else {
                DebugLogJava.e("权限没通过.......")
            }
        }
    }

    private fun CoordinateTest() {
        DebugLogJava.e("百度的经度为：116.404")
        DebugLogJava.e("百度的纬度为：39.915")
        //        DebugLogJava.e("百度的经度为：113.423635")
        //        DebugLogJava.e("百度的纬度为：23.176773")

        var bdLongtitude = 116.404
        var bdLatitude = 39.915

        DebugLogJava.e("首先百度转国测")

        val bd09ToGcj02 = HS_CoordinateUtils.bd09ToGcj02(bdLongtitude, bdLatitude)

        DebugLogJava.e("百度转国测的经度为：" + bd09ToGcj02[0])
        DebugLogJava.e("百度转国测的纬度为：" + bd09ToGcj02[1])

        DebugLogJava.e("下一步是国测转GPS")

        //        val gcj02ToWgs84 = CoordinateUtils.gcj02ToWgs84(bd09ToGcj02[0], bd09ToGcj02[1])
        val gcj02ToWgs84 = HS_CoordinateUtils.gcj02ToWgs84(116.404, 39.915)

        DebugLogJava.e("国测转GPS的经度为：" + gcj02ToWgs84[0])
        DebugLogJava.e("国测转GPS的纬度为：" + gcj02ToWgs84[1])

        DebugLogJava.e("转至另一个过程，从GPS转为百度坐标")

        //        val wgs84ToGcj02 = CoordinateUtils.wgs84ToGcj02(gcj02ToWgs84[0], gcj02ToWgs84[1])
        val wgs84ToGcj02 = HS_CoordinateUtils.wgs84ToGcj02(116.404, 39.915)

        DebugLogJava.e("GPS转国测的经度为：" + wgs84ToGcj02[0])
        DebugLogJava.e("GPS转国测的纬度为：" + wgs84ToGcj02[1])

        //        val gcj02ToBd09 = CoordinateUtils.gcj02ToBd09(wgs84ToGcj02[0], wgs84ToGcj02[1])
        val gcj02ToBd09 = HS_CoordinateUtils.gcj02ToBd09(116.404, 39.915)

        DebugLogJava.e("国测转百度的经度为：" + gcj02ToBd09[0])
        DebugLogJava.e("国测转百度的纬度为：" + gcj02ToBd09[1])
    }
}