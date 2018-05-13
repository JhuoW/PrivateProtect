package com.xzmc.zzzt.privateprotect.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.bean.User;

/**
 * Created by enritami on 17/5/7.
 */

public class PreferenceMap {

    public static final String ISREMENBER = "isremenber";
    public static final String ISPUSH = "ispush";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String ISLOGIN = "islogin";

    public static final String NAME = "curusername";
    public static final String SEX = "curusersex";
    public static final String ID = "curuserid";
    public static final String IMAGE = "curuserimage";

    public static final String USERPHONE = "curuserphone";
    public static final String EXPERTISE = "curuserexpertise";
    public static final String BRIEF = "curuserbrief";
    public static final String ROLE = "curuserrole";
    public static final String OFFICE = "curuseroffice";
    public static final String POSITION = "curuserposition";
    public static final String ADDRESS = "curuseraddress";

    Context cxt;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static PreferenceMap currentUserPreferenceMap;

    public PreferenceMap(Context cxt) {
        this.cxt = cxt;
        pref = PreferenceManager.getDefaultSharedPreferences(cxt);
        editor = pref.edit();
    }

    public PreferenceMap(Context cxt, String prefName) {
        this.cxt = cxt;
        pref = cxt.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static PreferenceMap getCurUserPrefDao(Context ctx) {
        if (currentUserPreferenceMap == null) {
            currentUserPreferenceMap = new PreferenceMap(ctx, Utils.getID());
        }
        return currentUserPreferenceMap;
    }

    public boolean isRemenberAccount() {
        return pref.getBoolean(ISREMENBER, false);
    }

    public void setIsRemenberAccount(Boolean flag) {
        editor.putBoolean(ISREMENBER, flag).commit();
    }

    public boolean isPush() {
        return pref.getBoolean(ISPUSH, true);
    }

    public void setIsPush(Boolean flag) {
        editor.putBoolean(ISPUSH, flag).commit();
    }

    public String getAccount() {
        return pref.getString(ACCOUNT, "");
    }

    public void setAccount(String account) {
        editor.putString(ACCOUNT, account).commit();
    }

    public String getPassword() {
        return pref.getString(PASSWORD, "");
    }

    public void setPassword(String password) {
        editor.putString(PASSWORD, password).commit();
    }
    public void setUser(User user) {
        editor.putString(ID, user.getID()).commit();
        editor.putString(NAME, user.getName()).commit();
        editor.putString(SEX, user.getSex()).commit();
        editor.putString(IMAGE, user.getImage()).commit();
        editor.putString(ADDRESS, user.getAddress()).commit();

        editor.putString(ACCOUNT, user.getPhone()).commit();
        editor.putString(POSITION, user.getPosition()).commit();
        editor.putString(ROLE, user.getRole()).commit();
        editor.putString(OFFICE, user.getOffice()).commit();
        editor.putString(EXPERTISE, user.getExpertise()).commit();
        editor.putString(BRIEF, user.getBriefIntroduction()).commit();
    }

    public User getUser() {
        User user = new User();
        user.setID(pref.getString(ID, ""));
        user.setName(pref.getString(NAME, ""));
        user.setSex(pref.getString(SEX, ""));
        user.setImage(pref.getString(IMAGE, ""));
        user.setAddress(pref.getString(ADDRESS,""));

        user.setPhone(pref.getString(ACCOUNT,""));
        user.setPosition(pref.getString(POSITION,""));
        user.setRole(pref.getString(ROLE,""));
        user.setOffice(pref.getString(OFFICE,""));
        user.setExpertise(pref.getString(EXPERTISE,""));
        user.setBriefIntroduction(pref.getString(BRIEF,""));
        return user;
    }

    public boolean isLogin() {
        return pref.getBoolean(ISLOGIN, false);
    }
    public void setIsLogin(Boolean flag) {
        editor.putBoolean(ISLOGIN, flag).commit();
    }
}
