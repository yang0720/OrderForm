package com.qingmaiding.orderform.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tag5)//手机
    EditText tag5;
    @BindView(R.id.tag6)//验证码
    EditText tag6;
    @BindView(R.id.tag1)//密码
    EditText tag1;
    @BindView(R.id.tag7)//中间商id
    EditText tag7;
    @BindView(R.id.tag2)//确认密码
    EditText tag2;
    @BindView(R.id.tag12_yes)//中间商
    RadioButton tag12_yes;
    private String group_id = "2";
    @OnClick(R.id.tag1)
    public void tag1(){
        //startActivity(new Intent(LoginActivity.this,SelectIdentityActivity.class));
    }
    @OnClick(R.id.tag2)
    public void tag2(){
//        startActivity(new Intent(RegisterActivity.this,SelectIdentityActivity.class));
    }
    @OnClick(R.id.tag4)//获取验证码
    public void tag4(){
//        startActivity(new Intent(RegisterActivity.this,SelectIdentityActivity.class));
        sendAuthCode();
    }
    @OnClick(R.id.tag3)
    public void tag3(){
//        startActivity(new Intent(RegisterActivity.this,SelectIdentityActivity.class));
        commitRegist();
    }

    private void commitRegist() {
        if(isEmpty(tag5.getText().toString().trim())){
            toast("手机号不能为空");
            return;
        }
        if(isEmpty(tag6.getText().toString().trim())){
            toast("验证码不能为空");
            return;
        }
        if(isEmpty(tag1.getText().toString().trim())){
            toast("密码不能为空");
            return;
        }
        if(isEmpty(tag2.getText().toString().trim())){
            toast("确认密码不能为空");
            return;
        }
        if(!(tag1.getText().toString().trim()).equals((tag2.getText().toString().trim()))){
            toast("密码与确认密码输入不一致");
            return;
        }
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/user/register"))
                .addParams("mobile",tag5.getText().toString().trim())
                .addParams("group_id",group_id)
                .addParams("middleman_id",tag7.getText().toString().trim())
                .addParams("code",tag6.getText().toString().trim())
                .addParams("password",tag1.getText().toString().trim())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                log(result.toString());
                            }
                        });
                    }
                });
    }



    private void sendAuthCode() {
        if(isEmpty(tag5.getText().toString().trim())){
            toast("手机号不能为空");
            return;
        }
        dialog("");
        OkHttpUtils.get()
                .url(getUrl(this,"/api/sms/send"))
                .addParams("mobile",tag5.getText().toString().trim())
                .addParams("event","register")
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
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setToolbar("注册");
        initView();
        //权限检查和申请
    }

//    private String middleman_id = "";
    private void initView() {
        tag12_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tag7.setVisibility(View.GONE);
//                    middleman_id = "";
                    group_id = "3";
                }else{
                    tag7.setVisibility(View.VISIBLE);
                    group_id = "2";
                }
            }
        });
    }

}
