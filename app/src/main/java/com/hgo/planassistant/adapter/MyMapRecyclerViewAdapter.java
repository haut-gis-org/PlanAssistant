package com.hgo.planassistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.avos.avoscloud.AVObject;
import com.hgo.planassistant.R;
import com.hgo.planassistant.activity.DetailPMapActivity;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MyMapRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<AVObject> mItems;
    private Bundle bundle;

    public MyMapRecyclerViewAdapter(List<AVObject> list, Context context) {
        this.mContext = context;
        this.mItems = list;
    }

    public void Updatelist(List<AVObject> list){
//        this.mItems.clear();
        this.mItems = list;
        notifyDataSetChanged();
    }

    public void setBundle(Bundle bundle){
        this.bundle = bundle;
    }

    public void addItem(int position, AVObject insertData) {
        mItems.add(position, insertData);
        notifyItemInserted(position);
    }
    public void addItem(AVObject insertData) {
        int position = 0;
        mItems.add(position, insertData);
        notifyItemInserted(position);
    }
    public void RemoveItem(int position){
        mItems.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyMapRecyclerViewAdapter.RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mymap_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyMapRecyclerViewAdapter.RecyclerViewHolder) {
            final MyMapRecyclerViewAdapter.RecyclerViewHolder recyclerViewHolder = (MyMapRecyclerViewAdapter.RecyclerViewHolder) holder;

            //加载地图
            recyclerViewHolder.mapview.onCreate(bundle);
            recyclerViewHolder.mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(34.833774, 113.537698))
                        .title("Eiffel Tower"));
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                    }
                });
            }
        });

            //加载名称、备注、事件
            recyclerViewHolder.TV_title.setText(mItems.get(position).get("name").toString());
            recyclerViewHolder.TV_remarks.setText(mItems.get(position).get("remarks").toString());
            recyclerViewHolder.TV_date.setText("创建时间："+ DateFormat.getDateTimeInstance().format((Date)mItems.get(position).get("createdAt")));


            //监听项目点击事件
            recyclerViewHolder.mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, DetailPMapActivity.class);
                    intent.putExtra("pmapObjectId", mItems.get(position).getObjectId());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView TV_title;
        private TextView TV_remarks;
        private TextView TV_date;
        private CardView mItem;
        private MapView mapview;

        private RecyclerViewHolder(View itemView) {
            super(itemView);
            TV_title = (TextView) itemView.findViewById(R.id.tv_mymap_item_title);
            TV_remarks = (TextView) itemView.findViewById(R.id.tv_mymap_item_remarks);
            TV_date = (TextView) itemView.findViewById(R.id.tv_mymap_item_date);
            mItem = (CardView) itemView.findViewById(R.id.card_view_item_mymap_view);
            mapview = itemView.findViewById(R.id.imrv_mapView);
        }
    }
}
