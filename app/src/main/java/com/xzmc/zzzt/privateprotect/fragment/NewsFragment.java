package com.xzmc.zzzt.privateprotect.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.PostDetaileActivity;
import com.xzmc.zzzt.privateprotect.Utils.BaseTools;
import com.xzmc.zzzt.privateprotect.Utils.Utils;
import com.xzmc.zzzt.privateprotect.adapter.CenterListAdp;
import com.xzmc.zzzt.privateprotect.bean.CategoryModel;
import com.xzmc.zzzt.privateprotect.bean.PostModel;
import com.xzmc.zzzt.privateprotect.db.DBHelper;
import com.xzmc.zzzt.privateprotect.http.APIHelper;
import com.xzmc.zzzt.privateprotect.http.SimpleNetTask;
import com.xzmc.zzzt.privateprotect.view.ColumnHorizontalScrollView;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;
import com.xzmc.zzzt.privateprotect.view.XListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 17/5/9.
 */

public class NewsFragment extends Fragment implements XListView.IXListViewListener{
    private List<CategoryModel> userChannelList = new ArrayList<CategoryModel>();
    private List<PostModel> newsList = new ArrayList<PostModel>();
    private XListView news_listview;
    private DBHelper dbHelper;
    private CenterListAdp mAdapter;
    LinearLayout mCater_content;
    RelativeLayout rl_column;
    /** 用户选择的新闻分类列表 */
    /** 当前选中的栏目 */
    private int columnSelectIndex = 0;
    /** 左阴影部分 */
    public ImageView shade_left;
    /** 右阴影部分 */
    public ImageView shade_right;
    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private int currentPage = 1;
    private int currentPageSize = 40;
    public static String channel_id;
    public static String channel_text;
    View headerView;
    HeaderLayout headerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_layout,container,false);
        news_listview = (XListView) view.findViewById(R.id.news_listview);
        news_listview.setPullRefreshEnable(true);
        news_listview.setPullLoadEnable(false);
        news_listview.setXListViewListener(this);
        shade_left = (ImageView) view.findViewById(R.id.shade_left);
        shade_right = (ImageView) view.findViewById(R.id.shade_right);
        headerLayout = (HeaderLayout) view.findViewById(R.id.headerLayout);
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view
                .findViewById(R.id.mColumnHorizontalScrollView);
        mCater_content = (LinearLayout) view
                .findViewById(R.id.mRadioGroup_content);
        rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);


        mScreenWidth = BaseTools.getWindowsWidth(getActivity());
        mItemWidth = mScreenWidth / 4;
        mAdapter = new CenterListAdp(getActivity(), newsList,
                R.layout.item_news);
        news_listview.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initAction();
    }

    private void initData() {
        headerLayout.showTitle("健康咨讯");
        dbHelper = DBHelper.getCurrentUserInstance(getActivity());
        dbHelper.openSqLiteDatabase();
        userChannelList = (ArrayList<CategoryModel>) dbHelper
                .queryAllCategory();
        if (userChannelList.size() != 0) {
            channel_text = userChannelList.get(0).getTitle();
            channel_id = userChannelList.get(0).getId();
            initTabColumn();
        } else {
            Utils.toast("暂无新闻栏目信息");
        }
    }

    /**
     * 初始化Column栏目项
     * */
    private void initTabColumn() {
        mCater_content.removeAllViews();
        mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth,
                mCater_content, shade_left, shade_right, rl_column);
        for (int i = 0; i < userChannelList.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    mItemWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            TextView columnTextView = new TextView(getActivity());
            columnTextView.setTextAppearance(getActivity(),
                    R.style.top_category_scroll_view_item_text);
            columnTextView.setBackgroundResource(R.drawable.radio_button_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).getTitle());
            columnTextView.setTextColor(getResources().getColorStateList(
                    R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mCater_content.getChildCount(); i++) {
                        View localView = mCater_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            selectTab(i);
                        }
                    }
                }
            });
            mCater_content.addView(columnTextView, i, params);
        }
        selectTab(0);
    }
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mCater_content.getChildCount(); i++) {
            View checkView = mCater_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        // 判断是否选中
        for (int j = 0; j < mCater_content.getChildCount(); j++) {
            View checkView = mCater_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
        channel_text = userChannelList.get(tab_postion).getTitle();
        channel_id = userChannelList.get(tab_postion).getId();
        // 加载新闻数据
        List<PostModel> newdata;
        newdata = dbHelper.queryPostsByCategoryId(channel_id + "", currentPage
                * currentPageSize + "");
        if (newdata != null) {
            newsList.clear();
            newsList.addAll(newdata);
            mAdapter.notifyDataSetChanged();
        }
        getNewsData(channel_id, false);
    }

    /**
     * get news data
     *
     * @param channel_id
     */
    private void getNewsData(final String channel_id, final boolean isLoad) {
        new SimpleNetTask(getActivity(), false) {
            @Override
            protected void onSucceed() {
                news_listview.stopRefresh();
                news_listview.stopLoadMore();

                if (newsList.size() == 0) {
                    // 加载新闻数据
                    List<PostModel> newdata;
                    newdata = dbHelper.queryPostsByCategoryId(channel_id + "",
                            currentPage * currentPageSize + "");
                    if (newdata != null) {
                        newsList.clear();
                        newsList.addAll(newdata);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected void doInBack() throws Exception {
                // 获取新闻数据
                String newsjsonstr = new APIHelper().getPostsByCategory(
                        channel_id + "", "news", currentPage, currentPageSize);
                try {
                    JSONObject json = new JSONObject(newsjsonstr);
                    if (json.get("ret").equals("success")) {
                        JSONArray jsonarray = json.getJSONArray("newsList");
                        dbHelper.insertPosts(jsonarray, channel_id);
                    }
                } catch (Exception e) {
                }
            }
        }.execute();
    }


    private void initAction() {
        news_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PostModel post = (PostModel) parent.getAdapter().getItem(
                        position);
                Intent intent = new Intent(getActivity(),
                        PostDetaileActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroyView();
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        getNewsData(channel_id, false);
    }

    @Override
    public void onLoadMore() {
        currentPage += 1;
        getNewsData(channel_id, true);
    }
}
