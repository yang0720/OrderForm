package com.qingmaiding.orderform.shop;


import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.adapter.OrderDetailAdapter;
import com.qingmaiding.orderform.utils.TimeUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.tag1)
    TextView tag1;//订单编号
    @BindView(R.id.tag2)
    TextView tag2;//币种编号currency
    @BindView(R.id.tag3)
    TextView tag3;//是否为货到付款cod
    @BindView(R.id.tag4)
    TextView tag4;//运单号  tracking_no
    @BindView(R.id.tag6)
    TextView tag6;//发货准备天数  days_to_ship
    @BindView(R.id.tag7)
    TextView tag7;//收货人姓名 name
    @BindView(R.id.tag8)
    TextView tag8;//收货人电话 phone
    @BindView(R.id.tag9)
    TextView tag9;//收货人地址 full_address
    @BindView(R.id.tag10)
    TextView tag10;//运费估算 estimated_shipping_fee
    @BindView(R.id.tag11)
    TextView tag11;//实际运费 actual_shipping_cost
    @BindView(R.id.tag12)
    TextView tag12;//实付款 total_amount
    @BindView(R.id.tag13)
    TextView tag13;//预收入 escrow_amount
    @BindView(R.id.tag14)
    TextView tag14;//物流服务商 shipping_carrier
    @BindView(R.id.tag15)
    TextView tag15;//付款方式 payment_method
    @BindView(R.id.tag16)
    TextView tag16;//是否需在海关报关 goods_to_declare
    @BindView(R.id.tag17)
    TextView tag17;//给买方的消息 message_to_seller
    @BindView(R.id.tag18)
    TextView tag18;//卖方的备注 note
    @BindView(R.id.tag19)
    TextView tag19;//备注时间 note_update_time
    @BindView(R.id.tag20)
    TextView tag20;//订单创建时间 create_time
    @BindView(R.id.tag21)
    TextView tag21;//支付时间 pay_time
    @BindView(R.id.tag22)
    TextView tag22;//发货人姓名 dropshipper
    @BindView(R.id.tag23)
    TextView tag23;//卡号后四位 credit_card_number
    @BindView(R.id.tag24)
    TextView tag24;//买家昵称 buyer_username
    @BindView(R.id.tag25)
    TextView tag25;//发货人电话 dropshipper_phone
    @BindView(R.id.tag26)
    TextView tag26;//寄送最后日期 ship_by_date
    @BindView(R.id.tag27)
    TextView tag27;//买方取消备注 buyer_cancel_reason
    @BindView(R.id.tag28)
    TextView tag28;//取消人员 cancel_by
    @BindView(R.id.tag29)
    TextView tag29;//最后一里号码 fm_tn
    @BindView(R.id.tag30)
    TextView tag30;//订单取消原因 cancel_reason
    @BindView(R.id.tag31)
    TextView tag31;//跨境税 escrow_tax
    @BindView(R.id.tag32)
    TextView tag32;//cpf编号 buyer_cpf_id
    @BindView(R.id.tag33)
    TextView tag33;//br最后一里号码 lm_tn
    @BindView(R.id.tag34)
    TextView tag34;//物流订单 express_no
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;//br最后一里号码 lm_tn
    //
//    goods_to_declare
    private JSONObject orderJson;


    private ArrayList<JSONObject> list;
    private OrderDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orderdetail);
        ButterKnife.bind(this);
        setToolbar("详情");
        //权限检查和申请
        initrecyclerview();
        getOrderInfo();
        getShopData();

    }

    private void initrecyclerview() {
        list = new ArrayList<>();
        adapter = new OrderDetailAdapter(list,this);
        recyclerview.setLayoutManager(new GridLayoutManager(this,1));
        recyclerview.setAdapter(adapter);
    }

    private void getOrderInfo() {

        try {
            orderJson = new JSONObject(getIntent().getStringExtra("orderInfo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getShopData() {
        dialog("");
        try {
            OkHttpUtils.post()
                    .url(getUrl(this,"/api/shop/orderDetaile"))
                    .addParams("ordersn",orderJson.getString("ordersn"))
                    .addParams("shopid",orderJson.getString("shopid"))
                    .addHeader("token",getToken())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {

                                tag1.setText(result.getJSONObject("data").getString("ordersn"));
                                tag2.setText(result.getJSONObject("data").getString("currency"));
                                if(result.getJSONObject("data").getString("cod").equals("true")){
                                    tag3.setText("是");
                                }else{
                                    tag3.setText("否");
                                }
                                tag4.setText(result.getJSONObject("data").getString("tracking_no"));
                                tag6.setText(result.getJSONObject("data").getString("days_to_ship"));
                                tag7.setText(result.getJSONObject("data").getJSONObject("recipient_address").getString("name"));
                                tag8.setText(result.getJSONObject("data").getJSONObject("recipient_address").getString("phone"));
                                tag9.setText(result.getJSONObject("data").getJSONObject("recipient_address").getString("full_address"));
                                tag10.setText(result.getJSONObject("data").getString("estimated_shipping_fee"));
                                tag11.setText(result.getJSONObject("data").getString("actual_shipping_cost"));
                                tag12.setText(result.getJSONObject("data").getString("total_amount"));
                                tag13.setText(result.getJSONObject("data").getString("escrow_amount"));
                                tag14.setText(result.getJSONObject("data").getString("shipping_carrier"));
                                tag15.setText(result.getJSONObject("data").getString("payment_method"));
                                if(result.getJSONObject("data").getString("goods_to_declare").equals("true")){
                                    tag16.setText("是");
                                }else{
                                    tag16.setText("否");
                                }
//                                tag16.setText(result.getJSONObject("data").getString("goods_to_declare"));
                                tag17.setText(result.getJSONObject("data").getString("message_to_seller"));
//                                tag18.setText(result.getJSONObject("data").getString("note"));
                                tag19.setText(TimeUtils.getCurrentTime(result.getJSONObject("data").getString("note_update_time")));
                                tag20.setText(TimeUtils.getCurrentTime(result.getJSONObject("data").getString("create_time")));
                                if(!result.getJSONObject("data").getString("pay_time").equals("null")){
                                    tag21.setText(TimeUtils.getCurrentTime(result.getJSONObject("data").getString("pay_time")));
                                }
                                JSONArray ja = result.getJSONObject("data").getJSONArray("items");
                                for (int i = 0; i < ja.length(); i++) {
                                    list.add(ja.getJSONObject(i));
                                }
                                adapter.notifyDataSetChanged();
//                                tag22.setText(result.getJSONObject("data").getString("dropshipper"));
                                tag23.setText(result.getJSONObject("data").getString("credit_card_number"));
                                tag24.setText(result.getJSONObject("data").getString("buyer_username"));
                                tag25.setText(result.getJSONObject("data").getString("dropshipper_phone"));
                                tag26.setText(TimeUtils.getCurrentTime(result.getJSONObject("data").getString("ship_by_date")));
                                tag27.setText(result.getJSONObject("data").getString("buyer_cancel_reason"));
                                tag28.setText(result.getJSONObject("data").getString("cancel_by"));
                                tag29.setText(result.getJSONObject("data").getString("fm_tn"));
                                tag30.setText(result.getJSONObject("data").getString("cancel_reason"));
                                tag31.setText(result.getJSONObject("data").getString("escrow_tax"));
//                                tag32.setText(result.getJSONObject("data").getString("buyer_cpf_id"));
                                tag33.setText(result.getJSONObject("data").getString("lm_tn"));
                                tag34.setText(result.getJSONObject("data").getString("express_no").equals("null")?"无":result.getJSONObject("data").getString("express_no").replace("|","\n"));
//                                tag34.setText("11");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
