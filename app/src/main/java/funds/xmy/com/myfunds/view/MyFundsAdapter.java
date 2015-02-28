/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package funds.xmy.com.myfunds.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import funds.xmy.com.myfunds.R;
import funds.xmy.com.myfunds.model.FundInfo;

/**
 * Created by xumengyang01 on 2015/2/27.
 */
public class MyFundsAdapter extends BaseAdapter{

    private Context mContext;
    private List<FundInfo> mData;

    public MyFundsAdapter(Context ctx,List<FundInfo> data){
        this.mData = data;
        this.mContext = ctx;
    }

    public void updateData(List<FundInfo> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fund_list_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        FundInfo info = mData.get(position);
        if(info != null){
            holder.mNameTV.setText(mContext.getString(R.string.list_item_name,info.name,info.fund_id+""));
            holder.mAverageRateTV.setText(info.cur_rate+"");
            holder.mProfitTV.setText(info.total_profit+"");
            float profitRate = (info.total_profit / info.total_cost) * 100;
            holder.mProfitRateTV.setText(profitRate+"%");
        }
        return convertView;
    }

    private class ViewHolder{

        TextView mNameTV;
        TextView mAverageRateTV;
        TextView mProfitTV;
        TextView mProfitRateTV;

        public ViewHolder(View contentView){
            this.mNameTV = (TextView)contentView.findViewById(R.id.fund_list_item_name_tv);
            this.mAverageRateTV = (TextView)contentView.findViewById(R.id.fund_list_item_average_rate_tv);
            this.mProfitRateTV = (TextView)contentView.findViewById(R.id.fund_list_item_profit_rate_tv);
            this.mProfitTV = (TextView)contentView.findViewById(R.id.fund_list_item_profit_tv);
        }
    }
}
