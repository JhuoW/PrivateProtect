package com.xzmc.zzzt.privateprotect.http;

import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.base.C;

import java.util.HashMap;
import java.util.Map;


public class APIHelper {
	Map<String, String> param;

	public APIHelper() {
		param = new HashMap<String, String>();
	}

	/**
	 * 获取新闻分类数据
	 * 
	 * @return
	 */
	public String getNewsCatergory() {
		param.clear();
		return new WebService(C.GETCATEGORY, param).getReturnInfo();
	}

	/**
	 * get top advertisment
	 * 
	 * @return
	 */
	public String getTopPosts() {
		param.clear();
		return new WebService(C.GETNEWS, param).getReturnInfo();

	}

	public String searchPosts(String keyWord, int page, int count) {
		return "";
	}

	public String getPostsByCategory(String caterid, String tag,int page, int count) {
		param.clear();
		param.put("userID", Utils.getID());
		param.put("category", caterid);
		param.put("tag", tag);
		param.put("page", page + "");
		param.put("count", count + "");
		return new WebService(C.GETNEWS, param).getReturnInfo();
	}

}
