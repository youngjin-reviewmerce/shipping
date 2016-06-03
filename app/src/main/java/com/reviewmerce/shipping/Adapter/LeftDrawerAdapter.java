package com.reviewmerce.shipping.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reviewmerce.shipping.CommonData.LeftDrawerItem;
import com.reviewmerce.shipping.R;

import java.util.List;

/**
 * Created by songmho on 2015-07-12.
 */
public class LeftDrawerAdapter extends RecyclerView.Adapter<LeftDrawerAdapter.ViewHolder> {
    Context context;
    List<LeftDrawerItem> mItemList;
    private OnItemSelecteListener mListener;
 //   public baseOnebuyFragment.FragmentChangeListener mCallback=null;
    int item_layout;

    public LeftDrawerAdapter(Context context, List<LeftDrawerItem> items) {
        this.context=context;
        this.mItemList=items;
        this.item_layout=item_layout;

    }
//    public void setCallback(baseOnebuyFragment.FragmentChangeListener callback)
//    {
//        mCallback = callback;
//    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.app_leftdrawer_item,parent,false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.itemIcon.setImageResource(items.get(position).getIconId());
        holder.itemText.setText(mItemList.get(position).getMenuItemText());
    }

    @Override
    public int getItemCount() {
        return this.mItemList.size();
    }
    public void setItemList(List<LeftDrawerItem>itemList)
    {
        mItemList = itemList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
    //    public ImageView  itemIcon;
        public TextView itemText;
        public ViewHolder(View itemView) {
            super(itemView);
            try {
      //          itemIcon = (ImageView) itemView.findViewById(R.id.drawer_icon);
                itemText = (TextView) itemView.findViewById(R.id.drawer_text);

                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "lcallig.ttf");
                itemText.setTypeface(typeface);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemSelected(view, getAdapterPosition());
                    }
                });
//                itemIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mListener.onItemSelected(view, getAdapterPosition());
//                    }
//                });
//                itemText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mListener.onItemSelected(view, getAdapterPosition());
//                    }
//                });
            }catch (Exception e)
            {
                Log.i("LeftDrawerAdapter","ViewHolder create fail");
            }
        }
    }
    public void setOnItemClickLister(OnItemSelecteListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemSelecteListener{
        public void onItemSelected(View v, int position);
    }
}
