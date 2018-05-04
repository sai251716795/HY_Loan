package com.hx.view.wedget.wheelview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.hx_view.R;

/**
 * Created by wsq on 2016/5/24.
 */
public class DateSelectorLayout extends LinearLayout{

    private int START_YEAR = 1700,END_YEAR=2500;
    public  static String SELECTOR_YEAR = "0";
    public  static String SELECTOR_MONTH = "0";
    public  static String SELECTOR_DAY = "0";
    public  static String SELECTOR_HOUR = "0";
    public  static String SELECTOR_MINS = "0";

    private  List<String> yearDate = new ArrayList<String>();
    private  List<String> monthDate = new ArrayList<String>();
    private  List<String> dayDate = new ArrayList<String>();
    private  List<String> hourDate = new ArrayList<String>();
    private  List<String> minsDate = new ArrayList<String>();

    private DATE_TYPE date_type;

    private CycleWheelView cycle_year, cycle_month, cycle_day, cycle_hour, cycle_mins;
    private LinearLayout layout_year, layout_month, layout_day, layout_hour, layout_mins;
    private TextView tv_year, tv_month, tv_day, tv_hour, tv_mins;
    private View view;
    private Context mContext;
    private Calendar calendar;
    private boolean isShowLable = false;
    private int selector_font_color = 0;
    private int select_size = 20;
    private int showItems = 5;
    private boolean cycleEnable = true;
    private int lableColor;

    private boolean isRunYear  =false;
    private Calendar changeDate;


    public DateSelectorLayout(Context context) {
        super(context);
        init(context);
    }

    public DateSelectorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DateSelectorLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context){
        mContext = context;
        addDate();
        calendar =Calendar.getInstance();
        changeDate = Calendar.getInstance();

        selector_font_color = Color.parseColor("#000000");
        lableColor = Color.parseColor("#000000");

    }

    public void initView(){
        view  = LayoutInflater.from(mContext).inflate(R.layout.layout_date_seletor, null);

        if (date_type ==DATE_TYPE.YEAR_MONTH){
            initYear();
            initMonth(false);
        }else if(date_type ==DATE_TYPE.YEAR_MONTH_DAY){
            initYear();
            initMonth(true);
            initDay(false);
        }else if(date_type == DATE_TYPE.HOUR_MINS){
            initHour();
            initMins(false);
        }else if(date_type ==DATE_TYPE.ALL){

            initYear();
            initMonth(true);
            initDay(true);
            initHour();
            initMins(false);
        }
        this.addView(view);
    }


    /**
     * 初始化年份
     */
    private void initYear(){
        layout_year = (LinearLayout) view.findViewById(R.id.layout_year);
        layout_year.setVisibility(VISIBLE);
        cycle_year = (CycleWheelView) view.findViewById(R.id.cycle_year);
        tv_year = (TextView) view.findViewById(R.id.tv_year);
        tv_year.setVisibility(isShowLable ? View.VISIBLE : View.GONE);
        setLableParams(tv_year);
        cycle_year.setLabels(yearDate);
        setParams(cycle_year);
        SELECTOR_YEAR  = calendar.get(Calendar.YEAR)+"";
        cycle_year.setSelection(Integer.parseInt(SELECTOR_YEAR)-START_YEAR);
        cycle_year.setOnWheelItemSelectedListener(yearListener);
    }

    /**
     * 初始化月份
     */
    private void initMonth(boolean isShowLine){
        layout_month = (LinearLayout) view.findViewById(R.id.layout_month);
        layout_month.setVisibility(VISIBLE);
        cycle_month = (CycleWheelView) view.findViewById(R.id.cycle_month);
        tv_month = (TextView) view.findViewById(R.id.tv_month);
        View view_month_line = view.findViewById(R.id.view_month_line);
        view_month_line.setVisibility(isShowLine ? View.VISIBLE :View.GONE);
        tv_month.setVisibility(isShowLable ? View.VISIBLE : View.GONE);
        setLableParams(tv_month);
        cycle_month.setLabels(monthDate);
        setParams(cycle_month);
        SELECTOR_MONTH = (calendar.get(Calendar.MONTH)+1)+"";
        Log.d("========","===当前月="+(Integer.parseInt(SELECTOR_MONTH)-1));
        cycle_month.setSelection(Integer.parseInt(SELECTOR_MONTH)-1);
        cycle_month.setOnWheelItemSelectedListener(monthListener);
    }

    /**
     * 初始化日
     */
    private void initDay(boolean isShowLine){
        layout_day = (LinearLayout) view.findViewById(R.id.layout_day);

        layout_day.setVisibility(VISIBLE);

        cycle_day = (CycleWheelView) view.findViewById(R.id.cycle_day);

        tv_day = (TextView) view.findViewById(R.id.tv_day);

        View view_day_line = view.findViewById(R.id.view_day_line);

        view_day_line.setVisibility(isShowLine ? View.VISIBLE :View.GONE);

        tv_day.setVisibility(isShowLable ? View.VISIBLE : View.GONE);

        setLableParams(tv_day);

        getDay(SELECTOR_YEAR, SELECTOR_MONTH);

        cycle_day.setLabels(dayDate);

        setParams(cycle_day);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        cycle_day.setSelection(day-1);

        cycle_day.setOnWheelItemSelectedListener(dayListener);
    }


    /**
     * 初始化时间
     */
    private void initHour(){
        layout_hour = (LinearLayout) view.findViewById(R.id.layout_hour);

        layout_hour.setVisibility(VISIBLE);

        cycle_hour = (CycleWheelView) view.findViewById(R.id.cycle_hour);

        tv_hour = (TextView) view.findViewById(R.id.tv_hour);

        tv_hour.setVisibility(isShowLable ? View.VISIBLE : View.GONE);

        setLableParams(tv_hour);

        cycle_hour.setLabels(hourDate);

        setParams(cycle_hour);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        cycle_hour.setSelection(hour);

        Log.d("","=====小时显示的位置===="+ (hour-1)+"");

        cycle_hour.setOnWheelItemSelectedListener(hourListener);
    }

    /**
     * 初始化分钟
     */
    private void initMins(boolean isShowLine){
        layout_mins = (LinearLayout) view.findViewById(R.id.layout_mins);

        layout_mins.setVisibility(VISIBLE);

        cycle_mins = (CycleWheelView) view.findViewById(R.id.cycle_mins);

        tv_mins = (TextView) view.findViewById(R.id.tv_mins);

        View view_mins_line = view.findViewById(R.id.view_mins_line);

        view_mins_line.setVisibility(isShowLine ? View.VISIBLE : View.GONE);

        tv_mins.setVisibility(isShowLable ? View.VISIBLE : View.GONE);

        setLableParams(tv_mins);

        cycle_mins.setLabels(minsDate);

        setParams(cycle_mins);

        int mins = calendar.get(Calendar.MINUTE);

        cycle_mins.setSelection(mins);

        Log.d("","=====分钟显示的位置===="+ mins+"");

        cycle_mins.setOnWheelItemSelectedListener(minsListener);
    }


    private void setLableParams(TextView text){
        text.setTextSize(select_size);
        text.setTextColor(selector_font_color);
        text.setBackgroundColor(Color.WHITE);
    }


    private void addDate(){

        for (int i=0; i< END_YEAR - START_YEAR; i++){


            yearDate.add((START_YEAR+i)+"");

            if (i<12){
                monthDate.add((i+1)<10 ? "0"+(i+1) : (i+1)+"");
            }
            if (i<28){
                dayDate.add((i+1) <10 ? "0"+(i+1) : (i+1)+"");
            }
            if (i<24){
                hourDate.add(i < 10 ? "0"+i : i+"");
            }
            if (i<60){
                minsDate.add((i) < 10 ? "0"+i : i+"");
            }
        }
    }

    public void getDay(String syear, String smonth){

        int year = Integer.parseInt(syear);
        int month = Integer.parseInt(smonth);
        changeDate.set(year, month==1 ? 12 : month-1 , 1);

        int maxDay = changeDate.getActualMaximum(Calendar.DATE);
        if (dayDate.size() == maxDay){
            return ;
        }else if(dayDate.size()<maxDay){
            for (int i = dayDate.size(); i< maxDay; i++){
                dayDate.add((i+1)+"");
            }

        }else if(dayDate.size() > maxDay){

            for (int i = dayDate.size()-1 ; i >= maxDay; i--){
                dayDate.remove(i);
            }
        }

        if(cycle_day!=null){

            cycle_day.setLabels(dayDate);
        }

    }


    private void setParams(CycleWheelView view){

        view.setCycleEnable(cycleEnable);

        //设置字体颜色
        view.setLabelColor(lableColor);
        //设置选中时的字体颜色
        view.setLabelSelectColor(selector_font_color);

        view.setSelectorTextSize(select_size);

        try {
            view.setWheelSize(showItems);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }

    }

    public void setType(DATE_TYPE date_type){
        this.date_type = date_type;

    }

    /**
     * 设置显示在item上面的字体颜色
     * @param lableColor
     */
    public void setLableColor(int lableColor){
        this.lableColor = lableColor;
    }
    /**
     * 设置是否需要循环显示
     * @param cycleEnable
     */
    public void setCycleEnable(boolean cycleEnable){
        this.cycleEnable = cycleEnable;
    }

    /**
     * 设置显示的items,必须是奇数且大于3；
     * @param showItems
     */
    public void setVisivilityItems(int showItems){
        if (showItems<3){
           return;
        }
        if (showItems % 2==0){
            showItems = showItems+1;
        }
        this.showItems = showItems;
    }

    /**
     * 设置选中时是否需要显示字
     * @param isShowLable
     */
    public void setShowLable(boolean isShowLable){
        this.isShowLable = isShowLable;
    }

    /**
     * 设置选中的字体颜色
     * @param color
     */
    public void setSelectColor(int color){

        this.selector_font_color = color;
    }

    /***
     * 设置选中时的字体大小
     * @param size
     */
    public void setSelectSize(int size){
        this.select_size = size;

    }


    /**
     * 年份监听
     */
    CycleWheelView.WheelItemSelectedListener yearListener = new CycleWheelView.WheelItemSelectedListener() {
        @Override
        public void onItemSelected(int position, String label) {
            SELECTOR_YEAR = label;
            getDay(SELECTOR_YEAR, SELECTOR_MONTH);
            if (null != cycle_day){
                cycle_day.setSelection(Integer.parseInt(SELECTOR_DAY)-1);
            }
        }
    };

    /**
     * 月份监听
     */
    CycleWheelView.WheelItemSelectedListener monthListener = new CycleWheelView.WheelItemSelectedListener() {
        @Override
        public void onItemSelected(int position, String label) {
            SELECTOR_MONTH = label;

            getDay(SELECTOR_YEAR, SELECTOR_MONTH);
            if (null != cycle_day){
                cycle_day.setSelection(Integer.parseInt(SELECTOR_DAY)-1);
            }
        }
    };

    CycleWheelView.WheelItemSelectedListener dayListener = new CycleWheelView.WheelItemSelectedListener() {
        @Override
        public void onItemSelected(int position, String label) {
            SELECTOR_DAY = label;
        }
    };

    CycleWheelView.WheelItemSelectedListener hourListener = new CycleWheelView.WheelItemSelectedListener() {
        @Override
        public void onItemSelected(int position, String label) {

            int hour = Integer.parseInt(label);
            SELECTOR_HOUR = label;
        }
    };

    CycleWheelView.WheelItemSelectedListener minsListener = new CycleWheelView.WheelItemSelectedListener() {
        @Override
        public void onItemSelected(int position, String label) {
            int mins = Integer.parseInt(label);
            SELECTOR_MINS = label;
        }
    };




}
