package com.qingmaiding.orderform.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

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


public class AutoCodeLoginActivity extends BaseActivity {


    @BindView(R.id.tag3)//手机号
    EditText tag3;
    @BindView(R.id.tag6)//验证码
    EditText tag6;
//    @BindView(R.id.tag4)//密码
//            EditText tag4;
    //登录
    @OnClick(R.id.tag1)
    public void tag1(){
        //startActivity(new Intent(LoginActivity.this,SelectIdentityActivity.class));1014237
        commitLogin();

    }
    @OnClick(R.id.tag4)//获取验证码
    public void tag4(){
//        startActivity(new Intent(RegisterActivity.this,SelectIdentityActivity.class));
        sendAuthCode();
    }
    private void sendAuthCode() {
        if(isEmpty(tag3.getText().toString().trim())){
            toast("手机号不能为空");
            return;
        }
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/Sms/send"))
                .addParams("mobile",tag3.getText().toString().trim())
                .addParams("event","mobilelogin")
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

    private void commitLogin() {
        if(isEmpty(tag3.getText().toString().trim())){
            toast("手机号不能为空");
            return;
        }
        if(isEmpty(tag6.getText().toString().trim())){
            toast("验证码不能为空");
            return;
        }
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/user/mobilelogin"))
                .addParams("mobile",tag3.getText().toString().trim())
                .addParams("captcha",tag6.getText().toString().trim())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             try {
                                    PrefMethed.settoken(AutoCodeLoginActivity.this,result.getJSONObject("data").getJSONObject("userinfo").getString("token"));

                                    PrefMethed.setuser_info(AutoCodeLoginActivity.this,result.getJSONObject("data").getJSONObject("userinfo").toString());

                                    if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("1")){
                                        //平台
                                        startActivity(new Intent(AutoCodeLoginActivity.this, PlatMainActivity.class));
                                    }else if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("2")){
                                        //商家
                                        startActivity(new Intent(AutoCodeLoginActivity.this, ShopMainActivity.class));
                                    }else if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("3")){
                                        //中间商
                                        startActivity(new Intent(AutoCodeLoginActivity.this, MidMainActivity.class));
                                    };
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
    }

    @OnClick(R.id.tag2)
    public void tag2(){
        startActivity(new Intent(AutoCodeLoginActivity.this,MyLoginActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authcode_login);
        ButterKnife.bind(this);
        setToolbar("登录");
        //权限检查和申请
    }

}
