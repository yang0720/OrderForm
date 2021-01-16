package com.alpamayo.utils.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException

/**
 * 跟App相关的辅助类

 * @author 土下木
 */
class AppUtils private constructor() {

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")

    }

    companion object {

        /**
         * 获取应用程序名称
         */
        fun getAppName(context: Context): String? {
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(
                        context.packageName, 0)
                val labelRes = packageInfo.applicationInfo.labelRes
                return context.resources.getString(labelRes)
            } catch (e: NameNotFoundException) {
                e.printStackTrace()
            }

            return null
        }

        /**
         * [获取应用程序版本名称信息]

         * @param context
         * *
         * @return 当前应用的版本名称
         */
        fun getVersionName(context: Context): String? {
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(
                        context.packageName, 0)
                return packageInfo.versionName

            } catch (e: NameNotFoundException) {
                e.printStackTrace()
            }

            return null
        }
    }

}  
