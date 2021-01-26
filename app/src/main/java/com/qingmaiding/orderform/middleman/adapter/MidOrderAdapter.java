package com.qingmaiding.orderform.middleman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.adapter.OrderGoodsAdapter;
import com.qingmaiding.orderform.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class MidOrderAdapter extends RecyclerView.Adapter<MidOrderAdapter.ViewHolder>{

    private List<JSONObject> mActiveList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View llClick;
        TextView tag1,tag2,tag3,tag4,tag5;
        Button tag6;
        RecyclerView itemRecyclerview;

        public ViewHolder (View view)
        {
            super(view);
            llClick = view;
            tag1 = (TextView) view.findViewById(R.id.tag1);
            tag2 = (TextView) view.findViewById(R.id.tag2);
            tag3 = (TextView) view.findViewById(R.id.tag3);
            tag4 = (TextView) view.findViewById(R.id.tag4);
            tag5 = (TextView) view.findViewById(R.id.tag5);
            tag6 = (Button) view.findViewById(R.id.tag6);
            itemRecyclerview = (RecyclerView)view.findViewById(R.id.itemrecyclerview);
        }
    }

    public MidOrderAdapter(List <JSONObject> goodsList, Context context){
        mActiveList = goodsList;
        mContext = context;
    }
    @NonNull
    @Override
    public MidOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.midorder_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MidOrderAdapter.ViewHolder holder, int position) {
        try {
            holder.tag1.setText(mActiveList.get(position).getString("ordersn"));
            holder.tag2.setText(mActiveList.get(position).getString("shopid"));
            if (mActiveList.get(position).getString("zjs_status").equals("0")){
                holder.tag3.setText("未发单");
                holder.tag6.setVisibility(View.GONE);
            }else if(mActiveList.get(position).getString("zjs_status").equals("1")){
                holder.tag3.setText("待签收");
                holder.tag6.setVisibility(View.GONE);
            }else if(mActiveList.get(position).getString("zjs_status").equals("3")){
                holder.tag3.setText("待发出");
                holder.tag6.setText("发出");
                holder.tag6.setVisibility(View.VISIBLE);

            }else if(mActiveList.get(position).getString("zjs_status").equals("5")){
                holder.tag3.setText("已完成");
                holder.tag6.setVisibility(View.GONE);
            }
            holder.tag4.setText(TimeUtils.getCurrentTime(mActiveList.get(position).getString("create_time")));
            if (mActiveList.get(position).getString("sh_fa_note").equals("null")){

            }else{
                String faStr = mActiveList.get(position).getString("sh_fa_note");
                String fcStr = mActiveList.get(position).getString("zjs_fc_note").equals("null")?"无":mActiveList.get(position).getString("zjs_fc_note");
                holder.tag5.setText(faStr.equals("null")?"无":faStr + "||" +fcStr);
            }
            List<JSONObject> goodsList = new ArrayList<>();
            JSONArray goodsJa = mActiveList.get(position).getJSONArray("items");
            for (int i = 0; i < goodsJa.length(); i++) {
                goodsList.add(goodsJa.getJSONObject(i));
            }
            MidOrderGoodsAdapter orderGoodsAdapter = new MidOrderGoodsAdapter(goodsList,mContext,mActiveList.get(position).getString("zjs_status")) {
                @Override
                public void itemClick(int position) {

                }

                @Override
                public void signOrderSNClick(int itemposition) {
                    signExpNoClick(position,itemposition);
                }
            };
            holder.itemRecyclerview.setLayoutManager(new GridLayoutManager(mContext,1));
            holder.itemRecyclerview.setAdapter(orderGoodsAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick(position);
            }
        });
        holder.tag6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fachuClick(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mActiveList.size();
    }

    public abstract void itemClick(int position);

    public abstract void fachuClick(int position);

    public abstract void signExpNoClick(int position,int itemposition);
}
