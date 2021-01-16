package com.qingmaiding.orderform.shop;


import android.widget.LinearLayout;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopFirFragment extends BaseFragment {
    @BindView(R.id.tag1)
    TextView tag1;
    @BindView(R.id.tag2_text)
    TextView tag2_text;
    @BindView(R.id.tag3_text)
    TextView tag3_text;
    @BindView(R.id.tag4)
    TextView tag4;
    @BindView(R.id.tag5)
    TextView tag5;
    @BindView(R.id.tag6)
    TextView tag6;
    @BindView(R.id.tag7)
    TextView tag7;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_fir;
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        getShopData();
    }
    @OnClick(R.id.tag2)
    public void tag2(){
        DatePickDialog datePickDialog = new DatePickDialog(getActivity());
        //设置上下年分限制
        datePickDialog.setYearLimt(5);
        //设置标题
        datePickDialog.setTitle("选择时间");
        //设置类型
        datePickDialog.setType(DateType.TYPE_YMDHM);
        //设置消息体的显示格式，日期格式
        datePickDialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        //timeDialog.setOnChangeLisener(null)
        //设置点击确定按钮回调
        datePickDialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                tag2_text.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
                getShopData();
            }
        });
        datePickDialog.setStartDate(new Date());
        datePickDialog.show();
    }

    @OnClick(R.id.tag3)
    public void tag3(){
        DatePickDialog datePickDialog = new DatePickDialog(getActivity());
        //设置上下年分限制
        datePickDialog.setYearLimt(5);
        //设置标题
        datePickDialog.setTitle("选择时间");
        //设置类型
        datePickDialog.setType(DateType.TYPE_YMDHM);
        //设置消息体的显示格式，日期格式
        datePickDialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        //timeDialog.setOnChangeLisener(null)
        //设置点击确定按钮回调
        datePickDialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                tag3_text.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date));
                getShopData();
            }
        });
        datePickDialog.setStartDate(new Date());
        datePickDialog.show();
    }

    private void getShopData() {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/index/index"))
                .addHeader("token",getToken())
                .addParams("start",tag2_text.getText().toString())
                .addParams("end",tag3_text.getText().toString())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            tag1.setText(result.getJSONObject("data").getString("score"));
                            tag4.setText(result.getJSONObject("data").getString("count_chu"));
                            tag5.setText(result.getJSONObject("data").getString("count_wchu"));
                            tag6.setText(result.getJSONObject("data").getString("count_chong"));
                            tag7.setText(result.getJSONObject("data").getString("count_xf"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
