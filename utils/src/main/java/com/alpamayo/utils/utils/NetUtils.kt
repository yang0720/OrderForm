package com.alpamayo.utils.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * ��������صĹ�����

 * @author ����ľ
 */
class NetUtils private constructor() {
    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        /**
         * �ж������Ƿ�����

         * @param context
         * *
         * @return
         */
        fun isConnected(context: Context): Boolean {

            val connectivity = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (null != connectivity) {

                val info = connectivity.activeNetworkInfo
                if (null != info && info.isConnected) {
                    if (info.state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
            return false
        }

        /**
         * �ж��Ƿ���wifi����
         */
//        fun isWifi(context: Context): Boolean {
//            val cm = context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false
//
//            return cm.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
//
//        }

        /**
         * ���������ý���
         */
        fun openSetting(activity: Activity) {
            val intent = Intent("/")
            val cm = ComponentName("com.android.settings",
                    "com.android.settings.WirelessSettings")
            intent.component = cm
            intent.action = "android.intent.action.VIEW"
            activity.startActivityForResult(intent, 0)
        }
    }

}  