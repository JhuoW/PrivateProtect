package com.xzmc.zzzt.privateprotect.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.view.MyProgressDialog;


/**
 * 自定义网络异步AsyncTask
 */
public abstract class NetAsyncTask extends AsyncTask<Void, Void, Void> {
	protected Context ctx;
	boolean openDialog = true;
	Exception exception;
	ProgressDialog dialog;

	protected NetAsyncTask(Context ctx) {
		this.ctx = ctx;
	}

	protected NetAsyncTask(Context ctx, boolean openDialog) {
		this.ctx = ctx;
		this.openDialog = openDialog;
	}

	public NetAsyncTask setOpenDialog(boolean openDialog) {
		this.openDialog = openDialog;
		return this;
	}

	public ProgressDialog getDialog() {
		return dialog;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (openDialog) {
			dialog = new ProgressDialog(ctx);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("加载中...");
			dialog.show();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			doInBack();
		} catch (Exception e) {
			e.printStackTrace();
			exception = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void aVoid) {
		super.onPostExecute(aVoid);
		if (openDialog) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
		}
		onPost(exception);
	}

	protected abstract void doInBack() throws Exception;

	protected abstract void onPost(Exception e);
}
