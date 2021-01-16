package com.qingmaiding.orderform;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.alpamayo.utils.UtilsActivity;
import com.alpamayo.utils.utils.PrefMethed;
import com.google.gson.Gson;
import com.qingmaiding.orderform.empty.AddressCityEmpty;
import com.qingmaiding.orderform.utils.DensityUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.nereo.multi_image_selector.utils.FileUtils;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;


public class BaseActivity extends UtilsActivity {

    String sd_cache_name = "DICOT";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        initImmersionBar();
//        getFacList();
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式

    }

    protected String getToken(){
        return PrefMethed.gettoken(this);
    }
    protected String getType(){
        return PrefMethed.getstatus(this);
    }

//    protected void initSearchView(){
//        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }

    protected boolean isLogin(){
        return !getToken().equals("");
    }

    public static boolean isEmpty(String input) {
        if (input == null || input.length() == 0 || "".equals(input) || "null".equals(input))
            return true;
        else
            return false;
    }

    public static String getUrl(Context context,String url){
        String ip = PrefMethed.getIPAddress(context);
        String port = PrefMethed.getPort(context);
        Log.e("1111111111","https://"+ip+url);
        return "https://"+ip+url;
    }

    public abstract class MyCallBack extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            dialogClose();
            alpaDialogToast("错误,请稍后重试","确定");
        }

        @Override
        public void onResponse(String response, int id) {
            dialogClose();
//            log(response);
            try{
                JSONObject result = new JSONObject(response);
                log("response",response);
                if(result.getString("code").equals("1")||result.getString("code").equals("0")){
                    onSuccess(result);
                }else if(result.getString("code").equals("400")){
                    toast1(result.getString("msg"));
                }else{
                    toast("请稍后重试");
                }
            }catch (JSONException e){
                onError(null,new Exception("0x000099致命错误,请联系服务商"),0);
            }

        }
        public abstract void onSuccess(JSONObject result);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            log("onOptionsItemSelected");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Context get(){
        return this;
    }



    /**
     * 对textview或者edittext进行非空判断
     */
    public boolean tvIsNull(TextView tv){
        return TextUtils.isEmpty(tv.getText());
    }
    public boolean tvIsNull(EditText et){
        return TextUtils.isEmpty(et.getText());
    }

    public String getCachePath(String path) {
        String path_tmpString = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + sd_cache_name + "/" + path + "/";
        File CacheDirectory = new File(path_tmpString);
        if (!CacheDirectory.exists()) {
            CacheDirectory.mkdirs();
        }
        return path_tmpString;
    }

    /**
     * 获取物资条码
     */
    public String getAssetsCode(String result){
        String code = "";
        String[] assetInfo = result.split("\\|\\|");
        if(assetInfo.length>4){
            code = assetInfo[0];
        }else{
            code =  result;
        }
        return code;
    }

    /**
     * 获取物资名称
     */
    public String getAssetsName(String result){
        String name = "";
        String[] assetInfo = result.split("\\|\\|");
        if(assetInfo.length>4){
            name = assetInfo[0];
        }else{
            name =  result;
        }
        return name;
    }
    //请求拍照权限
    public void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            showCamare();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "该功能需要相机和文件传输权限",
                    123, perms);
        }
    }
    public File mTmpFile = null;
    public File showCamare(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            mTmpFile = FileUtils.createTmpFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mTmpFile != null && mTmpFile.exists()) {
            Uri uri;
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                uri = Uri.fromFile(mTmpFile);
            }else{
                /**
                 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                 * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                 */
                uri = FileProvider.getUriForFile(getApplicationContext(),"com.dzioe.myapplication.provider",
                        mTmpFile);}
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, 55);
            
        }
        return mTmpFile;
    }
    /**
     * 获取所有的派系
     */
    public JSONArray facJSonArray;
    public ArrayList<Object> facList;
    public void getFacList(){
        if(facJSonArray == null){
            getAllClique();
        }
    }

    public void getAllClique() {
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/index/First/factions_all"))
                .addHeader("token", getToken())
                .addHeader("type",getType())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            facJSonArray = result.getJSONArray("factions");
                            facList = new ArrayList<>();
                            for (int i = 0; i < facJSonArray.length(); i++) {
                                try {
                                    facList.add(facJSonArray.getJSONObject(i).getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public ArrayList<AddressCityEmpty> options1Items = new ArrayList<>(); //省
    public ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//市
    public ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//区
    /**
     * 解析数据
     */
    public void initJsonData() {//解析数据 （省市区三级联动）
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = DensityUtil.getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<AddressCityEmpty> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三级）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    public ArrayList<AddressCityEmpty> parseData(String result) {//Gson 解析
        ArrayList<AddressCityEmpty> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                AddressCityEmpty entity = gson.fromJson(data.optJSONObject(i).toString(), AddressCityEmpty.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

}
