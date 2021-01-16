package com.qingmaiding.orderform.platform;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.platform.adapter.AuditAdapter;
import com.qingmaiding.orderform.shop.adapter.ConsHisAdapter;
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


public class WidhDepAuditActivity extends BaseActivity {

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    private ArrayList<JSONObject> list;
    private AuditAdapter adapter;
    @BindView(R.id.ptrFrame)
    PtrClassicFrameLayout ptrFrame;
    @BindView(R.id.tag7)
    Spinner tag7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_withdepaudit);
        ButterKnife.bind(this);
        initData();
        initView();
//        setToolbar("我的邀请");
        //权限检查和申请

    }
    private int page = 1;
    private int page_num = 10;
    private String status = "1";
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
        tag7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = position + 1 + "";
                list.clear();
                getShopData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.tag1)
    public void tag1(){
        finish();
    }

    @Override
    protected void onResume() {
//        getShopData();
        super.onResume();
    }

    private void initData() {
        initRecycler();
    }

    private void initRecycler() {
        list = new ArrayList<>();
        adapter = new AuditAdapter(list,this) {
            @Override
            public void itemClick(int position) {

            }

            @Override
            public void passClick(int position) {
                passCommit(position,"2");
                list.clear();
                getShopData();
            }

            @Override
            public void failClick(int position) {
                passCommit(position,"3");
                list.clear();
                getShopData();
            }

        };
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapter);
    }

    private void passCommit(int position,String type) {
        dialog("");
        try {
            OkHttpUtils.post()
                    .url(getUrl(this,"/api/user/audit_withdrawal"))
                    .addParams("id",list.get(position).getString("id"))
                    .addParams("status",type)
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
                            list.clear();
                            getShopData();
                        }

                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getShopData() {
        dialog("");
        OkHttpUtils.post()
                .url(getUrl(this,"/api/user/withdrawal_log"))
                .addParams("status",status)
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
