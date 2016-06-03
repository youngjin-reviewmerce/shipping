package com.reviewmerce.shipping.CommonData;

import com.reviewmerce.shipping.PublicLaboratory.ShoppingDataLab;

import org.json.JSONObject;

/**
 * Created by onebuy on 2016-05-20.
 */
public class ShippingItem extends ShippingBasedItem {
    protected String mAsin;
    protected String mDesc;
    static public String JSON_ID = "id";
    static public String JSON_NAME = "name";
    static public String JSON_DESCRIPTIN = "description";
    static public String JSON_ASIN = "asin";
    static public String JSON_TYPE = "type";
    static public String JSON_IMAGE = "image";
    static public String JSON_LINKURL = "detail_url";



    public ShippingItem() {
    }
    public String getAsin(){
        return mAsin;
    }
    public void setAsin(String sAsin)
    {
        mAsin = sAsin;
    }
    public String getDesc()
    {
        return mDesc;
    }
    public void setDesc(String sDesc)
    {
        mDesc = sDesc;
    }
    public ShippingItem(ShippingItem item) {
        setData(item);
    }

    public ShippingItem(String sId, String sName,  String sAsin, String sDesc, String sType) {
        setData(sId, sName, sAsin, sDesc, sType);
    }

    public ShippingItem(JSONObject json) throws Exception {
        setData(json);
    }

    public void setData(String sId, String sName, String sAsin, String sDesc, String sType) {
        super.setData(sId, sName,sType);
        mAsin = sAsin;
        mDesc = sDesc;
    }

    public void setData(ShippingItem item) {
        super.setData((ShippingBasedItem) this);
        mType = item.getType();
        mAsin = item.getAsin();
        mDesc = item.getDesc();
    }

    public void setData(JSONObject json) throws Exception {
        mId = json.getString(JSON_ID);
        mName = json.getString(JSON_NAME);
        mAsin = json.getString(JSON_ASIN);
        mDesc = json.getString(JSON_DESCRIPTIN);
        mType = ShoppingDataLab.DATASET_TYPE_SHIPPINGITEM;
//        mType = json.getString(JSON_TYPE);
//        mLinkUrl = json.getString(JSON_LINKURL);
    }

    public JSONObject toJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId);
        json.put(JSON_NAME, mName);
        json.put(JSON_ASIN,mAsin);
        json.put(JSON_TYPE, mType);
        json.put(JSON_DESCRIPTIN,mDesc);
//        json.put(JSON_LINKURL,mLinkUrl);
        return json;
    }
    public String getImageUrl()
    {
        String sVal = "item/"+mId +".jpg";
        return sVal;
    }
    public String getLinkUrl()
    {
        String sVal = "?asin="+mId;
        return sVal;
    }

}
