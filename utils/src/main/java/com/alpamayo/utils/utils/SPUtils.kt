package com.alpamayo.utils.utils

import android.content.Context
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.HashMap
import kotlin.collections.Map.Entry

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


import android.content.SharedPreferences

/**
 * SharedPreferences封装类SPUtils
 * @author 土下木
 */
object SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    val FILE_NAME = "xifengliquor"

    fun putMap(context: Context, key: String, maps: Map<String, Any>) {
        val iterator = maps.entries.iterator()
        val str = JSONObject()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            try {
                str.put(entry.key, entry.value)
            } catch (e: JSONException) {

            }

        }
        Log.e(key + "      " + str.toString())
        put(context, key, str.toString())
    }

    fun getMap(context: Context, key: String): Map<String, Any>? {
        try {
            val itemObject = JSONObject(get(context, key, "")!!.toString())
            val itemMap = HashMap<String, Any>()
            val names = itemObject.names()
            if (names != null) {
                for (j in 0..names.length() - 1) {
                    val name = names.getString(j)
                    val value = itemObject.getString(name)
                    itemMap.put(name, value)
                }
            }
            return itemMap
        } catch (e: Exception) {
            // TODO: handle exception
            Log.e("getMap:" + e.message)
        }

        return null
    }

    fun putList(context: Context, key: String, datas: List<Map<String, Any>>) {
        val mJsonArray = JSONArray()
        for (i in datas.indices) {
            val itemMap = datas[i]
            val iterator = itemMap.entries.iterator()
            val str = JSONObject()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                try {
                    str.put(entry.key, entry.value)
                } catch (e: JSONException) {

                }

            }
            mJsonArray.put(str)
        }
        put(context, key, mJsonArray.toString())
    }

    fun getList(context: Context, key: String): List<Map<String, Any>>? {
        try {
            val datas = ArrayList<Map<String, Any>>()
            val result = get(context, key, "")
            val array = JSONArray(result!!.toString())
            for (i in 0..array.length() - 1) {
                val itemObject = array.getJSONObject(i)
                val itemMap = HashMap<String, Any>()
                val names = itemObject.names()
                if (names != null) {
                    for (j in 0..names.length() - 1) {
                        val name = names.getString(j)
                        val value = itemObject.getString(name)
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
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法

     * @param context
     * *
     * @param key
     * *
     * @param object
     */
    fun put(context: Context, key: String, str: Any) {
        val sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
        val editor = sp.edit()

        if (str is String) {
            editor.putString(key, str)
        } else if (str is Int) {
            editor.putInt(key, str)
        } else if (str is Boolean) {
            editor.putBoolean(key, str)
        } else if (str is Float) {
            editor.putFloat(key, str)
        } else if (str is Long) {
            editor.putLong(key, str)
        } else {
            editor.putString(key, str.toString())
        }

        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值

     * @param context
     * *
     * @param key
     * *
     * @param defaultObject
     * *
     * @return
     */
    operator fun get(context: Context, key: String, defaultObject: Any): Any? {
        val sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)

        if (defaultObject is String) {
            return sp.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            return sp.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            return sp.getLong(key, defaultObject)
        }

        return null
    }

    /**
     * 移除某个key值已经对应的值
     * @param context
     * *
     * @param key
     */
    fun remove(context: Context, key: String) {
        val sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据
     * @param context
     */
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在
     * @param context
     * *
     * @param key
     * *
     * @return
     */
    fun contains(context: Context, key: String): Boolean {
        val sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对

     * @param context
     * *
     * @return
     */
    fun getAll(context: Context): Map<String, *> {
        val sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE)
        return sp.all
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类

     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * 反射查找apply的方法

         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz = SharedPreferences.Editor::class.java
                return clz!!.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }

            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit

         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }

            editor.commit()
        }
    }
}