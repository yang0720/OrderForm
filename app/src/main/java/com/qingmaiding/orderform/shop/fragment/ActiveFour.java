package com.qingmaiding.orderform.shop.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.EventMessage;
import com.qingmaiding.orderform.shop.OrderDetailActivity;
import com.qingmaiding.orderform.shop.adapter.OrderAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;


public class ActiveFour extends BaseFragment {

    @BindView(R.id.shop_uwei_active_recycleview)
    RecyclerView activeRecycleview;
    @BindView(R.id.ptrFrame)
    PtrClassicFrameLayout ptrFrame;
    private OrderAdapter adapter;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_active_all;
    }

    @Override
    protected void setUpView() {
        ptrInit();

        getPayData();
    }




    private String payScore = "0";
    private void getPayData() {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/shop/orderScore"))
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            payScore = result.getString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void ptrInit() {
        list = new ArrayList<>();
        adapter = new OrderAdapter(list, getActivity()) {
            @Override
            public void itemClick(int position) {
//                Intent intent = new Intent(getActivity(), ActiveSecActivity.class);
//                intent.putExtra("activeData",list.get(position).toString());
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderInfo",list.get(position).toString());
                startActivity(intent);
            }

            @Override
            public void fadanClick(int position) {
                tipsDialog(getActivity(),position).show();
            }

            @Override
            public void fukuanClick(int position) {
                fukuanDialog(getActivity(),position).show();
            }

            @Override
            public void addExpNoClick(int position, int itemPosition) {

            }

            @Override
            public void changeExpNoClick(int position, int itemPosition) {

            }
        };
        list.clear();
        getActiveData();
        activeRecycleview.setLayoutManager(new GridLayoutManager(getActivity(),1));
        activeRecycleview.setAdapter(adapter);

        ptrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        getActiveData();
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
                        getActiveData();
                    }
                },1000);
            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private String keyword = "";
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
//        Log.e("TAG", "onReceiveMsg: " + message.getKeyword());
        list.clear();
        keyword = message.getKeyword();
        getActiveData();
    }

    private int page = 1;
    private int page_num = 10;
    ArrayList<JSONObject> list;
    private void getActiveData() {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/shop/orderList"))
                .addParams("page",page+"")
                .addParams("keyword",keyword)
                .addParams("status","4")
                .addParams("page_size",page_num+"")
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ptrFrame.refreshComplete();
                        try {
                            JSONArray ja = result.getJSONObject("data").getJSONArray("data");
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jo = ja.getJSONObject(i);
                                list.add(jo);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }
    private MaterialDialog tipsDialog(Context context,int position) {
        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title("订单备注")
                .input("请输入备注", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.d("input", input.toString());
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

    private MaterialDialog fukuanDialog(Context context,int position) {
        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title("支付积分")
                .content("发单需支付积分"+payScore)
                .cancelable(true)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        payStatusOrder(dialog,position);
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
        dialog();
        try {
            OkHttpUtils.post()
                    .url(getUrl("/api/shop/faDan"))
                    .addParams("note",remake)
                    .addParams("ordersn",list.get(position).getString("ordersn"))
                    .addHeader("token", getToken())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            dialog.dismiss();
                            list.clear();
                            getActiveData();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void payStatusOrder(MaterialDialog dialog,int position) {
        dialog();
        try {
            OkHttpUtils.post()
                    .url(getUrl("/api/shop/payOrder"))
                    .addParams("score",payScore)
                    .addParams("ordersn",list.get(position).getString("ordersn"))
                    .addHeader("token", getToken())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            dialog.dismiss();
                            list.clear();
                            getActiveData();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
