package com.xzmc.zzzt.privateprotect.http;

import android.content.Context;

import com.xzmc.zzzt.privateprotect.Utils.Utils;

public abstract class SimpleNetTask extends NetAsyncTask {
	protected SimpleNetTask(Context cxt) {
		super(cxt);
	}

	protected SimpleNetTask(Context cxt, boolean openDialog) {
		super(cxt, openDialog);
	}
	@Override
	protected void onPost(Exception e) {
		if (e != null) {
			e.printStackTrace();
			Utils.toast(e.getMessage());
		} else {
			onSucceed();
		}
	}
	protected abstract void doInBack() throws Exception;

	protected abstract void onSucceed();
}

