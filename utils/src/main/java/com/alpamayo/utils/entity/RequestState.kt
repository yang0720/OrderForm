package com.alpamayo.utils.entity

import com.alpamayo.utils.utils.Alpa

import org.json.JSONObject

/**
 * Created by Administrator on 2016/10/26.
 */

class RequestState {

    var isFailure: Boolean = false
    var result: String? = null

    init {
        isFailure = true
        result = ""
    }

    val resultToJsonObject: JSONObject
        @Throws(Exception::class)
        get() {
            try {
                return JSONObject(result)
            } catch (e: Exception) {
                throw Exception("解析服务器数据失败！")
            }

        }
}
