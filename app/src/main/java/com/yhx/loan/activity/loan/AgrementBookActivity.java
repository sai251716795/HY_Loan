package com.yhx.loan.activity.loan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhx.loan.R;
import com.yhx.loan.base.BaseCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 显示协议书
 */
public class AgrementBookActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.AgreementBookWebView)
    WebView webView;
    String url;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_agrement_book);
        ButterKnife.bind(this);
        tvTitle.setText("协议书");
        url = getIntent().getStringExtra("agreement_url");
        data = getIntent().getStringExtra("agreement_data");
        webView.getSettings().setDomStorageEnabled(true);// 开启Dom storage API 功能
        webView.requestFocusFromTouch();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("webString", "shouldOverrideUrlLoading: " + request.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            //开始是
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                dismissLoadingDialog();
                initData();
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                tvTitle.setText(title);
            }
        });


    }

    private void initData() {
//       if(!data.equals(""))
        webView.loadUrl("javascript:insert(" + data + ")");
    }

    public void loadUrl(String url) {
        if (webView != null) {
            webView.loadUrl(url);
            showLoadingDialog();
            webView.reload();
        }
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
