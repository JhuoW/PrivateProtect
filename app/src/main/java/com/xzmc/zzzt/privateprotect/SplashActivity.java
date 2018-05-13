package com.xzmc.zzzt.privateprotect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.easemob.chatuidemo.chatuidemo.DemoHXSDKHelper;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.db.DBHelper;
import com.xzmc.zzzt.privateprotect.http.APIHelper;
import com.xzmc.zzzt.privateprotect.http.JsonParase;

/**
 * Created by zw on 17/5/8.
 */

public class SplashActivity extends BaseActivity{
    private static final int sleepTime = 2000;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lay_start_act);

        dbHelper = DBHelper.getCurrentUserInstance(ctx);
        dbHelper.openSqLiteDatabase();

        new Thread() {
            @Override
            public void run() {
                String jsonstr = new APIHelper().getNewsCatergory();
                JsonParase.getNewsCaters(dbHelper, jsonstr);
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }

        }.start();
    }
}
