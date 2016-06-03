package com.reviewmerce.shipping.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.shipping.Adapter.CategoryAdapter;
import com.reviewmerce.shipping.Adapter.PackAdapter;
import com.reviewmerce.shipping.AnalyticsApplication;
import com.reviewmerce.shipping.BasicInfo;
import com.reviewmerce.shipping.CommonData.CategoryItem;
import com.reviewmerce.shipping.CommonData.PackItem;
import com.reviewmerce.shipping.CommonData.ShippingBasedItem;
import com.reviewmerce.shipping.CommonData.ShippingItem;
import com.reviewmerce.shipping.PublicLaboratory.CategoryDataList;
import com.reviewmerce.shipping.PublicLaboratory.ShoppingDataLab;
import com.reviewmerce.shipping.R;

import java.util.List;

public class FirstFragment extends Fragment
//        implements LoaderManager.LoaderCallbacks<Cursor>
{
    private FragmentInterface.BasicImplement mCallback;
    private int mArgType=0;
    String mTag = "FirstFragment";
 //   private CategoryDataList mTopItems;
 //   private CategoryDataList mBottomItems;
    private ShoppingDataLab mDataLab = null;
    RecyclerView rvTop,rvBottom;
    PackAdapter mTopAdapter;
    CategoryAdapter mBotAdapter;
    ImageView ivTop, ivBottom;
    private Tracker mTracker;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            //mCallback = (FragmentInterface.BasicImplement) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.first_fragment, container, false);
        mCallback = (FragmentInterface.BasicImplement) getActivity();
        mDataLab = ShoppingDataLab.get(null);
        Bundle args = getArguments();
        mArgType = args.getInt("type");
        ivTop = (ImageView)rootView.findViewById(R.id.imageview_toptitle);
        ivBottom = (ImageView)rootView.findViewById(R.id.imageview_bottomtitle);
        initRecyclerView(rootView);
        data_to_ui();
        try {
            requestMainPage();
        }
        catch (Exception e)
        {
            Log.e(mTag,"onStart, CategoryDatalab Error");
        }
        return rootView;
    }
    public void initRecyclerView(View rootView)
    {
        rvTop = (RecyclerView) rootView.findViewById(R.id.recyclerview_top);
        rvBottom = (RecyclerView) rootView.findViewById(R.id.recyclerview_bottom);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //rvTop.addItemDecoration(new CustomRecyclerDecoration(getActivity(), R.drawable.recycler_divider));
        rvTop.setHasFixedSize(true);
        rvTop.setLayoutManager(layoutManager);
        mTopAdapter = new PackAdapter(getActivity(),R.layout.pack_list);
        mTopAdapter.setOnItemClickLister(mTopClickListener);
        rvTop.setAdapter(mTopAdapter);
        GridLayoutManager layoutMidManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        layoutMidManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBottom.setHasFixedSize(true);
        rvBottom.setLayoutManager(layoutMidManager);
        mBotAdapter = new CategoryAdapter(getActivity(),R.layout.category_list);
        mBotAdapter.setOnItemClickLister(mClickListener);
        rvBottom.setAdapter(mBotAdapter);
        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        mTracker.setScreenName("Screen" + mTag);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();


    }
    public void data_to_ui()
    {
        data_to_ui_top();
        data_to_ui_bottom();
    }
    public void data_to_ui_top()
    {
        try
        {
            List<ShippingItem> itemList = mDataLab.getTopItems().getDataList();
            if((itemList!=null)||(itemList.size() >0)) {

                mTopAdapter.setItems(itemList);

//                ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
//                String sImgUrl = mDataLab.getTemplateItem().getmImgUrlPrefix() + mDataLab.getTopItems().getInfoItem().getId() + ".png";
//                imageLoader.displayImage(sImgUrl, ivTop, mDataLab.getDisplayOptions());
            }

        }catch (Exception e)
        {
            Log.e(mTag,"data_to_ui_top() Error -> "+ e.toString());
        }
        mTopAdapter.notifyDataSetChanged();
    }
    public void data_to_ui_bottom()
    {
        List<ShippingBasedItem> itemList =mDataLab.getBottomItems().getDataList();

        try {
            if((itemList!=null)||(itemList.size() >0)) {
                mBotAdapter.setItems(itemList);

                mCallback.chgLeftMenu(itemList);
//                ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
//                String sImgUrl = mDataLab.getTemplateItem().getmImgUrlPrefix() + mDataLab.getBottomItems().getInfoItem().getId() + ".png";
//                imageLoader.displayImage(sImgUrl, ivBottom, mDataLab.getDisplayOptions());
            }
        }catch (Exception e)
        {
            Log.e(mTag,"data_to_ui_bottom() Error -> "+ e.toString());
        }
        mBotAdapter.notifyDataSetChanged();
    }
    CategoryAdapter.OnItemSelecteListener mClickListener = new CategoryAdapter.OnItemSelecteListener() {
        @Override
        public void onItemSelected(View v, int position, ShippingBasedItem item) {
            String sendJson;
            try {
                if (item.getType().indexOf(CategoryDataList.DATASET_TYPE_SHIPPINGITEM) >= 0)
                {
                    sendJson = ((ShippingItem)item).toJson().toString();
                    mCallback.chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM, sendJson);
                }
                else if (item.getType().indexOf(CategoryDataList.DATASET_TYPE_CATEGORYPACK) >= 0)
                {
                    sendJson = ((PackItem)item).toJson().toString();
                    mCallback.chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST, sendJson);

                } else if (item.getType().indexOf(CategoryDataList.DATASET_TYPE_CATEGORYITEM) >= 0) {
                    sendJson = ((CategoryItem)item).toJson().toString();
                    mCallback.chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST, sendJson);
                }
            }catch (Exception e)
            {
                Log.e(mTag,"ItemClick, Item.toJson() Error");
            }
        }
    };
    PackAdapter.OnItemSelecteListener mTopClickListener = new PackAdapter.OnItemSelecteListener() {
        @Override
        public void onItemSelected(View v, int position, ShippingBasedItem item) {
            String sendJson;
            try {
                if (item.getType().indexOf(CategoryDataList.DATASET_TYPE_SHIPPINGITEM) >= 0)
                {
                    sendJson = ((ShippingItem)item).toJson().toString();
                    mCallback.chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM, sendJson);
                }
                else if (item.getType().indexOf(CategoryDataList.DATASET_TYPE_CATEGORYPACK) >= 0)
                {
                    sendJson = ((PackItem)item).toJson().toString();
                    mCallback.chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST, sendJson);

                } else if (item.getType().indexOf(CategoryDataList.DATASET_TYPE_CATEGORYITEM) >= 0) {
                    sendJson = ((CategoryItem)item).toJson().toString();
                    mCallback.chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST, sendJson);
                }
            }catch (Exception e)
            {
                Log.e(mTag,"ItemClick, Item.toJson() Error");
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void requestMainPage()
    {
        mDataLab.getMainPageInfo_API(getHandler);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        String hostUrl = "https://3ycw9r0z65.execute-api.ap-northeast-1.amazonaws.com/v1/";
//        APIThread_shop thread = new APIThread_shop(hostUrl, getContext(), getHandler, BasicInfo.THREAD_TYPE_HTTPREQ_MAINPAGE,"1","", "1");
//        thread.execute();
    }
    Handler getHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BasicInfo.THREAD_TYPE_HTTPREQ_MAINPAGE:
                        mDataLab.getMainPageData_API(getHandler);
                    break;
                case BasicInfo.THREAD_TYPE_HTTPREQ_TOPPAGE:
                   data_to_ui_top();
                    break;
                case BasicInfo.THREAD_TYPE_HTTPREQ_BOTTOMPAGE:
//                    data_to_ui_bottom();
                    break;

            }
        }
    };
}