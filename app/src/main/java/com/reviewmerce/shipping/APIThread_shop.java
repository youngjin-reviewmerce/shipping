package com.reviewmerce.shipping;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.reviewmerce.shipping.CommonData.CategoryItem;
import com.reviewmerce.shipping.CommonData.PackItem;
import com.reviewmerce.shipping.PublicLaboratory.ShoppingDataLab;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by onebuy on 2015-07-30.
 */
public class APIThread_shop extends AsyncTask {

    // http://api.reviewmerce.com/v1/ui/main
    // http://api.reviewmerce.com/v1/ui/top?current_ui_ver=
    // http://api.reviewmerce.com/v1/ui/mid?current_ui_ver=
    // http://api.reviewmerce.com/v1/ui/bot?current_ui_ver=
    // http://api.reviewmerce.com/v1/search/query?keywords=
    // http://api.reviewmerce.com/v1/search/suggest?keywords=


    String urlStr;
    Handler mParentHandler;
    Context mParentContext = null;
    //int m_nDays = 0;
    int m_nPort = 5000; // 5050 : 개발용 5000 실서버
   // String mHostUrl = "3ycw9r0z65.execute-api.ap-northeast-1.amazonaws.com";//https://5fhdvexrml.execute-api.ap-northeast-1.amazonaws.com"; // http://api.reviewmerce.com";
    String mVer = "v1";
    String mParentId;
    private ShoppingDataLab mDataLab;
    private Bitmap bmImg;
    private int mType=0;
    private int mProcType = 0;
    private String mSearchId;
    private String mVersion;
    public APIThread_shop(String inStr, Context ctx, Handler handler, int nProcType,String sVersion, String sParentId, String sId) {
        mParentId = sParentId;
        urlStr = inStr;
        mParentContext = ctx;
        mParentHandler = handler;
        mProcType = nProcType;
        mDataLab = ShoppingDataLab.get(null);
        mType = nProcType;
        mSearchId = sId;
        mVersion = sVersion;
    }
    public void run() {
        long t = System.currentTimeMillis();
        switch (mProcType) {

            case BasicInfo.THREAD_TYPE_HTTPREQ_PACKITEM:
            case BasicInfo.THREAD_TYPE_HTTPREQ_TOPPAGE:
            case BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOM_PACKITEM:
                procAPI_getPackItem();
                break;
            case BasicInfo.THREAD_TYPE_HTTPREQ_CATEGORYITEM:
            case BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOMPAGE:
            case BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOM_CATEGORYITEM:
                procAPI_getCategoryItem();
                break;
            case BasicInfo.THREAD_TYPE_HTTPREQ_MAINPAGE:
                procAPI_getMainpage();
                break;
            case BasicInfo.THREAD_TYPE_HTTPREQ_TEMPLATE:
                procAPI_getTemplate();
                break;
            default:
                break;

        }
        long t2 = System.currentTimeMillis();
        String s = String.format("%d ms", t2 - t);
        Log.i("Network : ", s);
    }
    private boolean procAPI_getShoppingItem()
    {
        Message msg = mParentHandler.obtainMessage();
        msg.what = mProcType;
        try {
            String sRequest = urlStr + "/app/";
            String sType = "ui";
            String sOperator = "main";
            sRequest = sRequest + sType + "/" + sOperator; //+ "?current_ui_ver=" + mSearchKeyword;

            String sResponse = "";
            sResponse = doHttp_get(sRequest);
            String sId = "";
            if(sResponse.length()>0)
            {
                Bundle data = new Bundle();
                data.putString("id", sId);
                data.putString("parent_id", mParentId);
                msg.setData(data);
                msg.arg1 = 1;
            }
            else    // 더미 데이터 생성
            {
                msg.arg1 = 0;
            }
            mParentHandler.sendMessage(msg);
            return true;
        }
        catch (Exception ex) {
            msg.arg1 = 0;
            mParentHandler.sendMessage(msg);

            ex.printStackTrace();
            return false;
        }
    }
    private boolean procAPI_getCategoryItem()
    {
        Message msg = mParentHandler.obtainMessage();
        msg.what = mProcType;
        try {
            String sRequest = urlStr + "app";
            String sType = "data";
            String sOperator = "cate";
            String sOption = String.format("version=%s&id=%s",mVersion,mSearchId);
            sRequest = sRequest + "/" + sType + "/" + sOperator+"?"+sOption; //+ "?current_ui_ver=" + mSearchKeyword;

            String sResponse = "";
            sResponse = doHttp_get(sRequest);

            if(sResponse.length()>0)
            {
                Bundle data = new Bundle();
                String sId = "";
                JSONObject json = (JSONObject)new JSONTokener(sResponse).nextValue();
                sId = json.getString(CategoryItem.JSON_ID);
                data.putString("id", sId);
                data.putString("parent_id", mParentId);
                data.putString("data",sResponse);
                msg.setData(data);
                //if(mProcType == BasicInfo.THREAD_TYPE_HTTPREQ_TOPPAGE)
                msg.arg1 = 1;
            }
            else    // 더미 데이터 생성
            {
                msg.arg1 = 0;
            }
            mParentHandler.sendMessage(msg);
            return true;
        }
        catch (Exception ex) {
            msg.arg1 = 0;
            mParentHandler.sendMessage(msg);
            ex.printStackTrace();
            return false;
        }
    }
    private boolean procAPI_getPackItem()
    {
        Message msg = mParentHandler.obtainMessage();
        msg.what = mProcType;
        try {
            String sRequest = urlStr + "app";
            String sType = "data";
            String sOperator = "pack";
            String sOption = String.format("version=%s&id=%s&page=%s&page_items=%s",mVersion,mSearchId,"0","30");
            sRequest = sRequest + "/" + sType + "/" + sOperator+"?"+sOption; //+ "?current_ui_ver=" + mSearchKeyword;

            String sResponse = "";
            sResponse = doHttp_get(sRequest);

            if(sResponse.length()>0)
            {
                Bundle data = new Bundle();
                String sId = "";
                JSONObject json = (JSONObject)new JSONTokener(sResponse).nextValue();
                sId = json.getString(PackItem.JSON_ID);
                data.putString("id", sId);
                data.putString("parent_id", mParentId);
                data.putString("data",sResponse);
                msg.setData(data);
                //if(mProcType == BasicInfo.THREAD_TYPE_HTTPREQ_TOPPAGE)
                msg.arg1 = 1;
            }
            else    // 더미 데이터 생성
            {
                msg.arg1 = 0;
            }
            mParentHandler.sendMessage(msg);
            return true;
        }
        catch (Exception ex) {
            msg.arg1 = 0;
            mParentHandler.sendMessage(msg);
            ex.printStackTrace();
            return false;
        }
    }
    private boolean procAPI_getMainpage()
    {
        Message msg = mParentHandler.obtainMessage();
        msg.what = mProcType;
        try {
            String sRequest = urlStr + "app" ;
            String sType = "ui";
            String sOperator = "main";
            String sOption = String.format("version=%s",mVersion);
            sRequest = sRequest +"/"+ sType + "/" + sOperator+"?"+sOption; //+ "?current_ui_ver=" + mSearchKeyword;

            //sRequest = "https://ksmh2ahrej.execute-api.ap-northeast-1.amazonaws.com/M12/currency";
            String sResponse = "";
            sResponse = doHttp_get(sRequest);
            if(sResponse.length()>0)
            {
                JSONObject json =(JSONObject)new JSONTokener(sResponse).nextValue();
                String sTopId = json.getString("top");
                String sBotId = json.getString("bottom");
                Bundle data = new Bundle();
                data.putString("top", sTopId);
                data.putString("bottom", sBotId);
                msg.setData(data);
                msg.arg1 = 1;

            }
            else
            {
                msg.arg1 = 0;
            }
            mParentHandler.sendMessage(msg);
            return true;
        }
        catch (Exception ex) {
            Bundle data = new Bundle();
            data.putString("top", ShoppingDataLab.TOP_ID);
            data.putString("bottom", ShoppingDataLab.BOTTOM_ID);
            msg.arg1 = 0;
            mParentHandler.sendMessage(msg);

            ex.printStackTrace();
            return false;
        }
    }
    private boolean procAPI_getTemplate()
    {
        Message msg = mParentHandler.obtainMessage();
        msg.what = mProcType;
        try {
            String sRequest = urlStr + "app" ;
            String sType = "ui";
            String sOperator = "template";
            String sOption = String.format("version=%s",mVersion);
            sRequest = sRequest +"/"+ sType + "/" + sOperator+"?"+sOption; //+ "?current_ui_ver=" + mSearchKeyword;

            //sRequest = "https://ksmh2ahrej.execute-api.ap-northeast-1.amazonaws.com/M12/currency";
            String sResponse = "";
            sResponse = doHttp_get(sRequest);
            if(sResponse.length()>0)
            {
                Bundle data = new Bundle();
                data.putString("data", sResponse);
                msg.setData(data);
                msg.arg1 = 1;
            }
            else
            {
                msg.arg1 = 0;
            }
            mParentHandler.sendMessage(msg);
            return true;
        }
        catch (Exception ex) {
            Bundle data = new Bundle();
            data.putString("top", ShoppingDataLab.TOP_ID);
            data.putString("bottom", ShoppingDataLab.BOTTOM_ID);
            msg.arg1 = 0;
            mParentHandler.sendMessage(msg);

            ex.printStackTrace();
            return false;
        }
    }
    private String doHttp_post(String urlStr, JSONObject json) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        String response="";

        //HttpPost httpPost = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(4 * 1000);
            conn.setReadTimeout(6 * 1000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            os = conn.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();
                response = new String(byteData);
            }
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }
        return response;
    }
    private String doHttp_get(String urlStr) throws Exception {

        URL url = new URL(urlStr);
        //URL url = new URL(sEncode);
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        String response="";

        //HttpPost httpPost = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(1 * 1000);
            conn.setReadTimeout(1 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-api-key", "I1JQ1sWgB1nu0mY94Xo59amjhmpBSGm2fAi9Uiee");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            os = conn.getOutputStream();
//            os.write(json.toString().getBytes());
//            os.flush();

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();
                response = new String(byteData);
            }
        } catch (Exception ex) {
            Log.e(BasicInfo.TAG, "Exception in processing response.", ex);
        }
        return response;
    }

    protected Object doInBackground(Object[] params) {
        run();
        return null;
    }
}

