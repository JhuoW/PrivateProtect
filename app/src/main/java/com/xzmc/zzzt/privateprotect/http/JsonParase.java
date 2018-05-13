package com.xzmc.zzzt.privateprotect.http;

import com.xzmc.zzzt.privateprotect.db.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author xiaobian
 * @version 创建时间：2015年6月29日 下午9:55:14
 * 
 */
public class JsonParase {
	public static void getNewsCaters(DBHelper dbHelper, String jsonstr) {
		try {
			JSONObject json = new JSONObject(jsonstr);
			if (json.get("ret").equals("success")) {
				JSONArray jsonarray = json
						.getJSONArray("newsCategoryList");
				dbHelper.insertCategories(jsonarray);
			}
		} catch (Exception e) {
		}
	}
}
