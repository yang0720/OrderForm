package com.qingmaiding.orderform.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class ExpHisAdapter extends RecyclerView.Adapter<ExpHisAdapter.ViewHolder>{

    private List<JSONObject> mActiveList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View llClick;
        TextView tag1,tag2,tag3,tag4;

        public ViewHolder (View view)
        {
            super(view);
            llClick = view;
            tag1 = (TextView) view.findViewById(R.id.tag1);
            tag2 = (TextView) view.findViewById(R.id.tag2);
            tag3 = (TextView) view.findViewById(R.id.tag3);
            tag4 = (TextView) view.findViewById(R.id.tag4);
        }
    }

    public ExpHisAdapter(List <JSONObject> goodsList, Context context){
        mActiveList = goodsList;
        mContext = context;
    }
    @NonNull
    @Override
    public ExpHisAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exphis_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpHisAdapter.ViewHolder holder, int position) {
        try {
            holder.tag1.setText(mActiveList.get(position).getString("memo"));
            holder.tag2.setText(TimeUtils.getSecTime(mActiveList.get(position).getString("createtime")));
            if(mActiveList.get(position).getString("score").equals("null")){
                holder.tag3.setText("无");
            }else{
                holder.tag3.setText(mActiveList.get(position).getString("score"));
            }
            if(mActiveList.get(position).getString("after").equals("null")){
                holder.tag4.setText("无");
            }else{
                holder.tag4.setText(mActiveList.get(position).getString("after"));
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
