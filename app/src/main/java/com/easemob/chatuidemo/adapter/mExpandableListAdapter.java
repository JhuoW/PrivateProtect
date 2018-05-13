package com.easemob.chatuidemo.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.Utils.PhotoUtils;
import com.xzmc.zzzt.privateprotect.bean.Group;
import com.xzmc.zzzt.privateprotect.bean.User;

import static android.R.attr.thumbnail;

public class mExpandableListAdapter extends BaseExpandableListAdapter {

    private List<Group> groups;
    private LayoutInflater inflater, childinflater;
    Context context;
    public static ImageLoader imageLoader = ImageLoader.getInstance();


    public mExpandableListAdapter(Context context, List<Group> groups) {
        this.groups = groups;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        childinflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getMembers().size();
    }

    @Override
    public Group getGroup(int provincePosition) {
        return groups.get(provincePosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getMembers().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ImageView arrow;
        TextView name;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_item, parent, false);
        }

        name = (TextView) convertView.findViewById(R.id.category_text_large);
        arrow = (ImageView) convertView.findViewById(R.id.category_arow);

        name.setText(groups.get(groupPosition).getName());

        if (isExpanded) {
            arrow.setImageResource(R.drawable.arrow_collapse);
        } else {
            arrow.setImageResource(R.drawable.arrow_expand);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = childinflater.inflate(R.layout.group_child_item,
                    parent, false);
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            holder.office = (TextView) view.findViewById(R.id.tv_office);
            holder.ingood = (TextView) view.findViewById(R.id.tv_ingood);
            holder.position = (TextView) view.findViewById(R.id.tv_position);
            holder.avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        User user = groups.get(groupPosition).getMembers()
                .get(childPosition);
        holder.name.setText(user.getName());
        holder.position.setText(user.getPosition());
        holder.ingood.setText(user.getExpertise());
        holder.office.setText(user.getOffice());
        Log.d("showPic",user.getImage());
        ImageLoader.getInstance().displayImage(user.getImage(), holder.avatar,
               PhotoUtils.getImageOptions(R.drawable.icon_default_avatar));

        return view;
    }

    class ViewHolder {
        TextView name, office, ingood, position;
        ImageView avatar;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
