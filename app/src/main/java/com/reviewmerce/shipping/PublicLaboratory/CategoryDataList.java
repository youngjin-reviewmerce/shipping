package com.reviewmerce.shipping.PublicLaboratory;

import android.content.Context;
import android.util.Log;

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
public class CategoryDataList {
    public static final int LOAD_TYPE_DB = 0;
    public static final int SAVE_TYPE_DB = 0;
//    public static final String DATASET_TYPE_CATEGORYINFO = "category";
    public static final String DATASET_TYPE_CATEGORYITEM = "C";
    public static final String DATASET_TYPE_CATEGORYPACK = "P";
    public static final String DATASET_TYPE_SHIPPINGITEM = "item";
    protected String mTag="CategoryDataList";
    //protected String mTableName = "";
    //private DbBasicLab mDb;

    CategoryItem mCategoryInfoItem;
    private List<ShippingBasedItem> mItemList=null;
    private Context mAppContext;
    //private static CategoryDataList mCategoryDataLab;

    public CategoryDataList(Context context)
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
        mCategoryInfoItem = new CategoryItem();
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

    public void setCategoryInfo(String sJson)
    {
        try{
            //JSONArray arrayList = (JSONArray)new JSONTokener(sJson.toString()).nextValue();
            JSONObject json = (JSONObject)new JSONTokener(sJson).nextValue();
            String sDataset;
            mCategoryInfoItem = new CategoryItem(json);
            sDataset = ((CategoryItem)mCategoryInfoItem).getDataSet();


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
            String sType = "";
            try {
                sType = json.getString(CategoryItem.JSON_TYPE);
            }catch(Exception e){}
            try {
                sType = json.getString(PackItem.JSON_TYPE);
            }catch(Exception e){}
            ShippingBasedItem item=null;
            if (sType.indexOf(DATASET_TYPE_CATEGORYPACK) >= 0) {
                item = new PackItem(json);
                item.setType(sType);
            }
            else if (sType.indexOf(DATASET_TYPE_CATEGORYITEM) >= 0) {
                item = new CategoryItem(json);
                item.setType(sType);
            }

            mItemList.add(item);
            //mDataList.add(item);
        }
    }

    public void removeData(int nIndex) throws Exception {
        mItemList.remove(nIndex);
    }
    public ShippingBasedItem getItem(String sId)
    {
        for(ShippingBasedItem item : mItemList)
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

    public List<ShippingBasedItem> getDataList() {
        return mItemList;
    }
    public ShippingBasedItem getInfoItem()
    {
        return mCategoryInfoItem;
    }
    public JSONObject getCategory() {
        CategoryItem itemRoot = new CategoryItem("bottom_category","MAIN BOTTOM","0.1.1","설명1","cate","");
        CategoryItem item1 = new CategoryItem("wood_toy","목제장난감","0.1.1","설명1","cate","");
        CategoryItem item2 = new CategoryItem("lego","레고","0.1.1","설명1","cate","");
        CategoryItem item3 = new CategoryItem("board_game","보드게임","0.1.1","설명1","cate","");

        PackItem item4 = new PackItem("figure","피규어","0.1.1","설명1","pack","");
        PackItem item5 = new PackItem("dolls","인형","0.1.1","설명1","pack","");
        PackItem item6 = new PackItem("education","유아도구","0.1.1","설명1","pack","");

        try{
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

            rtnItem.put("children",jArrayCate.toString());
            return rtnItem;

        }catch (Exception e)
        {
            Log.e(mTag,"getCategory Error -> "  + e.toString());
        }

        return null;
    }


    public JSONObject toJson()
    {
        try {
            JSONArray jArrayCate = new JSONArray();
            JSONObject rtnItem;
            rtnItem = mCategoryInfoItem.toJson();
            for(ShippingBasedItem item : mItemList)
            {
                if(item.getType().indexOf(DATASET_TYPE_CATEGORYITEM)>=0)
                    jArrayCate.put(((CategoryItem)item).toJson());
                else if(item.getType().indexOf(DATASET_TYPE_CATEGORYPACK)>=0)
                    jArrayCate.put(((PackItem) item).toJson());

            }
            rtnItem.put(CategoryItem.JSON_DATASET, jArrayCate.toString());
            return rtnItem;

        } catch (Exception e) {
            Log.e(mTag, "toJson Error -> " + e.toString());
        }
        return null;
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


}
