package com.reviewmerce.shipping;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.reviewmerce.shipping.CommonData.CategoryItem;
import com.reviewmerce.shipping.Fragment.FirstFragment;
import com.reviewmerce.shipping.Fragment.ShoppingItemFragment;
import com.reviewmerce.shipping.Fragment.ShoppingItemListFragment;
import com.reviewmerce.shipping.PublicLaboratory.CategoryDataList;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by onebuy on 2016-04-25.
 */
public class MainSequence {

    public FirstFragment mFirstFragment;
    public ShoppingItemFragment mShoppingItemFragment;
    public ShoppingItemListFragment mShoppingItemListFragment;
    public int mLastFragmentId=0;

    //private Context mContext;
    private FragmentActivity mActivity=null;
    public MainSequence(FragmentActivity activity)
    {
        mActivity = activity;
        //mGlobalVar = GlobalVar.get();
    }
    int index = 0;
    public void chgFragment(int nMyIndex, int nDirect,String sVal)
    {
        try{
            switch(nMyIndex) {
                case BasicInfo.FRAGMENT_NONE:   // 새창 다시 띄움
                {
                    Bundle args = new Bundle();
                    args.putInt("type", nDirect);
                    args.putString("data", sVal);

                    if(nDirect == BasicInfo.FRAGMENT_FIRST)
                    {
                        mLastFragmentId = nDirect;
                        args.putString("title", "Category list");
                        stackAddFragment(BasicInfo.FRAGMENT_FIRST, args);
                    }
                    if(nDirect == BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST)
                    {
                        mLastFragmentId = nDirect;
                        args.putString("title", "Shopping item list");
                        stackAddFragment(BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST, args);
                    }
                    if(nDirect == BasicInfo.FRAGMENT_SHOPPING_ITEM)
                    {
                        mLastFragmentId = nDirect;
                        args.putString("title", "Shopping item");
                        stackAddFragment(BasicInfo.FRAGMENT_SHOPPING_ITEM, args);
                    }
                    break;
                }
            }
        }catch (Exception ee) {
            Log.d("CAT", ee.toString());}
    }
    public void stackAddFragment(int reqNewFragmentIndex,Bundle bundle) {

        Fragment newFragment = null;

        Log.d("mainActivity", "fragmentReplace " + reqNewFragmentIndex);

        newFragment = getFragment(reqNewFragmentIndex);
        newFragment.setArguments(bundle);
        // replace fragment
        final FragmentTransaction transaction = mActivity.getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);


        // Commit the transaction
        transaction.commit();
    }
    private Fragment getFragment(int idx) {
        Fragment newFragment = null;

        switch (idx) {
            case BasicInfo.FRAGMENT_FIRST:
                //           if(mFragmentGraph==null)
//                if(mFirstFragment == null)
                    mFirstFragment = new FirstFragment();
                newFragment = mFirstFragment;
                break;
            case BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST:
                //           if(mFragmentBank==null)
 //               if(mShoppingItemListFragment == null)
                    mShoppingItemListFragment = new ShoppingItemListFragment();
                newFragment = mShoppingItemListFragment;
                break;
            case BasicInfo.FRAGMENT_SHOPPING_ITEM:
                //           if(mFragmentPurse==null)
//                if(mShoppingItemFragment == null)
                    mShoppingItemFragment = new ShoppingItemFragment();
                newFragment = mShoppingItemFragment;
                break;
            default:
                Log.d("mainActivity", "Unhandle case");
                break;
        }
        return newFragment;
    }

    public boolean onNavigationItemSelected(int nItem,String sType, String sJson) {
        Bundle args = new Bundle();
        args.putString("title", "initsearch");
        //     args.putInt("type", BasicInfo.FRAGMENT_SEARCHPAGE);

        final FragmentTransaction transaction;
        Fragment fragment = null;
        try {


                if (sType.indexOf(CategoryDataList.DATASET_TYPE_SHIPPINGITEM) >= 0)
                {
                    chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM, sJson);
                }
                else if (sType.indexOf(CategoryDataList.DATASET_TYPE_CATEGORYPACK) >= 0)
                {
                    chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST, sJson);

                } else if (sType.indexOf(CategoryDataList.DATASET_TYPE_CATEGORYITEM) >= 0) {
                    chgFragment(0, BasicInfo.FRAGMENT_SHOPPING_ITEM_LIST, sJson);
                }

        }
        catch (Exception e) {

        }
//        if (id == R.id.1) {
//            chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_LICENSE);
//        }
        return true;
    }
    public void initImageLoader() {
        /*
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY-1)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
                */
        File cacheDir = StorageUtils.getCacheDirectory(mActivity);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mActivity)
                //.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .memoryCacheExtraOptions(1440, 2560) // default = device screen dimensions
                        //.diskCacheExtraOptions(480, 800, null)
                .diskCacheExtraOptions(1440, 2560, null)
                        //.taskExecutor(...)
                        //.taskExecutorForCachedImages(...)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(mActivity)) // default
//                .imageDecoder(new BaseImageDecoder()) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

    }
    public void showVersion()
    {
        try {
            String device_version = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName;
            final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("버전 정보");
            builder.setMessage(device_version);
    //        builder.setIcon(R.drawable.icon_cat);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }catch (Exception e)
        {

        }
    }
}
