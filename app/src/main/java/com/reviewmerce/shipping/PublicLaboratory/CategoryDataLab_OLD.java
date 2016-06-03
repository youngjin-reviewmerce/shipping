package com.reviewmerce.shipping.PublicLaboratory;

/**
 * Created by onebuy on 2016-05-23.
 */
public class CategoryDataLab_OLD {//implements BasicImplementDataLab{
    /*
    public static final int LOAD_TYPE_DB = 0;
    public static final int SAVE_TYPE_DB = 0;
    public static final int DATASET_TYPE_CATEGORY = 0;
    public static final int DATASET_TYPE_CATEGORYITEM = 1;
    public static final int DATASET_TYPE_IMAGELINK= 2;
    protected String mTag="CategoryDataList";
    private DbBasicLab mDb;

    HashMap<CategoryItem,List> mDataList;

    //private List<List> mCategoryItemList=null;
    private Context mAppContext;
    private static CategoryDataLab_OLD mCategoryDataLab;
    public static CategoryDataLab_OLD get(Context c) {
        if (mCategoryDataLab == null) {
            mCategoryDataLab = new CategoryDataLab_OLD(c.getApplicationContext());
        }
        return mCategoryDataLab;
    }
    public CategoryDataLab_OLD(Context context)
    {
        super();
        if(context != null) {
            mAppContext = context;
            try {
                initData();
            }catch (Exception e)
            {
                Log.e(mTag, "initData Fail");
            }
        }
    }
    @Override
    public void alloc() throws Exception {
        mDataList = new HashMap<CategoryItem,List>();
    }

    @Override
    public void release() throws Exception {
        mDataList.clear();
    }

    @Override
    public void loadData(HashMap<String, String> mapArg) throws Exception {

    }

    @Override
    public void saveData(HashMap<String, String> mapArg) throws Exception {

    }

    @Override
    public void initData() throws Exception {
        if(mDataList!=null)
            mDataList.clear();

    }

    @Override
    public void addJsonArray(String sArray) throws Exception {
        JSONArray arrayList = (JSONArray)new JSONTokener(sArray.toString()).nextValue();
        JSONArray array;
        String sDataSet;
        for(int i=0; i<arrayList.length();i++)
        {
            JSONObject json = arrayList.getJSONObject(i);
            CategoryItem item = new CategoryItem(json);

            // 아래는 따로 뺄것
            sDataSet = item.getDataSet();
            List list;
            switch(item.getType())
            {
                case DATASET_TYPE_CATEGORY:
                    list = getList_CategoryItem(sDataSet);
                    break;
                case DATASET_TYPE_CATEGORYITEM:
                    list = getList_CategoryItem(sDataSet);
                    break;
                case DATASET_TYPE_IMAGELINK:
                    //addData_ImageLink(sCategoryItemArray);
                    break;
                default:
                    Log.i(mTag,"Wrong DataType : " + Integer.toString(item.getType()));
                    break;
            }
            //mDataList.add(item);
        }
    }
    public List getList_CategoryItem(String sJsonArray) throws Exception
    {
        JSONArray array = (JSONArray)new JSONTokener(sJsonArray.toString()).nextValue();
        JSONObject json;
        List<CategoryItem>itemList = new ArrayList<CategoryItem>();
        for(int i=0 ; i<array.length() ; i++)
        {
            json = array.getJSONObject(i);
            CategoryItem newItem = new CategoryItem(json);
            itemList.add(newItem);
        }
        return itemList;
    }
    public List getList_ImageLink(String sJsonArray) throws Exception
    {
        JSONArray array = (JSONArray)new JSONTokener(sJsonArray.toString()).nextValue();
        JSONObject json;
        List<ImageAndLink>itemList = new ArrayList<ImageAndLink>();
        for(int i=0 ; i<array.length() ; i++)
        {
            json = array.getJSONObject(i);
            ImageAndLink newItem = new ImageAndLink(json);
            itemList.add(newItem);
        }
        return itemList;
    }

    @Override
    public void removeData(int nIndex) throws Exception {

        List list = mCategoryItemList.get(nIndex);
        if(list != null)
        {
            list.clear();
            mDataList.remove(nIndex);
        }
    }
    public void removeData(List targetList, int nIndex) throws Exception {
        targetList.remove(nIndex);
    }
    @Override
    public void clearData() throws Exception {
        initData();
        mCategoryItemList = null;
        mDataList = null;
    }

    public List getDateList() {
        return mDataList;
    }

    public JSONObject getCategory() {
        try {
            JSONArray jArray = new JSONArray();
            JSONObject rtnItem = new JSONObject();
            rtnItem.put(CategoryItem.JSON_ID, "0001");
            rtnItem.put(CategoryItem.JSON_NAME, "Category");
            rtnItem.put(CategoryItem.JSON_DESCRIPTION, "카테고리.");
            rtnItem.put(CategoryItem.JSON_TYPE, "0");

            CategoryItem item[] = new CategoryItem[6];
            item[0] = new CategoryItem(0,"목재 장난감","유아용",0,"","assets://category/item_image/wood_toy.png","assets://category/title_best.png");
            item[1] = new CategoryItem(0,"레고","전체",0,"","assets://category/item_image/lego.png","assets://category/title_lego.png");
            item[2] = new CategoryItem(0,"보드게임","",0,"","assets://category/item_image/board_game.png","assets://category/title_lego.png");
            item[3] = new CategoryItem(0,"피규어","",0,"","assets://category/item_image/figure.png","assets://category/title_lego.png");
            item[4] = new CategoryItem(0,"인형","",0,"","assets://category/item_image/dolls.png","assets://category/title_lego.png");
            item[5] = new CategoryItem(0,"유아교육","유아용",0,"","assets://category/item_image/education.png","assets://category/title_lego.png");

            for(int i=0;i<item.length;i++)
            {
                JSONObject categoryitem = item[i].toJson();
                jArray.put(categoryitem);
            }
            rtnItem.put(CategoryItem.JSON_DATASET, jArray.toString());
            return rtnItem;
        }catch (Exception e)
        {
            Log.e(mTag,"getCategory Error");
        }
        return null;
    }
    */
}
