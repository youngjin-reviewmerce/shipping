package com.reviewmerce.shipping.CommonData;

import org.json.JSONObject;

/**
 * Created by onebuy on 2016-05-20.
 */
public class ShippingBasedItem {
    protected String mId;
    protected String mName;
    //protected String mVer;
    protected String mType;
    static public String JSON_ID = "id";
    static public String JSON_NAME = "name";
    //static public String JSON_VER = "ver";
    static public String JSON_TYPE = "type";
    public String getType() {
        return mType;
    }

    public void setType(String sType) {
        this.mType = sType;
    }
    public String getId() {
        return mId;
    }
    public void setId(String sId) {
        this.mId = sId;
    }
    public String getName() {
        return mName;
    }

    public void setName(String Name) {
        this.mName = Name;
    }

//    public String getVer() {
//        return mVer;
//    }
//    public void setVer(String sVer) {
//        this.mVer = sVer;
//    }
    public ShippingBasedItem()
    {

    }
    public ShippingBasedItem(ShippingBasedItem item)
    {
        setData(item);
    }
    public ShippingBasedItem(String sId, String sName,String sType)
    {
        setData(sId,sName, sType);
    }
    public ShippingBasedItem(JSONObject json) throws Exception
    {
        setData(json);
    }
    public void setData(String sId, String sName,String sType)
    {
        mId = sId;
        mName = sName;
        mType = sType;

    }
    public void setData(ShippingBasedItem item)
    {
        mId = item.getId();
        mName = item.getName();
        mType = item.getType();
    }
    public void setData(JSONObject json) throws Exception
    {
        mId = json.getString(JSON_ID);
        mName = json.getString(JSON_NAME);
        mType = json.getString(JSON_TYPE);
    }
    public JSONObject toJson() throws Exception
    {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId);
        json.put(JSON_NAME, mName);
        json.put(JSON_TYPE,mType);
        return json;
    }
}
