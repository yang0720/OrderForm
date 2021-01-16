package com.alpamayo.utils.utils;

import android.content.Context;

public class PrefMethed {
    /**
     * 用户名
     *
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        return PrefTool.getStringPerf(context, "userName", "userName", "");
    }

    public static void setUserName(Context context, String msg) {
        PrefTool.setStringSave(context, "userName", msg, "");
    }

    /**
     * 密码
     *
     * @param context
     * @return
     */
    public static String getPassWord(Context context) {
        return PrefTool.getStringPerf(context, "passWord", "passWord", "");
    }

    public static void setPassWord(Context context, String msg) {
        PrefTool.setStringSave(context, "passWord", msg, "");
    }

    /**
     * ip
     */
    public static String getIPAddress(Context context) {
        return PrefTool.getStringPerf(context, "IPAddress", "IPAddress", "chinacxy.net");
    }

    public static void setIPAddress(Context context, String msg) {
        PrefTool.setStringSave(context, "IPAddress", "IPAddress", msg);
    }

    /**
     * 端口
     */
    public static String getPort(Context context) {
        return PrefTool.getStringPerf(context, "Port", "Port", "");
    }

    public static void setPort(Context context, String msg) {
        PrefTool.setStringSave(context, "Port", "Port", msg);
    }

    /**
     * 用户名user_account
     */
    public static String getuser_commpany(Context context) {
        return PrefTool.getStringPerf(context, "user_account", "user_commpany", "");
    }

    public static void setuser_commpany(Context context, String msg) {
        PrefTool.setStringSave(context, "user_account", "user_commpany", msg);
    }

    /**
     * 用户名user_account
     */
    public static String getuser_account(Context context) {
        return PrefTool.getStringPerf(context, "user_account", "user_account", "");
    }

    public static void setuser_account(Context context, String msg) {
        PrefTool.setStringSave(context, "user_account", "user_account", msg);
    }
    /**
     * 用户信息
     */
    public static String getuser_info(Context context) {
        return PrefTool.getStringPerf(context, "user_info", "user_info", "");
    }

    public static void setuser_info(Context context, String msg) {
        PrefTool.setStringSave(context, "user_info", "user_info", msg);
    }
    /**
     * 用户token
     */
    public static String gettoken(Context context) {
        return PrefTool.getStringPerf(context, "token", "token", "");
    }

    public static void settoken(Context context, String msg) {
        PrefTool.setStringSave(context, "token", "token", msg);
    }
    /**
     * 用户类型
     */
    public static String getstatus(Context context) {
        return PrefTool.getStringPerf(context, "user_status", "user_status", "");
    }

    public static void setstatus(Context context, String msg) {
        PrefTool.setStringSave(context, "user_status", "user_status", msg);
    }

    /**
     * 是否显示库存为零的批次
     */
    public static String getIsShowBatch(Context context) {
        return PrefTool.getStringPerf(context, "IsShowBatch", "token", "true");
    }

    public static void setIsShowBatch(Context context, String msg) {
        PrefTool.setStringSave(context, "IsShowBatch", "IsShowBatch", msg);
    }
}