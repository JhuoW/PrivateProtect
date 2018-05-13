package com.xzmc.zzzt.privateprotect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.Utils.MD5;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.base.C;
import com.xzmc.zzzt.privateprotect.http.GetObjectFromService;
import com.xzmc.zzzt.privateprotect.http.SimpleNetTask;
import com.xzmc.zzzt.privateprotect.http.WebService;
import com.xzmc.zzzt.privateprotect.sweetdialog.SweetAlertDialog;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;

import org.json.JSONObject;

/**
 * Created by zw on 17/5/9.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_inputphone, et_inputusername, et_inputpassword,
            et_input2password, et_inputaddress;
    private Button btn_register;
    private String mPhonenumber, username, password, repassword, address, sex;
    private CheckBox cb_male, cb_female;
    HeaderLayout headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        cb_male = (CheckBox) findViewById(R.id.cb_male);
        cb_female = (CheckBox) findViewById(R.id.cb_female);
        et_inputphone = (EditText) findViewById(R.id.et_inputphone);
        et_inputusername = (EditText) findViewById(R.id.et_inputusername);
        et_inputpassword = (EditText) findViewById(R.id.et_inputpassword);
        et_input2password = (EditText) findViewById(R.id.et_input2password);
        et_inputaddress = (EditText) findViewById(R.id.et_inputaddress);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        headerLayout = (HeaderLayout) this.findViewById(R.id.headerLayout);
        headerLayout.showTitle("用户注册");
        headerLayout.showLeftBackButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitRegist();
            }
        });
        cb_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_female.setChecked(false);
                }
            }
        });
        cb_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    cb_male.setChecked(false);
                }
            }
        });
    }

    /**
     * 注册
     */
    private void register() {
        mPhonenumber = et_inputphone.getEditableText().toString();
        username = et_inputusername.getEditableText().toString();
        password = et_inputpassword.getEditableText().toString();
        repassword = et_input2password.getEditableText().toString();
        address = et_inputaddress.getEditableText().toString();
        if (cb_male.isChecked()) {
            sex = "1";
        } else {
            sex = "0";
        }

        if (TextUtils.isEmpty(username)) {
            Utils.toast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Utils.toast(R.string.password_can_not_null);
            return;
        }
        if (!repassword.equals(password)) {
            Utils.toast(R.string.password_not_consistent);
            return;
        }
        if (TextUtils.isEmpty(mPhonenumber)) {
            Utils.toast(R.string.phone_cannot_null);
            return;
        }

        param.clear();
        param.put("nickname", username);
        param.put("phone", mPhonenumber);
        param.put("password", MD5.getMD5(password.getBytes()));
        param.put("sex", sex);
        param.put("address", address);

        new SimpleNetTask(ctx, true) {
            boolean flag;
            String jsonstr;

            @Override
            protected void onSucceed() {
                if (flag) {
                    Utils.toast("注册成功");
                    RegisterActivity.this.finish();
                } else {
                    try {
                        JSONObject json = new JSONObject(jsonstr);
                        String errormsg = json.getString("errorMsg");
                        new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("注册失败")
                                .setContentText(errormsg)
                                .setConfirmText("确定")
                                .setConfirmClickListener(
                                        new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(
                                                    SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                            }
                                        }).show();
                    } catch (Exception e) {
                    }

                }

            }

            @Override
            protected void doInBack() throws Exception {
                jsonstr = new WebService(C.REGISTER, param).getReturnInfo();
                flag = GetObjectFromService.getSimplyResult(jsonstr);// errorMsg
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                register();// 注册
                break;
        }
    }

    private void quitRegist() {
        new SweetAlertDialog(ctx).setTitleText("退出注册")
                .setContentText("确定退出本次注册？").setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        RegisterActivity.this.finish();
                    }
                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            quitRegist();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
