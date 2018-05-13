package com.xzmc.zzzt.privateprotect.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.bean.User;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;

/**
 * Created by zw on 17/5/9.
 */

public class BaseFragment extends Fragment {
    public HeaderLayout headerLayout;
    public Context ctx;
    public User curUser;
    public boolean isDoctor;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        curUser= Utils.getUser();
    }
}
