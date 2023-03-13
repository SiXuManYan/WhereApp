package com.jiechengsheng.city.features.com100;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.comm100.livechat.core.VisitorClientCore;
import com.comm100.livechat.view.ChatWindowWebChromeClient;
import com.comm100.livechat.view.VisitorClientCustomJS;

import java.util.ArrayList;


public class Common100WebView extends WebView {

    private ArrayList<String> injectCustomJSs = new ArrayList<>();

    private ChatWindowWebChromeClient mWebChromeClient;

    private WebViewClient mWebViewClient = new WebViewClient() {
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            StringBuilder scriptBuilder = new StringBuilder();
            scriptBuilder.append("Comm100API.onReady = function() {");
            if (injectCustomJSs.size() > 0) {
                for (int i = 0; i < injectCustomJSs.size(); ++i) {
                    scriptBuilder.append(injectCustomJSs.get(i));
                }
            }
            scriptBuilder.append(VisitorClientCustomJS.chatReadyScript());
            scriptBuilder.append(VisitorClientCustomJS.hideCloseLinkScript());
            scriptBuilder.append("}");
            view.loadUrl("javascript:" + scriptBuilder.toString());
        }
    };


    public Common100WebView(@NonNull Context context) {
        super(context);
        this.mWebChromeClient = new ChatWindowWebChromeClient((Activity) context);
        this.initWebView();
    }

    public Common100WebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mWebChromeClient = new ChatWindowWebChromeClient((Activity) context);
        this.initWebView();
    }

    public Common100WebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mWebChromeClient = new ChatWindowWebChromeClient((Activity) context);
        this.initWebView();
    }

    public Common100WebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mWebChromeClient = new ChatWindowWebChromeClient((Activity) context);
        this.initWebView();
    }

    public Common100WebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        this.mWebChromeClient = new ChatWindowWebChromeClient((Activity) context);
        this.initWebView();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        this.setLayerType(2, (Paint)null);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccessFromFileURLs(true);

        settings.setAllowContentAccess(true);
//        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        WebView.setWebContentsDebuggingEnabled(true);

        settings.setSupportMultipleWindows(true);
        this.setWebChromeClient(this.mWebChromeClient);
        this.setWebViewClient(this.mWebViewClient);
        this.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void onChatReady(String server, int siteId, String guid) {
                VisitorClientCore.getInstance().chatReady(server, siteId, guid);
            }
        }, "chatOnReadyHandler");
    }


}
