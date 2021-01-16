package com.alpamayo.utils

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.InputType
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.alpamayo.utils.entity.RequestState
import com.alpamayo.utils.utils.Alpa
import com.alpamayo.utils.utils.Log
import com.alpamayo.utils.utils.SBUtils
import com.alpamayo.utils.utils.SPUtils
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.codbking.widget.DatePickDialog
import com.codbking.widget.OnSureLisener
import com.codbking.widget.bean.DateType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Administrator on 2016/11/18.
 */

open class UtilsActivity: AppCompatActivity(), View.OnClickListener {
    var requestState: RequestState? = null
    var toolbar: Toolbar? = null
    var message: Message? = null
    var strSql: String? = null
    var result: String? = null
    //声音相关
    private var soundpool: SoundPool? = null
    private var errorsoundid: Int = 0
    private var scansoundid: Int = 0
    private var confirmsound: Int = 0
    private var backsound: Int = 0
    var cursor: Cursor? = null
    var jsonArray: JSONArray? = null
    var jsonObject: JSONObject? = null
    var jo: JSONObject? = null

    open var map: MutableMap<String, Any> = HashMap()
    open var list: MutableList<MutableMap<String, Any>> = ArrayList()


    //define interface
    interface OnRecyclerViewItemClickListener {
        fun onItemClick(position: Int,v: View? = null)
    }
    interface OnRecyclerViewItemListener {
        fun onItemClick(position: Int,v: View? = null)
        fun onItemLongClick(position: Int,v: View? = null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //错误提示音
        soundpool = SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100)
        errorsoundid = soundpool!!.load(context, R.raw.error, 1)
        scansoundid = soundpool!!.load("/etc/Scan_new.ogg", 1)
        confirmsound = soundpool!!.load(context, R.raw.sound_refresh, 1)
        backsound = soundpool!!.load(context, R.raw.back, 1)
        //WebService配置
    }
    /**
     * 日期选择器
     */
    fun alpaSelectTime(onSureLisener: OnSureLisener, startDate:Date=Date(), yearLimt:Int=0, title:String="选择时间", dateType: DateType = DateType.TYPE_YMD){
        var timeDialog = DatePickDialog(context);
        //设置上下年分限制
        timeDialog.setYearLimt(yearLimt)
        //设置标题
        timeDialog.setTitle(title)
        //设置类型
        timeDialog.setType(dateType)
        //设置消息体的显示格式，日期格式
        timeDialog.setMessageFormat("yyyy-MM-dd HH:mm")
        //设置选择回调
        //timeDialog.setOnChangeLisener(null)
        //设置点击确定按钮回调
        timeDialog.setOnSureLisener(onSureLisener)
        timeDialog.setStartDate(startDate)
        timeDialog.show()
    }
    /**
     * 释放锁定
     */
    public override fun onPause() {
        super.onPause()
    }





    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        cursor = null
        jsonArray = null
        jsonObject = null
        soundpool = null
        super.onDestroy()
    }
    fun getRandom6(): Int{
        return ((Math.random() * 9 + 1) * 1000).toInt()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
            }
            KeyEvent.KEYCODE_HOME -> {
            }
            else -> {
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    open fun handler(msg: Message) {
        dialogClose()
    }

    fun baseThread(run: Runnable) {
        baseThread(null, run)
    }

    fun baseThread(message: String?, run: Runnable) {
        dialog(message)
        Thread(run).start()
    }

    fun baseThreadNoDialog(run: Runnable) {
        Thread(run).start()
    }

    var baseHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            handler(msg)
        }
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftKeyboard(v: View) {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 防止连续点击按钮
     * @return
     */
    val isFastClick: Boolean
        @Synchronized get() {
            val time = System.currentTimeMillis()
            if (time - lastClickTime < 500) {
                return true
            }
            lastClickTime = time
            return false
        }

    /**
     * 防止连续点击
     */
    private val MIN_CLICK_DELAY_TIME = 500
    private var lastClickTime: Long = 0
    override fun onClick(v: View) {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            onNoDoubleClick(v)
        }
    }

    open fun setToolbar(title: String){
        toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar?.setTitle(title)
        setSupportActionBar(toolbar)
        setTitleCenter(toolbar!!)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    open fun setTitleCenter(toolbar: Toolbar) {
        val title = "title"
        val originalTitle = toolbar.title
        toolbar.title = title
        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view is TextView) {
                val textView = view
                if (title == textView.text) {
                    textView.gravity = Gravity.CENTER
                    val params = Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT)
                    params.gravity = Gravity.CENTER
                    textView.layoutParams = params
                }
            }
            toolbar.title = originalTitle
        }
    }

    /**
     * 已优化重复点击
     * 点击间隔为 1000ms
     * @param v
     */
    open fun onNoDoubleClick(v: View) {}

    /**
     * 扫描头
     */
//    private val mScanReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val barocode = intent.getByteArrayExtra("barocode")
//            val barocodelen = intent.getIntExtra("length", 0)
//            onScanResults(String(barocode, 0, barocodelen))
//            soundToScan()
//        }
//    }

    /**
     * 扫描成功后调用
     * @param result
     */
    open fun onScanResults(result: String) {}

    /**
     * 扫描声音
     */
    fun soundToScan() {
        try {
            soundpool!!.play(scansoundid, 1f, 1f, 0, 0, 1f)
        }catch (e:java.lang.Exception){
            soundException(e)
        }
    }

    /**
     * 错误声音
     */
    fun soundToError() {
        try {
            soundpool!!.play(errorsoundid, 1f, 1f, 0, 0, 1f)
        }catch (e:java.lang.Exception){
            soundException(e)
        }
    }

    /**
     * 确定声音
     */
    fun soundToConfirm() {
        try {
            soundpool!!.play(confirmsound, 1f, 1f, 0, 0, 1f)
        }catch (e:java.lang.Exception){
            soundException(e)
        }

    }

    /**
     * 返回声音
     */
    fun soundToBack() {
        try {
            soundpool!!.play(backsound, 1f, 1f, 0, 0, 1f)
        }catch (e:java.lang.Exception){
            soundException(e)
        }
    }



    fun soundException(e:Exception){

    }
    /**
     * 全局唯一标识符
     * @return
     */
    val guid: String
        get() = UUID.randomUUID().toString()
    /**
     * 当前
     * @return
     */
    val context: Context
        get() = this

    /**
     * 获取一个对象的值,支持
     * （EditText, TextView, Message, Exception）
     * @param arg
     * *
     * @param defaultText
     * *
     * @return 如果获取的值为"",null,长度为0时，则使用默认值 没有默认值时显示""
     */
    fun getText(arg: Any?, defaultText: String = ""): String {
        return Alpa.getText(arg,defaultText)
    }
    fun getTextIsEmpty(arg: Any, defaultValue: Any): String {
        return Alpa.getTextIsEmpty(arg,defaultValue)
    }

    /**
     * 获取当前[JsongObject]值
     * @param key
     * *
     * @return
     * *
     * @throws JSONException
     */
    fun getJText(key: String): String {
        try {
            return getText(jsonObject!!.getString(key))
        } catch (e: Exception) {
            log(e.message)
        }

        return ""
    }

    /**
     * 获取当前[JsongObject]值
     * @param json
     * *
     * @param key
     * *
     * @return
     */
    fun getJText(json: JSONObject, key: String): String {
        try {
            return getText(json.getString(key))
        } catch (e: Exception) {
            log(e.message)
        }

        return ""
    }

    /**
     * 获取当前[Cursor]的值
     * @param key
     * *
     * @return
     * *
     * @throws JSONException
     */
    fun getCText(key: String): String {
        try {
            return getText(cursor!!.getString(cursor!!.getColumnIndex(key)))
        } catch (e: Exception) {
            log(e.message)
        }

        return ""
    }

    /**
     * 获取SharedPreferences的值
     * @param key
     * *
     * @return
     */
    fun getSPText(key: String): String {
        try {
            return SPUtils.get(applicationContext, key, "")!!.toString()
        } catch (e: Exception) {
            log(e.message)
        }

        return ""
    }

    /**
     * 删除SharedPreferences的值
     * @param key
     */
    fun remove(key: String) {
        SPUtils.remove(applicationContext, key)
    }

    /**
     * 保存SharedPreferences的值
     * @param key
     * *
     * @param object
     */
    fun put(key: String, `object`: Any) {
        SPUtils.put(applicationContext, key, `object`)
    }

    /**
     * 将当前的Map保存到SP里面
     * @param key
     * *
     * @param maps
     */
    fun putMap(key: String, maps: Map<String, Any>) {
        SPUtils.putMap(applicationContext, key, maps)
    }

    /**
     * 获取当前年月日
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    fun getDate(fs: String? = null,date: Date? = Date()): String {
        return SimpleDateFormat(fs ?: "yyyy-MM-dd").format(date)
    }

    /*Dialog私有属性*/
    var material_design_dialog: Dialog? = null
    var material_design_dialog_text: TextView? = null

    /*是否自动关闭当前Dialog*/
    var dialog_is_auto_close_dialog = true
    /*当前Dialog是否已显示*/
    var dialog_is_showing = false

    /**
     * 弹出加载框
     * @param msg
     */
    fun dialog(msg: String? = null) {
        //如果当前Dialog正在运行则忽略启动
        if (!dialog_is_showing) {
            if (material_design_dialog == null) {
                val dialog_viewView = LayoutInflater.from(this).inflate(R.layout.material_design_dialog, null)
                material_design_dialog_text = dialog_viewView.findViewById(R.id.material_design_dialog_text) as TextView
                material_design_dialog = Dialog(this, R.style.utils_material_design_dialog)// 创建自定义样式dialog
                material_design_dialog?.setCancelable(false)// 不可以用“返回键”取消
                material_design_dialog?.setCanceledOnTouchOutside(false)
                material_design_dialog?.setContentView(dialog_viewView, LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT))// 设置布局
            }
            if (msg != null && msg.length > 1) {
                material_design_dialog_text?.text = msg// 设置加载信息
                material_design_dialog_text?.visibility = View.VISIBLE
            } else {
                material_design_dialog_text?.visibility = View.GONE
            }
            material_design_dialog?.show()
            dialog_is_showing = true
            log("创建了Dialog")
        }
    }

    /**
     * 关闭Dialog
     */
    @JvmOverloads fun dialogClose(forceClose: Boolean = false) {
        try {
            if (forceClose) {
                material_design_dialog!!.dismiss()
                dialog_is_showing = false
                log("关闭了Dialog")
            } else {
                if (dialog_is_auto_close_dialog && dialog_is_showing) {
                    material_design_dialog!!.dismiss()
                    dialog_is_showing = false
                    log("关闭了Dialog")
                }
            }
        } catch (e: Exception) {
            dialog_is_showing = false
            log("dialogClose意外关闭：" + getText(e))
        }

    }

    /**
     * 弹窗
     */
    fun alpaDialog(isCancel: Boolean,msg: String?="",title: String="系统提示"): MaterialDialog.Builder{
        if(msg.isNullOrBlank()){
            return MaterialDialog.Builder(context).theme(Theme.LIGHT).title(title).canceledOnTouchOutside(isCancel).cancelable(isCancel)
        }
        return MaterialDialog.Builder(context).theme(Theme.LIGHT).title(title).content(msg!!).canceledOnTouchOutside(isCancel).cancelable(isCancel)
    }

    fun alpaDialogToast(msg: String,btnText: String="确定"){
        try{
            alpaDialog(true,msg).negativeText(btnText).build().show()
        }catch (e:Exception){

        }
    }
    fun alpaDialogConfirm(msg: String,btnText: String,callback: MaterialDialog.SingleButtonCallback,isCancel: Boolean=false,canceText: String="取消"){
        alpaDialog(isCancel,msg).negativeText(btnText).onNegative(callback).negativeColor(ContextCompat.getColor(context, R.color.def_bg)).positiveText(canceText).build().show()
    }
    fun alpaDialogToast(msg: String,btnText: String,callback: MaterialDialog.SingleButtonCallback){
        alpaDialog(false,msg).negativeText(btnText).onNegative(callback).build().show()
    }
    fun alpaDialogList(title: String,obj: ArrayList<Any>,isCancel: Boolean,callback: MaterialDialog.ListCallback): MaterialDialog.Builder{
        return alpaDialog(isCancel,null,title).items(obj).itemsCallback(callback)
    }
    fun alpaDialogInput(title: String,msg: String,input_hint: String?,input_prefill: String?,isCancel: Boolean,InputCallback: MaterialDialog.InputCallback){
        alpaDialog(isCancel,msg,title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(input_hint ?: "内容不能为空", input_prefill ?: "", InputCallback)
                .show();
    }
    fun alpaDialogInputNum(title: String,msg: String,input_hint: String?,input_prefill: String?,isCancel: Boolean,InputCallback: MaterialDialog.InputCallback){
        alpaDialog(isCancel,msg,title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(input_hint ?: "内容不能为空", input_prefill ?: "", InputCallback)
                .show();
    }
    fun alpaListToArray(obj: List<Map<String, Any>>,key: String): ArrayList<String> {
        val to = ArrayList<String>()
        for (i in obj.indices) {
            to.add(obj[i][key].toString())
        }
        return to
    }


    fun logs(s: Any?){
        Alpa.log("-->"+s.toString())
    }
    /**
     * 输出日志
     * @param s
     */
    fun log(s: Any?): String {
        return Log.e(s)
    }
    fun logF(s: Any?): String{
        return log("#----->$s")
    }
    /**
     * 输出日志
     * @param s
     * *
     * @param e
     */
    fun log(s: Any, e: Any) {
        Log.e(s.toString() + ":" + getText(e))
    }

    fun log(s: Any, s1: String) {
        Log.e(s.toString() + ":" + s1)
    }

    fun makeLong(v: View, s: String?): SBUtils {
        return SBUtils.makeLong(v, s ?: "null")
    }

    fun makeShort(v: View, s: String?): SBUtils {
        return SBUtils.makeShort(v, s ?: "null")
    }

    internal var toast: Toast? = null
    /**
     * 提示框
     * @param msg
     */
    open fun toast1(msg: Any) {
        soundToConfirm()
        log("Toast:" + msg.toString())
        if (toast == null) {

            toast = Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT)
        } else {

            toast!!.setText(msg.toString())
        }
        toast!!.show()

    }

    /**
     * 提示框
     * @param msg
     */
    open fun toast(msg: Any) {
        try{
            log("toast:" + getText(msg))
            soundToError()
            if (toast == null) {
                toast = Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT)
            } else {
                toast!!.setText(msg.toString())
            }
            toast!!.show()
        }catch (e: Exception){
            log("toast 错误："+getText(e))
        }
    }

    fun toast(msg: Any?, e: Exception) {
        toast(if (msg == null) "" else msg.toString() + ":" + getText(e))
    }

    fun toastError(e: Any) {
        toast("操作失败，错误信息:" + getText(e))
    }

    /**
     * 格式化数字
     * @param value
     * *
     * @return
     */
    fun getDecimalFormat(format: String?, value: Double): String {
        return DecimalFormat(format ?: "#0.00").format(value)
    }

    fun getDecimalFormat(value: Double): String {
        return getDecimalFormat(null, value)
    }

    /**
     * 如果末尾包含0则去掉
     * @param format
     * *
     * @param value
     * *
     * @return
     */
    fun getDecimalFormatNo0(format: String, value: Double): Any {
        var value = value
        try {
            value = java.lang.Double.valueOf(getDecimalFormat(format, value))!!
            val num = java.lang.Double.valueOf(value)!!
            if (num % 1.0 == 0.0) {
                return num.toInt()
            }
            return num
        } catch (e: Exception) {
            log("BaseActivity -> getDecimalFormatNo0 Error", getText(e))
        }

        return value
    }

    var db: SQLiteDatabase? = null
    /**
     * 打开数据库
     */
    fun openDataBase() {
        if (Alpa.existsDB(resources)) {
            if (db == null) {
                db = SQLiteDatabase.openDatabase(Alpa.dataBasePath, null, SQLiteDatabase.OPEN_READWRITE)
                log("数据库地址：" + Alpa.dataBasePath + ",已打开")
            }
        } else {
            log("数据库地址：" + Alpa.dataBasePath + ",无法打开数据库")
        }
    }

    fun beginTransaction() {
        try {
            db?.beginTransaction()
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }

    fun transactionSuccessful() {
        try {
            db?.setTransactionSuccessful()
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }

    fun endTransaction() {
        try {
            db?.endTransaction()
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }

    fun execSQL() {
        try {
            log(strSql)
            db?.execSQL(strSql)
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }

    /**
     * 关闭数据库
     */
    fun closeDataBase() {
        if (db != null) {
            db?.close()
            db = null
            log("数据库地址：" + Alpa.dataBasePath + ",已关闭")
        }
    }

    fun rawSql() {
        try {
            log(strSql)
            cursor = db?.rawQuery(strSql, null)
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }

    fun rawSqlOne() {
        try {
            log(strSql)
            cursor = db?.rawQuery(strSql, null)
            cursor!!.moveToFirst()
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }

    companion object {
        private val SCAN_ACTION = "urovo.rcv.message"
    }




    var _scan_callback_is_success = true
    var _if_the_thread_is_done = true
    var _alert_sheet_to_default = true

    fun alert(title: String, msg: String?, cancel: String, data: Array<String>?, listener: OnItemClickListener?, destructive:String?=null){
        var alertView = AlertView.Builder().setContext(context)
        alertView.setTitle(title)
        alertView.setCancelText(cancel ?: null)
        if (!destructive.isNullOrEmpty()) alertView.setDestructive(destructive ?: "无效选项")
        if(data !=null && data.size > 0) alertView.setOthers(data)
        alertView.setOnItemClickListener(listener)
        alertView.setMessage(msg)
        if (_alert_sheet_to_default) {
            alertView.setStyle(AlertView.Style.ActionSheet)
        } else {
            alertView.setStyle(AlertView.Style.Alert)
        }
        alertView.build().show()
    }

    fun alertList(title: String, message: String?, cancel: String, data: List<Map<String, Any>>, key: String, listener: OnItemClickListener, destructive:String?=null) {
        val arrayList = ArrayList<Any>()
        var value: String? = null
        for (string in data) {
            value = string.get(key) as String?
            if(!value?.trim().isNullOrEmpty()) { arrayList.add(value!!)}
        }
        alert(title, message, cancel, arrayList.toArray(arrayOfNulls<String>(arrayList.size)), listener,destructive)
    }

    fun makeLongSys(v: View, s: String?,error: Boolean = false): SBUtils {
        if (error){
            soundToError()
        }
        return SBUtils.makeLong(v, "系统提示\n"+s ?: "null")
    }
    fun makeShortSys(v: View, s: String?,error: Boolean = false): SBUtils {
        if (error){
            soundToError()
        }
        return SBUtils.makeShort(v, "系统提示\n"+s ?: "null")
    }
}