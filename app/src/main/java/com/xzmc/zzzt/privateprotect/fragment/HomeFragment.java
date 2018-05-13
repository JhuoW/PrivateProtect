package com.xzmc.zzzt.privateprotect.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.adapter.mExpandableListAdapter;
import com.easemob.chatuidemo.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.chatuidemo.domain.User;
import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.DoctorDetailActivity;
import com.xzmc.zzzt.privateprotect.PostDetaileActivity;
import com.xzmc.zzzt.privateprotect.adapter.TopVpAdp;
import com.xzmc.zzzt.privateprotect.base.App;
import com.xzmc.zzzt.privateprotect.bean.Group;
import com.xzmc.zzzt.privateprotect.bean.PostModel;
import com.xzmc.zzzt.privateprotect.bean.TopVpItem;
import com.xzmc.zzzt.privateprotect.db.DBHelper;
import com.xzmc.zzzt.privateprotect.http.APIHelper;
import com.xzmc.zzzt.privateprotect.http.SimpleNetTask;
import com.xzmc.zzzt.privateprotect.service.UserService;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zw on 17/5/9.
 */

public class HomeFragment extends BaseFragment{
    private List<TopVpItem> topVpItems = new ArrayList<TopVpItem>();
    private List<PostModel> topPostModels = new ArrayList<PostModel>();
    private ViewPager advertiseViewPager;
    private RadioGroup centerRadioGroup;
    private TopVpAdp topVpAdp;
    private DBHelper dbHelper;
    private int w, h;
    private RadioGroup.LayoutParams layoutParams;
    HeaderLayout headerLayout;
    private mExpandableListAdapter myExpandAdapter;
    private ExpandableListView groupList;
    private List<Group> datas = new ArrayList<Group>();
    SwipeRefreshLayout swipe_container;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, container,
                false);
        swipe_container = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        //设置刷新时动画的颜色，可以设置4个
        swipe_container.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
        groupList = (ExpandableListView) view
                .findViewById(R.id.user_expandablelistview);
        groupList.setGroupIndicator(null);

        myExpandAdapter = new mExpandableListAdapter(getActivity(), datas);
        groupList.setAdapter(myExpandAdapter);
        w = getResources().getDrawable(R.drawable.radiobutton_normal)
                .getIntrinsicWidth();
        h = getResources().getDrawable(R.drawable.radiobutton_normal)
                .getIntrinsicHeight();
        layoutParams = new RadioGroup.LayoutParams(w, h);
        layoutParams.setMargins(0, 0, 30, 0);

        advertiseViewPager = (ViewPager) view
                .findViewById(R.id.centerViewPager);
        centerRadioGroup = (RadioGroup) view
                .findViewById(R.id.centerRadioGroup);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initAction();
    }

    private void initData() {
        headerLayout.showTitle("专家在线");
        dbHelper = DBHelper.getCurrentUserInstance(getActivity());
        dbHelper.openSqLiteDatabase();
        getData(true);

    }

    private void getData(boolean b) {
        new SimpleNetTask(getActivity(), b) {
            List<Group> temp;

            @Override
            protected void onSucceed() {
                if (temp != null) {
                    datas.clear();
                    datas.addAll(temp);
                    myExpandAdapter.notifyDataSetChanged();
                    for(int i = 0; i < myExpandAdapter.getGroupCount(); i++){

                        groupList.expandGroup(i);

                    }

                    try {
                        Map<String, User> usermap = new HashMap<String, User>();
                        for (Group group : temp) {
                            int usernum = group.getMembers().size();
                            for (int j = 0; j < usernum; j++) {
                                com.xzmc.zzzt.privateprotect.bean.User qxUser = group.getMembers().get(j);
                                User tempuser = new User();
                                tempuser.setAvatar(qxUser.getImage());
                                tempuser.setNick(qxUser.getName());
                                tempuser.setUsername(qxUser.getID());
                                usermap.put(qxUser.getID(), tempuser);
                            }
                        }
                        App.getInstance().setContactList(usermap);
                        UserDao dao = new UserDao(ctx);
                        List<User> users = new ArrayList<User>(usermap.values());
                        dao.saveContactList(users);
                    } catch (Exception e) {
                    }

                }
                topPostModels.clear();
                topVpItems.clear();
                List<PostModel> models = dbHelper.queryPostsByCategoryId("-1",
                        "6");
                topPostModels.addAll(models);
                for (int i = 0; i < topPostModels.size(); i++) {
                    TopVpItem topVpItem = new TopVpItem();
                    topVpItem.relativeLayout = (RelativeLayout) getActivity()
                            .getLayoutInflater().inflate(
                                    R.layout.lay_center_vp_item, null);
                    topVpItem.relativeLayout.setTag(i);
                    topVpItem.imageView = (ImageView) topVpItem.relativeLayout
                            .findViewById(R.id.ivViewPageItem);
                    topVpItem.textView = (TextView) topVpItem.relativeLayout
                            .findViewById(R.id.tvViewPageItem);
                    topVpItem.relativeLayout
                            .setOnClickListener(topImgsClickListenner);
                    topVpItems.add(topVpItem);
                }
                topVpAdp = new TopVpAdp(topVpItems, topPostModels);
                advertiseViewPager.setAdapter(topVpAdp);
                // 添加小圆点
                centerRadioGroup.removeAllViews();
                for (int i = 0; i < topVpItems.size(); i++) {
                    RadioButton radioButton = new RadioButton(getActivity());
                    radioButton.setLayoutParams(layoutParams);
                    radioButton.setButtonDrawable(R.drawable.radiobutton_sel);
                    radioButton
                            .setBackgroundResource(R.drawable.radiobutton_sel);
                    if (i == 0) {
                        radioButton.setSelected(true);
                    } else {
                        radioButton.setClickable(false);
                    }
                    centerRadioGroup.addView(radioButton, i);
                }

            }

            @Override
            protected void doInBack() throws Exception{}

        }.execute();
    }

    private void initAction() {

        groupList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                startActivity(new Intent(getActivity(),
                        DoctorDetailActivity.class).putExtra("doctor",
                        ((Group) parent.getAdapter().getItem(groupPosition))
                                .getMembers().get(childPosition)));
                return false;
            }
        });

        advertiseViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < centerRadioGroup.getChildCount(); i++) {
                    if (i == arg0) {
                        centerRadioGroup.getChildAt(i).setSelected(true);
                    } else {
                        centerRadioGroup.getChildAt(i).setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        centerRadioGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        for (int i = 0; i < centerRadioGroup.getChildCount(); i++) {
                            if (i == checkedId) {
                                centerRadioGroup.getChildAt(i)
                                        .setSelected(true);
                            } else {
                                centerRadioGroup.getChildAt(i).setSelected(
                                        false);
                            }
                        }
                        advertiseViewPager.setCurrentItem(checkedId);
                    }
                });

        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // getData(false);
                getRefreshData();
            }
        });
    }


    private void getRefreshData(){
        new SimpleNetTask(getActivity(), false) {
            List<Group> temp;

            @Override
            protected void onSucceed() {
                if (temp != null) {
                    datas.clear();
                    datas.addAll(temp);
                    myExpandAdapter.notifyDataSetChanged();
                    for(int i = 0; i < myExpandAdapter.getGroupCount(); i++){

                        groupList.expandGroup(i);

                    }

                    try {
                        Map<String, User> usermap = new HashMap<String, User>();
                        for (Group group : temp) {
                            int usernum = group.getMembers().size();
                            for (int j = 0; j < usernum; j++) {
                                com.xzmc.zzzt.privateprotect.bean.User qxUser = group.getMembers().get(j);
                                User tempuser = new User();
                                tempuser.setAvatar(qxUser.getImage());
                                tempuser.setNick(qxUser.getName());
                                tempuser.setUsername(qxUser.getID());
                                usermap.put(qxUser.getID(), tempuser);
                            }
                        }
                        App.getInstance().setContactList(usermap);
                        UserDao dao = new UserDao(ctx);
                        List<User> users = new ArrayList<User>(usermap.values());
                        dao.saveContactList(users);
                    } catch (Exception e) {
                    }

                }
                topPostModels.clear();
                topVpItems.clear();
                List<PostModel> models = dbHelper.queryPostsByCategoryId("-1",
                        "6");
                topPostModels.addAll(models);
                for (int i = 0; i < topPostModels.size(); i++) {
                    TopVpItem topVpItem = new TopVpItem();
                    topVpItem.relativeLayout = (RelativeLayout) getActivity()
                            .getLayoutInflater().inflate(
                                    R.layout.lay_center_vp_item, null);
                    topVpItem.relativeLayout.setTag(i);
                    topVpItem.imageView = (ImageView) topVpItem.relativeLayout
                            .findViewById(R.id.ivViewPageItem);
                    topVpItem.textView = (TextView) topVpItem.relativeLayout
                            .findViewById(R.id.tvViewPageItem);
                    topVpItem.relativeLayout
                            .setOnClickListener(topImgsClickListenner);
                    topVpItems.add(topVpItem);
                }
                topVpAdp = new TopVpAdp(topVpItems, topPostModels);
                advertiseViewPager.setAdapter(topVpAdp);
                // 添加小圆点
                centerRadioGroup.removeAllViews();
                for (int i = 0; i < topVpItems.size(); i++) {
                    RadioButton radioButton = new RadioButton(getActivity());
                    radioButton.setLayoutParams(layoutParams);
                    radioButton.setButtonDrawable(R.drawable.radiobutton_sel);
                    radioButton
                            .setBackgroundResource(R.drawable.radiobutton_sel);
                    if (i == 0) {
                        radioButton.setSelected(true);
                    } else {
                        radioButton.setClickable(false);
                    }
                    centerRadioGroup.addView(radioButton, i);
                }
                swipe_container.setRefreshing(false);
            }

            @Override
            protected void doInBack() throws Exception {
                String adverjsonstr = new APIHelper().getPostsByCategory("1",
                        "advert", 1, 6);
                try {
                    JSONObject json = new JSONObject(adverjsonstr);
                    if (json.get("ret").equals("success")) {
                        JSONArray jsonarray = json.getJSONArray("newsList");
                        dbHelper.insertPosts(jsonarray, -1 + "");
                    }
                } catch (Exception e) {
                }
                temp = UserService.findFriends();
            }
        }.execute();
    }


    private View.OnClickListener topImgsClickListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag();// index
            Intent intent = new Intent(getActivity(), PostDetaileActivity.class);
            intent.putExtra("post", topPostModels.get(index));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.anim_r_to_0,
                    R.anim.anim_0_to_l);
        }
    };

}
