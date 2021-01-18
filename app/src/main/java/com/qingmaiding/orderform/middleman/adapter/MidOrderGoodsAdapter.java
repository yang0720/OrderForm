package com.qingmaiding.orderform.middleman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qingmaiding.orderform.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class MidOrderGoodsAdapter extends RecyclerView.Adapter<MidOrderGoodsAdapter.ViewHolder>{

    private List<JSONObject> mActiveList;
    private Context mContext;
    private String mSjstatus;//订单状态
    static class ViewHolder extends RecyclerView.ViewHolder{
        View llClick;
        ImageView tag1;
        TextView tag2,tag3,tag6;
        Button tag7;
        LinearLayout tag5;

        public ViewHolder (View view)
        {
            super(view);
            llClick = view;
            tag1 = (ImageView) view.findViewById(R.id.tag1);
            tag2 = (TextView) view.findViewById(R.id.tag2);
            tag3 = (TextView) view.findViewById(R.id.tag3);
            tag5 = (LinearLayout) view.findViewById(R.id.tag5);
            tag6 = (TextView) view.findViewById(R.id.tag6);
            tag7 = (Button) view.findViewById(R.id.tag7);
        }
    }

    public MidOrderGoodsAdapter(List <JSONObject> goodsList, Context context, String sjstatus){
        mActiveList = goodsList;
        mContext = context;
        mSjstatus = sjstatus;
    }
    @NonNull
    @Override
    public MidOrderGoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.midorder_goods_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MidOrderGoodsAdapter.ViewHolder holder, int position) {
        try {

            holder.tag2.setText(mActiveList.get(position).getString("item_name"));//名称
            holder.tag3.setText(mActiveList.get(position).getString("variation_name"));//规格
            holder.tag6.setText(mActiveList.get(position).getString("express_no"));
            if(mActiveList.get(position).getString("status").equals("0")){
                //未签收
                holder.tag7.setVisibility(View.VISIBLE);
            }else{
                //已经签收的单号
                holder.tag7.setVisibility(View.GONE);
            }

            JSONArray ja = mActiveList.get(position).getJSONArray("image");
            String goodsImageURL = "";
            if (ja.length()>0){
                goodsImageURL = ja.get(0).toString();
            }

            Glide.with(mContext).load(goodsImageURL).into(holder.tag1);//图片
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick(position);
            }
        });

        holder.tag7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOrderSNClick(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mActiveList.size();
    }

    public abstract void itemClick(int position);

    public abstract void signOrderSNClick(int position);

}
