package com.xzmc.zzzt.privateprotect.http;

import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.bean.Group;
import com.xzmc.zzzt.privateprotect.bean.User;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 17/5/8.
 */

public class GetObjectFromService {
    static PreferenceMap preference = new PreferenceMap(App.ctx);
    /**
     * 获取登录结果
     *
     * @param jsonstr
     * @param number
     * @param password
     * @return
     */

    public static List<Group> getFriend(String jsonstr) {
        List<Group> list = new ArrayList<Group>();
        try {
            JSONObject json = new JSONObject(jsonstr);
            if (json.get("ret").equals("success")) {
                JSONArray jsonarray = json.getJSONArray("friendlist");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject groupobj = jsonarray.getJSONObject(i);
                    List<User> userdata=new ArrayList<User>();
                    Group group = new Group();
                    group.setName(groupobj.getString("groupname"));
                    group.setType(groupobj.getString("grouprole"));
                    JSONArray members=groupobj.getJSONArray("groupnumber");
                    if(groupobj.getString("grouprole").equals("doctor")){
                        for(int j=0;j<members.length();j++){
                            JSONObject obj=members.getJSONObject(j);
                            User user=new User();
                            user.setID(obj.getString("userID"));
                            user.setImage(obj.getString("picture"));
                            user.setName(obj.getString("name"));
                            user.setPhone(obj.getString("phone"));
                            user.setSex(obj.getString("sex"));
                            user.setOffice(obj.getString("office"));
                            user.setPosition(obj.getString("position"));
                            user.setExpertise(obj.getString("expertise"));
                            user.setBriefIntroduction(obj.getString("briefIntroduction"));
                            user.setRole("doctor");
                            userdata.add(user);
                        }
                    }else if(groupobj.getString("grouprole").equals("patient")){
                        for(int j=0;j<members.length();j++){
                            JSONObject obj=members.getJSONObject(j);
                            User user=new User();
                            user.setID(obj.getString("userID"));
                            user.setImage(obj.getString("picture"));
                            user.setName(obj.getString("name"));
                            user.setPhone(obj.getString("phone"));
                            user.setSex(obj.getString("sex"));
                            user.setRole("patient");
                            userdata.add(user);
                        }
                    }
                    group.setMembers(userdata);
                    list.add(group);
                }
            }
        } catch (Exception e) {
        }
        return list;
    }
    public static Boolean getLoginResult(String jsonstr, String account) {
        boolean result = false;
        try {
            JSONObject json = new JSONObject(jsonstr);
            if (json.get("ret").equals("success")) {
                final String id = json.getString("ID");
                final String image = json.getString("picture");
                final String name = json.getString("name");
                final String sex = json.getString("sex");
                final String address = json.getString("address");
                final String role = json.getString("role");
                final String office = json.getString("office");
                final String position = json.getString("position");
                final String expertise = json.getString("expertise");
                final String briefIntroduction = json
                        .getString("briefIntroduction");

                User user = new User();
                user.setID(id);
                user.setAddress(address);
                user.setImage(image);
                user.setName(name);
                user.setSex(sex);
                user.setRole(role);
                user.setOffice(office);
                user.setPosition(position);
                user.setExpertise(expertise);
                user.setPhone(account);
                user.setBriefIntroduction(briefIntroduction);

                preference.setUser(user);
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }
    public static Boolean getSimplyResult(String jsonstr) {
        Boolean flag = false;
        try {
            JSONObject json = new JSONObject(jsonstr);
            if (!json.getString("ret").equals("success")) {
                return flag;
            } else {
                flag = true;
            }
        } catch (Exception e) {
        }
        return flag;
    }

}
