package com.qingmaiding.orderform.platform.fragment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.platform.adapter.ShopManAdapter;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;


public class PlatSecTwo extends BaseFragment {

    @BindView(R.id.shop_uwei_active_recycleview)
    RecyclerView activeRecycleview;
    @BindView(R.id.ptrFrame)
    PtrClassicFrameLayout ptrFrame;
    private ShopManAdapter adapter;
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
                .url(getUrl("/api/user/getmanagementList"))
                .addParams("group_id","2")
                .addParams("page",page+"")
                .addParams("page_size",page_num+"")
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {

                    }
                });
    }

    private void ptrInit() {
        list = new ArrayList<>();
        adapter = new ShopManAdapter(list, getActivity()) {
            @Override
            public void itemClick(int position) {

            }

            @Override
            public void jifenClick(int position) {
                tipsDialog(getActivity(),position).show();
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

    private int page = 1;
    private int page_num = 10;
    ArrayList<JSONObject> list;
    private void getActiveData() {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/getmanagementList"))
                .addParams("page",page+"")
                .addParams("group_id","3")
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
                .title("修改积分")
                .input("请输入积分", "", new MaterialDialog.InputCallback() {
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
                    .url(getUrl("/api/user/scoreSet"))
                    .addParams("score",remake)
                    .addParams("user_id",list.get(position).getString("id"))
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
