package com.qingmaiding.orderform.platform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class PlatStoreAdapter extends RecyclerView.Adapter<PlatStoreAdapter.ViewHolder>{

    private List<JSONObject> mActiveList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View llClick;
        TextView tag1,tag2,tag3,tag4,tag5;

        public ViewHolder (View view)
        {
            super(view);
            llClick = view;
            tag1 = (TextView) view.findViewById(R.id.tag1);
            tag2 = (TextView) view.findViewById(R.id.tag2);
            tag3 = (TextView) view.findViewById(R.id.tag3);
            tag4 = (TextView) view.findViewById(R.id.tag4);
            tag5 = (TextView) view.findViewById(R.id.tag5);
        }
    }

    public PlatStoreAdapter(List <JSONObject> goodsList, Context context){
        mActiveList = goodsList;
        mContext = context;
    }
    @NonNull
    @Override
    public PlatStoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.platstore_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlatStoreAdapter.ViewHolder holder, int position) {
        try {
            holder.tag1.setText(mActiveList.get(position).getString("shop_id"));
            holder.tag2.setText(TimeUtils.getCurrentTime(mActiveList.get(position).getString("create_time")));
            if(mActiveList.get(position).getString("shop_name").equals("null")){
                holder.tag3.setText("店铺名称未设置");
            }else{
                holder.tag3.setText(mActiveList.get(position).getString("shop_name"));
            }
            if(mActiveList.get(position).getString("contact").equals("null")){
                holder.tag4.setText("店铺联系人未设置");
            }else{
                holder.tag4.setText(mActiveList.get(position).getString("shop_name"));
            }
            if(mActiveList.get(position).getString("address").equals("null")){
                holder.tag5.setText("店铺地址未设置");
            }else{
                holder.tag5.setText(mActiveList.get(position).getString("address"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mActiveList.size();
    }

    public abstract void itemClick(int position);

}
