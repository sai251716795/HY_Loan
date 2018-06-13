package com.yhx.loan.activity.pay;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hx.view.calendar.CaledarAdapter;
import com.hx.view.calendar.CalendarBean;
import com.hx.view.calendar.CalendarDateView;
import com.hx.view.calendar.CalendarUtil;
import com.hx.view.calendar.CalendarView;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pay.library.config.AppConfig;
import com.pay.library.config.URLConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.DateUtils;
import com.pay.library.uils.GsonUtil;
import com.yhx.loan.R;
import com.yhx.loan.adapter.PayHistoryAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.bean.pay.MacObject;
import com.yhx.loan.bean.pay.MacUtils;
import com.yhx.loan.bean.pay.PayListBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hx.view.calendar.Utils.px;

public class PayHistoryListActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.pay_history_list)
    ListView payHistoryList;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.calendarDateView)
    CalendarDateView mCalendarDateView;

    List<PayListBean> arrayData = new ArrayList<>();
    PayHistoryAdapter myAdapter;
    String trDate = "";
    String nowDate = DateUtils.getNowDate("yyyyMMdd");
    TextView headView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_pay_history_list);
        ButterKnife.bind(this);
        rightIcon.setVisibility(View.GONE);
        myAdapter = new PayHistoryAdapter(getContext(), arrayData);
        payHistoryList.setAdapter(myAdapter);
        setHeaderView();
        trDate = DateUtils.getNowDate("yyyyMMdd"); //结束时间
        getListServer(trDate);
        initView();

    }

    private void initView() {

        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(48), px(48));
                    convertView.setLayoutParams(params);
                }
                view = (TextView) convertView.findViewById(R.id.text);

                view.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    view.setTextColor(0xff9299a1);
                } else {
                    view.setTextColor(0xffffffff);
                }
                return convertView;
            }
        });

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                title.setText(bean.year + "年" + bean.moth + "月" + bean.day+"日");
                String selectDate = ""+bean.year+ bean.moth+ bean.day;
                trDate = DateUtils.changeFormatDate(selectDate,"yyyyMd","yyyyMMdd");

                if (!DateUtils.compare_date(trDate, "20180601", "yyyyMMdd")) {
                    headView.setText("2018年06月01日前无数据查询");
                    headView.setVisibility(View.VISIBLE);
                    arrayData = new ArrayList<>();
                    lsitViewChanged();
                    return;
                }else if(DateUtils.compare_date(trDate, nowDate, "yyyyMMdd")){
                    headView.setText(DateUtils.getNowDate("MM月dd日")+"后无数据查询");
                    headView.setVisibility(View.VISIBLE);
                    arrayData = new ArrayList<>();
                    lsitViewChanged();
                    return;
                }
                getListServer(trDate);

            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        title.setText(data[0] + "年" + data[1] + "月" + data[2]+"日");
    }

    /**
     * 数据请求加载
     */
    private void getListServer(String startDate) {

        final MacObject macObject = new MacObject();
        macObject.setOrgCode(URLConfig.yhxBranchCode);
        macObject.setSecretKey(URLConfig.yhxsecretKey);
        // 查询
        macObject.setTrCode("06");
        Map<String, Object> map = new HashMap<>();
        map.put("merch_no", myApplication.getUserBeanData().getMerch_no());  //商户在永鸿兴付入驻生成的商户号
        map.put("tr_date", startDate);          //开始交易日期
        macObject.setReqData(map);
        try {
            String sendString = MacUtils.getClientPack(macObject);
            System.out.println("mac:" + sendString);

            okGo.<String>post(AppConfig.payAPP_url)
                    .upJson(sendString)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Logger.e("onSuccess", response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if ("0000".equals(jsonObject.getString("res_code"))) {
                                    List<PayListBean> list = GsonUtil.jsonToArrayList(jsonObject.getString("item"), PayListBean.class);
                                    addListDataRefresh(list);
                                } else {
                                    headView.setVisibility(View.VISIBLE);
                                    headView.setText(title.getText().toString()+"无交易记录");
                                    arrayData = new ArrayList<>();
                                    lsitViewChanged();
                                    Logger.i("没有数据");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            toast_short("网络连接失败...");
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addListDataRefresh(List<PayListBean> list) {
        headView.setVisibility(View.GONE);
        arrayData = new ArrayList<>();
//        int startIndex = arrayData.size() - 1;
        //移除未支付订单
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTr_status().equals("2")) {
                list.remove(i);
                i--;
            }
        }

        if(list.size()==0){
            headView.setVisibility(View.VISIBLE);
            headView.setText(title.getText().toString()+"无交易记录");
        }
        arrayData.addAll(list);
        //排序
        Collections.sort(arrayData, new Comparator<PayListBean>() {
            @Override
            public int compare(PayListBean o1, PayListBean o2) {
                int i = o2.getStart_time().compareTo(o1.getStart_time());
                return i;
            }
        });
        lsitViewChanged();

//        if (startIndex > 0) {
//            payHistoryList.setSelection(startIndex);//跳转显示到第n条数据
//        }

    }

    private void lsitViewChanged(){
        myAdapter = new PayHistoryAdapter(getContext(), arrayData);
        payHistoryList.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_back, R.id.right_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.right_icon:
                break;
        }
    }


    protected void showCustomDialog(String title, String msg, final boolean isFinish) {
        if (title == null)
            title = "提示";
        if (TextUtils.isEmpty(msg)) {
            msg = "信息错误！";
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false);
        builder.setTitle(title)
                .setMessage(msg)
                .setOkBtn("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        if (isFinish)
                            finish();
                    }
                });
        builder.create().show();
    }


    private void setHeaderView(){
        headView = new TextView(getContext());
        headView.setGravity(Gravity.CENTER);
        headView.setPadding(0,20,0,20);
        headView.setTextColor(0xffffffff);
        headView.setBackgroundColor(0xff29a6ef);
        headView.setText("今日交易数据~");
        payHistoryList.addHeaderView(headView);
    }

}
