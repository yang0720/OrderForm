package com.qingmaiding.orderform.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{

    private List<JSONObject> mActiveList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View llClick;
        TextView tag1,tag2,tag3,tag4,tag5,tag6,tag7,tag8,tag9;
        ImageView tag10;

        public ViewHolder (View view)
        {
            super(view);
            llClick = view;
            tag1 = (TextView) view.findViewById(R.id.tag1);//item_name
            tag2 = (TextView) view.findViewById(R.id.tag2);//  商品规格 variation_sku
            tag3 = (TextView) view.findViewById(R.id.tag3);// 商品数量 variation_quantity_purchased
            tag4 = (TextView) view.findViewById(R.id.tag4);//原价  variation_original_price
            tag5 = (TextView) view.findViewById(R.id.tag5);//折扣价  variation_discounted_price
            tag6 = (TextView) view.findViewById(R.id.tag6);//是否批发价  is_wholesale
            tag7 = (TextView) view.findViewById(R.id.tag7);//重量  weight
            tag8 = (TextView) view.findViewById(R.id.tag8);//附加处理  is_add_on_deal
            tag9 = (TextView) view.findViewById(R.id.tag9);//是否是主项目  is_main_item
            tag10 = (ImageView) view.findViewById(R.id.tag10);
        }
    }

    public OrderDetailAdapter(List <JSONObject> goodsList, Context context){
        mActiveList = goodsList;
        mContext = context;
    }
    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetail_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.ViewHolder holder, int position) {
        try {
            holder.tag1.setText(mActiveList.get(position).getString("item_name"));
            holder.tag2.setText(mActiveList.get(position).getString("variation_sku"));
            holder.tag3.setText(mActiveList.get(position).getString("variation_quantity_purchased"));
            holder.tag4.setText(mActiveList.get(position).getString("variation_original_price"));
            holder.tag5.setText(mActiveList.get(position).getString("variation_discounted_price"));
            holder.tag6.setText(mActiveList.get(position).getString("is_wholesale"));
            holder.tag7.setText(mActiveList.get(position).getString("weight"));
            holder.tag8.setText(mActiveList.get(position).getString("is_add_on_deal"));
            holder.tag9.setText(mActiveList.get(position).getString("is_main_item"));
            JSONArray ja = mActiveList.get(position).getJSONArray("image");
            String goodsImageURL = "";
            if (ja.length()>0){
                goodsImageURL = ja.get(0).toString();
            }

            Glide.with(mContext).load(goodsImageURL).into(holder.tag10);//图片
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    @Override
    public int getItemCount() {
        return mActiveList.size();
    }

}
