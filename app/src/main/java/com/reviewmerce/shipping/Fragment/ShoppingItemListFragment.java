package com.reviewmerce.shipping.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.shipping.Adapter.PackAdapter;
import com.reviewmerce.shipping.AnalyticsApplication;
import com.reviewmerce.shipping.BasicInfo;
import com.reviewmerce.shipping.CommonData.PackItem;
import com.reviewmerce.shipping.CommonData.ShippingBasedItem;
import com.reviewmerce.shipping.CommonData.ShippingItem;
import com.reviewmerce.shipping.PublicLaboratory.CategoryDataList;
import com.reviewmerce.shipping.PublicLaboratory.ShoppingDataLab;
import com.reviewmerce.shipping.R;

import java.util.List;

public class ShoppingItemListFragment extends Fragment
//        implements LoaderManager.LoaderCallbacks<Cursor>
{
    private FragmentInterface.BasicImplement mCallback;
    private int mArgType=0;
    ShoppingDataLab mDataLab;
    String mTag = "ItemListFragment";
    private PackItem mItem;
    private List<ShippingItem> mItemList;

    RecyclerView rvItem;
    PackAdapter mAdapter;
    TextView tvTitle;
    private Tracker mTracker;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mCallback = (FragmentInterface.BasicImplement) activity;
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
        final View rootView = inflater.inflate(R.layout.shopping_list_fragment, container, false);
        Bundle args = getArguments();
        mArgType = args.getInt("type");
        String sItemJson = args.getString("data");
        mDataLab = ShoppingDataLab.get(null);
        mItem = (PackItem)mDataLab.jsonParser_shippingitem(sItemJson);
        mItemList = mDataLab.getPackDataLab().getItem(mItem.getId()).getDataList();
        tvTitle = (TextView)rootView.findViewById(R.id.textview_title);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "lcallig.ttf");
        tvTitle.setTypeface(typeface);
        initRecyclerView(rootView);
        AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        mTracker.setScreenName("Screen" + mTag + "  id : " + mItem.getId());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        data_to_ui();
        getApiData();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCallback = (FragmentInterface.BasicImplement) getActivity();
/*
        mCategory = new CategoryDataList(getActivity());
        try {
            mCategory.initData();
            JSONObject json = mCategory.getBestItem();
            if(json!=null)
                mCategory.setCategoryInfo(json.toString());

        }
        catch (Exception e)
        {
            Log.e(mTag,"onStart, CategoryDatalab Error");
        }
        */
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void initRecyclerView(View rootView)
    {
        rvItem = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPositionWithOffset(1, 200);


        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(layoutManager);
        mAdapter = new PackAdapter(getActivity(),R.layout.shoppingitem_list);
        mAdapter.setOnItemClickLister(mClickListener);
        rvItem.setAdapter(mAdapter);

    }
    PackAdapter.OnItemSelecteListener mClickListener = new PackAdapter.OnItemSelecteListener() {
        @Override
        public void onItemSelected(View v, int position, ShippingBasedItem item) {
            //Toast.makeText(MainActivity.this, "You clicked at position: " + position, Toast.LENGTH_SHORT).show();
            //      if (position == 0)
            try {
                if ((item.getType().indexOf(CategoryDataList.DATASET_TYPE_SHIPPINGITEM) >= 0)
                        || (item.getType().indexOf(CategoryDataList.DATASET_TYPE_CATEGORYPACK) >= 0)) {
                    mCallback.chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM, item.toJson().toString());
                } else if (item.getType().indexOf(CategoryDataList.DATASET_TYPE_CATEGORYITEM) >= 0) {
                    mCallback.chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST, item.toJson().toString());
                }
            }catch (Exception e)
            {
                Log.e(mTag, "ItemClick, Item.toJson() Error");
            }
        }
    };
    public void data_to_ui()
    {
        if(mItemList != null)
            if(mItemList.size()>=0) {
                mAdapter.setItems(mItemList);
                mAdapter.notifyDataSetChanged();
            }
        tvTitle.setText(mItem.getName());
    }
    public void getApiData()
    {

    }
    private int getCount=0;
    Handler mHandlerHttp = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case BasicInfo.THREAD_TYPE_HTTPREQ_CATEGORYITEM:
                    getCount-=1;
                    if(msg.arg1 == 1)
                    {
                        String sResponse = msg.getData().getString("id");
                        //mItemList.setCategoryInfo(sResponse);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "해당 데이터를 받아 올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        //KeyEvent event = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
                        //onKeyDown(KeyEvent.KEYCODE_BACK, event);
                        delayClose(800);
                    }
                    if(getCount<=0) {

                        data_to_ui();
                      //  mDataLab.saveData(mItem.getId());
                    }
                    break;
            }
        }
    };
    public void delayClose(int nMilSec)
    {
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                getActivity().onBackPressed();
                //next();       // 3 초후 이미지를 닫아버림
            }
        }, nMilSec);
    }
}