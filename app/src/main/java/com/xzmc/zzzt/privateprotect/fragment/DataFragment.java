package com.xzmc.zzzt.privateprotect.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.DataSuggestActivity;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.sweetdialog.SweetAlertDialog;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;

/**
 * Created by zw on 17/5/9.
 */

public class DataFragment extends BaseFragment{
    private HeaderLayout headerLayout;

    private Button btn_submit;
    private Button btn_suggest;
    private ProgressDialog dialog;
    private static final int sleepTime = 2000;
    private mHandler handler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container,
                false);
        headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
        headerLayout.showTitle("个人数据上传");

        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_suggest = (Button) view.findViewById(R.id.btn_suggest);
        dialog = new ProgressDialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("数据正在上传中...");
        handler = new mHandler();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAction();
    }

    private void initAction(){
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity()).setTitleText("是否上传数据？")
                        .setCancelText("取消").setConfirmText("确定").showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                dialog.show();
                                //开启线程
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(sleepTime);
                                            Message msg = handler.obtainMessage();
                                            msg.obj = 1;
                                            handler.sendMessage(msg);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                    }
                }).show();
            }
        });
        btn_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.goActivity(getActivity(), DataSuggestActivity.class);
            }
        });
    }
    public class mHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = (int) msg.obj;
            if(i==1){
                dialog.dismiss();
                new SweetAlertDialog(getActivity(),SweetAlertDialog.SUCCESS_TYPE).
                        showCancelButton(false).setConfirmText("确定").setTitleText("上传数据成功")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();
            }
        }
    }

    private void setEtEmpty(EditText et){
        et.setText(null);
    }
}
