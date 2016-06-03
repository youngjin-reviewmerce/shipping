package com.reviewmerce.shipping.Fragment;

import com.reviewmerce.shipping.CommonData.ShippingBasedItem;

import java.util.List;

/**
 * Created by onebuy on 2016-05-18.
 */
public class FragmentInterface {
    public interface BasicImplement {
        void chgFragment(int nMyIndex, int nDirect,String sVal);
        void chgLeftMenu(List<ShippingBasedItem> mItem);
        // type(int) : 종류

    }
}
