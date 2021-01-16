package com.qingmaiding.orderform.platform;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.platform.adapter.PlatStoreAdapter;
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


public class PlatShopStoreActivity extends BaseActivity {

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    private ArrayList<JSONObject> list;
    private PlatStoreAdapter adapter;

    boolean isChanged = false;
    private JSONObject addressJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_paltshopstore);
        ButterKnife.bind(this);
//        setToolbar("我的邀请");
        //权限检查和申请
        initRecycler();
        isChanged = !isEmpty(getIntent().getStringExtra("shopstoreList"));
        if(isChanged){
//            setToolbar("修改");
            try {
                addressJson = new JSONObject(getIntent().getStringExtra("shopstoreList"));
                JSONArray ja = addressJson.getJSONArray("shop");
                for (int i = 0; i < ja.length(); i++) {
                    list.add(ja.getJSONObject(i));
                }
                adapter.notifyDataSetChanged();
//                tag3.setText(addressJson.getString("address"));
//                tag4.setText(addressJson.getString("addr"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
//        initRecycler();

    }

    private void initRecycler() {
        list = new ArrayList<>();
        adapter = new PlatStoreAdapter(list,this) {
            @Override
            public void itemClick(int position) {

            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapter);
    }



}
