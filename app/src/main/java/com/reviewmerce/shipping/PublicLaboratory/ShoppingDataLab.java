package com.reviewmerce.shipping.PublicLaboratory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.reviewmerce.shipping.APIThread_shop;
import com.reviewmerce.shipping.BasicInfo;
import com.reviewmerce.shipping.CommonData.CategoryItem;
import com.reviewmerce.shipping.CommonData.PackItem;
import com.reviewmerce.shipping.CommonData.ShippingBasedItem;
import com.reviewmerce.shipping.CommonData.ShippingItem;
import com.reviewmerce.shipping.CommonData.TemplateItem;
import com.reviewmerce.shipping.Fragment.DbBasicLab;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by onebuy on 2015-09-07.
 */
public class ShoppingDataLab {

    private static ShoppingDataLab DataLab;
    private DisplayImageOptions mDisplayOptions;
    private String mTopItemId;
    private String mBottomItemId;
    private String mVersion="M1";
    private String mTag = "ShoppingDataLab";
    private String hostUrl = "https://3ycw9r0z65.execute-api.ap-northeast-1.amazonaws.com/v1/";
    public static String TOP_ID = "idtop01";
    public static String BOTTOM_ID = "idbottom01";
    private String mDbName = "shipping_main.db";
    private String mDbTable_info="info";
    //    static public String JSON_ID = "id";
//    static public String JSON_TYPE = "type";
//    static public String JSON_DATASET = "children";
    static public String JSON_TOPID = "top_id";
    static public String JSON_BOTTOMID = "bottom_id";
    static public String JSON_TEMPLATE = "template";
    static public String JSON_VERSION = "version";

    public static final String DATASET_TYPE_CATEGORYITEM = "C";
    public static final String DATASET_TYPE_CATEGORYPACK = "P";
    public static final String DATASET_TYPE_SHIPPINGITEM = "item";
    private PackDataLab mPackDataLab;
    private CateDataLab mCateDataLab;
    private DbBasicLab mDb;
    int mGetMainBottomCategoryCount = 0;
    private Handler mParentHandler;
    private TemplateItem mTemplateItem;
    public static ShoppingDataLab get(Context c) {
            if (DataLab == null) {
                DataLab = new ShoppingDataLab(c.getApplicationContext());
            }
            return DataLab;
    }
    ////////////////////////////////////////////////////////////////////////////

    private Context mAppContext;


    public DisplayImageOptions getDisplayOptions()
    {
        return mDisplayOptions;
    }
    public ShoppingDataLab(Context appContext) {
        if(appContext != null) {
            mAppContext = appContext;
            allocData();
            //initData();
        }
    }

    public String getTopItemId()
    {
        return mTopItemId;
    }
    public void setTopItemId(String sId)
    {
        mTopItemId = sId;
    }
    public String getBottomItemId()
    {
        return mBottomItemId;
    }
    public void setBottomItemId(String sId)
    {
        mBottomItemId = sId;
    }
    public TemplateItem getTemplateItem()
    {
        return mTemplateItem;
    }
    public PackDataList getTopItems()
    {
        PackDataList rtnData = mPackDataLab.getItem(mTopItemId);
        return rtnData;
    }
    public CategoryDataList getBottomItems()
    {
        CategoryDataList rtnData = mCateDataLab.getItem(mBottomItemId);
        return rtnData;
    }
    public PackDataLab getPackDataLab()
    {
        return mPackDataLab;
    }
    public CateDataLab getCateDataLab()
    {
        return mCateDataLab;
    }
    public void allocData()
    {
        mTemplateItem = new TemplateItem("1","http://asdf.com","assets://category/");
        mCateDataLab = new CateDataLab(mAppContext);
   //     mCateDataLab.allocData();
        mPackDataLab = new PackDataLab(mAppContext);
   //     mPackDataLab.allocData();
        mDisplayOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();
        mDb = new DbBasicLab(mAppContext);
        mDb.setTag("Shipping_BaseDb");
        mDb.setDbName(mDbName);

        try {
            mDb.openDatabase();
            mDb.createTable(getSql_createTable(mDbTable_info));
        } catch (Exception e) {

        }
    }
    public void release() throws Exception {
        mCateDataLab.release();
        mPackDataLab.release();
        mDb.closeDatabase();
    }
    public void initData()
    {
        mCateDataLab.initData();
        mPackDataLab.initData();
        mTopItemId = "";
        mBottomItemId = "";
    }

    public boolean loadData_all()
    {
        boolean rtn = true;
        if(load_Mainpage()==false)
            rtn =  false;
        if(mCateDataLab.loadData()==false)
            rtn = false;
        if(mPackDataLab.loadData()==false)
            rtn = false;
        return rtn;
    }
    public boolean load_Mainpage()
    {
        try
        {
            String sSql = "select * "
                    + "from " + mDbTable_info;// + " WHERE id=='"+"'";
            Cursor cursor = mDb.loadDatabase(sSql);
            JSONArray resultSet = new JSONArray();
            try {
                cursor.moveToFirst();
                mVersion = cursor.getString(0);
                mVersion = "M1";
                mTopItemId = cursor.getString(1);
                mBottomItemId = cursor.getString(2);
                JSONObject json = (JSONObject) new JSONTokener(cursor.getString(3)).nextValue();
                mTemplateItem.setData(json);
//                    cursor.moveToNext();
            }catch (Exception e)
            {
                Log.d(mTag, "cur2Data -> " + e.toString());
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }catch(Exception e)
        {
            Log.e(mTag, "loadMainPage() Error -> " + e.toString());
        }
        return false;
    }
    public boolean save_MainPage()
    {
        try {
            ContentValues cv = new ContentValues();
            cv.put(JSON_VERSION, mVersion);
            cv.put(JSON_TOPID, mTopItemId);
            cv.put(JSON_BOTTOMID, mBottomItemId);
            cv.put(JSON_TEMPLATE,mTemplateItem.toJson().toString());
            mDb.saveDatabase(mDbTable_info, cv);
            return true;

        } catch (Exception e) {

        }
        return false;
    }



    public ShippingBasedItem jsonParser_shippingitem(String sInput)
    {
        ShippingBasedItem rtnItem=null;
        try {
            JSONObject json  = (JSONObject) new JSONTokener(sInput).nextValue();
            String sType = json.getString(ShippingBasedItem.JSON_TYPE);
            if(sType.indexOf(DATASET_TYPE_CATEGORYITEM)>=0)
            {
                rtnItem = new CategoryItem(json);
            }
            else if(sType.indexOf(DATASET_TYPE_SHIPPINGITEM)>=0)
            {
                rtnItem = new ShippingItem(json);
            }
            else if(sType.indexOf(DATASET_TYPE_CATEGORYPACK)>=0)
            {
                rtnItem = new PackItem(json);
            }

        }catch (Exception e)
        {
            Log.e(mTag,"onCreateView-> " + e.toString());
        }
        return rtnItem;
    }
    public String getSql_createTable(String sTable)
    {
        String sRtn = "create table IF NOT EXISTS " + sTable + " ("
                + JSON_VERSION + " STRING NOT NULL PRIMARY KEY, "
                + JSON_TOPID + " STRING, "
                + JSON_BOTTOMID + " STRING, "
                + JSON_TEMPLATE + " STRING"
                + " );";
        return sRtn;
    }
    public void getTemplete_API(Handler handler)
    {
        mParentHandler = handler;
        APIThread_shop thread = new APIThread_shop(hostUrl, mAppContext, getMainPageInfoHandler, BasicInfo.THREAD_TYPE_HTTPREQ_TEMPLATE,mVersion,"", mTopItemId);
        thread.execute();
    }
    public void getMainPageInfo_API(Handler handler)
    {
        mParentHandler = handler;
        APIThread_shop thread = new APIThread_shop(hostUrl, mAppContext, getMainPageInfoHandler, BasicInfo.THREAD_TYPE_HTTPREQ_MAINPAGE,mVersion,"", "");
        thread.execute();
    }
    public void getMainPageData_API(Handler handler)
    {
        mParentHandler = handler;
        APIThread_shop thread = new APIThread_shop(hostUrl, mAppContext, getPackHandler, BasicInfo.THREAD_TYPE_HTTPREQ_TOPPAGE,mVersion,"", mTopItemId);
        thread.execute();
        thread = new APIThread_shop(hostUrl, mAppContext, getCateHandler, BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOMPAGE,mVersion,"", mBottomItemId);
        thread.execute();
    }
    public void getSubCategoryData_API(String sParentId)
    {
        CategoryDataList dataList = mCateDataLab.getItem(sParentId);
        mGetMainBottomCategoryCount = dataList.getDataList().size();
        for(int i=0;i<dataList.getDataList().size();i++) {

            String sId = dataList.getDataList().get(i).getId();
            if(dataList.getDataList().get(i).getType().indexOf(DATASET_TYPE_CATEGORYITEM)>=0)
            {
                APIThread_shop thread = new APIThread_shop(hostUrl, mAppContext, getCateHandler, BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOM_CATEGORYITEM, mVersion, sParentId, sId);
                thread.execute();
            }
            else if(dataList.getDataList().get(i).getType().indexOf(DATASET_TYPE_CATEGORYPACK)>=0)
            {
                APIThread_shop thread = new APIThread_shop(hostUrl, mAppContext, getPackHandler, BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOM_PACKITEM, mVersion, sParentId, sId);
                thread.execute();
            }
        }
    }
    Handler getTemplateInfoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


        }
    };
    Handler getMainPageInfoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case BasicInfo.THREAD_TYPE_HTTPREQ_MAINPAGE:
                    String sTop = msg.getData().getString("top");
                    String sBottom = msg.getData().getString("bottom");
                    if ((sTop != null) && (sTop.length() > 0))
                        mTopItemId = sTop;
                    if ((sBottom != null) && (sBottom.length() > 0))
                        mBottomItemId = sBottom;
                    Message sendMsg = mParentHandler.obtainMessage();
                    sendMsg.what = BasicInfo.THREAD_TYPE_HTTPREQ_MAINPAGE;
                    mParentHandler.sendMessage(sendMsg);
                    save_MainPage();
                    break;
                case BasicInfo.THREAD_TYPE_HTTPREQ_TEMPLATE:
                    try {
                        String sData = msg.getData().getString("data");
                        JSONObject json = (JSONObject) new JSONTokener(sData).nextValue();
                        mTemplateItem.setData(json);
                    }catch (Exception e)
                    {
                        Log.d(mTag,""+e.toString());
                    }
                    break;
            }
        }
    };
    Handler getCateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String sId = msg.getData().getString("id");
            String sParentId = msg.getData().getString("parent_id");
            String sData = msg.getData().getString("data");

            if (msg.arg1 == 1) // 성공
            {
                addCategoryDataList(sId, sData);

                if(msg.what == BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOMPAGE) {
                    getSubCategoryData_API(sId);
                }
                else if(msg.what == BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOM_CATEGORYITEM)
                {
                    mGetMainBottomCategoryCount--;
                    setParentItem_for_DestItemList(sParentId,sId);
                    if(mGetMainBottomCategoryCount<=0)
                    {

                        Message sendMsg = mParentHandler.obtainMessage();
                        sendMsg.what = BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOMPAGE;
                        mParentHandler.sendMessage(sendMsg);
                    }
                }
            }
            else
            {
            }
        }
    };
    Handler getPackHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String sId = msg.getData().getString("id");
            String sParentId = msg.getData().getString("parent_id");
            String sData = msg.getData().getString("data");

            if (msg.arg1 == 1) // 성공
            {
                addPackDataList(sId, sData);
                if(msg.what == BasicInfo.THREAD_TYPE_HTTPREQ_TOPPAGE)
                {
                    Message sendMsg = mParentHandler.obtainMessage();
                    sendMsg.what = BasicInfo.THREAD_TYPE_HTTPREQ_TOPPAGE;
                    mParentHandler.sendMessage(sendMsg);
                }

                else if(msg.what == BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOM_PACKITEM)
                {
                    mGetMainBottomCategoryCount--;
                    setParentItem_for_DestItemList(sParentId,sId);
                    if(mGetMainBottomCategoryCount<=0)
                    {
                        Message sendMsg = mParentHandler.obtainMessage();
                        sendMsg.what = BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOMPAGE;
                        mCateDataLab.saveData_pack(sParentId);
                        mParentHandler.sendMessage(sendMsg);

                    }
                }
            }
            else
            {
            }

        }
    };
    private void setParentItem_for_DestItemList(String sParentId, String sId)
    {
        ShippingBasedItem sParentItem = mCateDataLab.getItem(sParentId).getItem(sId);

        if(sParentItem.getType().indexOf(DATASET_TYPE_CATEGORYITEM)>=0)
        {
            CategoryItem srcItem = (CategoryItem)mCateDataLab.getItem(sId).getInfoItem();
            ((CategoryItem)sParentItem).setData(srcItem.getId(), srcItem.getName(),srcItem.getVer(),srcItem.getDescription(),DATASET_TYPE_CATEGORYITEM,"");
        }
        else if(sParentItem.getType().indexOf(DATASET_TYPE_CATEGORYPACK)>=0)
        {
            PackItem srcItem = (PackItem)mPackDataLab.getItem(sId).getInfoItem();
            ((PackItem)sParentItem).setData(srcItem.getId(), srcItem.getName(),srcItem.getVer(),srcItem.getDescription(),DATASET_TYPE_CATEGORYPACK,"");
                        //((PackItem)srcItem).setData((PackItem) destItem);
        }

//        if(mGetMainBottomCategoryCount<=0)
//        {
//            Message sendMsg = mParentHandler.obtainMessage();
//            sendMsg.what = BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOMPAGE;
//            mParentHandler.sendMessage(sendMsg);
//        }
    }
    private void addCategoryDataList(String sId, String sData)
    {
        CategoryDataList datalist = new CategoryDataList(mAppContext);
        datalist.setCategoryInfo(sData);
        mCateDataLab.putItem(datalist.getInfoItem().getId(), datalist);
        mCateDataLab.saveData_pack(datalist.getInfoItem().getId());
    }
    private void addPackDataList(String sId, String sData)
    {
        PackDataList datalist = new PackDataList(mAppContext);
        datalist.setPackInfo(sData);
        mPackDataLab.putItem(datalist.getInfoItem().getId(), datalist);
        mPackDataLab.saveData_pack(datalist.getInfoItem().getId());
    }
}
