package com.xzmc.zzzt.privateprotect;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.Utils.MD5;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.base.C;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;
import com.xzmc.zzzt.privateprotect.http.GetObjectFromService;
import com.xzmc.zzzt.privateprotect.http.SimpleNetTask;
import com.xzmc.zzzt.privateprotect.http.WebService;

import static com.baidu.location.b.g.G;
import static com.baidu.location.b.g.i;


/**
 * Created by zw on 17/5/4.
 */

public class LoginActivity extends BaseActivity{

    private TextView forget_password, register;
    private EditText et_account, et_password;
    private Button btn_login;
    private boolean remenberAccount = false;
    private PreferenceMap accountPre;
    String account ;
    String password;
    private TextView tv_forget_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果用户名密码都有，直接进入主页
        accountPre = new PreferenceMap(ctx);
        setContentView(R.layout.layout_login);
        initView();
        initAction();
        initData();
    }

    private void initView() {
        forget_password = (TextView) findViewById(R.id.tv_forget_password);
        register = (TextView) findViewById(R.id.tv_register);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        tv_forget_password.setVisibility(View.GONE);
        et_account.setText(new PreferenceMap(ctx).getAccount());
        Utils.setEditTextLastPosition(et_account);
    }

    private void initAction() {
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_password.setText(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.goActivity(LoginActivity.this,RegisterActivity.class);
            }
        });
    }

    private void initData() {
        if (accountPre.isRemenberAccount()) {
            et_account.setText(accountPre.getAccount());
            et_password.setText(accountPre.getPassword());
            remenberAccount = true;
            Utils.setEditTextLastPosition(et_account);
        }
    }

    private void login(){
        account = et_account.getText().toString();
        password = et_password.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Utils.toast(R.string.username_cannot_null);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Utils.toast(R.string.password_can_not_null);
            return;
        }
        accountPre.setIsRemenberAccount(remenberAccount);
        accountPre.setAccount(account);
        accountPre.setPassword(password);
        param.clear();
        param.put("phone", account);
        param.put("password", MD5.getMD5(password.getBytes()));
        new SimpleNetTask(ctx, true) {
            boolean flag;

            @Override
            protected void onSucceed() {
                if (!flag) {
                    Utils.toast(R.string.login_error);
                } else {
                    MainActivity
                            .goMainActivity(LoginActivity.this);
                    finish();
                }
            }

            @Override
            protected void doInBack() throws Exception {
                String jsonstr = new WebService(C.LOGIN, param).getReturnInfo();
                flag = GetObjectFromService.getLoginResult(jsonstr, account);
                if (flag) {
                    String id = Utils.getID();
                    String passwordstr = MD5.getMD5(password.getBytes());
                    EMChatManager.getInstance().login(id, passwordstr,
                            new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    EMChatManager.getInstance()
                                            .updateCurrentUserNick(
                                                    accountPre.getUser()
                                                            .getName());
                                }
                                @Override
                                public void onProgress(int arg0, String arg1) {
                                }
                                @Override
                                public void onError(int arg0, String arg1) {
                                }
                            });
                }
            }
        }.execute();
    }
}
