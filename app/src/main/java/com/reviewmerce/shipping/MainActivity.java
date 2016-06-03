package com.reviewmerce.shipping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.reviewmerce.shipping.Adapter.LeftDrawerAdapter;
import com.reviewmerce.shipping.CommonData.CategoryItem;
import com.reviewmerce.shipping.CommonData.LeftDrawerItem;
import com.reviewmerce.shipping.CommonData.PackItem;
import com.reviewmerce.shipping.CommonData.ShippingBasedItem;
import com.reviewmerce.shipping.Fragment.FragmentInterface;
import com.reviewmerce.shipping.PublicLaboratory.CategoryDataList;
import com.reviewmerce.shipping.PublicLaboratory.ShoppingDataLab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentInterface.BasicImplement{
    private RecyclerView mRecyclerView;
    private ArrayList<LeftDrawerItem> mDrawerItemList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private MainSequence mMainSequence = null;
    private ShoppingDataLab mShoppingDataLab;
    private BackPressCloseHandler mBackPressCloseHandler;
    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainSequence = new MainSequence(this);
        mShoppingDataLab = ShoppingDataLab.get(this);
        mShoppingDataLab.loadData_all();
        mMainSequence.initImageLoader();
        mBackPressCloseHandler = new BackPressCloseHandler(this);
        List<ShippingBasedItem>blankList = new ArrayList<>();
        initLeftDrawer();
        chgLeftMenu(blankList);
        getSupportActionBar().setTitle(null);
        Intent openingIntent = new Intent(this,OpeningActivity.class);
        startActivityForResult(openingIntent, 1);
        mMainSequence.chgFragment(0, BasicInfo.FRAGMENT_FIRST, "");
        AnalyticsApplication application = (AnalyticsApplication)getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.enableAdvertisingIdCollection(true);
        Log.i(BasicInfo.TAG, "Setting screen name: " + "MainActivity");
        mTracker.setScreenName("Screen" + "Start Application");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        ImageView ivTitle = (ImageView)findViewById(R.id.main_toolbar_ibtitle);
        mShoppingDataLab.getTemplete_API(null);
        ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chgFragment(BasicInfo.FRAGMENT_NONE,BasicInfo.FRAGMENT_FIRST,null);
            }
        });
        try{
           // test();
        }catch (Exception e){}
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mOnKeyBackPressedListener != null)
        {
            mOnKeyBackPressedListener.onBack();
        }
        else {
            int nCount = getSupportFragmentManager().getBackStackEntryCount();
            if(nCount<=1)
                mBackPressCloseHandler.onBackPressed();
            else {
                int nPreCount = getSupportFragmentManager().getBackStackEntryCount();
                getSupportFragmentManager().popBackStack();
            }
            //super.onBackPressed();
        }
    }

    @Override
    public void chgFragment(int nMyIndex, int nDirect,String sVal) {
        mMainSequence.chgFragment(nMyIndex, nDirect,sVal);
    }
    public List<ShippingBasedItem> mMenuList;
    @Override
    public void chgLeftMenu(List<ShippingBasedItem> itemList) {

        mMenuList = itemList;
        //Dummy Data
//        mDrawerItemList = new ArrayList<LeftDrawerItem>();
//        LeftDrawerItem item = new LeftDrawerItem(R.drawable.icon_title, "FirstFragment","");
//        mDrawerItemList.add(item);
//        LeftDrawerItem item2 = new LeftDrawerItem(R.drawable.icon_title, "ShoppingItemList","");
//        mDrawerItemList.add(item2);
//        LeftDrawerItem item3 = new LeftDrawerItem(R.drawable.icon_title, "ShoppingItem","");
//        mDrawerItemList.add(item3);
        mDrawerItemList = new ArrayList<LeftDrawerItem>();
        for(int i=0;i<mMenuList.size();i++)
        {
            try {
                String sType = mMenuList.get(i).getType();
                if (sType.indexOf(mShoppingDataLab.DATASET_TYPE_CATEGORYITEM) >= 0) {
                    CategoryItem item = (CategoryItem) mMenuList.get(i);
                    LeftDrawerItem addItem = new LeftDrawerItem(0, item.getName(), item);
                    mDrawerItemList.add(addItem);
                } else if (sType.indexOf(mShoppingDataLab.DATASET_TYPE_CATEGORYPACK) >= 0) {
                    CategoryItem item = new CategoryItem();
                    PackItem pack = (PackItem) mMenuList.get(i);
                    item.setData(pack.getId(), pack.getName(), pack.getVer(), pack.getDescription(), pack.getType(), pack.getDataSet());
                    LeftDrawerItem addItem = new LeftDrawerItem(0, item.getName(), item);
                    mDrawerItemList.add(addItem);
                }
            }catch ( Exception e)
            {
                Log.e("MainActivity","Menu Error -> "+ e.toString());
            }
//            CategoryItem item = (CategoryItem)mMenuList.get(i);


        }
        LeftDrawerAdapter adapter = (LeftDrawerAdapter) (mRecyclerView.getAdapter());
        adapter.setItemList(mDrawerItemList);
        adapter.notifyDataSetChanged();
    }

    public interface onKeyBackPressedListener {
        public void onBack();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }
    public class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {

            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                //activity.finish(); // 이건 Activity만 종료되고 App은 Background로 살아 있다.
                //               saveAwsData();
                finish();
//                System.exit(0);
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity,
                    "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initLeftDrawer() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);


        //Dummy Data
//        mDrawerItemList = new ArrayList<LeftDrawerItem>();
//        LeftDrawerItem item = new LeftDrawerItem(R.drawable.icon_title, "FirstFragment");
//        mDrawerItemList.add(item);
//        LeftDrawerItem item2 = new LeftDrawerItem(R.drawable.icon_title, "ShoppingItemList");
//        mDrawerItemList.add(item2);
//        LeftDrawerItem item3 = new LeftDrawerItem(R.drawable.icon_title, "ShoppingItem");
//        mDrawerItemList.add(item3);
//
//        for(int i=0;i<mMenuList.size();i++)
//        {
//            LeftDrawerItem addItem = new LeftDrawerItem(0,mMenuList.get(i).getName());
//            mDrawerItemList.add(addItem);
//        }

        mRecyclerView = (RecyclerView) findViewById(R.id.drawerRecyclerView);

        LeftDrawerAdapter adapter = new LeftDrawerAdapter(this, mDrawerItemList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //TODO Add some action here
                //Executed when drawer closes

                Toast.makeText(MainActivity.this, "Closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //TODO Add some action here
                //executes when drawer open
                Toast.makeText(MainActivity.this, "Opened", Toast.LENGTH_SHORT).show();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        adapter.setOnItemClickLister(new LeftDrawerAdapter.OnItemSelecteListener() {
            @Override
            public void onItemSelected(View v, int position) {
                //Toast.makeText(MainActivity.this, "You clicked at position: " + position, Toast.LENGTH_SHORT).show();
          //      if (position == 0)
                try{
                    String sType = mMenuList.get(position).getType();
                    String sJson="";
                    if (mMenuList.get(position).getType().indexOf(CategoryDataList.DATASET_TYPE_CATEGORYITEM) >= 0)
                    {
                        sJson= ((CategoryItem)mMenuList.get(position)).toJson().toString();
                    }
                    else if (mMenuList.get(position).getType().indexOf(CategoryDataList.DATASET_TYPE_CATEGORYPACK) >= 0)
                    {
                        sJson= ((PackItem)mMenuList.get(position)).toJson().toString();

                    }
                    mMainSequence.onNavigationItemSelected(position,sType,sJson);


                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }catch ( Exception e)
                {
                    Log.e("shipping","menu Error -> "+ e.toString());
                }
            }
        });

    }

    public String connectTest()
    {
        return "success";
    }
}
