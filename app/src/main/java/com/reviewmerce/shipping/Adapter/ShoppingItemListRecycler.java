package com.reviewmerce.shipping.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by songmho on 2015-07-12.
 */
public class ShoppingItemListRecycler extends GridLayoutManager {
    public ShoppingItemListRecycler(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ShoppingItemListRecycler(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ShoppingItemListRecycler(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler,
                                 RecyclerView.State state) {
        //Scrap measure one child
        View scrap = recycler.getViewForPosition(0);
        addView(scrap);
        measureChildWithMargins(scrap, 0, 0);

    /*
     * We make some assumptions in this code based on every child
     * view being the same size (i.e. a uniform grid). This allows
     * us to compute the following values up front because they
     * won't change.
     */
        /*
        mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap);
        mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap);
        detachAndScrapView(scrap, recycler);

        updateWindowSizing();
        int childLeft;
        int childTop;

        mFirstVisiblePosition = 0;
        childLeft = childTop = 0;

        //Clear all attached views into the recycle bin
        detachAndScrapAttachedViews(recycler);
        //Fill the grid for the initial layout of views
        fillGrid(DIRECTION_NONE, childLeft, childTop, recycler);
        */
    }

}
