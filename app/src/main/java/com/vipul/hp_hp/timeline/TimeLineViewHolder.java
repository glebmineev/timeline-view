package com.vipul.hp_hp.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.spb.sigma.timelineview.TimelineView;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    public TextView history_month;
    public TextView history_pay;
    public  TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        history_month = (TextView) itemView.findViewById(R.id.history_month);
        history_pay = (TextView) itemView.findViewById(R.id.history_pay);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
    }
}
