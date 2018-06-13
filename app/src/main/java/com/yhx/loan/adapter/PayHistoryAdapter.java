package com.yhx.loan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.pay.library.config.AppConfig;
import com.pay.library.config.URLConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.DateUtils;
import com.pay.library.uils.GsonUtil;
import com.pay.library.uils.TimeUtils;
import com.yhx.loan.R;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.pay.MacObject;
import com.yhx.loan.bean.pay.MacUtils;
import com.yhx.loan.bean.pay.OrderQueryResult;
import com.yhx.loan.bean.pay.PayListBean;
import com.yhx.loan.bean.xybank.RepayHistoryList;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 25171 on 2017/11/20.
 */

public class PayHistoryAdapter extends BaseAdapter {
    private List<PayListBean> arryList = new ArrayList<>();
    private Context context;


    public PayHistoryAdapter(Context context, List<PayListBean> arryList) {
        this.arryList = arryList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return arryList.size();
    }

    @Override
    public Object getItem(int position) {
        return arryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pay_histroy_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PayListBean payList = arryList.get(position);
        //清算状态
        viewHolder.clearState.setText(clearStatusMap.get(payList.getClear_state()));
        viewHolder.trStatus.setTextColor(0xff666666);
        // 状态
        if (payList.getTr_status().equals("1")) {
            viewHolder.trStatus.setText("交易失败");
            viewHolder.trStatus.setTextColor(0xffea0606);
        } else if (payList.getTr_status().equals("0")) {
            viewHolder.trStatus.setText("交易成功");
            queryOrd(payList.getOrder_no(),viewHolder.clearState);
        } else if (payList.getTr_status().equals("2")) {
            viewHolder.trStatus.setText("未支付");
        } else if (payList.getTr_status().equals("3")) {
            viewHolder.trStatus.setText("处理中");
        } else if (payList.getTr_status().equals("4")) {
            viewHolder.trStatus.setText("已支付冲正中");
        }
        //订单号
        viewHolder.orderNo.setText(payList.getOrder_no());
        // 摘要
        viewHolder.orderName.setText(payList.getOrder_name());
        // 时间
        Date date = DateUtils.dateWithString(payList.getStart_time(), "yyyyMMddHHmmss");
        viewHolder.startTime.setText(TimeUtils.multiSendTimeToStr(date.getTime()));
        // 金额
        viewHolder.trAmt.setText(String.format("%.2f",Double.valueOf(payList.getTr_amt()))+"元");
        // payChannel 支付方式
        viewHolder.payChannel.setText(payList.getPay_channel().equals("0001")?"微信支付":"支付宝支付");


        return convertView;
    }

    private void queryOrd(String orderId,final TextView textView) {
        final MacObject macObject = new MacObject();
        macObject.setOrgCode(URLConfig.yhxBranchCode);
        macObject.setSecretKey(URLConfig.yhxsecretKey);
        // 查询
        macObject.setTrCode("04");
        Map<String, Object> map = new HashMap<>();
        //商户在永鸿兴付入驻生成的商户号
        map.put("merch_no", MyApplication.getInstance().getUserBeanData().getMerch_no());
        //20位订单号，系统唯一
        map.put("order_no", orderId);
        macObject.setReqData(map);
        try {
            String sendString = MacUtils.getClientPack(macObject);
            System.out.println("mac:" + sendString);

            MyApplication.getInstance().okGo.<String>post(AppConfig.payAPP_url)
                    .upJson(sendString)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Logger.e("onSuccess", response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if ("0000".equals(jsonObject.getString("res_code"))) {
                                    OrderQueryResult result = GsonUtil.fromJson(response.body(), OrderQueryResult.class);
                                    textView.setText(clearStatusMap.get(result.getClear_state()));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder {
        @BindView(R.id.order_no)
        TextView orderNo;
        @BindView(R.id.tr_status)
        TextView trStatus;
        @BindView(R.id.tr_amt)
        TextView trAmt;
        @BindView(R.id.start_time)
        TextView startTime;
        @BindView(R.id.pay_channel)
        TextView payChannel;
        @BindView(R.id.order_name)
        TextView orderName;
        @BindView(R.id.clear_state)
        TextView clearState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    static Map<String ,String> clearStatusMap = new HashMap<>();
    static {
        clearStatusMap.put("0","未清算");
        clearStatusMap.put("1","一级清算中");
        clearStatusMap.put("2","一级清算失败");
        clearStatusMap.put("3","一级清算完成");
        clearStatusMap.put("4","二级清算中");
        clearStatusMap.put("5","二级清算失败");
        clearStatusMap.put("6","二级清算完成");
        clearStatusMap.put("7","清算成功");
    }
}
