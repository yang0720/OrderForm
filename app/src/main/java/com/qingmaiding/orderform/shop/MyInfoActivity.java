package com.qingmaiding.orderform.shop;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyInfoActivity extends BaseActivity {

    @BindView(R.id.tag1)
    TextView tag1;
    @BindView(R.id.tag2)
    TextView tag2;
    @BindView(R.id.tag3)
    TextView tag3;
    @BindView(R.id.tag4)
    TextView tag4;
    @OnClick(R.id.tag5)//个人信息
    public void tag5(){
//        startActivity(new Intent(com.ruihuada.ulishop.mymessage.myinfo.MyInfoActivity.this, BasicMessageActivity.class));
        alpaDialogInput("输入中间商id", "", "请输入中间商id", "", true, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                postZhongId(dialog,input);
            }
        });
    }

    private void postZhongId(MaterialDialog dialog, CharSequence input) {
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/user/bindmiddleman_id"))
                .addParams("middleman_id",input.toString())
                .addHeader("token",getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            toast(result.getString("msg"));
                            getShopData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myinfo);
        ButterKnife.bind(this);
        setToolbar("我的");
        //权限检查和申请
        getShopData();

    }

    private void getShopData() {
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/user/index"))
                .addHeader("token",getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            tag1.setText(result.getJSONObject("data").getString("nickname"));
                            tag2.setText(result.getJSONObject("data").getString("mobile"));
                            tag3.setText(result.getJSONObject("data").getString("score"));
                            tag4.setText(result.getJSONObject("data").getString("middleman_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
