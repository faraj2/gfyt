package com.f.schoolsintouchteachers.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f.schoolsintouchteachers.R;
import com.f.schoolsintouchteachers.Reports;
import com.f.schoolsintouchteachers.adapters.AttendanceAdapter;
import com.f.schoolsintouchteachers.models.Cell;
import com.f.schoolsintouchteachers.models.ColTitle;
import com.f.schoolsintouchteachers.models.RowTitle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.zhouchaoyuan.excelpanel.ExcelPanel;

/**
 * Created by admin on 3/15/2018.
 */

public class Attendance extends Fragment{
    private ArrayList<RowTitle> rowTitles;
    private ArrayList<ColTitle> colTitles;
    private ArrayList<List<Cell>> cells;
    private ExcelPanel excelPanel;
    private BarChart barChart;
    private AttendanceAdapter adapter;
    private boolean isLoading;
    private int ROW_SIZE=12;
    int p,a;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);
        Reports.fab.setVisibility(View.VISIBLE);
        excelPanel = rootView.findViewById(R.id.content_container);
        barChart=rootView.findViewById(R.id.barchart);

        adapter = new AttendanceAdapter(getActivity(), blockListner);
        excelPanel.setAdapter(adapter);

        initData();

        return rootView;
    }
    private View.OnClickListener blockListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void initData() {
        rowTitles = new ArrayList<>();
        colTitles = new ArrayList<>();
        cells = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            cells.add(new ArrayList<Cell>());
        }
        loadData(false);

    }

        private void loadData(boolean b) {

            isLoading = true;
            Message message = new Message();
            message.arg1 = b ? 1 : 2;

            loadDataHandler.sendMessageDelayed(message, 1000);

        }

    private int PAGE_SIZE=31;
    private ArrayList<List<Cell>> cells1;
    @SuppressLint("HandlerLeak")
    private  Handler loadDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isLoading = false;

            ArrayList<RowTitle> rowTitles1 = genRowData();
           cells1 = genCellData();
            if (msg.arg1 == 1) {//history
               // historyStartTime -= ONE_DAY * PAGE_SIZE;
                rowTitles.addAll(0, rowTitles1);
                for (int i = 0; i < cells1.size(); i++) {
                    cells.get(i).addAll(0, cells1.get(i));
                }

                //加载了数据之后偏移到上一个位置去
                if (excelPanel != null) {
                    excelPanel.addHistorySize(PAGE_SIZE);
                }
            } else {
               // moreStartTime += ONE_DAY * PAGE_SIZE;
                rowTitles.addAll(rowTitles1);
                for (int i = 0; i < cells1.size(); i++) {
                    cells.get(i).addAll(cells1.get(i));
                }
            }
            if (colTitles.size() == 0) {
                colTitles.addAll(genColData());
            }

            adapter.setAllData(colTitles, rowTitles, cells);



            //barchart
           ArrayList<BarEntry> entriesGroup1 = new ArrayList<>();
            ArrayList<BarEntry> entriesGroup2 = new ArrayList<>();
            for (int j=0;j<cells1.size();j++){
                ArrayList<Cell> barcell=new ArrayList<>();
                barcell.addAll(cells1.get(j));
                p=0;
                a=0;
                for (int f=0;f<barcell.size();f++){
                    if (barcell.get(f).isChecked()){
                        p++;
                    }else {
                        a++;
                    }
                }
                entriesGroup1.add(new BarEntry(j,p));
                entriesGroup2.add(new BarEntry(j,a));



            }
            BarDataSet set1,set2;
            float groupSpace = 0.1f;
            float barSpace = 0f; // x2 dataset
            float barWidth =0.45f; // x2 dataset
            if (barChart.getData() != null &&
                    barChart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet)barChart.getData().getDataSetByIndex(0);
                set2 = (BarDataSet)barChart.getData().getDataSetByIndex(1);
                set1.setValues(entriesGroup1);
                set2.setValues(entriesGroup2);
                barChart.getData().notifyDataChanged();
                barChart.notifyDataSetChanged();
            }else{
               set1 = new BarDataSet(entriesGroup1, "Present");
                set2 = new BarDataSet(entriesGroup2, "Absent");
                set1.setColor(ContextCompat.getColor(getContext(),android.R.color.holo_green_dark));
                set2.setColor(ContextCompat.getColor(getContext(),android.R.color.holo_blue_bright));



                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                dataSets.add(set2);


//

//
                BarData data = new BarData(dataSets);
               data.setValueFormatter(new LargeValueFormatter());
                barChart.setData(data);
                barChart.setFitBars(true);
            }
            XAxis xAxis = barChart.getXAxis();

            final ArrayList<String> moths=new ArrayList<>();
            moths.add("Jan/Feb");
            moths.add("Jan/Feb");
            moths.add("Mar/Apr");
            moths.add("Mar/Apr");
            moths.add("May/Jun");
            moths.add("May/Jun");
            moths.add("Jul/Aug");
            moths.add("Jul/Aug");
            moths.add("Sep/Oct");
            moths.add("Sep/Oct");
            moths.add("Nov/Dec");
            moths.add("Nov/Dec");


           // XAxis xAxis = chart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
            xAxis.setCenterAxisLabels(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMaximum(12);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(moths));


            // specify the width each bar should have
            barChart.getBarData().setBarWidth(barWidth);
            barChart.getXAxis().setAxisMinimum(0);

             barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 12 );
              barChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
            barChart.invalidate();

          //


// (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
//final

        }
    };


    private ArrayList<ColTitle> genColData() {
        ArrayList<ColTitle> colTitles = new ArrayList<>();
        for (int i = 1; i <= ROW_SIZE; i++) {
            ColTitle colTitle = new ColTitle();
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
            try {
                colTitle.setMonth( monthDisplay.format(monthParse.parse(String.valueOf(i))));
                colTitles.add(colTitle);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return colTitles;
    }

    private ArrayList<List<Cell>> genCellData() {
        Random r=new Random();

        ArrayList<List<Cell>> cells = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            ArrayList<Cell> cellList = new ArrayList<>();
            cells.add(cellList);
            for (int j = 0; j < PAGE_SIZE; j++) {
                Cell cell = new Cell();
boolean tick=r.nextBoolean();


                cell.setChecked(tick);


                cellList.add(cell);
            }
        }
        return cells;
    }

    private ArrayList<RowTitle> genRowData() {
        ArrayList<RowTitle> rowTitles = new ArrayList<>();
        for (int i=1;i<=PAGE_SIZE;i++){
            rowTitles.add(new RowTitle(String.valueOf(i)));
        }
        return rowTitles;
    }


}
