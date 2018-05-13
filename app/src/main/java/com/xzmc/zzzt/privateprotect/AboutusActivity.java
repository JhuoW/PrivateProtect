package com.xzmc.zzzt.privateprotect;

import android.os.Bundle;
import android.view.View;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;

/**
 * Created by zw on 17/5/11.
 */

public class AboutusActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setContentView(R.layout.activity_aboutus_layout);
        initView();
        initData();
        initAction();
    }
    private void initAction() {
        headerLayout.showLeftBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutusActivity.this.finish();
            }
        });
    }
    private void initData() {
    }
    private void initView() {
        headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
    }


}
