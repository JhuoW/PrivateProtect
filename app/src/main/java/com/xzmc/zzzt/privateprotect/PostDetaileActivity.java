package com.xzmc.zzzt.privateprotect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xzmc.health.R;
import com.xzmc.zzzt.privateprotect.bean.PostModel;
import com.xzmc.zzzt.privateprotect.bean.User;
import com.xzmc.zzzt.privateprotect.db.PreferenceMap;
import com.xzmc.zzzt.privateprotect.view.HeaderLayout;

/**
 * Created by zw on 17/5/10.
 */

public class PostDetaileActivity extends BaseActivity{
    public static String newsbaseurl = "http://114.215.81.194:7788/web-news-visit-clean.aspx?nid=";
    private WebView webView;
    private PostModel curPost;
    private ProgressBar load_pro;
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_post_detail_act);
        curUser = new PreferenceMap(ctx).getUser();
        initView();
        Intent intent = getIntent();
        if (!setData(intent)) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void initView(){
        load_pro = (ProgressBar) findViewById(R.id.load_pro);
        headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
        headerLayout.showLeftBackButton();
        webView = (WebView) findViewById(R.id.webView);
        setWebSeeting(webView);
        headerLayout.showLeftBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetaileActivity.this.finish();
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
    }
    public boolean setData(Intent intent) {
        curPost = (PostModel) intent.getSerializableExtra("post");
        String id = curPost.getId();
        String url = newsbaseurl + id;
        webView.loadUrl(url);
        headerLayout.showTitle(curPost.getTitle());
        return true;
    }
    public void setWebSeeting(final WebView webView) {
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 加载完毕，关闭进度条
                    load_pro.setVisibility(View.GONE);
                } else {
                    load_pro.setVisibility(View.VISIBLE);
                    // 网页正在加载，显示进度框
                    load_pro.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebView(webView);
    }

    public void clearWebView(WebView webView) {
        if (webView != null) {
            webView.stopLoading();
            webView.removeAllViews();
            webView.loadUrl("");
            webView.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            load_pro.setVisibility(View.GONE);
            finish();
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
