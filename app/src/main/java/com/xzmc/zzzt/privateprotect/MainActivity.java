package com.xzmc.zzzt.privateprotect;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.baidu.location.LocationClient;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.chatuidemo.Constant;
import com.easemob.chatuidemo.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.controller.HXSDKHelper;
import com.easemob.util.NetUtils;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.base.C;
import com.xzmc.zzzt.privateprotect.bean.User;
import com.xzmc.zzzt.privateprotect.db.FriendsTable;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;
import com.xzmc.zzzt.privateprotect.fragment.DataFragment;
import com.xzmc.zzzt.privateprotect.fragment.HomeFragment;
import com.xzmc.zzzt.privateprotect.fragment.MineFragment;
import com.xzmc.zzzt.privateprotect.fragment.NewsFragment;
import com.xzmc.zzzt.privateprotect.http.GetObjectFromService;
import com.xzmc.zzzt.privateprotect.http.SimpleNetTask;
import com.xzmc.zzzt.privateprotect.http.WebService;
import com.xzmc.zzzt.privateprotect.service.LoginFinishReceiver;
import com.xzmc.zzzt.privateprotect.sweetdialog.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        EMEventListener {
    private LinearLayout tab_home, tab_news, tab_me,tab_data;
    public LocationClient locClient;

    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    // 当前fragment的index
    private int currentTabIndex;
    private FrameLayout fragment_container;

    private HomeFragment homeFragment;
    private NewsFragment newsFragment;
    private MineFragment mineFragment;
    private DataFragment dataFragment;
    // 页面列表
    private ArrayList<Fragment> fragmentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
                false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            App.getInstance().logout(null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setSelected(0);
        LoginFinishReceiver.broadcast(this);
        if (getIntent().getBooleanExtra("conflict", false)
                && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        // 注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(
                new MyConnectionListener());
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();
    }

    private void setSelected(int i) {
        currentTabIndex = i;
        initButtom(i);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (i) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    ft.add(R.id.fragment_container, homeFragment);
                } else {
                    ft.show(homeFragment);
                }
                break;
            case 1:
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    ft.add(R.id.fragment_container,newsFragment);
                } else {
                    ft.show(newsFragment);
                }
                break;
            case 2:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    ft.add(R.id.fragment_container, mineFragment);
                } else {
                    ft.show(mineFragment);
                }
                break;
            case 3:
                if(dataFragment == null){
                    dataFragment = new DataFragment();
                    ft.add(R.id.fragment_container,dataFragment);
                }else {
                    ft.show(dataFragment);
                }
            default:
                break;
        }
        ft.commit();
    }

    private void initButtom(int arg0) {
        switch (arg0) {
            case 0:
                tab_home.setSelected(true);
                tab_news.setSelected(false);
                tab_me.setSelected(false);
                tab_data.setSelected(false);
                break;
            case 1:
                tab_home.setSelected(false);
                tab_news.setSelected(true);
                tab_me.setSelected(false);
                tab_data.setSelected(false);
                break;
            case 2:
                tab_home.setSelected(false);
                tab_news.setSelected(false);
                tab_me.setSelected(true);
                tab_data.setSelected(false);

                break;
            case 3:
                tab_home.setSelected(false);
                tab_news.setSelected(false);
                tab_me.setSelected(false);
                tab_data.setSelected(true);

                break;
        }
    }

    private void hideFragment(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (newsFragment != null) {
            ft.hide(newsFragment);
        }
        if (mineFragment != null) {
            ft.hide(mineFragment);
        }
        if(dataFragment != null){
            ft.hide(dataFragment);
        }
    }


    public static void goMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_home:
                setSelected(0);
                break;
            case R.id.tab_news:
                setSelected(1);
                break;
            case R.id.tab_me:
                setSelected(2);
                break;
            case R.id.tab_data:
                setSelected(3);
                break;
        }
    }

    private void initView(){
        tab_home = (LinearLayout) findViewById(R.id.tab_home);
        tab_news = (LinearLayout) findViewById(R.id.tab_news);
        tab_me = (LinearLayout) findViewById(R.id.tab_me);
        tab_data = (LinearLayout) findViewById(R.id.tab_data);
        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        tab_home.setOnClickListener(this);
        tab_news.setOnClickListener(this);
        tab_me.setOnClickListener(this);
        tab_data.setOnClickListener(this);
    }

    private void initData(){
        FriendsTable table = FriendsTable.getInstance();
        List<User> localfriend = table.selectFriends();
        Map<String, com.easemob.chatuidemo.chatuidemo.domain.User> usermap = new HashMap<String, com.easemob.chatuidemo.chatuidemo.domain.User>();

        for (int i = 0; i < localfriend.size(); i++) {
            User qxUser = localfriend.get(i);
            com.easemob.chatuidemo.chatuidemo.domain.User user = new com.easemob.chatuidemo.chatuidemo.domain.User();
            user.setAvatar(qxUser.getImage());
            user.setNick(qxUser.getName());
            user.setUsername(qxUser.getID());
            usermap.put(qxUser.getID(), user);
        }
        User localuser = new User();
        localuser = new PreferenceMap(ctx).getUser();
        com.easemob.chatuidemo.chatuidemo.domain.User curuser = new com.easemob.chatuidemo.chatuidemo.domain.User();
        curuser.setAvatar(localuser.getImage());
        curuser.setNick(localuser.getName());
        curuser.setUsername(localuser.getID());
        usermap.put(localuser.getID(), curuser);
        App.getInstance().setContactList(usermap);
        UserDao dao = new UserDao(ctx);
        List<com.easemob.chatuidemo.chatuidemo.domain.User> users = new ArrayList<com.easemob.chatuidemo.chatuidemo.domain.User>(usermap.values());
        dao.saveContactList(users);
    }


    /**
     * check app upgrade
     */
    public void checkUpgrade() {
        param.clear();
        String version = getVersion();
        param.put("apkVersion", version);
        param.put("apkType", "upgrade_Android");
        new SimpleNetTask(ctx, false) {
            boolean flag;
            String jsonstr;

            @Override
            protected void onSucceed() {
                if (!flag) {
                    return;
                } else {
                    try {
                        dealUpgrade(jsonstr);
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            protected void doInBack() throws Exception {
                jsonstr = new WebService(C.GETUPGRADE, param).getReturnInfo();
                flag = GetObjectFromService.getSimplyResult(jsonstr);
            }
        }.execute();
    }
    /**
     * deal with the upgrade
     *
     * @param jsonstr
     * @throws JSONException
     */
    private void dealUpgrade(String jsonstr) throws JSONException {
        JSONObject json = new JSONObject(jsonstr);

        String des = json.getString("description");
        final String url = json.getString("fileUrl");
        String version = json.getString("version");
        boolean isNessary = json.getBoolean("isNecessary");

        new SweetAlertDialog(ctx).setTitleText("有新版本" + version + "啦")
                .setContentText(des)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).setConfirmText("立即更新")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse(url)));
                    }
                }).show();
    }


    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Utils.toast(R.string.Double_quit_app);
                mExitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 消息监听可以注册多个，SDK支持事件链的传递，不过一旦消息链中的某个监听返回能够处理某一事件，消息将不会进一步传递。
     * 后加入的事件监听会先收到事件的通知 如果收到的事件，能够被处理并且不需要其他的监听再处理，可以返回true，否则返回false
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();
                // 提示新消息
                HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                refreshUI();
                break;
            }

            case EventOfflineMessage: {
                refreshUI();
                break;
            }

            default:
                break;
        }
    }

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    private void refreshUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                // updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面

                    // if (fragment_conversation != null) {
                    // ((ConvsationFragment) fragment_conversation).refresh();
                    // }
                }
            }
        });
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
                .getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    // /**
    // * 刷新未读消息数
    // */
    // public void updateUnreadLabel() {
    // int count = getUnreadMsgCountTotal();
    // if (count > 0) {
    // unreadLabel.setText(String.valueOf(count));
    // unreadLabel.setVisibility(View.VISIBLE);
    // } else {
    // unreadLabel.setVisibility(View.INVISIBLE);
    // }
    // }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false)
                && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
                && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    public String getVersion() {
        return Utils.getVersion();
    }

    /**
     * 连接监听listener
     */
    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // chatHistoryFragment.errorItem.setVisibility(View.GONE);
                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources().getString(
                    R.string.Less_than_chat_server_connection);
            final String st2 = getResources().getString(
                    R.string.the_current_network);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    } else {
                        // chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                        }
                        // chatHistoryFragment.errorText.setText(st1);
                        // else
                        // chatHistoryFragment.errorText.setText(st2);
                    }
                }
            });
        }
    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        App.getInstance().logout(null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(
                            MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                accountRemovedBuilder = null;
                                finish();
                                startActivity(new Intent(MainActivity.this,
                                        LoginActivity.class));
                            }
                        });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
            }
        }
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        App.getInstance().logout(null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(
                            MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                conflictBuilder = null;
                                finish();
                                startActivity(new Intent(MainActivity.this,
                                        LoginActivity.class));
                            }
                        });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
            }

        }

    }
}
