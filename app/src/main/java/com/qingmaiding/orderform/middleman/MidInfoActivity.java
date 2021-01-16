package com.qingmaiding.orderform.middleman;


import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MidInfoActivity extends BaseActivity {

    @BindView(R.id.tag1)
    TextView tag1;
    @BindView(R.id.tag2)
    TextView tag2;
    @BindView(R.id.tag3)
    TextView tag3;
    @BindView(R.id.tag4)
    TextView tag4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_midinfo);
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
                            tag4.setText(result.getJSONObject("data").getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
