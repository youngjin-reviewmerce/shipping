package com.reviewmerce.shipping.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.shipping.AnalyticsApplication;
import com.reviewmerce.shipping.CommonData.ShippingBasedItem;
import com.reviewmerce.shipping.CommonData.ShippingItem;
import com.reviewmerce.shipping.MainActivity;
import com.reviewmerce.shipping.PublicLaboratory.ShoppingDataLab;
import com.reviewmerce.shipping.R;

public class ShoppingItemFragment extends Fragment
//        implements LoaderManager.LoaderCallbacks<Cursor>
{
    private FragmentInterface.BasicImplement mCallback;
    private int mArgType=0;
    String mTag = "ShoppingItemFragment";
    private int mTestValue;
    //ShoppingDataLab mDataLab;
    ShippingBasedItem mItem;
    ShoppingDataLab mDataLab;
    private final Handler mHandlerWebOrder = new Handler();
    public WebView mWebview;
    private Tracker mTracker;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mCallback = (FragmentInterface.BasicImplement) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.webitem_fragment, container, false);
        Bundle args = getArguments();
        mArgType = args.getInt("type");
        String sItemJson = args.getString("data");
        mDataLab = ShoppingDataLab.get(null);
        mItem = mDataLab.jsonParser_shippingitem(sItemJson);

        //mDataLab = ShoppingDataLab.get(null);
        //mItem = mDataLab.getItem(mDataId);
        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        mTracker.setScreenName("Screen" + mTag + "  id : "+mItem.getId());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mWebview = (WebView)rootView.findViewById(R.id.webview_main);
        initWebView();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initWebView()
    {
        //javascript의 window.open 허용
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//javascript 허용
//스크립트 확장
        //mWebviewBottom.addJavascriptInterface(new AndroidBridge(), "HybridApp");
//meta태그의 viewport사용 가능
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        // 아래는 enable cross-origin resource sharing 때문에 추가
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebview.getSettings().setAllowFileAccessFromFileURLs(true);
            mWebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        AndroidBridge bridge = new AndroidBridge();
        mWebview.addJavascriptInterface(new JavaScriptInterface(getContext()),"cat_message");

        // 아래는 https block 문제때문에 추가 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mWebview, true);
        }
        String url = mDataLab.getTemplateItem().getLinkUrlPrefix() + ((ShippingItem)mItem).getLinkUrl();
        mWebview.loadUrl(url);
        //   mWebviewTop.setWebViewClient(new MyWebViewClient());
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        mWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //This is the filter
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        ((MainActivity)getActivity()).onBackPressed();
                    }

                    return true;
                }

                return false;
            }
        });
    }
    public class MyWebViewClient extends WebViewClient {
        boolean timeout;

        public MyWebViewClient() {
            timeout = true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(timeout) {
                        Toast.makeText(getContext(), "Time-out", Toast.LENGTH_SHORT);
                        // do what you want
                    }
                }
            }).start();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            timeout = false;
        }
    }
    private class AndroidBridge {
        public void getJavascriptMessage(final String msg){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("bridge", "Webview Message :: " + msg);
                }
            });
        }
        public void setMessage(final String arg) { // must be final
            mHandlerWebOrder.post(new Runnable() {
                public void run() {
                    Log.d("bridge", "setMessage(" + arg + ")");
                    Toast.makeText(getActivity(),arg,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //HTML 문서 내에서 JavaScript로 호출가능한 함수
    //브라우저에서 load가 완료되었을 때 호출하는 함수
    public class JavaScriptInterface {
        Context context = null;

        JavaScriptInterface(Context aContext) {
            context = aContext;
        }

        @JavascriptInterface
        public void setMessage(final String msg) {
            Log.d("HybridApp", "setMessage(" + msg + ")");
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }

        //HTML 문서 내에서 JavaScript로 호출가능한 함수
        //asset의 파일을 로컬에 저장
        @JavascriptInterface
        public void getJavascriptMessage(final String msg) {
            Log.d("HybridApp", "getJavascriptMessage(" + msg + ")");
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void setShippingAgency(final int nIndex, final String sAddress) {
            String msg = "getJavascriptMessage(" + String.valueOf(nIndex) + " >> " + sAddress +")";
            Log.d("HybridApp", msg);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }//window.cat_message.onSelectAppAddress
        @JavascriptInterface
        public void onSelectAppAddress(final int nIndex) {
            //runSelectAddressDialog();
        }
    }
    public final class ChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            return true;
        }
    }
}