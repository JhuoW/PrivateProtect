package com.xzmc.zzzt.privateprotect.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.Utils.PhotoUtils;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.base.C;
import com.xzmc.zzzt.privateprotect.bean.Group;
import com.xzmc.zzzt.privateprotect.bean.User;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;
import com.xzmc.zzzt.privateprotect.http.GetObjectFromService;
import com.xzmc.zzzt.privateprotect.http.WebService;
import com.xzmc.zzzt.privateprotect.listener.ICallBack;

public class UserService {
	public static final int ORDER_UPDATED_AT = 1;
	public static final int ORDER_DISTANCE = 0;
	public static ImageLoader imageLoader = ImageLoader.getInstance();


	public static List<Group> findFriends() throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		User user= Utils.getUser();
		String role=user.getRole();
		if(role.equals("doctor")){
			param.put("tag", "patient");
		}else if(role.equals("patient")){
			param.put("tag", "doctor");
		}
		
		String jsonstr = new WebService(C.GETFRIEND, param).getReturnInfo();
		List<Group> friend = GetObjectFromService.getFriend(jsonstr);
		final List<Group> friends = new ArrayList<Group>();
		friends.addAll(friend);
		return friends;
	}

	public static void displayAvatar(String imageUrl, ImageView avatarView) {
		imageLoader.displayImage(imageUrl, avatarView,
				PhotoUtils.getImageOptions(R.drawable.icon_default_avatar));
	}

	public static void addFriend(String friendId,
			final ICallBack saveCallback) {
		User user = new PreferenceMap(App.ctx).getUser();
		Map<String,String> param = new HashMap<String, String>();
		param.put("Remarks","你好，我是"+user.getName());
		param.put("fromUserID", user.getID());
		param.put("toUserID", friendId);
		String jsonstr = new WebService(C.REQUESTFRIEND, param).getReturnInfo();
		Boolean flag = GetObjectFromService.getSimplyResult(jsonstr);
		saveCallback.onBackMessage(flag);
	}
}
