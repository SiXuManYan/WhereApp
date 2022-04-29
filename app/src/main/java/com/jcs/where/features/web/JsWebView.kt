package com.jcs.where.features.web

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AlertDialog
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.EditText
import android.widget.FrameLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

/**
 * 支持JavaScript的WebView
 */
@SuppressLint("SetJavaScriptEnabled")
open class JsWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : WebView(context, attrs) {

    private var appCacheDirName: String? = null
    private var callID = 0

    @Volatile
    private var alertBoxBlock = true
    private var javascriptCloseWindowListener: JavascriptCloseWindowListener? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    public var mChromeClient: WebChromeClient? = null

    private var handlerList = SparseArray<JsHandler<*>>()

    init {
        appCacheDirName = context?.filesDir?.absolutePath + "/webcache"

        settings.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        settings.allowFileAccess = false
        settings.setAppCacheEnabled(false)
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.blockNetworkImage = false
        settings.setAppCachePath(appCacheDirName)
//        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        //        settings.setUseWideViewPort(true);
        webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (mChromeClient != null) {
                    mChromeClient!!.onProgressChanged(view, newProgress)
                } else {
                    super.onProgressChanged(view, newProgress)
                }
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                if (mChromeClient != null) {
                    mChromeClient!!.onReceivedTitle(view, title)
                } else {
                    super.onReceivedTitle(view, title)
                }
            }

            override fun onReceivedIcon(view: WebView, icon: Bitmap) {
                if (mChromeClient != null) {
                    mChromeClient!!.onReceivedIcon(view, icon)
                } else {
                    super.onReceivedIcon(view, icon)
                }
            }

            override fun onReceivedTouchIconUrl(view: WebView, url: String, precomposed: Boolean) {
                if (mChromeClient != null) {
                    mChromeClient!!.onReceivedTouchIconUrl(view, url, precomposed)
                } else {
                    super.onReceivedTouchIconUrl(view, url, precomposed)
                }
            }

            override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
                if (mChromeClient != null) {
                    mChromeClient!!.onShowCustomView(view, callback)
                } else {
                    super.onShowCustomView(view, callback)
                }
            }

            override fun onHideCustomView() {
                if (mChromeClient != null) {
                    mChromeClient!!.onHideCustomView()
                } else {
                    super.onHideCustomView()
                }
            }

            override fun onCreateWindow(view: WebView, isDialog: Boolean,
                                        isUserGesture: Boolean, resultMsg: Message): Boolean {
                return if (mChromeClient != null) {
                    mChromeClient!!.onCreateWindow(view, isDialog,
                            isUserGesture, resultMsg)
                } else super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
            }

            override fun onRequestFocus(view: WebView) {
                if (mChromeClient != null) {
                    mChromeClient!!.onRequestFocus(view)
                } else {
                    super.onRequestFocus(view)
                }
            }

            override fun onCloseWindow(window: WebView) {
                if (mChromeClient != null) {
                    mChromeClient!!.onCloseWindow(window)
                } else {
                    super.onCloseWindow(window)
                }
            }

            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                if (!alertBoxBlock) {
                    result.confirm()
                }
                if (mChromeClient != null) {
                    if (mChromeClient!!.onJsAlert(view, url, message, result)) {
                        return true
                    }
                }
                val alertDialog = AlertDialog.Builder(getContext()).setMessage(message).setCancelable(false).setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    if (alertBoxBlock) {
                        result.confirm()
                    }
                }.create()
                alertDialog.show()
                return true
            }

            override fun onJsConfirm(view: WebView, url: String, message: String,
                                     result: JsResult): Boolean {
                if (!alertBoxBlock) {
                    result.confirm()
                }
                if (mChromeClient != null && mChromeClient!!.onJsConfirm(view, url, message, result)) {
                    return true
                } else {
                    val listener = DialogInterface.OnClickListener { dialog, which ->
                        if (alertBoxBlock) {
                            if (which == Dialog.BUTTON_POSITIVE) {
                                result.confirm()
                            } else {
                                result.cancel()
                            }
                        }
                        dialog.dismiss()
                    }
                    AlertDialog.Builder(getContext())
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, listener)
                            .setNegativeButton(android.R.string.cancel, listener).show()
                    return true

                }
            }

            override fun onJsPrompt(view: WebView, url: String, message: String,
                                    defaultValue: String?, result: JsPromptResult): Boolean {
                if (!alertBoxBlock) {
                    result.confirm()
                }

                if (mChromeClient != null && mChromeClient!!.onJsPrompt(view, url, message, defaultValue, result)) {
                    return true
                } else {
                    val editText = EditText(getContext())
                    editText.setText(defaultValue)
                    if (defaultValue != null) {
                        editText.setSelection(defaultValue.length)
                    }
                    val dpi = getContext().resources.displayMetrics.density
                    val listener = DialogInterface.OnClickListener { dialog, which ->
                        if (alertBoxBlock) {
                            if (which == Dialog.BUTTON_POSITIVE) {
                                result.confirm(editText.text.toString())
                            } else {
                                result.cancel()
                            }
                        }
                    }
                    AlertDialog.Builder(getContext())
                            .setTitle(message)
                            .setView(editText)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, listener)
                            .setNegativeButton(android.R.string.cancel, listener)
                            .show()
                    val layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    val t = (dpi * 16).toInt()
                    layoutParams.setMargins(t, 0, t, 0)
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL
                    editText.layoutParams = layoutParams
                    val padding = (15 * dpi).toInt()
                    editText.setPadding(padding - (5 * dpi).toInt(), padding, padding, padding)
                    return true
                }

            }

            override fun onJsBeforeUnload(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return if (mChromeClient != null) {
                    mChromeClient!!.onJsBeforeUnload(view, url, message, result)
                } else super.onJsBeforeUnload(view, url, message, result)
            }

            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                if (mChromeClient != null) {
                    mChromeClient!!.onGeolocationPermissionsShowPrompt(origin, callback)
                } else {
                    super.onGeolocationPermissionsShowPrompt(origin, callback)
                }
            }

            override fun onGeolocationPermissionsHidePrompt() {
                if (mChromeClient != null) {
                    mChromeClient!!.onGeolocationPermissionsHidePrompt()
                } else {
                    super.onGeolocationPermissionsHidePrompt()
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionRequest(request: PermissionRequest) {
                if (mChromeClient != null) {
                    mChromeClient!!.onPermissionRequest(request)
                } else {
                    super.onPermissionRequest(request)
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onPermissionRequestCanceled(request: PermissionRequest) {
                if (mChromeClient != null) {
                    mChromeClient!!.onPermissionRequestCanceled(request)
                } else {
                    super.onPermissionRequestCanceled(request)
                }
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                return if (mChromeClient != null) {
                    mChromeClient!!.onConsoleMessage(consoleMessage)
                } else super.onConsoleMessage(consoleMessage)
            }

            override fun getDefaultVideoPoster(): Bitmap? {

                return if (mChromeClient != null) {
                    mChromeClient!!.defaultVideoPoster
                } else super.getDefaultVideoPoster()
            }

            override fun getVideoLoadingProgressView(): View? {
                return if (mChromeClient != null) {
                    mChromeClient!!.videoLoadingProgressView
                } else super.getVideoLoadingProgressView()
            }

            override fun getVisitedHistory(callback: ValueCallback<Array<String>>) {
                if (mChromeClient != null) {
                    mChromeClient!!.getVisitedHistory(callback)
                } else {
                    super.getVisitedHistory(callback)
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                                           fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
                return if (mChromeClient != null) {
                    mChromeClient!!.onShowFileChooser(webView, filePathCallback, fileChooserParams)
                } else super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            }
        }
    }

    interface JavascriptCloseWindowListener {
        /**
         * @return If true, close the current activity, otherwise, do nothing.
         */
        fun onClose(): Boolean
    }

//    private fun parseNamespace(method: String): Array<String> {
//        val pos = method.lastIndexOf('.')
//        var namespace = ""
//        if (pos != -1) {
//            namespace = method.substring(0, pos)
//            method = method.substring(pos + 1)
//        }
//        return arrayOf(namespace, method)
//    }

    interface JsHandler<T> {
        fun callback(retValue: T)
    }

    /**
     * This method can be called in any thread, and if it is not called in the main thread,
     * it will be automatically distributed to the main thread.
     *
     * @param script
     */
    fun evaluateJavascript(script: String) {
        runOnMainThread(Runnable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                super@JsWebView.evaluateJavascript(script, null)
            } else {
                super.loadUrl("javascript:$script")
            }
        })
    }

    /**
     * This method can be called in any thread, and if it is not called in the main thread,
     * it will be automatically distributed to the main thread.
     *
     * @param url
     */
    override fun loadUrl(url: String) {
        runOnMainThread(Runnable {
            super@JsWebView.loadUrl(url)
        })
    }

    /**
     * This method can be called in any thread, and if it is not called in the main thread,
     * it will be automatically distributed to the main thread.
     *
     * @param url
     * @param additionalHttpHeaders
     */
    override fun loadUrl(url: String, additionalHttpHeaders: Map<String, String>) {
        runOnMainThread(Runnable {
            super@JsWebView.loadUrl(url, additionalHttpHeaders)
        })
    }

    override fun reload() {
        runOnMainThread(Runnable {
            super@JsWebView.reload()
        })
    }

    /**
     * set a listener for javascript closing the current activity.
     */
    fun setJavascriptCloseWindowListener(listener: JavascriptCloseWindowListener) {
        javascriptCloseWindowListener = listener
    }

    private class CallInfo internal constructor(private val method: String, val callbackId: Int, var args: Array<String>?) {
        private val data: String

        init {
            if (args == null) args = arrayOf()
            data = JSONArray(Arrays.asList(*args!!)).toString()
        }

        override fun toString(): String {
            val jo = JSONObject()
            try {
                jo.put("method", method)
                jo.put("callbackId", callbackId)
                jo.put("data", data)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return jo.toString()
        }
    }

    private fun dispatchJavascriptCall(info: CallInfo) {
        evaluateJavascript(String.format("window._handleMessageFromNative(%s)", info.toString()))
    }

    @Synchronized
    fun <T> callHandler(method: String, args: Array<String>?, handler: JsHandler<T>?) {

        val callInfo = CallInfo(method, ++callID, args)
        if (handler != null) {
            handlerList.put(callInfo.callbackId, handler)
        }

        dispatchJavascriptCall(callInfo)

    }

    fun callHandler(method: String, args: Array<String>) {
        callHandler<Any>(method, args, null)
    }

    fun <T> callHandler(method: String, handler: JsHandler<T>) {
        callHandler(method, null, handler)
    }


    /**
     * Test whether the handler exist in javascript
     *
     * @param handlerName
     * @param existCallback
     */
    fun hasJavascriptMethod(handlerName: String, existCallback: JsHandler<Boolean>) {
        callHandler("_hasJavascriptMethod", arrayOf(handlerName), existCallback)
    }

    fun disableJavascriptDialogBlock(disable: Boolean) {
        alertBoxBlock = !disable
    }

    override fun clearCache(includeDiskFiles: Boolean) {
        super.clearCache(includeDiskFiles)
        CookieManager.getInstance().removeAllCookie()
        val context = context
        try {
            context.deleteDatabase("webview.db")
            context.deleteDatabase("webviewCache.db")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val appCacheDir = File(appCacheDirName)
        val webviewCacheDir = File(context.cacheDir
                .absolutePath + "/webviewCache")

        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir)
        }

        if (appCacheDir.exists()) {
            deleteFile(appCacheDir)
        }
    }

    fun deleteFile(file: File) {
        if (file.exists()) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                val files = file.listFiles()
                for (f in files) {
                    deleteFile(f)
                }
            }
            file.delete()
        } else {
            Log.e("Webview", "delete file no exists " + file.absolutePath)
        }
    }

    private fun runOnMainThread(runnable: Runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run()
            return
        }
        mainHandler.post(runnable)
    }

    companion object {
        const val BRIDGE_NAME = "whereapp"
    }
}
