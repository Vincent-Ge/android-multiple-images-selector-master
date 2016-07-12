package com.zfdang.demo;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zfdang.demo.utils.PixelFormat;


/** 微信客户端回调activity示例 */  
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {  
    // IWXAPI 是第三方app和微信通信的openapi接口  
	public static final String TAG = "WXEntryActivity";
	private Toast mToast;
    private IWXAPI api;  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
    	mToast = new Toast(WXEntryActivity.this);
        api = WXAPIFactory.createWXAPI(this, "wxb0bd227589a41b8c", false);  
        api.handleIntent(getIntent(), this);  
        super.onCreate(savedInstanceState);  
    }  
    @Override  
    public void onReq(BaseReq arg0) { }  
  
    @Override  
    public void onResp(BaseResp resp) {  
//        LogManager.show(TAG, "resp.errCode:" + resp.errCode + ",resp.errStr:"  
//                + resp.errStr, 1);  
        switch (resp.errCode) {  
        case BaseResp.ErrCode.ERR_OK:  

			LayoutInflater inflater = LayoutInflater
					.from(WXEntryActivity.this);
			View view = inflater.inflate(R.layout.activity_toast, null);
			TextView mTextView2 = (TextView) view
					.findViewById(R.id.mTextView2);
			mTextView2.setText("分享成功 ");
			mToast.setGravity(Gravity.BOTTOM, 0,
					PixelFormat.formatDipToPx(WXEntryActivity.this, 70));
			mToast.setDuration(Toast.LENGTH_LONG);
			mToast.setView(view);
			mToast.show();
		
            //分享成功  
            break;  
        case BaseResp.ErrCode.ERR_USER_CANCEL:  
            //分享取消  
            break;  
        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
            //分享拒绝  
            break;  
        }  
    }  
}  