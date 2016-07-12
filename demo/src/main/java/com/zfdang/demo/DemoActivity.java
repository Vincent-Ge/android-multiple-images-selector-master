package com.zfdang.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zfdang.demo.adapter.SharePartAdapter;
import com.zfdang.demo.bean.SharePart;
import com.zfdang.demo.utils.PixelFormat;
import com.zfdang.demo.view.MyListView;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 732;
    private Context mContext;
    private IWXAPI wxApi;
    private  Button mButtonSelect;
    private  Button mButtonShare;
    private ArrayList<String> mResults = new ArrayList<>();
    private EditText mEditText;
    private MyListView mListView;
    private List<String> imagePathList;
    private ScrollView mScrollView;
    private Toast mToast;
    private List<SharePart> sharePartList = new ArrayList<SharePart>();
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mContext = DemoActivity.this;
        mButtonSelect = (Button) findViewById(R.id.mButtonSelect);
        mButtonShare = (Button) findViewById(R.id.mButtonShare);
        mEditText = (EditText) findViewById(R.id.mEditText);
        mListView = (MyListView) findViewById(R.id.mMyListView);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        mToast = new Toast(DemoActivity.this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mButtonSelect!=null){
            mButtonSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0); //强制隐藏键盘
                    // start multiple photos selector
                    Intent intent = new Intent(DemoActivity.this, ImagesSelectorActivity.class);
                    // max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
                    // min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                    // show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                    // pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                    // start the selector
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
        }
        if (mButtonShare!=null){
            //实例化
            wxApi = WXAPIFactory.createWXAPI(this, "wxb0bd227589a41b8c");
            wxApi.registerApp("wxb0bd227589a41b8c");
            mButtonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0); //强制隐藏键盘
                    wechatShare(0);//分享给朋友  若分享朋友圈值为1
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String> aList = null;
        // get selected images from selector
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                imagePathList = new ArrayList<String>();
                imagePathList.clear();
                aList =  data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                imagePathList.addAll(aList) ;
            }
        }
        if(aList!=null){
            LayoutInflater inflater = LayoutInflater
                    .from(DemoActivity.this);
            View view = inflater.inflate(R.layout.activity_toast, null);
            TextView mTextView2 = (TextView) view
                    .findViewById(R.id.mTextView2);
            mTextView2.setText("编辑完成");
            mToast.setGravity(Gravity.BOTTOM, 0,
                    PixelFormat.formatDipToPx(DemoActivity.this, 70));
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(view);
            mToast.show();
        }
        if (mEditText.equals("输入分享内容. . .")) {
            mEditText.setText("");
        }
        String partContent = mEditText.getText().toString().trim();
        if (sharePartList==null&&sharePartList.size()==0&&partContent==null) {
        }else{
            SharePart aSharePart = new SharePart();
            aSharePart.partContent = partContent;
            aSharePart.partList =  (ArrayList<String>) imagePathList;
            sharePartList.add(aSharePart);
            mListView.setAdapter(new SharePartAdapter(mContext, sharePartList));
            mEditText.setText("");
            mListView.postInvalidate();
            mScrollView.fullScroll(ScrollView.FOCUS_UP);   //滚动到顶部
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 微信分享
     * @param flag(0:分享到微信好友，1：分享到微信朋友圈)
     */
    private void wechatShare(int flag){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "骑行、带你装逼带你飞";
        msg.description = mEditText.getText().toString().trim();
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession: SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

}

