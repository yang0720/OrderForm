package com.qingmaiding.orderform.shop;


import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.utils.Constants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopThreeFragment extends BaseFragment {
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_three;
    }

    @Override
    protected void setUpView() {

    }
    @BindView(R.id.tag2)
    EditText tag2;

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
                            tag1.setText("1元等于"+result.getJSONObject("data").getString("exchange")+"积分");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    @OnClick(R.id.tag13)
    public void tag13(){
        if(tvIsNull(tag2)){
            toastShort("充值金额不能为空");
            return;
        }
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/recharge"))
                .addParams("price",tag2.getText().toString())
                .addParams("type","1")
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.e("result", result.toString());

                        try {
                            PayReq request = new PayReq();
                            request.appId = result.getString("appid");
                            request.partnerId = result.getString("partnerid");
                            request.prepayId= result.getString("prepayid");
                            request.packageValue = result.getString("package");
                            request.nonceStr= result.getString("noncestr");
                            request.timeStamp= result.getString("timestamp");
                            request.sign= result.getString("sign");
                            Constants.wx_api.sendReq(request);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


}
