package com.qingmaiding.orderform.middleman;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.middleman.adapter.MidShopManAdapter;
import com.qingmaiding.orderform.middleman.adapter.MidStoreAdapter;
import com.qingmaiding.orderform.platform.PlatShopStoreActivity;
import com.qingmaiding.orderform.platform.adapter.ShopManAdapter;
import com.qingmaiding.orderform.shop.PostShopStoreListActivity;
import com.qingmaiding.orderform.shop.adapter.ShopStoreAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyMidStoreActivity extends BaseActivity {

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.tag_commit)
    Button tag_commit;
    private ArrayList<JSONObject> list;
    private MidShopManAdapter adapter;
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
        tag_commit.setVisibility(View.GONE);
//        setToolbar("我的邀请");
        //权限检查和申请

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
//        adapter = new MidStoreAdapter(list,this) {
//            @Override
//            public void itemClick(int position) {
//                Intent intent = new Intent(MyMidStoreActivity.this, PostShopStoreListActivity.class);
//                intent.putExtra("shopstore",list.get(position).toString());
//                startActivity(intent);
//            }
//        };
        adapter = new MidShopManAdapter(list, this) {
            @Override
            public void itemClick(int position) {
                Intent intent = new Intent(MyMidStoreActivity.this, PlatShopStoreActivity.class);
                intent.putExtra("shopstoreList",list.get(position).toString());
                startActivity(intent);
            }

            @Override
            public void jifenClick(int position) {
                tipsDialog(MyMidStoreActivity.this,position).show();
            }

        };
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapter);
    }

//    private void getOrderData(int position) {
//        dialog("");
//        try {
//            OkHttpUtils.post()
//                    .url(getUrl(this,"/api/shop/shopeeapi"))
//                    .addParams("shopid",list.get(position).getString("shop_id"))
//                    .addHeader("token",getToken())
//                    .build()
//                    .execute(new MyCallBack() {
//                        @Override
//                        public void onSuccess(JSONObject result) {
//                            try {
//                                toast(result.getString("msg"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void getShopData() {
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/user/myInvitation"))
                .addHeader("token",getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            JSONArray ja = result.getJSONObject("data").getJSONArray("data");
                            for (int i = 0; i < ja.length(); i++) {
                                list.add(ja.getJSONObject(i));
//                                JSONArray ja1 = ja.getJSONObject(i).getJSONArray("shop");
//                                for (int j = 0; j < ja1.length(); j++) {
//                                    list.add(ja1.getJSONObject(j));
//                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private MaterialDialog tipsDialog(Context context, int position) {
        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title("修改发单积分")
                .input("请输入发单积分", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//                        Log.d("input", input.toString());
                        postStatusOrder(dialog,input.toString(),position);
                    }
                })
                .cancelable(true)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialog = null;

                    }
                })
                .build();
        return tipsDialog;

    }

    private void postStatusOrder(MaterialDialog dialog,String remake,int position) {
        dialog("");
        try {
            OkHttpUtils.post()
                    .url(getUrl(MyMidStoreActivity.this,"/api/user/shopScoreset"))
                    .addParams("orderscore",remake)
                    .addParams("shop_user_id",list.get(position).getString("id"))
                    .addHeader("token", getToken())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            dialog.dismiss();
                            list.clear();
                            getShopData();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
