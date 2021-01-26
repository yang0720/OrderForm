package com.qingmaiding.orderform.platform.fragment;


import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alpamayo.utils.utils.PrefMethed;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.platform.WidhDepAuditActivity;
import com.qingmaiding.orderform.shop.MyExpHisActivity;
import com.qingmaiding.orderform.shop.MyInfoActivity;
import com.qingmaiding.orderform.shop.MyShopStoreActivity;
import com.qingmaiding.orderform.ui.login.MyLoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class PlatFourFragment extends BaseFragment {
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_fourplat;
    }

    @Override
    protected void setUpView() {
        getFenData();
        getChongData();
        getJifenData();
        getScoreData();
    }

    private void getFenData() {
        dialog();
        OkHttpUtils.get()
                .url(getUrl("/api/user/distributionSet"))
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
//                            toastShort(result.getString("msg"));
                            tag1_text.setText("平："+result.getJSONObject("data").getString("platform_com")+"中："+result.getJSONObject("data").getString("zjs_com"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getChongData() {
        dialog();
        OkHttpUtils.get()
                .url(getUrl("/api/user/chongzhiSet"))
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
//                            toastShort(result.getString("msg"));
                            tag2_text.setText(result.getJSONObject("data").getString("exchange"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getJifenData() {
        dialog();
        OkHttpUtils.get()
                .url(getUrl("/api/user/witminSet"))
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
//                            toastShort(result.getString("msg"));
                            tag3_text.setText(result.getJSONObject("data").getString("wit_min"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @BindView(R.id.tag1_text)
    TextView tag1_text;
    @BindView(R.id.tag2_text)
    TextView tag2_text;
    @BindView(R.id.tag3_text)
    TextView tag3_text;
    @BindView(R.id.tag4_text)
    TextView tag4_text;
    @OnClick(R.id.logout)
    public void logout(){
        PrefMethed.settoken(getActivity(),"");
        PrefMethed.setuser_info(getActivity(),"");
        startActivity(new Intent(getActivity(), MyLoginActivity.class));
        getActivity().finish();
    }
    private void getScoreData() {
        dialog();
        OkHttpUtils.get()
                .url(getUrl("/api/user/payscoreSet"))
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
//                            toastShort(result.getString("msg"));
                            tag4_text.setText("低:"+result.getJSONObject("data").getString("min_orderscore")+"高:"+result.getJSONObject("data").getString("max_orderscore"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void setUpData() {

    }
    @OnClick(R.id.tag3)
    public void tag3(){
        //提现设置
        jifenDialog(getActivity()).show();
    }
    @OnClick(R.id.tag4)
    public void tag4(){
        //提现设置
        qujianDialog();
    }
    @OnClick(R.id.tag1)
    public void tag1(){
        //分销设置
        fenxiaoDialog();
    }

    @OnClick(R.id.tag5)
    public void tag5(){
        //添加中间商设置
        addMidDialog();
    }

    @OnClick(R.id.tag6)
    public void tag6(){
        //提现审核
//        addMidDialog();
        startActivity(new Intent(getActivity(), WidhDepAuditActivity.class));
    }
    @OnClick(R.id.tag7)
    public void tag7(){
        //提现审核
//        addMidDialog();
//        startActivity(new Intent(getActivity(), WidhDepAuditActivity.class));
        tongzhiDialog(getActivity()).show();
    }
    private String phoneStr,passwordStr;
    private void addMidDialog() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title("添加中间商")
                .customView(R.layout.addmid_dialog, true)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText et_oldpassword = (EditText)dialog.findViewById(R.id.pingtai);
                        EditText et_newpassword = (EditText)dialog.findViewById(R.id.zhongjianshang);
                        phoneStr = et_oldpassword.getText().toString();
                        passwordStr = et_newpassword.getText().toString();
                        if(tvIsNull(et_oldpassword)){
                            toastShort("中间商手机号不能为空");
                            return;
                        }
                        commitAddMid();
                    }
                }).show();
    }
    private void commitAddMid() {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/regMid"))
                .addParams("mobile",phoneStr)
                .addParams("password",passwordStr)
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            toastShort(result.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String pingStr,zhongStr;
    private void fenxiaoDialog() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title("分销设置")
                .customView(R.layout.fenxiao_dialog, true)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText et_oldpassword = (EditText)dialog.findViewById(R.id.pingtai);
                        EditText et_newpassword = (EditText)dialog.findViewById(R.id.zhongjianshang);
                        pingStr = et_oldpassword.getText().toString();
                        zhongStr = et_newpassword.getText().toString();
                        if(tvIsNull(et_oldpassword)){
                            toastShort("平台分销不能为空");
                            return;
                        }
                        if(tvIsNull(et_newpassword)){
                            toastShort("中间商分销不能为空");
                            return;
                        }
                        commitUserPassWord();
                    }
                }).show();
    }

    private void commitUserPassWord() {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/distributionSet"))
                .addParams("zjs_com",zhongStr)
                .addParams("platform_com",pingStr)
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            toastShort(result.getString("msg"));
                            tag1_text.setText("平："+pingStr+"中："+zhongStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String minStr,maxStr;
    private void qujianDialog() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                .title("发单区间设置")
                .customView(R.layout.qujian_dialog, true)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText et_oldpassword = (EditText)dialog.findViewById(R.id.pingtai);
                        EditText et_newpassword = (EditText)dialog.findViewById(R.id.zhongjianshang);
                        minStr = et_oldpassword.getText().toString();
                        maxStr = et_newpassword.getText().toString();
                        if(tvIsNull(et_oldpassword)){
                            toastShort("最低积分不能为空");
                            return;
                        }
                        if(tvIsNull(et_newpassword)){
                            toastShort("最高积分不能为空");
                            return;
                        }
                        commitCoreSet();
                    }
                }).show();
    }

    private void commitCoreSet() {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/payscoreSet"))
                .addParams("min_orderscore",minStr)
                .addParams("max_orderscore",maxStr)
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            toastShort(result.getString("msg"));
                            tag4_text.setText("低："+minStr+"高："+maxStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick(R.id.tag2)
    public void tag2(){
        //充值设置
        tipsDialog(getActivity()).show();
    }

    private MaterialDialog tipsDialog(Context context) {

        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title("充值设置")
                .input("一元可以兑换的积分比例", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.d("input", input.toString());
                        rechargeSetting(input.toString());
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

    private void rechargeSetting(String setStr) {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/chongzhiSet"))
                .addParams("exchange",setStr)
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            toastShort(result.getString("msg"));
                            tag2_text.setText(setStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private MaterialDialog tongzhiDialog(Context context) {

        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title("发布通知")
                .input("输入发布的通知", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.d("input", input.toString());
                        tongzhiSetting(input.toString());
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
    private void tongzhiSetting(String jifenStr) {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/news"))
                .addParams("content",jifenStr)
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            toastShort(result.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private MaterialDialog jifenDialog(Context context) {

        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title("最低积分提现")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("提现最低积分数", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.d("input", input.toString());
                        jifenSetting(input.toString());
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

    private void jifenSetting(String jifenStr) {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/witminSet"))
                .addParams("wit_min",jifenStr)
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            toastShort(result.getString("msg"));
                            tag3_text.setText(jifenStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private MaterialDialog fadanDialog(Context context) {

        MaterialDialog tipsDialog = new MaterialDialog.Builder(context)
                .title("发单积分设置")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("发单所需积分", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Log.d("input", input.toString());
                        fadanSetting(input.toString());
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

    private void fadanSetting(String jifenStr) {
        dialog();
        OkHttpUtils.post()
                .url(getUrl("/api/user/payscoreSet"))
                .addParams("orderscore",jifenStr)
                .addHeader("token", getToken())
                .build()
                .execute(new MyCallBack() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        try {
                            toastShort(result.getString("msg"));
                            tag4_text.setText(jifenStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
