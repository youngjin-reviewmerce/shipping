package com.reviewmerce.shipping.CommonData;

import org.json.JSONObject;

/**
 * Created by onebuy on 2016-05-20.
 */
public class CategoryItem extends ShippingBasedItem {
    protected String mDescription;
    protected String mVer;
    protected String mDataSet;

    //private List<CategoryItem> mDataList;

    static public String JSON_ID = "id";
    static public String JSON_NAME = "name";
    static public String JSON_VER = "ver";
    static public String JSON_DESCRIPTION = "description";
    static public String JSON_TYPE = "cate_type";
    static public String JSON_DATASET = "sub-categories";
    static public String JSON_IMAGE = "image";
    public static final String DATASET_TYPE_CATEGORYITEM = "C";

    //static public String JSON_IMAGE_URL = "image_url";

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String sDescription) {
        this.mDescription = sDescription;
    }


    public void setVer(String sVer) {
        this.mVer = sVer;
    }
    public String getVer() {
        return mVer;
    }
    public String getDataSet() {
        return mDataSet;
    }

    public void setDataSet(String DataSet) {
        this.mDataSet = DataSet;
    }
    public CategoryItem()
    {
    }
    public CategoryItem(CategoryItem item)
    {
        setData(item);
    }
    public CategoryItem(String sId, String sName, String sVer,String sDesceription, String sType, String sDataSet)
    {
        setData(sId,sName,sVer,sDesceription,sType,sDataSet);
    }
    public CategoryItem(JSONObject json) throws Exception
    {
        setData(json);
    }
    public void setData(String sId, String sName, String sVer, String sDesceription, String sType, String sDataSet)
    {
        super.setData(sId, sName, sType);
        mDescription = sDesceription;
        mType = sType;
        mDataSet = sDataSet;
        mVer = sVer;
    }
    public void setData(CategoryItem item)
    {
        super.setData((ShippingBasedItem)this);
        mDescription = item.getDescription();
        mType = item.getType();
        mDataSet = item.getDataSet();
        mVer = item.getVer();
    }
    public void setData(JSONObject json) throws Exception
    {
        try {
            mId = json.getString(JSON_ID);
        }catch (Exception e){}
        try {
            mName = json.getString(JSON_NAME);
        }catch (Exception e){}
        try {
            mVer = json.getString(JSON_VER);
        }catch (Exception e){}
        try {
            mDescription = json.getString(JSON_DESCRIPTION);
        }catch (Exception e){}
        try {
            mDataSet = json.getString(JSON_DATASET);
        }catch (Exception e){}
        mType = DATASET_TYPE_CATEGORYITEM;
    }
    public JSONObject toJson() throws Exception
    {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId);
        json.put(JSON_NAME, mName);
        json.put(JSON_VER, mVer);
        json.put(JSON_DESCRIPTION, mDescription);
        json.put(JSON_TYPE, mType);
        json.put(JSON_DATASET, mDataSet);
        return json;
    }
    public String getImageUrl()
    {
        String sVal = "cate/"+mId +".jpg";
        return sVal;
    }
}
