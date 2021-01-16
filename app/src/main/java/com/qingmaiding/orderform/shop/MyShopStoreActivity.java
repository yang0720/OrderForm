package com.qingmaiding.orderform.shop;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.adapter.ShopStoreAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyShopStoreActivity extends BaseActivity {

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    private ArrayList<JSONObject> list;
    private ShopStoreAdapter adapter;
    @OnClick(R.id.tag_commit)
    public void tag_commit(){
//        startActivity(new Intent(MyShopStoreActivity.this, WebViewActivity.class));
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/shop/authShop"))
                .addHeader("token",getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String str = result.getString("data");
                            str = str.replaceAll("\\\\","");
                            log(str);
                            Intent intent= new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(str);
                            intent.setData(content_url);
                            startActivity(intent);
//                            tag1.loadUrl(str);
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
        setContentView(R.layout.activity_myshopstore);
        ButterKnife.bind(this);
//        setToolbar("我的邀请");

        //权限检查和申请

    }
    @OnClick(R.id.tag1)
    public void tag1(){
        finish();
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
        initRecycler();
        getShopData();

    }

    private void initRecycler() {
        list = new ArrayList<>();
        adapter = new ShopStoreAdapter(list,this) {
            @Override
            public void itemClick(int position) {
                Intent intent = new Intent(MyShopStoreActivity.this,PostShopStoreListActivity.class);
                intent.putExtra("shopstore",list.get(position).toString());
                startActivity(intent);
            }

            @Override
            public void getDataClick(int position) {
                getOrderData(position);
            }

            @Override
            public void unBindClick(int position) {
                alpaDialogConfirm("是否解绑此店铺","确定", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        unbingData(position);
                    }
                },true,"取消");
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapter);
    }

    private void unbingData(int position) {
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/shop/cancel_auth_partner"))
                .addHeader("token",getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            String str = result.getString("data");
                            str = str.replaceAll("\\\\","");
                            log(str);
                            Intent intent= new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(str);
                            intent.setData(content_url);
                            startActivity(intent);
//                            tag1.loadUrl(str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getOrderData(int position) {
        dialog("");
        try {
            OkHttpUtils.post()
                    .url(getUrl(this,"/api/shop/shopeeapi"))
                    .addParams("shopid",list.get(position).getString("shop_id"))
                    .addHeader("token",getToken())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                toast(result.getString("msg"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                            JSONArray ja = result.getJSONObject("data").getJSONArray("shop");
                            for (int i = 0; i < ja.length(); i++) {
                                list.add(ja.getJSONObject(i));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
