package com.reviewmerce.shipping.CommonData;

import org.json.JSONObject;

/**
 * Created by onebuy on 2016-05-20.
 */
public class PackItem extends ShippingBasedItem {
    protected String mDescription;
    protected String mDataSet;
    protected String mVer;
    //private List<CategoryItem> mDataList;

    static public String JSON_ID = "id";
    static public String JSON_NAME = "name";
    static public String JSON_VER = "ver";
    static public String JSON_DESCRIPTION = "description";
    static public String JSON_TYPE = "type";
    static public String JSON_DATASET = "sub-items";
    static public String JSON_IMAGE = "image";
    //static public String JSON_IMAGE_URL = "image_url";

    public static final String DATASET_TYPE_CATEGORYPACK = "P";

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String sDescription) {
        this.mDescription = sDescription;
    }
        public String getVer() {
        return mVer;
    }
    public void setVer(String sVer) {
        this.mVer = sVer;
    }
    public String getDataSet() {
        return mDataSet;
    }

    public void setDataSet(String DataSet) {
        this.mDataSet = DataSet;
    }
    public PackItem()
    {
    }
    public PackItem(PackItem item)
    {
        setData(item);
    }
    public PackItem(String sId, String sName, String sVer, String sDesceription, String sType, String sDataSet)
    {
        setData(sId,sName,sVer,sDesceription,sType,sDataSet);
    }
    public PackItem(JSONObject json) throws Exception
    {
        setData(json);
    }
    public void setData(String sId, String sName, String sVer, String sDesceription, String sType, String sDataSet)
    {
        super.setData(sId, sName,sType);
        mDescription = sDesceription;
        mType = sType;
        mVer = sVer;
        mDataSet = sDataSet;
    }
    public void setData(PackItem item)
    {
        super.setData((ShippingBasedItem)this);
        mDescription = item.getDescription();
        mType = item.getType();
        mDataSet = item.getDataSet();
        mVer = item.getVer();
    }
    public void setData(JSONObject json) throws Exception
    {
        //super.setData(json);
        //mId = json.getString(JSON_ID);
        //mName = json.getString(JSON_NAME);
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
        mType = DATASET_TYPE_CATEGORYPACK;

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
        String sVal = "pack/"+mId +".jpg";
        return sVal;
    }
}
