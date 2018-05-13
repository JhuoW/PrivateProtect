package com.xzmc.zzzt.privateprotect;

import android.os.Bundle;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;

/**
 * Created by zw on 17/5/11.
 */

public class SettingActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setContentView(R.layout.activity_setting);
        //accountPre = new PreferenceMap(ctx, "accountInformation");
    }
}
