package com.qingmaiding.orderform.ui.login;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;

import com.alpamayo.utils.utils.PrefMethed;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.middleman.MidMainActivity;
import com.qingmaiding.orderform.platform.PlatMainActivity;
import com.qingmaiding.orderform.shop.ShopMainActivity;
import com.qingmaiding.orderform.utils.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyLoginActivity extends BaseActivity {

    @BindView(R.id.tag3)//手机号
            EditText tag3;
    @BindView(R.id.tag4)//密码
            EditText tag4;
    //登录
    @OnClick(R.id.tag1)
    public void tag1(){
        //startActivity(new Intent(LoginActivity.this,SelectIdentityActivity.class));
        commitLogin();
    }

    private void commitLogin() {
        if(isEmpty(tag3.getText().toString().trim())){
            toast("手机号不能为空");
            return;
        }
        if(isEmpty(tag4.getText().toString().trim())){
            toast("密码不能为空");
            return;
        }
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/user/login"))
                .addParams("account",tag3.getText().toString().trim())
                .addParams("password",tag4.getText().toString().trim())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                log(result.toString());
//                                toast("发送成功");
                                try {
                                    PrefMethed.settoken(MyLoginActivity.this,result.getJSONObject("data").getJSONObject("userinfo").getString("token"));

                                    PrefMethed.setuser_info(MyLoginActivity.this,result.getJSONObject("data").getJSONObject("userinfo").toString());

                                    if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("1")){
                                        //平台
                                        startActivity(new Intent(MyLoginActivity.this, PlatMainActivity.class));
                                    }else if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("2")){
                                        //商家
                                        startActivity(new Intent(MyLoginActivity.this, ShopMainActivity.class));
                                    }else if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("3")){
                                        //中间商
                                        startActivity(new Intent(MyLoginActivity.this, MidMainActivity.class));
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
        log("转注册接口");
        startActivity(new Intent(MyLoginActivity.this,RegisterActivity.class));
    }
    @OnClick(R.id.tag5)
    public void tag5(){
        startActivity(new Intent(MyLoginActivity.this,AutoCodeLoginActivity.class));
        //验证码登录

    }
    @OnClick(R.id.tag6)
    public void tag6(){
        log("微信登录");
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        Constants.wx_api.sendReq(req);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mylogin);
        ButterKnife.bind(this);
        setToolbar("登录");
        initWxData();
//        commit();
        //权限检查和申请
    }

    // IWXAPI 是第三方app和微信通信的openApi接口
    private void initWxData() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        Constants.wx_api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);

        // 将应用的appId注册到微信
        Constants.wx_api.registerApp(Constants.APP_ID);

        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该app注册到微信
                Constants.wx_api.registerApp(Constants.APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

}
