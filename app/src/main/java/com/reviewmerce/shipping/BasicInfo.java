package com.reviewmerce.shipping;

/**
 * Created by onebuy on 2015-07-24.
 */
public class BasicInfo {
    public static String language = "";
    public static final String TAG = "SHIPPING";
    /**
     */
    public static String ExternalPath = "/sdcard/shipping/";
    public static String InternalPath = "/data/data/com.reviewmerce.shipping/";
    public static String BackgroudPath = "image/background/";

    public static String DATABASE_NAME = "shipping.db";
    public static final String LISTEDITEM_DBNAME = "listeddb.db";

    public static String INITFILE_NAME = "initOK.txt";

    public static final int TYPE_MONITOR = 1;

    public static int g_nMovePos = 80;
    //////////////////////////////////////////////////////////////////////////////

    public static final int FRAGMENT_NONE = 0;
    public static final int FRAGMENT_FIRST = 1;
    public static final int FRAGMENT_SHOPPING_ITEM_LIST = 2;
    public static final int FRAGMENT_SHOPPING_ITEM = 3;

//    public final static int THREAD_TYPE_HTTP_MAINPAGE = 0;
//    public final static int THREAD_TYPE_HTTP_CATEGORYDATA = 1;
//    public final static int THREAD_TYPE_HTTP_CATEGORYTOP = 11;
//    public final static int THREAD_TYPE_HTTP_CATEGORYBOTTOM = 12;
//    public final static int THREAD_TYPE_HTTP_SHOPPINGITEM = 2;
//    public final static int THREAD_TYPE_HTTP_PACKDATA = 3;

    public final static int THREAD_TYPE_HTTPREQ_MAINPAGE = 10;
    public final static int THREAD_TYPE_HTTPREQ_TOPPAGE = 11;
    public final static int THREAD_TYPE_HTTPREQ_BOTTOMPAGE = 12;
    public final static int THREAD_TYPE_HTTPREQ_BOTTOM_CATEGORYITEM = 121;
    public final static int THREAD_TYPE_HTTPREQ_BOTTOM_PACKITEM = 122;

    public final static int THREAD_TYPE_HTTPREQ_CATEGORYITEM = 20;
    public final static int THREAD_TYPE_HTTPREQ_PACKITEM = 30;

    public final static int THREAD_TYPE_HTTPREQ_TEMPLATE = 40;

}

