package com.xzmc.zzzt.privateprotect.bean;

import java.io.Serializable;

public class CategoryModel implements Serializable{
	private static final long serialVersionUID = 11111111111112L;

	private String id ;
	private String description;
	private String title;

	@Override
	public String toString() {
		return "CategoryModel [id=" + id + ", description=" + description
				+ ", title=" + title + ", isSelected=" + isSelected + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private boolean isSelected = false;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
