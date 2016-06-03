package com.reviewmerce.shipping.CommonData;


/**
 * Created by onebuy on 2015-09-07.
 */
public class LeftDrawerItem {
    public int mIconId;
    public String mMenuItemText;
    public CategoryItem mItem;

    public LeftDrawerItem(int nIconId, String sMenuItem, CategoryItem sItemId) {
        mIconId = nIconId;
        mMenuItemText = sMenuItem;
        mItem = sItemId;
    }

    public String getMenuItemText() {
        return mMenuItemText;
    }

    public void setText(String sMenuItem) {
        mMenuItemText = sMenuItem;
    }

    public CategoryItem getItem() {
        return mItem;
    }

    public void setItem(CategoryItem item)
    {
        mItem = item;
    }

}
