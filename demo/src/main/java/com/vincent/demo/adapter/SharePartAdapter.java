package com.vincent.demo.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincent.demo.R;
import com.vincent.demo.bean.SharePart;
import com.vincent.demo.view.ImageSection;


public class SharePartAdapter extends BaseAdapter{
	 private  List<SharePart> sharePartList;
	 private  Context mContext;
	private TextView lTextView;
	public SharePartAdapter (Context mContext ,List<SharePart> sharePartList){
		 Collections.reverse(sharePartList);
		this.sharePartList =sharePartList;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		return sharePartList.size();
	}

	@Override
	public Object getItem(int position) {
		return sharePartList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.item_partlist, null);
		LinearLayout lLinearLayout = (LinearLayout) view.findViewById(R.id.lLinearLayout);
		lTextView = new TextView(mContext);
		lTextView.setTextSize(18);
		lTextView.setText(sharePartList.get(position).partContent);
		lLinearLayout.addView(lTextView);
		ImageSection lImageSection = new ImageSection(mContext);
		lImageSection.imageCreat(sharePartList.get(position).partList);
		lLinearLayout.addView(lImageSection);
		return view;
	}
		
}
