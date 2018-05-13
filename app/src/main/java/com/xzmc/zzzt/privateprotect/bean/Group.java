package com.xzmc.zzzt.privateprotect.bean;

import java.util.List;

/** 
 * @author xiaobian 
 * @version 创建时间：2015年7月14日 下午7:34:41 
 * 
 */
public class Group {
private List<User> members;
private String name;
private String type;


public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public List<User> getMembers() {
	return members;
}
public void setMembers(List<User> members) {
	this.members = members;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

}
