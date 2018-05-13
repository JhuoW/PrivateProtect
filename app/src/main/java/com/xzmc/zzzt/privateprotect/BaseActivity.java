package com.xzmc.zzzt.privateprotect;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends FragmentActivity {
	protected HeaderLayout headerLayout;
	protected Context ctx;
	protected Map<String,String> param=new HashMap<String,String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		App.pushActivity((Activity)ctx);
	}
	protected class BackListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			((Activity) ctx).finish();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		App.popActivity((Activity)ctx);
	}
}
