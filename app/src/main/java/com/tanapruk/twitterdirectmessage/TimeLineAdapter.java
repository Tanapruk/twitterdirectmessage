package com.tanapruk.twitterdirectmessage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanapruk.twitterdirectmessage.model.TimeLineItem;

import java.util.List;

/**
 * Created by trusttanapruk on 3/6/2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineVH> {

    List<TimeLineItem> timeLineItemList;

    public TimeLineAdapter(List<TimeLineItem> timeLineItemList) {
        this.timeLineItemList = timeLineItemList;
    }

    public class TimeLineVH extends RecyclerView.ViewHolder {

        TextView tvText;
        TextView tvDate;

        public TimeLineVH(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    @Override
    public TimeLineAdapter.TimeLineVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        return new TimeLineVH(v);
    }

    @Override
    public void onBindViewHolder(TimeLineAdapter.TimeLineVH holder, int position) {
        TimeLineItem timeLineItem = timeLineItemList.get(position);
        holder.tvDate.setText(timeLineItem.getDate().toString());
        holder.tvText.setText(timeLineItem.getText());
    }

    @Override
    public int getItemCount() {
        return timeLineItemList.size();
    }
}
