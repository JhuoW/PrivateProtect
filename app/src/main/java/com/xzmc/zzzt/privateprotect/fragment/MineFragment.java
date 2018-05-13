package com.xzmc.zzzt.privateprotect.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
import com.easemob.EMCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.AboutusActivity;
import com.xzmc.zzzt.privateprotect.CropImageActivity;
import com.xzmc.zzzt.privateprotect.LoginActivity;
import com.xzmc.zzzt.privateprotect.SettingActivity;
import com.xzmc.zzzt.privateprotect.Utils.PhotoUtils;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.base.C;
import com.xzmc.zzzt.privateprotect.bean.User;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;
import com.xzmc.zzzt.privateprotect.http.SimpleNetTask;
import com.xzmc.zzzt.privateprotect.http.WebService;
import com.xzmc.zzzt.privateprotect.sweetdialog.SweetAlertDialog;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;
import com.xzmc.zzzt.privateprotect.view.RoundImageView;

import org.json.JSONObject;
import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import static com.xzmc.zzzt.privateprotect.base.App.ctx;


/**
 * Created by zw on 17/5/9.
 */

public class MineFragment extends Fragment implements View.OnClickListener{
    private HeaderLayout headerLayout;
    protected Map<String, String> param = new HashMap<String, String>();
    private RoundImageView avatar;
    private TextView name, sex, id;
    private RelativeLayout layout_collection, layout_infor, layout_suggestion;
    private LinearLayout layout_questionaire, layout_setting, layout_aboutus;
    public static ImageLoader imageLoader = ImageLoader.getInstance();

    private static final int CAMERA_WITH_DATA = 3023;
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    private static final int CAMERA_CROP_DATA = 3022;
    private TextView albumButton, camButton, cancelButton, tv_title;
    private User user;
    private Dialog ab;
    private File PHOTO_DIR = null;
    private File mCurrentPhotoFile;
    private String mFileName;
    Button btn_exit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_layout,container,false);
        avatar = (RoundImageView) view.findViewById(R.id.iv_infor_avatar);
        name = (TextView) view.findViewById(R.id.tv_infor_name);
        sex = (TextView) view.findViewById(R.id.tv_infor_sex);
        id = (TextView) view.findViewById(R.id.tv_id);
        layout_infor = (RelativeLayout) view.findViewById(R.id.layout_infor);

        layout_setting = (LinearLayout) view.findViewById(R.id.layout_setting);
        layout_aboutus = (LinearLayout) view.findViewById(R.id.layout_aboutus);
        headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        String photo_dir = AbFileUtil.getDownPathFileDir();
        if (AbStrUtil.isEmpty(photo_dir)) {
            Utils.toast("存储卡不存在");
        } else {
            PHOTO_DIR = new File(photo_dir);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LayoutInflater dialoginflater = LayoutInflater.from(getActivity());
        View dialogview = dialoginflater.inflate(
                R.layout.dialog_custom_selectpicture, null);
        ab = new Dialog(getActivity(), R.style.dialog);
        albumButton = (TextView) dialogview.findViewById(R.id.choose_album);
        tv_title = (TextView) dialogview.findViewById(R.id.tv_title);
        camButton = (TextView) dialogview.findViewById(R.id.choose_cam);
        cancelButton = (TextView) dialogview.findViewById(R.id.choose_cancel);
        albumButton.setOnClickListener(this);
        camButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        tv_title.setText("更换头像");
        ab.setCanceledOnTouchOutside(true);
        ab.setContentView(dialogview);
        headerLayout.showTitle("个人主页");
        user = Utils.getUser();
        name.setText(user.getName());
        sex.setText(user.getSex());
        String url = user.getImage();
        imageLoader.displayImage(url, avatar, PhotoUtils
                .getImageOptions(R.drawable.icon_default_avatar_normal));
        initAction();
    }

    private void initAction() {

//        layout_setting.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//               Utils.goActivity(getActivity(), SettingActivity.class);
//            }
//        });
        layout_aboutus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.goActivity(getActivity(), AboutusActivity.class);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ab.show();
            }
        });
//        layout_infor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            Utils.goActivity(getActivity(), ChangeInformationActivity.class);
//            }
//        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity())
                        .setTitleText(getString(R.string.setting_comfirm_exit))
                        .setCancelText(getString(R.string.cancel))
                        .setConfirmText(getString(R.string.ok))
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(
                                    final SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                App.getInstance().logout(new EMCallBack() {
                                    @Override
                                    public void onSuccess() {
                                       User user = new User();
                                        new PreferenceMap(ctx).setUser(user);
                                        new PreferenceMap(ctx).setPassword("");
                                        new PreferenceMap(ctx).setAccount("");
                                        new PreferenceMap(ctx).setIsRemenberAccount(false);
                                        //App.getInstance().exit();
                                        sweetAlertDialog.dismiss();
                                        new PreferenceMap(ctx).setIsLogin(false);
                                        getActivity().finish();
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                    }

                                    @Override
                                    public void onProgress(int arg0, String arg1) {

                                    }

                                    @Override
                                    public void onError(int arg0, String arg1) {

                                    }
                                });

                            }
                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(
                            SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_album:
                ab.dismiss();
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    intent.setType("image/*");
                    startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
                } catch (ActivityNotFoundException e) {
                    Utils.toast("没有找到照片");
                }
                break;
            case R.id.choose_cam:
                ab.dismiss();
                doPickPhotoAction();
                break;
            case R.id.choose_cancel:
                ab.dismiss();
                break;
        }
    }

    private void doPickPhotoAction() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            doTakePhoto();
        } else {
            Utils.toast("没有可用的存储卡");
        }
    }

    protected void doTakePhoto() {
        try {
            mFileName = System.currentTimeMillis() + ".jpg";
            mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            Utils.toast("未找到系统相机程序");
        }
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent mIntent) {
        if (resultCode != -1) {
            return;
        }
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA:
                Uri uri = mIntent.getData();
                String currentFilePath = getPath(uri);
                if (!AbStrUtil.isEmpty(currentFilePath)) {
                    Intent intent1 = new Intent(getActivity(),
                            CropImageActivity.class);
                    intent1.putExtra("PATH", currentFilePath);
                    startActivityForResult(intent1, CAMERA_CROP_DATA);
                } else {
                    Utils.toast("未在存储卡中找到这个文件");
                }
                break;
            case CAMERA_WITH_DATA:
                String currentFilePath2 = mCurrentPhotoFile.getPath();
                Intent intent2 = new Intent(getActivity(), CropImageActivity.class);
                intent2.putExtra("PATH", currentFilePath2);
                startActivityForResult(intent2, CAMERA_CROP_DATA);
                break;
            case CAMERA_CROP_DATA:
                final String path = mIntent.getStringExtra("PATH");
                new SimpleNetTask(getActivity(), true) {
                    boolean flag = true;

                    @Override
                    protected void onSucceed() {
                        if (flag) {
                            Utils.toast("更换头像成功");
                            imageLoader
                                    .displayImage(
                                            user.getImage(),
                                            avatar,
                                            PhotoUtils
                                                    .getImageOptions(R.drawable.icon_default_avatar_normal));
                        } else {
                            Utils.toast("更换头像失败");
                        }
                    }

                    @Override
                    protected void doInBack() throws Exception {
                        FileInputStream fis = new FileInputStream(path);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int count = 0;
                        while ((count = fis.read(buffer)) >= 0) {
                            baos.write(buffer, 0, count);
                        }
                        String uploadBuffer = new String(Base64.encode(baos
                                .toByteArray()));
                        param.clear();
                        param.put("userID", Utils.getID());
                        param.put("Picture", uploadBuffer);
                        String jsonstr = new WebService(C.MODIFYAVATAR, param)
                                .getReturnInfo();
                        try {
                            JSONObject json = new JSONObject(jsonstr);
                            if (json.get("ret").equals("success")) {
                                String imageurl = json.getString("pictureUrl");
                                user.setImage(imageurl);
                                new PreferenceMap(ctx).setUser(user);
                                flag = true;
                                fis.close();
                            } else {
                                flag = false;
                            }
                        } catch (Exception e) {
                            flag = false;
                        }
                    }

                }.execute();
                break;
        }
    }

    public String getPath(Uri uri) {
        if (AbStrUtil.isEmpty(uri.getAuthority())) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        CursorLoader cursorloder = new CursorLoader(getActivity(), uri,
                projection, null, null, null);
        Cursor cursor = cursorloder.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        return path;
    }
}
