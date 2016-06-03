package com.reviewmerce.shipping.PublicLaboratory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.reviewmerce.shipping.CommonData.PackItem;
import com.reviewmerce.shipping.CommonData.ShippingBasedItem;
import com.reviewmerce.shipping.Fragment.DbBasicLab;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onebuy on 2015-09-07.
 */
public class PackDataLab {

    private static PackDataLab DataLab;

    private String mTag = "PackDataLab";
    private String mDbName = "shipping_main.db";
    private String mDbTable_pack = "pack_list";

    static public String JSON_ID = "id";
    static public String JSON_TYPE = "type";
    static public String JSON_DATASET = "children";

    public static final String DATASET_TYPE_CATEGORYITEM = "C";
    public static final String DATASET_TYPE_CATEGORYPACK = "P";
    public static final String DATASET_TYPE_SHIPPINGITEM = "item";

    private int mGetCount;
    Handler mParentHandler;

    Map<String, PackDataList> mPackDataMap;
    List<String> mAddPackIdList;
    private DbBasicLab mDb;
    public static PackDataLab get(Context c) {
            if (DataLab == null) {
                DataLab = new PackDataLab(c.getApplicationContext());
            }
            return DataLab;
    }
    ////////////////////////////////////////////////////////////////////////////

    private Context mAppContext;


    public PackDataLab(Context appContext) {
        if(appContext != null) {
            mAppContext = appContext;
            allocData();
            //initData();
        }
    }
    public void putItem(String sId, PackDataList pack)
    {
        mPackDataMap.put(sId,pack);
    }
    public PackDataList getItem(String sId)
    {
        PackDataList rtnData = mPackDataMap.get(sId);
        return rtnData;
    }
    public void allocData()
    {
        mPackDataMap = new HashMap<>();
        mAddPackIdList = new ArrayList<>();
        mDb = new DbBasicLab(mAppContext);
        mDb.setTag("Shipping_BaseDb");
        mDb.setDbName(mDbName);

        try {
            mDb.openDatabase();
            mDb.createTable(getSql_createTable(mDbTable_pack));
        } catch (Exception e) {

        }
    }
    public void release() throws Exception {
        mPackDataMap.clear();
        mAddPackIdList.clear();
        mDb.closeDatabase();
    }
    public void initData()
    {
        mAddPackIdList.clear();
    }

    public boolean loadData()
    {
        try
        {
            String sSql = "select * "
                    + "from " + mDbTable_pack;// + " WHERE id=='"+"'";
            Cursor cursor = mDb.loadDatabase(sSql);
            cur2Data(cursor);
            return true;
        }catch(Exception e)
        {
            Log.e(mTag, "loadMainPage() Error -> " + e.toString());
        }
        return false;
    }

    public boolean saveData_pack(String sIndex)
    {
        try {
            //     mDb.deleteCreateTable(mDbTable, getSql_createTable(mDbTable));
            saveData_to_db(mDbTable_pack, sIndex);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean cur2Data(Cursor cursor) {
        JSONArray resultSet = new JSONArray();
        try {
        cursor.moveToFirst();
        String sIndex, sData, sType;

            while (cursor.isAfterLast() == false) {
                sIndex = cursor.getString(0);
                sData = cursor.getString(1);
                sType = cursor.getString(2);

                if(sType.indexOf(DATASET_TYPE_CATEGORYPACK)>=0) {
                    PackDataList dataLab = new PackDataList(mAppContext);
                    dataLab.setPackInfo(sData);
                    mPackDataMap.put(sIndex, dataLab);
                }
                cursor.moveToNext();
            }
        }catch (Exception e)
        {
            Log.d(mTag, "cur2Data -> " + e.toString());
            return false;
        }
        cursor.close();
        return true;

    }


    public String addNewItem(String sResponse) {
        try {
            JSONObject json = (JSONObject)new JSONTokener(sResponse).nextValue();
            ShippingBasedItem item = new ShippingBasedItem(json);
            PackDataList datalab = new PackDataList(mAppContext);
            datalab.setPackInfo(sResponse);
            mPackDataMap.put(datalab.getInfoItem().getId(), datalab);
            mAddPackIdList.add(datalab.getInfoItem().getId());
        }catch (Exception e)
        {
            Log.e(mTag, "addNewItem() Error -> " + e.toString());
        }
        return null;
    }


    private void saveData_to_db(String sTable, String sId) throws Exception {

        try {

            PackItem item = (PackItem)mPackDataMap.get(sId).getInfoItem();
            String sData = mPackDataMap.get(sId).toJson().toString();
            ContentValues cv = new ContentValues();
            cv.put(JSON_ID, sId);
            cv.put(JSON_DATASET, sData);
            cv.put(JSON_TYPE, item.getType());
            mDb.saveDatabase(sTable, cv);
        } catch (Exception e) {
            Log.e(mTag,"saveData() Error -> " + e.toString());
        }
    }
    public String getSql_createTable(String sTable)
    {
        String sRtn = "create table IF NOT EXISTS " + sTable + " ("
                + JSON_ID + " STRING NOT NULL PRIMARY KEY, "
                + JSON_DATASET + " STRING, "
                + JSON_TYPE + " STRING"
                + " );";
        return sRtn;
    }

}
