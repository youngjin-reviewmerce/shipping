package com.reviewmerce.shipping.CommonData;


import org.json.JSONObject;

/**
 * Created by onebuy on 2015-09-07.
 */
public class TemplateItem {
    public String getVer() {
        return mVer;
    }

    public void setVer(String Ver) {
        this.mVer = Ver;
    }

    public String getLinkUrlPrefix() {
        return mLinkUrlPrefix;
    }

    public void setLinkUrlPrefix(String LinkUrlPrefix) {
        this.mLinkUrlPrefix = LinkUrlPrefix;
    }

    public String getmImgUrlPrefix() {
        return mImgUrlPrefix;
    }

    public void setmImgUrlPrefix(String mImgUrlPrefix) {
        this.mImgUrlPrefix = mImgUrlPrefix;
    }

    public String mVer;
    public String mLinkUrlPrefix;
    public String mImgUrlPrefix;

    static public String JSON_VER = "ver";
    static public String JSON_LINKURL_PREFIX = "item_url_prefix";
    static public String JSON_IMGURL_PREFIX = "image_url_prefix";

    public TemplateItem()
    {}
    public TemplateItem(String ver, String link, String img)
    {
        setData(ver,link,img);
    }
    public TemplateItem(JSONObject json) throws Exception
    {
        setData(json);
    }
    public void setData(String ver, String link, String img)
    {
        mVer = ver;
        mLinkUrlPrefix = link;
        mImgUrlPrefix = img;
    }
    public void setData(JSONObject json) throws Exception
    {
        mVer = json.getString(JSON_VER);
        mLinkUrlPrefix = json.getString(JSON_LINKURL_PREFIX);
        mImgUrlPrefix = json.getString(JSON_IMGURL_PREFIX);
    }
    public JSONObject toJson()throws Exception
    {
        JSONObject json = new JSONObject();
        json.put(JSON_VER,mVer);
        json.put(JSON_LINKURL_PREFIX,mLinkUrlPrefix);
        json.put(JSON_IMGURL_PREFIX,mImgUrlPrefix);
        return json;
    }
}
