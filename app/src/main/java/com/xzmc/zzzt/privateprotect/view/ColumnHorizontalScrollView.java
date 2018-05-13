package com.xzmc.zzzt.privateprotect.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class ColumnHorizontalScrollView extends HorizontalScrollView {
	/** 传入整体布局  */
	private View ll_content;
	/** 传入更多栏目选择布局 */
	private View ll_more;
	/** 传入拖动栏布�? */
	private View rl_column;
	/** 左阴影图�? */
	private ImageView leftImage;
	/** 右阴影图�? */
	private ImageView rightImage;
	/** 屏幕宽度 */
	private int mScreenWitdh = 0;
	/** 父类的活动activity */
	private Activity activity;
	
	public ColumnHorizontalScrollView(Context context) {
		super(context);
	}

	public ColumnHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColumnHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
		shade_ShowOrHide();
		if(!activity.isFinishing() && ll_content !=null && leftImage!=null && rightImage!=null && ll_more!=null && rl_column !=null){
			if(ll_content.getWidth() <= mScreenWitdh){
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			}
		}else{
			return;
		}
		if(paramInt1 ==0){
			leftImage.setVisibility(View.GONE);
			rightImage.setVisibility(View.VISIBLE);
			return;
		}
		if(ll_content.getWidth() - paramInt1 + ll_more.getWidth() + rl_column.getLeft() == mScreenWitdh){
			leftImage.setVisibility(View.VISIBLE);
			rightImage.setVisibility(View.GONE);
			return;
		}
		leftImage.setVisibility(View.VISIBLE);
	   rightImage.setVisibility(View.VISIBLE);
	}
	/** 
	 * 传入父类布局中的资源文件
	 * */
	public void setParam(Activity activity, int mScreenWitdh,View paramView1,ImageView paramView2, ImageView paramView3 ,View paramView5){
		this.activity = activity;
		this.mScreenWitdh = mScreenWitdh;
		ll_content = paramView1;
		leftImage = paramView2;
		rightImage = paramView3;
		rl_column = paramView5;
	}
	/** 
	 * 判断左右阴影的显示隐藏效�?
	 * */
	public void shade_ShowOrHide() {
		if (!activity.isFinishing() && ll_content != null) {
			measure(0, 0);
			//如果整体宽度小于屏幕宽度的话，那左右阴影都隐�?
			if (mScreenWitdh >= getMeasuredWidth()) {
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			}
		} else {
			return;
		}
		//如果滑动在最左边时�?�，左边阴影隐藏，右边显�?
		if (getLeft() == 0) {
			leftImage.setVisibility(View.GONE);
			rightImage.setVisibility(View.VISIBLE);
			return;
		}
		//如果滑动在最右边时�?�，左边阴影显示，右边隐�?
		if (getRight() == getMeasuredWidth() - mScreenWitdh) {
			leftImage.setVisibility(View.VISIBLE);
			rightImage.setVisibility(View.GONE);
			return;
		}
		//否则，说明在中间位置，左、右阴影都显�?
		leftImage.setVisibility(View.VISIBLE);
		rightImage.setVisibility(View.VISIBLE);
	}
}
