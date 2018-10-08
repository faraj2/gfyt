package com.f.schoolsintouchteachers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.f.schoolsintouchteachers.R;
import com.f.schoolsintouchteachers.models.Cell;
import com.f.schoolsintouchteachers.models.ColTitle;
import com.f.schoolsintouchteachers.models.RowTitle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import cn.zhouchaoyuan.excelpanel.BaseExcelPanelAdapter;

/**
 * Created by admin on 3/22/2018.
 */

public class AttendanceAdapter extends BaseExcelPanelAdapter<RowTitle, ColTitle, Cell> {
    private Context context;
    private View.OnClickListener blockListener;

    public AttendanceAdapter(Context context, View.OnClickListener blockListener) {
        super(context);
        this.context = context;
        this.blockListener = blockListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_normal_cell, parent, false);

        return new CellHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(RecyclerView.ViewHolder holder, int verticalPosition, int horizontalPosition) {
        Cell cell = getMajorItem(verticalPosition, horizontalPosition);
        if (null == holder || !(holder instanceof CellHolder) || cell == null) {
            return;
        }
        final CellHolder viewHolder = (CellHolder) holder;
        viewHolder.cellContainer.setTag(cell);
        viewHolder.cellContainer.setOnClickListener(blockListener);
        viewHolder.channelName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                  // buttonView.setText("Present");
                    viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.bluecolor));
                }else {
                 //   buttonView.setText("Absent");
                    viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
                }
            }

        });
        Log.d("MONTH",getLeftItem(verticalPosition).getMonth());
        SimpleDateFormat monthParse = new SimpleDateFormat("MM",Locale.US);
        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM",Locale.US);
        int m=0;
        try {
             m= Integer.parseInt(monthParse.format(monthDisplay.parse(getLeftItem(verticalPosition).getMonth())));
             Log.d("MONTHNUMBER",String.valueOf(m));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("DAYNUMBER",getTopItem(horizontalPosition).getDay());
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(2018, m-1, Integer.parseInt(getTopItem(horizontalPosition).getDay()));

        // this formatter will have the current locale
        SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.US);
        if (format.format(cal.getTime()).equals("Sunday")){
viewHolder.channelName.setVisibility(View.INVISIBLE);
viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context,R.color.cardview_dark_background));
        }else {
            viewHolder.channelName.setVisibility(View.VISIBLE);
            if (cell.isChecked()) {

                viewHolder.channelName.setText(format.format(cal.getTime()));
                viewHolder.channelName.setChecked(true);
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.bluecolor));
            } else {

                viewHolder.channelName.setText(format.format(cal.getTime()));
                viewHolder.channelName.setChecked(false);
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));

            }


          //  viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
        }


    }

    static class CellHolder extends RecyclerView.ViewHolder {


        public final CheckBox channelName;
        public final RelativeLayout cellContainer;

        public CellHolder(View itemView) {
            super(itemView);
            //   bookingName = (TextView) itemView.findViewById(R.id.booking_name);
            channelName = itemView.findViewById(R.id.checkattendace);
            cellContainer = itemView.findViewById(R.id.pms_cell_container);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateTopViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_column_cell, parent, false);

        return new Columnholder(layout);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindTopViewHolder(RecyclerView.ViewHolder holder, int position) {
        RowTitle rowTitle = getTopItem(position);
        if (null == holder || !(holder instanceof Columnholder) || rowTitle == null) {
            return;
        }
        Columnholder viewHolder = (Columnholder) holder;
        viewHolder.day.setText(rowTitle.getDay());
        viewHolder.colmn.setBackgroundColor(R.color.torqiz);
    }

    @Override
    public RecyclerView.ViewHolder onCreateLeftViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_left_header_item, parent, false);
        return new LeftHolder(layout);
    }

    @Override
    public void onBindLeftViewHolder(RecyclerView.ViewHolder holder, int position) {
        ColTitle colTitle = getLeftItem(position);
        if (null == holder || !(holder instanceof LeftHolder) || colTitle == null) {
            return;
        }
        LeftHolder viewHolder = (LeftHolder) holder;
        viewHolder.month.setText(colTitle.getMonth());
    }

    static class LeftHolder extends RecyclerView.ViewHolder {

        public final TextView month;


        public LeftHolder(View itemView) {
            super(itemView);

            month = (TextView) itemView.findViewById(R.id.month);

        }
    }

    @Override
    public View onCreateTopLeftView() {

        View v = LayoutInflater.from(context).inflate(R.layout.room_column_cell, null);
        return v;
    }

    static class Columnholder extends RecyclerView.ViewHolder {
        TextView day;
        RelativeLayout colmn;

        public Columnholder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            colmn = itemView.findViewById(R.id.colmn);
        }
    }
}
