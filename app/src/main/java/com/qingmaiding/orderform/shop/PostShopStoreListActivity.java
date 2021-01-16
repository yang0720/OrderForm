package com.qingmaiding.orderform.shop;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PostShopStoreListActivity extends BaseActivity {

    @BindView(R.id.tag1)
    EditText tag1;
    @BindView(R.id.tag2)
    EditText tag2;
    @BindView(R.id.tag3)
    TextView tag3;
    @OnClick(R.id.tag3)
    public void tag3(){
        showPickerView();
    }
    @OnClick(R.id.btn_commit)
    public void btn_commit(){
        if(isChanged){
            updataData();
        }else{
            commitData();
        }
    }

    private void setDefoutAddress() {

    }

    private String str1,str2,str3;
    private JSONObject addressJson;
    private void commitData() {

    }
    private void updataData() {
        dialog("");
        try {
            OkHttpUtils.post()
                    .url(getUrl(this,"/api/shop/shopDetail"))
                    .addParams("shop_id",addressJson.getString("shop_id"))
                    .addParams("shop_name",tag1.getText().toString().trim())
                    .addParams("contact",tag2.getText().toString().trim())
                    .addParams("address",tag3.getText().toString().trim())
                    .addHeader("token", getToken())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            toast("修改成功");
                            finish();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    boolean isChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_post_shopstore);
        ButterKnife.bind(this);
        initJsonData();
        isChanged = !isEmpty(getIntent().getStringExtra("shopstore"));
        if(isChanged){
            setToolbar("修改");
            try {
                addressJson = new JSONObject(getIntent().getStringExtra("shopstore"));
                if(addressJson.getString("shop_name").equals("null")){

                }else{
                    tag1.setText(addressJson.getString("shop_name"));
                }
                if(addressJson.getString("contact").equals("null")){

                }else{
                    tag2.setText(addressJson.getString("contact"));
                }
                if(addressJson.getString("address").equals("null")){

                }else{
                    tag3.setText(addressJson.getString("address"));
                }
//                tag3.setText(addressJson.getString("address"));
//                tag4.setText(addressJson.getString("addr"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            setToolbar("新增收货地址");
        }

    }

    private void showPickerView() {// 弹出选择器（省市区三级联动）
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                tag3.setText(options1Items.get(options1).getPickerViewText() + "  "
                        + options2Items.get(options1).get(options2) + "  "
                        + options3Items.get(options1).get(options2).get(options3));
                str1 = options1Items.get(options1).getPickerViewText();
                str2 = options2Items.get(options1).get(options2);
                str3 = options3Items.get(options1).get(options2).get(options3);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

}
