package com.qingmaiding.orderform;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alpamayo.utils.utils.PrefMethed;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import okhttp3.Call;

public abstract class BaseFragment extends Fragment {
    private View mContentView;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(), container, false);
        ButterKnife.bind(this, mContentView);
        mContext = getContext();
        init();
        setUpView();
        setUpData();
        return mContentView;
    }



    protected String getToken(){
        return PrefMethed.gettoken(getActivity());
    }

    protected String getType(){
        return PrefMethed.getstatus(getActivity());
    }

    protected String getUrl(String url){
        String ip = PrefMethed.getIPAddress(getActivity());
        Log.e("22222222", "https://"+ip+url );
        return "https://"+ip+url;
    }

    protected boolean isLogin(){
        return !PrefMethed.gettoken(getActivity()).equals("");
    }

    /**
     * 让popupwindow以外区域阴影显示
     * @param popupWindow
     */
    public void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;//设置阴影透明度
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }
    /**
     * toast方法
     */
    private Toast toast;
    public void toastShort(Object object){
        String msg = object.toString();
        if(toast == null){
            toast = Toast.makeText(getMContext(),msg,Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID * * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();

    /**
     * 一些View的相关操作
     */
    protected abstract void setUpView();

    /**
     * 一些Data的相关操作
     */
    protected abstract void setUpData();

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据 * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    protected void init() {
    }



    public View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }

    private MaterialDialog materialDialog;
    public void dialog(){
        if(materialDialog == null){

            materialDialog = new MaterialDialog.Builder(getActivity())
                    .title("")
                    .content("")
                    .progress(true, 0)
                    .show();
        }else{
            if(!materialDialog.isShowing()){
                materialDialog = new MaterialDialog.Builder(getActivity())
                        .title("")
                        .content("")
                        .progress(true, 0)
                        .show();
            }
        }


    }

    /**
     * 对textview或者edittext进行非空判断
     */
    public boolean tvIsNull(TextView tv){
        return TextUtils.isEmpty(tv.getText());
    }
    public boolean tvIsNull(EditText et){
        return TextUtils.isEmpty(et.getText());
    }

    public void dialogClose(){
        if (materialDialog.isShowing()){
            materialDialog.dismiss();
        }
    }


    public abstract class MyCallBack extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            dialogClose();
            new MaterialDialog.Builder(getActivity())
                    .content("错误,请稍后重试")
                    .positiveText("确认")
                    .show();
        }

        @Override
        public void onResponse(String response, int id) {
            dialogClose();
            try{
                JSONObject result = new JSONObject(response);
                Log.e("response",response);
                if(result.getString("code").equals("1")||result.getString("code").equals("0")){
                    onSuccess(result);
                }else{
                    toastShort(result.getString("error"));
                }
            }catch (JSONException e){
                onError(null,new Exception("0x000099致命错误,请联系服务商"),0);
            }

        }
        public abstract void onSuccess(JSONObject result);
    }
}
