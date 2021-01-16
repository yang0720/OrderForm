package com.qingmaiding.orderform.middleman.fragment;


import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.utils.Constants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class MidThreeFragment extends BaseFragment {
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_threemid;
    }

    @Override
    protected void setUpView() {

    }
    @BindView(R.id.tag2)
    EditText tag2;
    @BindView(R.id.tag3)
    EditText tag3;
    @BindView(R.id.tag4)
    RadioButton tag4;

    @Override
    protected void setUpData() {
        getChongData();
    }
    @BindView(R.id.tag1)
    TextView tag1;
    private void getChongData() {
        dialog();
        OkHttpUtils.get()
                .url(getUrl("/api/user/chongzhiSet"))
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
//                            toastShort(result.getString("msg"));
                            tag1.setText(result.getJSONObject("data").getString("exchange")+"积分"+"等于1元");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick(R.id.tag13)
    public void tag13(){
        if(tvIsNull(tag2)){
            toastShort("提现积分不能为空");
            return;
        }
        if(tvIsNull(tag3)){
            toastShort("提现账号不能为空");
            return;
        }
        String typeStr = "";
        if(tag4.isChecked()){
            typeStr = "1";
        }else{
            typeStr = "2";
        }
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/add_withdrawal"))
                .addParams("score",tag2.getText().toString())
                .addParams("account",tag3.getText().toString())
                .addParams("type",typeStr)
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.e("result", result.toString());
                        try {
                            toastShort(result.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
