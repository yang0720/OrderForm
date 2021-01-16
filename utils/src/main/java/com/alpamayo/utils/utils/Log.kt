package com.alpamayo.utils.utils

import android.content.Context

/**
 * Created by Administrator on 2016/6/1.
 */
object Log {

    fun e(o: Any?): String {
        android.util.Log.e("alpamayo:utils", o?.toString() ?: "")
        return o?.toString() ?: ""
    }
}
