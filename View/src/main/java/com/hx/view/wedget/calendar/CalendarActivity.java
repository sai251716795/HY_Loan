package com.hx.view.wedget.calendar;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import com.example.hx_view.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


/**
 * 日期选择空间
 * 
 */
public class CalendarActivity extends Activity implements View.OnClickListener {

	private GestureDetector gestureDetector = null;
	private CalendarAdapter calV = null;
	private ViewFlipper flipper = null;
	private GridView gridView = null;
	private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	/** 每次添加gridview到viewflipper中时给的标记 */
	private int gvFlag = 0;
	/** 当前的年月，现在日历顶端 */
	private TextView currentMonth;
	/** 上个月 */
	private ImageView prevMonth;
	/** 下个月 */
	private ImageView nextMonth;

	private int currMonth;
	int tempstart, tempend;

	public static final String ACTION_PICKER_ONE_DAY = "0";
	public static final String ACTION_PICKET_TWO_DAYS = "1";
	private String action;

	public CalendarActivity() {

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		System.out.println("当前月份=" + currMonth);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		currentMonth = (TextView) findViewById(R.id.currentMonth);
		prevMonth = (ImageView) findViewById(R.id.prevMonth);
		nextMonth = (ImageView) findViewById(R.id.nextMonth);
		setListener();

		gestureDetector = new GestureDetector(this, new MyGestureListener());
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.removeAllViews();
		calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear,
				year_c, month_c, day_c);
		addGridView();
		gridView.setAdapter(calV);
		flipper.addView(gridView, 0);
		addTextToTopTextView(currentMonth);
		action = getIntent().getAction();
		if (action.equals(ACTION_PICKET_TWO_DAYS))
			Toast.makeText(CalendarActivity.this, "请选择开始日期", Toast.LENGTH_SHORT)
					.show();
	}

	private class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
			if (e1.getX() - e2.getX() > 120) {
				if (calV.getShowMonth().equals(currMonth + "")) {
					return true;
				}
				// 像左滑动
				enterNextMonth(gvFlag);
				return true;
			} else if (e1.getX() - e2.getX() < -120) {

				// 向右滑动
				enterPrevMonth(gvFlag);
				return true;
			}
			return false;
		}
	}

	/**
	 * 移动到下一个月
	 * 
	 * @param gvFlag
	 */
	private void enterNextMonth(int gvFlag) {
		if (jumpMonth == currMonth) {
			return;
		}
		addGridView(); // 添加一个gridView
		jumpMonth++; // 下一个月

		calV = new CalendarAdapter(this, this.getResources(), jumpMonth,
				jumpYear, year_c, month_c, day_c);
		gridView.setAdapter(calV);
		addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
		gvFlag++;
		flipper.addView(gridView, gvFlag);
		flipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_out));
		flipper.showNext();
		flipper.removeViewAt(0);
	}

	/**
	 * 移动到上一个月
	 * 
	 * @param gvFlag
	 */
	private void enterPrevMonth(int gvFlag) {
		addGridView(); // 添加一个gridView
		jumpMonth--; // 上一个月

		calV = new CalendarAdapter(this, this.getResources(), jumpMonth,
				jumpYear, year_c, month_c, day_c);
		gridView.setAdapter(calV);
		gvFlag++;
		addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
		flipper.addView(gridView, gvFlag);

		flipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_out));
		flipper.showPrevious();
		flipper.removeViewAt(0);
	}

	/**
	 * 添加头部的年份 闰哪月等信息
	 * 
	 * @param view
	 */
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		// draw = getResources().getDrawable(R.drawable.top_day);
		// view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("年")
				.append(calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
	}

	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int Width = display.getWidth();
		int Height = display.getHeight();

		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(40);
		// gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		if (Width == 720 && Height == 1280) {
			gridView.setColumnWidth(40);
		}
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		gridView.setOnTouchListener(new OnTouchListener() {
			// 将gridview中的触摸事件回传给gestureDetector

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CalendarActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				// 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (startPosition <= position + 7
						&& position <= endPosition - 7) {
					String scheduleDay = calV.getDateByClickItem(position)
							.split("\\.")[0]; // 这一天的阳历
					// String scheduleLunarDay =
					// calV.getDateByClickItem(position).split("\\.")[1];
					// //这一天的阴历
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
					int tempyear = Integer.parseInt(scheduleYear);
					int tempmonth = Integer.parseInt(scheduleMonth);
					int tempday = Integer.parseInt(scheduleDay);

					if (tempday < 10) {
						scheduleDay = "0" + scheduleDay;
					}
					if (tempmonth < 10) {
						scheduleMonth = "0" + scheduleMonth;
					}
					if (action.equals(ACTION_PICKER_ONE_DAY)) {
						Intent i = new Intent();
						i.putExtra("data", scheduleYear + "" + scheduleMonth
								+ scheduleDay);
						setResult(10, i);
						finish();
					} else {
						if (days[0] == null) {
							if (action.equals(ACTION_PICKET_TWO_DAYS))
								Toast.makeText(CalendarActivity.this,
										"请选择结束日期", Toast.LENGTH_SHORT).show();
							days[0] = scheduleYear + "" + scheduleMonth + ""
									+ scheduleDay;
							tempstart = tempyear + tempmonth + tempday;
						} else {
							days[1] = scheduleYear + "" + scheduleMonth + ""
									+ scheduleDay;
							tempend = tempyear + tempmonth + tempday;
							if (tempstart > tempend) {
								days[2] = days[0];
								days[0] = days[1];
								days[1] = days[2];
							}
							Intent i = new Intent();
							i.putExtra("d1", days[0]);
							i.putExtra("d2", days[1]);
							setResult(10, i);
							System.out.println(days[0] + "\n" + days[1]);
							finish();
						}
					}
					arg1.setBackgroundColor(getResources().getColor(
							R.color.green_color));
				}

			}
		});
		gridView.setLayoutParams(params);
	}

	private void setListener() {
		prevMonth.setOnClickListener(this);
		nextMonth.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		System.out.println(gvFlag);
		// TODO Auto-generated method stub
		if (v.getId() == R.id.nextMonth) {// 下一个月
			if (calV.getShowMonth().equals(currMonth + "")) {
				System.out.println(calV.getShowMonth());
				return;
			}
			enterNextMonth(gvFlag);
		}else if(v.getId() == R.id.prevMonth){// 上一个月
			enterPrevMonth(gvFlag);
		}
	}

	// public void setOndaysselected(OnDaysSelected ondaysselected) {
	// this.ondaysselected = ondaysselected;
	// }
	// private OnDaysSelected ondaysselected;
	//
	//
	private String[] days = new String[3];
	// public interface OnDaysSelected{
	// public void onDaysSelected(String[] days);
	// }

}