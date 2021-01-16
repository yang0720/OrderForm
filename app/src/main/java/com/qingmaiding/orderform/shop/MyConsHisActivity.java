package com.qingmaiding.orderform.shop;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.adapter.ConsHisAdapter;
import com.qingmaiding.orderform.shop.adapter.ShopStoreAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;


public class MyConsHisActivity extends BaseActivity {

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    private ArrayList<JSONObject> list;
    private ConsHisAdapter adapter;
    @BindView(R.id.ptrFrame)
    PtrClassicFrameLayout ptrFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_conshis);
        ButterKnife.bind(this);
        initView();
//        setToolbar("我的邀请");
        //权限检查和申请

    }
    private int page = 1;
    private int page_num = 10;
    private void initView() {
        ptrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getShopData();
                    }
                },1000);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        page = 1;
                        getShopData();
                    }
                },1000);
            }
        });
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
        adapter = new ConsHisAdapter(list,this) {
            @Override
            public void itemClick(int position) {

            }

        };
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapter);
    }


    private void getShopData() {
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/user/score_log"))
                .addParams("type","1")
                .addParams("page",page+"")
                .addParams("page_size",page_num+"")
                .addHeader("token",getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ptrFrame.refreshComplete();
                        try {
                            JSONArray ja = result.getJSONObject("data").getJSONArray("data");
                            for (int i = 0; i < ja.length(); i++) {
                                list.add(ja.getJSONObject(i));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        ptrFrame.refreshComplete();
                    }
                });
    }

}
