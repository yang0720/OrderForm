package com.qingmaiding.orderform.shop;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.recyclerview.widget.RecyclerView;

import com.alpamayo.utils.utils.PrefMethed;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.ui.login.MyLoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WebViewActivity extends BaseActivity {

    @BindView(R.id.tag1)
    WebView tag1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
//        setToolbar("我的邀请");
        //权限检查和申请
        initData();
        getAuthUrl();
    }

    private void getAuthUrl() {
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

    private void initData() {
//        tag1.loadUrl("");

        tag1.getSettings().setDomStorageEnabled(true);
        tag1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        tag1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //注意这边必须返回false
                log(url);
                tag1.loadUrl("https://account.seller.shopee.com/signin/oauth/identifier?timestamp=1608522208&sign=96195f129b3d80c433451302e4933eb2&response_type=code&client_id=221574d99f4b44ccd5ddbaf725e0ce12&lang=en&title=sla_title_open_platform_app_login&require_passwd=true&region=SG&login_types=%5B1,4%5D&max_auth_age=3600&state=eyJub25jZSI6IjczNzc0NDE3Y2RhMjA5NzUiLCJhdXRoX3Nob3AiOnRydWUsImlkIjoyMDAwMTQ1LCJuZXh0IjoiaHR0cHM6Ly9vcGVuLnNob3BlZS5jb20vYXV0aG9yaXplP2F1dGhfc2hvcD10cnVlJmlkPTIwMDAxNDUmcmFuZG9tPTczNzc0NDE3Y2RhMjA5NzUmaXNSZWRpcmVjdD10cnVlIn0%3D&redirect_uri=https%3A%2F%2Fopen.shopee.com%2Fapi%2Fv1%2Foauth2%2Fcallback&scope=profile");
                return true;
            }
        });
    }

}
