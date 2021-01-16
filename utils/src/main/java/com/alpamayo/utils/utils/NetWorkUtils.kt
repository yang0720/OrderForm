package com.alpamayo.utils.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager

class NetWorkUtils private constructor() {

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        /**
         * 网络状态
         * @param context
         * *
         * @return
         */
        fun isNetworkConnected(context: Context): Boolean {
            val ni = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            return ni != null && ni.isConnectedOrConnecting
        }

        /**
         * 获取网络类型
         * @param context
         * *
         * @return
         */
        fun GetNetworkType(context: Context): String {
            var strNetworkType = ""

            val networkInfo = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

            if (networkInfo != null && networkInfo.isConnected) {
                if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    strNetworkType = "WIFI"
                } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    val _strSubTypeName = networkInfo.subtypeName

                    Log.e("Network getSubtypeName : " + _strSubTypeName)

                    // TD-SCDMA   networkType is 17
                    val networkType = networkInfo.subtype
                    when (networkType) {
                        TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN //api<8 : replace by 11
                        -> strNetworkType = "2G"
                        TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B //api<9 : replace by 14
                            , TelephonyManager.NETWORK_TYPE_EHRPD  //api<11 : replace by 12
                            , TelephonyManager.NETWORK_TYPE_HSPAP  //api<13 : replace by 15
                        -> strNetworkType = "3G"
                        TelephonyManager.NETWORK_TYPE_LTE    //api<11 : replace by 13
                        -> strNetworkType = "4G"
                        else ->
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if (_strSubTypeName.equals("TD-SCDMA", ignoreCase = true) || _strSubTypeName.equals("WCDMA", ignoreCase = true) || _strSubTypeName.equals("CDMA2000", ignoreCase = true)) {
                                strNetworkType = "3G"
                            } else {
                                strNetworkType = _strSubTypeName
                            }
                    }
                    Log.e("Network getSubtype : " + Integer.valueOf(networkType)!!.toString())
                }
            }
            Log.e("Network Type : " + strNetworkType)
            return strNetworkType
        }
    }
}
