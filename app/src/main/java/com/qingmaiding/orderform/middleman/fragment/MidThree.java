package com.qingmaiding.orderform.middleman.fragment;

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
import com.qingmaiding.orderform.middleman.adapter.MidOrderAdapter;
import com.qingmaiding.orderform.shop.EventMessage;
import com.qingmaiding.orderform.shop.OrderDetailActivity;
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


public class MidThree extends BaseFragment {

    @BindView(R.id.shop_uwei_active_recycleview)
    RecyclerView activeRecycleview;
    @BindView(R.id.ptrFrame)
    PtrClassicFrameLayout ptrFrame;
    private MidOrderAdapter adapter;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_active_all;
    }

    @Override
    protected void setUpView() {
        ptrInit();
    }

    private void ptrInit() {
        list = new ArrayList<>();
        adapter = new MidOrderAdapter(list, getActivity()) {
            @Override
            public void itemClick(int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderInfo",list.get(position).toString());
                startActivity(intent);
            }

            @Override
            public void qianshouClick(int position) {
                tipsDialog(getActivity(),position,"1").show();
            }

            @Override
            public void bohuiClick(int position) {
                tipsDialog(getActivity(),position,"2").show();
            }

            @Override
            public void fachuClick(int position) {
                fachuDialog(getActivity(),position).show();
            }
        };
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
    private String keyword = "";
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventMessage message) {
//        Log.e("TAG", "onReceiveMsg: " + message.getKeyword());
        list.clear();
        keyword = message.getKeyword();
        getActiveData();
    }
    @Override
    protected void setUpData() {

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
                .addParams("status","3")
                .addParams("page_size",page_num+"")
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ptrFrame.refreshComplete();
                        list.clear();
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
    private MaterialDialog tipsDialog(Context context,int position,String type) {

        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title(type.equals("1")?"签收备注":"驳回备注")
                .input("请输入备注", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.d("input", input.toString());
                        postStatusOrder(dialog,input.toString(),position,type);
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

    private MaterialDialog fachuDialog(Context context,int position) {

        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title("发出备注")
                .input("请输入备注", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.d("input", input.toString());
                        fachuStatusOrder(dialog,input.toString(),position);
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

    private void postStatusOrder(MaterialDialog dialog,String remake,int position,String type) {
        dialog();
        try {
            OkHttpUtils.post()
                    .url(getUrl("/api/shop/qianShou"))
                    .addParams("note",remake)
                    .addParams("ordersn",list.get(position).getString("ordersn"))
                    .addParams("type",type)
                    .addHeader("token", getToken())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            dialog.dismiss();
                            getActiveData();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void fachuStatusOrder(MaterialDialog dialog,String remake,int position) {
        dialog();
        try {
            OkHttpUtils.post()
                    .url(getUrl("/api/shop/faChu"))
                    .addParams("note",remake)
                    .addParams("ordersn",list.get(position).getString("ordersn"))
                    .addHeader("token", getToken())
                    .build()
                    .execute(new MyCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            dialog.dismiss();
                            getActiveData();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
