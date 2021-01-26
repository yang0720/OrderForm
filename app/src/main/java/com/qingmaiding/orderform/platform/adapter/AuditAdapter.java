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

public abstract class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.ViewHolder>{

    private List<JSONObject> mActiveList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View llClick;
        TextView tag1,tag2,tag3,tag4;
        Button tag5,tag6;

        public ViewHolder (View view)
        {
            super(view);
            llClick = view;
            tag1 = (TextView) view.findViewById(R.id.tag1);
            tag2 = (TextView) view.findViewById(R.id.tag2);
            tag3 = (TextView) view.findViewById(R.id.tag3);
            tag4 = (TextView) view.findViewById(R.id.tag4);
            tag5 = (Button) view.findViewById(R.id.tag5);
            tag6 = (Button) view.findViewById(R.id.tag6);
        }
    }

    public AuditAdapter(List <JSONObject> goodsList, Context context){
        mActiveList = goodsList;
        mContext = context;
    }
    @NonNull
    @Override
    public AuditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AuditAdapter.ViewHolder holder, int position) {
        try {
            holder.tag1.setText(mActiveList.get(position).getString("account"));
            holder.tag2.setText(TimeUtils.getSecTime(mActiveList.get(position).getString("createtime")));
            if(mActiveList.get(position).getString("money").equals("null")){
                holder.tag3.setText("无");
            }else{
                holder.tag3.setText(mActiveList.get(position).getString("money"));
            }
            if(mActiveList.get(position).getString("status").equals("1")){
                holder.tag5.setVisibility(View.VISIBLE);
                holder.tag6.setVisibility(View.VISIBLE);
            }else{
                holder.tag5.setVisibility(View.GONE);
                holder.tag6.setVisibility(View.GONE);
            }
            if(mActiveList.get(position).getString("type").equals("null")){
                holder.tag4.setText("无");
            }else{
                if(mActiveList.get(position).getString("type").equals("1")){
                    holder.tag4.setText("微信");
                }else{
                    holder.tag4.setText("支付宝");
                }

            }
            ;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.tag5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passClick(position);
            }
        });
        holder.tag6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failClick(position);
            }
        });
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
    public abstract void passClick(int position);
    public abstract void failClick(int position);
}
