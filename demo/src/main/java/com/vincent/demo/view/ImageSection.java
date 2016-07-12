package com.vincent.demo.view;

import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vincent.demo.DemoActivity;
import com.vincent.demo.utils.ImageFactory;

public class ImageSection extends LinearLayout {
    private Context mContext;
    private int width, height;
    private static volatile ImageSection mImageSection = null;
    private ImageView imageView;

    public ImageSection(Context context) {
        super(context);
        mContext = context;
        this.setOrientation(LinearLayout.VERTICAL);
        DisplayMetrics metric = new DisplayMetrics();
        ((DemoActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        height = metric.heightPixels; // 屏幕高度（像素）
    }

    public void imageCreat(List<String> partList) {
        if (partList != null) {
            for (String string : partList) {
                imageView = new ImageView(mContext);
                imageView.setImageBitmap(ImageFactory.ratio(string, width, height));
                this.addView(imageView);
            }
        }
    }
}
