package com.xzmc.zzzt.privateprotect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.Utils.PhotoUtils;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.bean.PostModel;
import com.xzmc.zzzt.privateprotect.bean.User;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;
import com.xzmc.zzzt.privateprotect.sweetdialog.SweetAlertDialog;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;

/**
 * Created by zw on 17/5/11.
 */

public class DoctorDetailActivity extends BaseActivity{
    User curDoctor;
    private TextView tv_ingood, tv_office, tv_name, tv_position, name_online;
    private ImageView iv_avatar;
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    private LinearLayout check_layout, voice_layout, chat_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctordetail_layout);
        curDoctor = getIntent().getExtras().getParcelable("doctor");
        if (curDoctor == null || !curDoctor.getRole().equals("doctor")) {
            finish();
        }
        initView();
        initData();
        initAction();
    }
    private void initView(){
        headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
        headerLayout.showTitle("名医专栏");
        headerLayout.showLeftBackButton("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_ingood = (TextView) findViewById(R.id.tv_ingood);
        tv_office = (TextView) findViewById(R.id.tv_office);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_position = (TextView) findViewById(R.id.tv_position);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        name_online = (TextView) findViewById(R.id.name_online);

        voice_layout = (LinearLayout) findViewById(R.id.voice_layout);
        chat_layout = (LinearLayout) findViewById(R.id.chat_layout);
    }
    private void initData() {
        tv_ingood.setText(curDoctor.getExpertise());
        tv_office.setText(curDoctor.getOffice());
        tv_name.setText(curDoctor.getName());
        tv_position.setText(curDoctor.getPosition());
        name_online.setText(curDoctor.getName() + "在线");
        imageLoader.displayImage(curDoctor.getImage(), iv_avatar,
                PhotoUtils.getImageOptions(R.drawable.icon_default_avatar));
    }
    private void initAction(){
        voice_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!EMChatManager.getInstance().isConnected())
                {
                    Utils.toast("尚未连接服务器，请稍后再试");}
                else{
//                    startActivity(new Intent(ctx,
//                            VoiceCallActivity.class).putExtra("username",
//                            curDoctor.getID()).putExtra("isComingCall", false));

                    new SweetAlertDialog(ctx).setTitleText("拨打电话")
                            .setContentText("是否拨打专家电话？").setConfirmText("确定").showCancelButton(true).setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();

                        }
                    }).show();
                }
            }
        });
        chat_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, ChatActivity.class).putExtra(
                        "userId", curDoctor.getID()));
            }
        });
    }
}
