package com.qingmaiding.orderform.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.alpamayo.utils.utils.PrefMethed;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.middleman.MidMainActivity;
import com.qingmaiding.orderform.platform.PlatMainActivity;
import com.qingmaiding.orderform.shop.ShopMainActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PhoneBindActivity extends BaseActivity {


    @OnClick(R.id.tag1)
    public void tag1(){
//        toast("修改成功");
        if(typeStr.equals("1")){
            changPhone();
        }else{
            commitNewPhone();
        }

    }
    @BindView(R.id.tag12_yes)//已有账号
    RadioButton tag12_yes;
    @BindView(R.id.tag3)
    EditText tag3;
    @BindView(R.id.tag4)
    EditText tag4;
    @BindView(R.id.tag8)
    LinearLayout tag8;
    @BindView(R.id.tag5)//密码
    EditText tag5;
    @BindView(R.id.tag6)//确认密码
    EditText tag6;
    @BindView(R.id.tag7)//中间商iD
    EditText tag7;
    /**
     * 发送验证码
     */
    @OnClick(R.id.tag2)
    public void tag2(){

        sendAuthCode();
    }
    private void changPhone() {
        if(isEmpty(tag3.getText().toString().trim())){
            toast("手机号不能为空");
            return;
        }
        if(isEmpty(tag4.getText().toString().trim())){
            toast("验证码不能为空");
            return;
        }
        dialog("");
        try {
            log(wxJson.getString("data"));
            OkHttpUtils.post()
                    .url(getUrl(this,"/api/user/bind"))
                    .addParams("type",typeStr)
                    .addParams("mobile",tag3.getText().toString().trim())
                    .addParams("code",tag4.getText().toString().trim())
                    .addParams("platform",wxJson.getJSONObject("data").getString("platform"))
                    .addParams("openid",wxJson.getJSONObject("data").getString("openid"))
                    .addParams("openname",wxJson.getJSONObject("data").getString("openname"))
                    .addParams("access_token",wxJson.getJSONObject("data").getString("access_token"))
                    .addParams("refresh_token",wxJson.getJSONObject("data").getString("refresh_token"))
                    .addParams("expires_in",wxJson.getJSONObject("data").getString("expires_in"))
                    .addParams("logintime",wxJson.getJSONObject("data").getString("logintime"))
                    .addParams("avatar",wxJson.getJSONObject("data").getString("avatar"))
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    log(result.toString());
                                    try {
                                        toast(result.getString("msg"));
                                        PrefMethed.settoken(PhoneBindActivity.this,result.getJSONObject("data").getJSONObject("userinfo").getString("token"));

                                        PrefMethed.setuser_info(PhoneBindActivity.this,result.getJSONObject("data").getJSONObject("userinfo").toString());

                                        if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("1")){
                                            //平台
                                            startActivity(new Intent(PhoneBindActivity.this, PlatMainActivity.class));
                                        }else if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("2")){
                                            //商家
                                            startActivity(new Intent(PhoneBindActivity.this, ShopMainActivity.class));
                                        }else if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("3")){
                                            //中间商
                                            startActivity(new Intent(PhoneBindActivity.this, MidMainActivity.class));
                                        };
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void commitNewPhone() {
        if(isEmpty(tag3.getText().toString().trim())){
            toast("手机号不能为空");
            return;
        }
        if(isEmpty(tag4.getText().toString().trim())){
            toast("验证码不能为空");
            return;
        }
        if(isEmpty(tag5.getText().toString().trim())){
            toast("密码不能为空");
            return;
        }
        if(isEmpty(tag6.getText().toString().trim())){
            toast("确认密码不能为空");
            return;
        }
        if(!(tag5.getText().toString().trim()).equals((tag6.getText().toString().trim()))){
            toast("密码与确认密码输入不一致");
            return;
        }
        dialog("");
        try {
            OkHttpUtils.post()
                    .url(getUrl(this,"/api/user/bind"))
                    .addParams("type",typeStr)
                    .addParams("mobile",tag3.getText().toString().trim())
                    .addParams("code",tag4.getText().toString().trim())
                    .addParams("data",wxJson.getString("data"))
                    .addParams("password",tag5.getText().toString().trim())
                    .addParams("group_id",tag7.getText().toString().trim())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    log(result.toString());
                                    toast("绑定成功");
                                }
                            });
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void sendAuthCode() {
        if(isEmpty(tag3.getText().toString().trim())){
            toast("手机号不能为空");
            return;
        }
        dialog("");
        OkHttpUtils.get()
                .url(getUrl(this,"/api/sms/send"))
                .addParams("mobile",tag3.getText().toString().trim())
                .addParams("event","mobilebind")
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                log(result.toString());
                                toast("发送成功");
                            }
                        });
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        setToolbar("手机号");
        initData();
        getWxData();
        //权限检查和申请
    }
    private String typeStr = "1";
    private void initData() {
        tag12_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tag8.setVisibility(View.VISIBLE);
//                    middleman_id = "";
                    typeStr = "2";
                }else{
                    tag8.setVisibility(View.GONE);
                    typeStr = "1";
                }
            }
        });
    }

    private JSONObject wxJson;
    private void getWxData() {
        try {
            wxJson = new JSONObject(getIntent().getStringExtra("wxData"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
