package com.xzmc.zzzt.privateprotect.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.xzmc.health.R;


public class MyProgressDialog extends Dialog {
	private Context context;
	CircularProgress view;
	

	public MyProgressDialog(Context context) {
		super(context, R.style.MyprogressDialog);
		this.context=context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        super.onCreate(savedInstanceState);
        View view=LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(  
        		LinearLayout.LayoutParams.MATCH_PARENT,  
        		LinearLayout.LayoutParams.MATCH_PARENT);  
        lp.gravity = Gravity.CENTER; 
        addContentView(view, lp); 
        setCanceledOnTouchOutside(false);
	}
	 @Override
	public void show() {
		super.show();
	}
//	public MyProgressDialog(Context context,int theme) {
//		super(context,theme);
//	}
//
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		CircularProgress view=new CircularProgress(context);
//		 setContentView(view);
//	}
//	public static class Builder {
//		private Context context;
//		public Builder(Context context) {
//			this.context = context;
//		}
//		public Builder setContentView(View v) {  
//            return this;  
//        }  
//		
//		public MyProgressDialog create(){
//			MyProgressDialog dialog=new MyProgressDialog(context,R.style.dialog);
//			return dialog;
//			
//		}
//	}
}
