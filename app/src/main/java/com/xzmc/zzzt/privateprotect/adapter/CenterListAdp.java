package com.xzmc.zzzt.privateprotect.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.bean.PostModel;

import java.util.List;

/**
 * Created by zw on 17/5/11.
 */

public class CenterListAdp extends BaseListAdapter<PostModel>{


    public CenterListAdp(Context ctx, List<PostModel> datas, int layoutId) {
        super(ctx, datas, layoutId);
    }

    @Override
    public void conver(ViewHolder holder, int position, PostModel t) {
        holder.setText(R.id.item_title, t.getTitle());
        holder.setText(R.id.item_des, t.getContent());
        holder.setText(R.id.publish_time, t.getTime());
        holder.setText(R.id.item_view, "浏览量   "+t.getView_count());
        holder.setText(R.id.item_study, "评论数   "+t.getComment_count());
        holder.setImage(R.id.right_image, t.getImageurl(), R.drawable.icon_400_300);
        ImageView image=holder.getView(R.id.right_image);
        if(t.getImageurl().isEmpty()){
            image.setVisibility(View.GONE);
        }else{
            image.setVisibility(View.VISIBLE);
        }
    }
}
