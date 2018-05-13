package com.xzmc.zzzt.privateprotect.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xzmc.zzzt.privateprotect.Utils.PhotoUtils;

public class ViewHolder {
	SparseArray<View> mViews;
	private int position;
	private View mConvertView;
	public static ImageLoader imageLoader = ImageLoader.getInstance();

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.position = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder  holder = (ViewHolder) convertView.getTag();
			holder.position=position;
			return holder;
		}
	}
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
	
	public ViewHolder setText(int viewId,String text){
		TextView tv=getView(viewId);
		tv.setText(text);
		return this;
	}
	public ViewHolder setImage(int viewId,String url,int defaulturl){
		ImageView iv=getView(viewId);
		imageLoader.displayImage(url, iv,
				PhotoUtils.getImageOptions(defaulturl));
		return this;
	}
	public View getConvertView() {
		return mConvertView;
	}

}
