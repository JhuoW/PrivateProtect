package com.easemob.chatuidemo.chatuidemo.utils;

import android.content.Context;
import android.widget.ImageView;

import com.easemob.chatuidemo.chatuidemo.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.Utils.PhotoUtils;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;

/**
 * 用户操作工具类
 * 
 * @author xiaobian ok
 */
public class UserUtils {
	public static ImageLoader imageLoader = ImageLoader.getInstance();

	/**
	 * 获取数据库好友用户用户对象 如果对象为空则新建一个对象
	 * 
	 * @param username
	 * @return
	 */
	public static com.xzmc.zzzt.privateprotect.bean.User getUserInfo(String username) {
		com.xzmc.zzzt.privateprotect.bean.User user = null;
		if (username.equals(Utils.getID())) {
			return new PreferenceMap(App.ctx).getUser();
		} else {
			if (App.getInstance().getContactList() != null) {
				User tempuser = App.getInstance().getContactList().get(username);
				user = new com.xzmc.zzzt.privateprotect.bean.User();
				user.setName(tempuser.getNick());
				user.setID(tempuser.getUsername());
				user.setImage(tempuser.getAvatar());
			}
			else{
				user = new com.xzmc.zzzt.privateprotect.bean.User();
				user.setID(username);
				user.setName(username);
				user.setImage("");
			}
			return user;
		}
	}

	/**
	 * 设置用户头像
	 * @param username
	 */
	public static void setUserAvatar(Context context, String username,
			ImageView imageView) {
		com.xzmc.zzzt.privateprotect.bean.User user = getUserInfo(username);
		String imageUrl = "";
		int defaultres = 0;
		if (user != null && user.getImage() != null) {
			imageUrl = user.getImage();
		}
			defaultres = R.drawable.icon_default_avatar;
		imageLoader.displayImage(imageUrl, imageView,
				PhotoUtils.getImageOptions(defaultres));
	}

//		else {
//			ChatGroup group = GroupsTable.getInstance().selectGroupByEaseId(
//					groupEaseId);
//			if (group == null) {
//				return userName;
//			} else {
//				List<QXUser> members = group.getMember();
//				for (QXUser groupuser : members) {
//					if (groupuser.getID().equals(userId)) {
//						userName = groupuser.getName();
//						break;
//					}
//				}
//			}
//		}
		
		// else if(){
		// //去本地缓存的数据里面找数据
		// return "";
		// }
}
