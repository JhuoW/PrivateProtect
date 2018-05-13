package com.xzmc.zzzt.privateprotect.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xzmc.health.R;


/**
 * 自定义actionbar
 */
public class HeaderLayout extends LinearLayout {

	LayoutInflater mInflater;
	RelativeLayout header;
	TextView titleView;
	LinearLayout leftContainer, rightContainer;
	Button backBtn;

	public HeaderLayout(Context context) {
		super(context);
		init();
	}

	public HeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mInflater = LayoutInflater.from(getContext());
		header = (RelativeLayout) mInflater.inflate(
				R.layout.base_common_header, null, false);
		// 中间标题
		titleView = (TextView) header.findViewById(R.id.titleView);
		// 左侧视图容器
		leftContainer = (LinearLayout) header.findViewById(R.id.leftContainer);
		// 右侧视图容器
		rightContainer = (LinearLayout) header
				.findViewById(R.id.rightContainer);
		// 返回键
		backBtn = (Button) header.findViewById(R.id.backBtn);
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("click header");
			}
		});
		addView(header);
	}

	public void showTitle(int titleId) {
		titleView.setText(titleId + "");
	}

	public void showTitle(String s) {
		titleView.setText(s);
	}

	public void showLeftBackButton(OnClickListener listener) {
		showLeftBackButton("", listener);
	}

	public void showLeftBackButton() {
		showLeftBackButton(null);
	}

	public void showLeftBackButton(String backText, OnClickListener listener) {
		backBtn.setVisibility(View.VISIBLE);
		backBtn.setText(backText);
		if (listener == null) {
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					((Activity) getContext()).finish();
				}
			};
		}
		backBtn.setOnClickListener(listener);
	}

	public void showRightImageButton(int rightResId, OnClickListener listener) {
		rightContainer.removeAllViews();
		View imageViewLayout = mInflater.inflate(
				R.layout.base_common_header_right_image_btn, null, false);
		ImageButton rightButton = (ImageButton) imageViewLayout
				.findViewById(R.id.imageBtn);
		rightButton.setImageResource(rightResId);
		rightButton.setOnClickListener(listener);
		rightContainer.addView(imageViewLayout);
	}
	public void showOtherRightImageButton(int rightResId,OnClickListener listener) {
		View imageViewLayout2 = mInflater.inflate(
				R.layout.base_common_header_right_image_btn_2,null, false);
		ImageButton rightButton2 = (ImageButton) imageViewLayout2
				.findViewById(R.id.imageBtn);
		rightButton2.setImageResource(rightResId);
		rightButton2.setOnClickListener(listener);
		rightContainer.addView(imageViewLayout2,0);
	}

	

	public void showLeftImageButton(int rightResId, OnClickListener listener) {
		leftContainer.removeAllViews();
		View imageViewLayout = mInflater.inflate(
				R.layout.base_common_header_right_image_btn, null, false);
		ImageButton leftButton = (ImageButton) imageViewLayout
				.findViewById(R.id.imageBtn);
		leftButton.setImageResource(rightResId);
		leftButton.setOnClickListener(listener);
		leftContainer.addView(imageViewLayout);
	}

	public void showRightTextButton(String rightResId, OnClickListener listener) {
		rightContainer.removeAllViews();
		View imageViewLayout = mInflater.inflate(
				R.layout.base_common_header_right_btn, null, false);
		TextView rightButton = (TextView) imageViewLayout
				.findViewById(R.id.textBtn);
		rightButton.setText(rightResId);
		rightButton.setOnClickListener(listener);
		rightContainer.addView(imageViewLayout);
	}
	public View getAddButton() {
		return rightContainer.getChildAt(0);

	}
}
