package com.alpamayo.utils.utils

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import android.os.Message
import android.telephony.TelephonyManager
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import kotlin.collections.Map.Entry
import java.util.Objects
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import com.alpamayo.utils.R
import java.security.MessageDigest
import kotlin.experimental.and

/**
 * Created by Alpamayo on 16/6/5.
 */
object Alpa {

    val sd_cache_name = "DICOT"
    var sd_database_name = "database.db"

    /**
     * 获取一个对象的值,支持
     * （EditText, TextView, Message, Exception）
     * @param arg
     * @param defaultText
     * @return 如果获取的值为"",null,长度为0时，则使用默认值 没有默认值时显示""
     */
    @JvmOverloads fun getText(arg: Any?, defaultText: String = ""): String {
        var _arg = ""
        try {
            if (arg is EditText) {
                _arg = arg.text.toString().trim { it <= ' ' }
            } else if (arg is TextView) {
                _arg = arg.text.toString().trim { it <= ' ' }
            } else if (arg is Message) {
                _arg = arg.obj.toString().trim { it <= ' ' }
            } else if (arg is Exception) {
                _arg = (arg as Exception).message.toString().trim({ it <= ' ' })
            } else {
                _arg = if (arg != null) arg.toString().trim { it <= ' ' } else ""
            }
            _arg = if (_arg.toLowerCase() == "null" || _arg.length == 0) defaultText ?: "" else _arg

        } catch (e: Exception) {
            log("getText error:" + e.message)
            return defaultText
        }
        return _arg
    }
    fun getTextIsEmpty(arg: Any, defaultValue: Any): String {
        return if (getText(arg).length == 0) defaultValue.toString() else getText(arg)
    }
    /**
     * 输出日志
     * @param s
     */
    fun log(s: Any?) {
        Log.e(s)
    }
    fun log(s: Any, e: Any) {
        Log.e(s.toString() + ":" + getText(e))
    }
    fun log(s: Any, s1: String) {
        Log.e(s.toString() + ":" + s1)
    }
    /**
     * 将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     */
    fun bytesToHexString(src: ByteArray?): String? {
        val stringBuilder = StringBuilder("")
        if (src == null || src.size <= 0) {
            return null
        }
        for (i in src.indices) {
            val v = src.get(i).toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }
//    /**
//     * SHA1加密
//     */
//    fun getSHA1(str: String):String{
//        log(str)
//        var buf = MD5Util.md5(str)
//        return bytesToHexString(buf)!!.toUpperCase();
//    }
    /**
     * 获取时间戳
     */
    fun getTimestamp(): String {
        val time = System.currentTimeMillis() / 1000//获取系统时间的10位的时间戳
        val str = time.toString()
        return str
    }
    /**
     * 提示
     * @param context
     * @param obj
     */
    fun ts(context: Context, obj: Any) {
        Log.e(obj.toString())
        Toast.makeText(context, obj.toString(), Toast.LENGTH_LONG).show()
    }


    /**
     * 验证是否为价格
     * @param price
     * *
     * @return
     */
    fun isPrice(price: String): Boolean {
        val p = Pattern.compile("^-?\\d*(\\.)?\\d*$")
        val m = p.matcher(price)
        return m.matches()
    }
    /**
     * 验证是否为手机号
     * @param mobiles
     * *
     * @return
     */
    fun isMobileNO(mobiles: String): Boolean {
        val p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")
        val m = p.matcher(mobiles)
        return m.matches()
    }
    /**
     * 验证是否为邮箱
     * @param email
     * *
     * @return
     */
    fun isEmail(email: String): Boolean {
        val str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$"
        val p = Pattern.compile(str)
        val m = p.matcher(email)
        return m.matches()
    }
    /**
     * 将Map转换JSONObject
     * @param map
     * *
     * @return
     */
    fun mapToJSONObject(map: Map<String, Any>): JSONObject {
        val iterator = map.entries.iterator()
        val `object` = JSONObject()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            try {
                `object`.put(entry.key, entry.value)
            } catch (e: JSONException) {

            }
        }
        return `object`
    }
    fun jsonObjectToMap(jsonObject: JSONObject): MutableMap<String, Any>? {
        try {
            val itemMap = HashMap<String, Any>()
            val names = jsonObject.names()
            if (names != null) {
                for (j in 0..names.length() - 1) {
                    val name = names.getString(j)
                    val value = jsonObject.getString(name)
                    itemMap.put(name, value)
                }
            }
            return itemMap
        } catch (e: Exception) {
        }
        return null
    }
    /**
     * 将List转换成JSONArray
     * @param list
     * *
     * @return
     */
    fun listToJSONArray(list: List<Map<String, Any>>): JSONArray {
        val mJsonArray = JSONArray()
        for (i in list.indices) {
            val itemMap = list[i]
            val iterator = itemMap.entries.iterator()
            val `object` = JSONObject()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                try {
                    `object`.put(entry.key, entry.value)
                } catch (e: JSONException) {

                }

            }
            mJsonArray.put(`object`)
        }
        return mJsonArray
    }
    /**
     * 将JSONArray转换成List
     * @param jsonArray
     * *
     * @return
     */
    fun jsonArrayToList(jsonArray: JSONArray): MutableList<MutableMap<String, Any>>? {
        try {
            val datas:  MutableList<MutableMap<String, Any>> = ArrayList()
            for (i in 0..jsonArray.length() - 1) {
                val itemObject = jsonArray.getJSONObject(i)
                val itemMap = HashMap<String, Any>()
                val names = itemObject.names()
                if (names != null) {
                    for (j in 0..names.length() - 1) {
                        val name = names.getString(j)
                        val value = itemObject.get(name)
                        itemMap.put(name, value)
                    }
                }
                datas.add(itemMap)
            }
            return datas
        } catch (e: JSONException) {
        }
        return null
    }
    /**
     * 将Cursor数据集合读取到list集合中
     * @param cursor
     * *
     * @return
     */
    fun cursortToList(cursor: Cursor?): List<Map<String, Any>>? {
        if (cursor != null && cursor.count > 0) {
            val datas = ArrayList<Map<String, Any>>()
            var map: MutableMap<String, Any>
            val columnNames = cursor.columnNames
            var colString = ""
            for (i in columnNames.indices) {
                colString += columnNames[i] + ","
            }
            Log.e("column ：" + colString)
            Log.e("size   ：" + cursor.count)
            while (cursor.moveToNext()) {
                map = HashMap<String, Any>()
                for (i in columnNames.indices) {
                    map.put(columnNames[i], cursor.getString(cursor.getColumnIndex(columnNames[i])))
                }
                datas.add(map)
            }
            cursor.close()
            return datas
        }
        return null
    }

    /**
     * 科学计数发转换字符串
     * @param x
     * *
     * @param j
     * *
     * @return
     */
    fun spaceConversion(x: Double, j: Boolean, mLength: Int): String {
        var _value = x.toString()
        if (j) {
            val mBigDecimal = BigDecimal(x)
            _value = mBigDecimal.toPlainString()
        }
        try {
            if (_value.indexOf(".") > -1) {
                _value = _value.substring(0, _value.indexOf(".") + mLength)
            }
        } catch (e: Exception) {
        }

        return _value
    }
    fun decimalFormat(str: Double): String{
        val df = java.text.DecimalFormat("#.##")
        return df.format(str)
    }
    fun decimals(str: Double): String{
        val df = java.text.DecimalFormat("#####################.##")
        return df.format(str)
    }
    /**
     * 删除目录下所有文件
     * @param root
     */
    fun deleteAllFiles(root: File) {
        val files = root.listFiles()
        if (files != null) {
            for (f in files) {
                if (f.isDirectory) { // 判断是否为文件夹
                    deleteAllFiles(f)
                    try {
                        f.delete()
                    } catch (e: Exception) {
                    }

                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f)
                        try {
                            f.delete()
                        } catch (e: Exception) {
                        }

                    }
                }
            }
        }
    }
    /**
     * 删除目录下所有文件
     */
    fun deleteAllFiles(path: String) {
        deleteAllFiles(File(path))
    }
    /**
     * Get请求，获得返回数据,超时20秒

     * @param urlStr
     * *
     * @return
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun doGet(urlStr: String): String {
        var url: URL? = null
        var conn: HttpURLConnection? = null
        var `is`: InputStream? = null
        var baos: ByteArrayOutputStream? = null
        try {
            url = URL(urlStr)
            conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 20000
            conn.connectTimeout = 20000
            conn.requestMethod = "GET"
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            if (conn.responseCode == 200) {
                `is` = conn.inputStream
                baos = ByteArrayOutputStream()
                var len: Int = -1
                val buf = ByteArray(128)
                len = `is`.read(buf)
                while (len != -1) {
                    baos.write(buf, 0, len)
                    len = `is`.read(buf)
                }
                baos.flush()
                return baos.toString()
            } else {
                throw RuntimeException(" responseCode is not 200 ... ")
            }

        } catch (e: Exception) {
            throw e
        } finally {
            try {
                if (`is` != null)
                    `is`.close()
            } catch (e: IOException) {
            }

            try {
                if (baos != null)
                    baos.close()
            } catch (e: IOException) {
            }

            conn!!.disconnect()
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求 ,超时20秒

     * @param url
     * *            发送请求的 URL
     * *
     * @param param
     * *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * *
     * @return 所代表远程资源的响应结果
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun doPost(url: String, param: String?): String {
        var param = param
        Log.e("Request-URI：  " + url)
        Log.e("Request-Param: " + param!!)
        var out: PrintWriter? = null
        var `in`: BufferedReader? = null
        var result = ""
        try {
            val realUrl = URL(url)
            // 打开和URL之间的连接
            val conn = realUrl
                    .openConnection() as HttpURLConnection
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*")
            conn.setRequestProperty("connection", "Keep-Alive")
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded")
            conn.setRequestProperty("charset", "utf-8")
            conn.useCaches = false
            // 发送POST请求必须设置如下两行
            conn.doOutput = true
            conn.doInput = true
            conn.readTimeout = 20000
            conn.connectTimeout = 20000
            if (param == null || param.trim { it <= ' ' } == "") {
                param = "tmp=1"
            }
            if (param != null && param.trim { it <= ' ' } != "") {
                // 获取URLConnection对象对应的输出流
                out = PrintWriter(conn.outputStream)
                // 发送请求参数
                out.print(param)
                // flush输出流的缓冲
                out.flush()
            }
            // 定义BufferedReader输入流来读取URL的响应
            `in` = BufferedReader(
                    InputStreamReader(conn.inputStream))
            var line: String = `in`.readLine()
            while (line != null) {
                result += line
                line = `in`.readLine()
            }
        } catch (e: Exception) {
            throw e
        } finally {
            try {
                if (out != null) {
                    out.close()
                }
                if (`in` != null) {
                    `in`.close()
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

        }// 使用finally块来关闭输出流、输入流
        return result
    }

    /**
     * 将Bitmap保存到本地
     * @param bitmap
     * *
     * @return
     * *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun saveBitmap(bitmap: Bitmap, path: String): String {
        try {
            val mFileName = getTimeStr(null) + ".jpg"
            val out = FileOutputStream(path + mFileName)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            return mFileName
        } catch (e: Exception) {
            throw e
        }

    }

    /**
     * 获取当前时间字符串
     * @param format yyyyMMddHHmmssSSS
     * *
     * @return
     */
    fun getTimeStr(format: String?): String {
        return SimpleDateFormat(format ?: "yyyyMMddHHmmssSSS").format(Date())
    }



    /**
     * 获取当前软件版本
     */
    fun getCurrentVersion(context: Context): String{
        try{
            var info =  context.packageManager.getPackageInfo(context.packageName,0)
            return info.versionName
        }catch (e: Exception){
        }
        return ""
    }

    /**
     * 获取升级号
     */
    fun getCurrentVersionCode(context: Context): Int{
        try{
            var info =  context.packageManager.getPackageInfo(context.packageName,0)
            return info.versionCode
        }catch (e: Exception){
        }
        return 0
    }

	/**
	 * 获取设备名称
	 * @return
	 */
	fun getDeviceModel(): String{
		try {
			var bd: String = android.os.Build.MODEL
            return bd
		} catch (e: Exception) {
		}
		return "";
	}

    /**
     * 获取系统缓存目录地址
     * @return
     */
    fun getCachePath(path: String): String {
        val path_tmpString = android.os.Environment.getExternalStorageDirectory().absolutePath + "/" + sd_cache_name + "/" + path + "/"
        val CacheDirectory = File(path_tmpString)
        if (!CacheDirectory.exists()) {
            CacheDirectory.mkdirs()
        }
        return path_tmpString
    }

    val cachePath: String
        get() {
            val path_tmpString = android.os.Environment.getExternalStorageDirectory().absolutePath + "/" + sd_cache_name + "/"
            val CacheDirectory = File(path_tmpString)
            if (!CacheDirectory.exists()) {
                CacheDirectory.mkdirs()
            }
            return path_tmpString
        }

    fun saveLog(className:String,method: String, text: String){
        Thread(Runnable {
            try {
                val FilePath = getCachePath("log") +className +"_" +method + "_" + SimpleDateFormat("yyyyMMddHHmmss").format(Date()) + ".txt"
                log("**警告发生了异常，日志已记录")
                log("位置："+FilePath)
                val outStream = FileOutputStream(FilePath, true)
                val writer = OutputStreamWriter(outStream, "utf-8")
                writer.write(text.toString())
                writer.flush()
                writer.close()//记得关闭
                outStream.close()
            } catch (e: Exception) {
            }
        }).start()
    }

    /**
     * 数据地址
     * @return
     */
    val dataBasePath: String
        get() = getCachePath("db") + sd_database_name

    /**
     * 将基础数据库输出到本地
     * @param mResources
     * *
     * @return
     */
    fun existsDB(mResources: Resources): Boolean {
        var mState = false
        try {
            if (!File(dataBasePath).exists()) {
                val `is` = mResources.openRawResource(R.raw.health)
                val fos = FileOutputStream(dataBasePath)
                val buffer = ByteArray(8192)
                var count = `is`.read(buffer)
                // 开始复制hf_database.db文件
                while (count > 0) {
                    fos.write(buffer, 0, count)
                    count = `is`.read(buffer)
                }
                fos.close()
                `is`.close()
            }
            mState = true
        } catch (e: Exception) {
            mState = false
        }

        return mState
    }

    //10进制转16进制，运用辗转相除法，取余数补对应的位数，直到相除结果为0。
    fun IntToHex(n: Int): String {
        var n = n
        val ch = CharArray(20)
        var nIndex = 0
        while (true) {
            val m = n / 16
            val k = n % 16
            if (k == 15)
                ch[nIndex] = 'F'
            else if (k == 14)
                ch[nIndex] = 'E'
            else if (k == 13)
                ch[nIndex] = 'D'
            else if (k == 12)
                ch[nIndex] = 'C'
            else if (k == 11)
                ch[nIndex] = 'B'
            else if (k == 10)
                ch[nIndex] = 'A'
            else
                ch[nIndex] = ('0' + k).toChar()
            nIndex++
            if (m == 0)
                break
            n = m
        }
        val sb = StringBuffer()
        sb.append(ch, 0, nIndex)
        sb.reverse()
        var strHex ="0x"
        strHex += sb.toString()
        return strHex
    }

    //16进制转10进制2、16进制转10进制，对16进制数的每一位数乘以其对应的16的幂，相加。
    fun HexToInt(strHex: String): Int {
        var nResult = 0
        if (!IsHex(strHex))
            return nResult
        var str = strHex.toUpperCase()
        if (str.length > 2) {
            if (str[0] == '0' && str[1] == 'X') {
                str = str.substring(2)
            }
        }
        val nLen = str.length
        for (i in 0 until nLen) {
            val ch = str[nLen - i - 1]
            try {
                nResult += GetHex(ch) * GetPower(16, i)
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }
        return nResult
    }

    //计算16进制对应的数值
    @Throws(Exception::class)
    fun GetHex(ch: Char): Int {
        if (ch >= '0' && ch <= '9')
            return (ch - '0').toInt()
        if (ch >= 'a' && ch <= 'f')
            return (ch - 'a' + 10).toInt()
        if (ch >= 'A' && ch <= 'F')
            return (ch - 'A' + 10).toInt()
        throw Exception("error param")
    }

    //计算幂
    @Throws(Exception::class)
    fun GetPower(nValue: Int, nCount: Int): Int {
        if (nCount < 0)
            throw Exception("nCount can't small than 1!")
        if (nCount == 0)
            return 1
        var nSum = 1
        for (i in 0 until nCount) {
            nSum = nSum * nValue
        }
        return nSum
    }

    //判断是否是16进制数
    fun IsHex(strHex: String): Boolean {
        var i = 0
        if (strHex.length > 2) {
            if (strHex[0] == '0' && (strHex[1] == 'X' || strHex[1] == 'x')) {
                i = 2
            }
        }
        while (i < strHex.length) {
            val ch = strHex[i]
            if (ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f') {
                ++i
                continue
            }
            return false
            ++i
        }
        return true
    }

}
