package com.alpamayo.utils

import android.app.Application
import androidx.multidex.MultiDex
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2017/6/5.
 */
class App: Application(){
    override fun onCreate() {
        super.onCreate()
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build()
        MultiDex.install(this);
        OkHttpUtils.initClient(okHttpClient)
    }
}
