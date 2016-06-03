package com.reviewmerce.shipping.PublicLaboratory;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.reviewmerce.shipping.BasicInfo;
import com.reviewmerce.shipping.CommonData.CategoryItem;
import com.reviewmerce.shipping.CommonData.PackItem;
import com.reviewmerce.shipping.CommonData.ShippingBasedItem;
import com.reviewmerce.shipping.CommonData.ShippingItem;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by onebuy on 2016-05-23.
 */
public class PackDataList {
    public static final int LOAD_TYPE_DB = 0;
    public static final int SAVE_TYPE_DB = 0;
//    public static final String DATASET_TYPE_CATEGORYINFO = "category";
    public static final String DATASET_TYPE_CATEGORYITEM = "cate";
    public static final String DATASET_TYPE_CATEGORYPACK = "pack";
    public static final String DATASET_TYPE_SHIPPINGITEM = "item";
    protected String mTag="CategoryDataList";
    //protected String mTableName = "";
    //private DbBasicLab mDb;

    PackItem mCategoryInfoItem;
    private List<ShippingItem> mItemList=null;
    private Context mAppContext;
    //private static CategoryDataList mCategoryDataLab;

    public PackDataList(Context context)
    {
        super();
        if(context != null) {
            mAppContext = context;
            try {
                alloc();
            }catch (Exception e)
            {
                Log.e(mTag, "initData Fail");
            }
        }
    }
    public void alloc() throws Exception {
        mCategoryInfoItem = new PackItem();
        mItemList = new ArrayList<>();

    }

    public void release() throws Exception {
        mItemList.clear();
    }

    public void loadData(HashMap<String, String> mapArg) throws Exception {

    }



    public void initData() throws Exception {
        if(mItemList!=null)
            mItemList.clear();

    }

    public void setPackInfo(String sJson)
    {
        try{
            //JSONArray arrayList = (JSONArray)new JSONTokener(sJson.toString()).nextValue();
            JSONObject json = (JSONObject)new JSONTokener(sJson).nextValue();
            String sDataset;
            mCategoryInfoItem = new PackItem(json);
            sDataset = ((PackItem)mCategoryInfoItem).getDataSet();

            if(sDataset.length()>0)
            {
                addItemJsonArray(sDataset);
            }

        }catch(Exception e)
        {
            Log.e(mTag,"setCategoryInfo Error -> " + e.toString());
        }
    }

    public void addItemJsonArray(String sArray) throws Exception {
        JSONArray arrayList = (JSONArray)new JSONTokener(sArray.toString()).nextValue();
        for(int i=0; i<arrayList.length();i++) {
            JSONObject json = arrayList.getJSONObject(i);

           // String sType = json.getString("type");
            ShippingItem item=null;
            //if (sType.indexOf(DATASET_TYPE_SHIPPINGITEM) >= 0)
            {
                item = new ShippingItem(json);
                item.setType(DATASET_TYPE_SHIPPINGITEM);
            }

            mItemList.add(item);

            //mDataList.add(item);
        }
    }

    public void removeData(int nIndex) throws Exception {
        mItemList.remove(nIndex);
    }
    public ShippingItem getItem(String sId)
    {
        for(ShippingItem item : mItemList)
        {
            if(item.getId().indexOf(sId)>=0)
                return item;
        }
        return null;
    }


    public void clearData() throws Exception {
        initData();
        mItemList = null;
        mCategoryInfoItem = null;
    }

    public List<ShippingItem> getDataList() {
        return mItemList;
    }
    public PackItem getInfoItem()
    {
        return mCategoryInfoItem;
    }


    public JSONObject getDummyItem() {
        CategoryItem itemRoot = new CategoryItem("top_category", "MAIN BOTTOM", "0.1.1", "설명1", "cate" ,"");
        ShippingItem item1 = new ShippingItem("wood_toy", "아이템", "asin00001","http://shipping-webpage.s3-website.ap-northeast-2.amazonaws.com/?asin=B00JI2APZQ", DATASET_TYPE_SHIPPINGITEM);
        ShippingItem item2 = new ShippingItem("lego", "레고", "asin00002", "http://shipping-webpage.s3-website.ap-northeast-2.amazonaws.com/?asin=B00JI2APZQ",DATASET_TYPE_SHIPPINGITEM);
        ShippingItem item3 = new ShippingItem("board_game", "보드게임","asin00003","http://shipping-webpage.s3-website.ap-northeast-2.amazonaws.com/?asin=B00JI2APZQ", DATASET_TYPE_SHIPPINGITEM);

        ShippingItem item4 = new ShippingItem("figure", "피규어", "asin00004", "http://shipping-webpage.s3-website.ap-northeast-2.amazonaws.com/?asin=B00JI2APZQ",DATASET_TYPE_SHIPPINGITEM);
        ShippingItem item5 = new ShippingItem("dolls", "인형", "asin00005","http://shipping-webpage.s3-website.ap-northeast-2.amazonaws.com/?asin=B00JI2APZQ", DATASET_TYPE_SHIPPINGITEM);
        ShippingItem item6 = new ShippingItem("education", "유아도구", "asin00006","http://shipping-webpage.s3-website.ap-northeast-2.amazonaws.com/?asin=B00JI2APZQ", DATASET_TYPE_SHIPPINGITEM);

        try {
            JSONArray jArrayCate = new JSONArray();
            JSONObject rtnItem = new JSONObject();
            JSONObject json;
            rtnItem = itemRoot.toJson();
            jArrayCate.put(item1.toJson());
            jArrayCate.put(item2.toJson());
            jArrayCate.put(item3.toJson());
            jArrayCate.put(item4.toJson());
            jArrayCate.put(item5.toJson());
            jArrayCate.put(item6.toJson());

            rtnItem.put("children", jArrayCate.toString());
            return rtnItem;

        } catch (Exception e) {
            Log.e(mTag, "getBestItem Error -> " + e.toString());
        }
        return null;
    }
    public JSONObject toJson()
    {
        try {
            JSONArray jArrayCate = new JSONArray();
            JSONObject rtnItem;
            rtnItem = mCategoryInfoItem.toJson();
            for(ShippingItem item : mItemList)
            {
                jArrayCate.put(item.toJson());
            }
            rtnItem.put(PackItem.JSON_DATASET, jArrayCate.toString());
            return rtnItem;

        } catch (Exception e) {
            Log.e(mTag, "toJson Error -> " + e.toString());
        }
        return null;
    }
    public void getListItem_to_http()
    {


    }
    public void setData(ShippingBasedItem srcItem, ShippingBasedItem destItem)
    {
        if(srcItem.getType().indexOf(DATASET_TYPE_CATEGORYITEM)>=0)
        {
            ((CategoryItem)destItem).setData((CategoryItem) srcItem);
        }
        else if(srcItem.getType().indexOf(DATASET_TYPE_CATEGORYPACK)>=0)
        {
            ((PackItem)destItem).setData((PackItem) srcItem);
        }
        else if(srcItem.getType().indexOf(DATASET_TYPE_SHIPPINGITEM)>=0)
        {
            ((ShippingItem)destItem).setData((ShippingItem)srcItem);
        }
        else
        {
            destItem.setData(srcItem);
        }
    }
    Handler getHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BasicInfo.THREAD_TYPE_HTTPREQ_PACKITEM:
                    if (msg.arg1 == 1) // 성공
                    {

                    }
                    break;

            }
        }
    };

}
