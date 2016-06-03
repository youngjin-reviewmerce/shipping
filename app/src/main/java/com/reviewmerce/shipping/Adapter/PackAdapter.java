package com.reviewmerce.shipping.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.reviewmerce.shipping.CommonData.ShippingBasedItem;
import com.reviewmerce.shipping.CommonData.ShippingItem;
import com.reviewmerce.shipping.PublicLaboratory.ShoppingDataLab;
import com.reviewmerce.shipping.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songmho on 2015-07-12.
 */
public class PackAdapter extends RecyclerView.Adapter<PackAdapter.ViewHolder> {
    Context context;
    List<ShippingItem> ItemList;
    OnItemSelecteListener mListener;
    int mResourceId;
    public PackAdapter(Context context, int nResourceId) {
        this.context=context;
        mResourceId = nResourceId;
        ItemList = new ArrayList<ShippingItem>();
    }
    public void setItems(List <ShippingItem> items)
    {
        if(ItemList.size() > 0)
            ItemList.clear();
        ItemList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(mResourceId,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        ShoppingDataLab dataLab = ShoppingDataLab.get(null);
        String sImgUrl = dataLab.getTemplateItem().getmImgUrlPrefix()+ ItemList.get(position).getImageUrl();
        imageLoader.displayImage(sImgUrl,holder.imageView, dataLab.getDisplayOptions());
        holder.textView.setText(ItemList.get(position).getName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(v,position,ItemList.get(position));
                //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        int nRtnVal = ItemList.size();

        return nRtnVal;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.image);
            textView = (TextView)itemView.findViewById(R.id.text);
        }
    }
    public void setOnItemClickLister(OnItemSelecteListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemSelecteListener{
        void onItemSelected(View v, int position, ShippingBasedItem item);
    }

}
