package com.qingmaiding.orderform.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alpamayo.utils.utils.PrefMethed;
import com.qingmaiding.orderform.middleman.MidMainActivity;
import com.qingmaiding.orderform.platform.PlatMainActivity;
import com.qingmaiding.orderform.shop.ShopMainActivity;
import com.qingmaiding.orderform.ui.login.MyLoginActivity;
import com.qingmaiding.orderform.ui.login.PhoneBindActivity;
import com.qingmaiding.orderform.utils.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

import static com.qingmaiding.orderform.BaseActivity.getUrl;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Constants.wx_api.handleIntent(getIntent(), this);

	}

	//微信请求相应
	@Override
	public void onReq(BaseReq baseReq) {

	}

	private WxLoginInterface wxLoginInterface;

	//发送到微信请求的响应结果
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				Log.e("WXTest","onResp OK");
				if(resp instanceof SendAuth.Resp){
					SendAuth.Resp newResp = (SendAuth.Resp) resp;
					//获取微信传回的code
					String code = newResp.code;
					Log.e("WXTest","onResp code = "+code);
					wxLogin(code);
//					setListener();
//					wxLoginInterface.OnWxLoginListener(code);
//					wxLogin(code);
				}

				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				Log.e("WXTest","onResp ERR_USER_CANCEL ");
				//发送取消
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				Log.e("WXTest","onResp ERR_AUTH_DENIED");
				//发送被拒绝
				break;
			default:
				Log.e("WXTest","onResp default errCode " + resp.errCode);
				//发送返回
				break;
		}
		finish();
	}

	private void wxLogin(String code) {
		OkHttpUtils.post()
				.url(getUrl(this,"/api/user/third"))
				.addParams("code",code)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
//						alpaDialogToast("错误,请稍后重试","确定");
//						Log.e("wy","loginresponse"+e);
//						Log.e("wy","loginresponse"+call.toString());
						Toast.makeText(WXEntryActivity.this,"错误，稍后重试",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onResponse(String response, int id) {
						Log.e("wy","loginresponse"+response);
						try {
							JSONObject result = new JSONObject(response);
							if(result.getString("code").equals("0")){
								//未绑定手机号
								Intent intent = new Intent(new Intent(WXEntryActivity.this, PhoneBindActivity.class));
								intent.putExtra("wxData",response);
								startActivity(intent);
							}else{
								try {
									PrefMethed.settoken(WXEntryActivity.this,result.getJSONObject("data").getJSONObject("userinfo").getString("token"));

									PrefMethed.setuser_info(WXEntryActivity.this,result.getJSONObject("data").getJSONObject("userinfo").toString());

									if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("1")){
										//平台
										startActivity(new Intent(WXEntryActivity.this, PlatMainActivity.class));
									}else if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("2")){
										//商家
										startActivity(new Intent(WXEntryActivity.this, ShopMainActivity.class));
									}else if(result.getJSONObject("data").getJSONObject("userinfo").getString("group_id").equals("3")){
										//中间商
										startActivity(new Intent(WXEntryActivity.this, MidMainActivity.class));
									};
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						//未绑定手机号，先绑定手机号

					}
				});
	}

}