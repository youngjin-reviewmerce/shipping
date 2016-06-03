package com.reviewmerce.shipping;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.reviewmerce.shipping.PublicLaboratory.CategoryDataList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by onebuy on 2016-05-09.
 */
@RunWith(AndroidJUnit4.class)
public class testCategoryData {
    MainActivity mTestActivity;
    CategoryDataList mDatalab;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mTestActivity = mActivityRule.getActivity();
   //     mDatalab = CategoryDataList.get(mTestActivity);
    }

    @Test
    public void testSaveLoad()
    {
        try {
            String str = mTestActivity.connectTest();
            assertThat("success", is(str));     // 연결 확인
            testAlloc();
            testInit();                         // 초기화 테스트
            JSONArray jarray = getDummyJson();
            mDatalab.getCategory();
            mDatalab.addItemJsonArray(jarray.toString());
            HashMap<String,String> mArg = new HashMap<String,String>();
            mArg.put("type", Integer.toString(CategoryDataList.SAVE_TYPE_DB));
            mDatalab.saveData(mArg);           // DB 저장
            if(testInit()==false) {
                fail();
                Log.d("Test-CategoryData", "testSaveLoad() was succeed");
            }
        }catch (Exception e)
        {
            Log.d("Test-BankDataLab","testSave() fail");
//            fail();
        }
    }
    public void initData()
    {
        try{

        }
        catch (Exception e)
        {
            Log.d("Test-ShoppingDataLab","testSave() fail");
        }
    }
    public JSONArray getDummyJson() throws Exception
    {
        JSONArray jArray = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("id", "0000");
        item.put("name","BestDesign");
        item.put("description","추천합니다.");
        item.put("type", "0");

        for(int i=0;i<3;i++)
        {

        }
        item.put("datalist", jArray.toString());

        JSONArray jArrayBigger = new JSONArray();
        JSONObject item2 = new JSONObject();
        item2.put("id", "0001");
        item2.put("name","Category");
        item2.put("description","카테고리.");
        item2.put("type", "0");
        for(int i=0;i<3;i++)
        {
            JSONObject categoryitem = new JSONObject();
            categoryitem.put("id","001"+Integer.toString(i+1));
            categoryitem.put("name","Designer's choice "+ "#"+Integer.toString(i+1));
            categoryitem.put("description","세부 항목");
            categoryitem.put("type","0");
            jArray.put(categoryitem);
        }
        item2.put("datalist", jArray.toString());
        jArrayBigger.put(item);
        jArrayBigger.put(item2);
        Log.i("jsontest", jArrayBigger.toString());

        return jArrayBigger;
    }
    public boolean testAlloc() throws Exception
    {
        Context context = mTestActivity;
        if(mDatalab!=null)
            mDatalab.release();
        mDatalab = new CategoryDataList(context);
        if(mDatalab == null)
            return false;
        mDatalab.alloc();
        return true;
    }
    public boolean testInit() throws Exception
    {
        mDatalab.initData();
        if(mDatalab.getDataList().size()>0)
            return false;
        return true;
    }
}
