package com.xzmc.zzzt.privateprotect.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected Context ctx;
	protected LayoutInflater inflater;
	protected List<T> datas = new ArrayList<T>();
	protected int layoutId;

	public void initWithContext(Context ctx) {
		this.ctx = ctx;
		inflater = LayoutInflater.from(ctx);
	}
	 public BaseListAdapter(Context ctx) {
		    initWithContext(ctx);
		  }
	public BaseListAdapter(Context ctx, List<T> datas, int layoutId) {
		initWithContext(ctx);
		this.datas = datas;
		this.layoutId = layoutId;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void addAll(List<T> subDatas) {
		datas.addAll(subDatas);
		notifyDataSetChanged();
	}

	public void updateDatas(List<T> datas) {
		datas.clear();
		datas.addAll(datas);
		notifyDataSetChanged();
	}

	public void add(T object) {
		datas.add(object);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		datas.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public T getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(ctx, convertView, parent, layoutId,
				position);
		conver(holder, position, getItem(position));
		return holder.getConvertView();
	}

	public abstract void conver(ViewHolder holder, int position, T t);

	public void clear() {
		datas.clear();
		notifyDataSetChanged();
	}
}
