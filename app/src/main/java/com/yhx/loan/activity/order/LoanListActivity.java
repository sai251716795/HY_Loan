package com.yhx.loan.activity.order;
/**
 * ===============================================
 *
 * @company 恒圆金服
 * @author niusaibing
 * 功能 ：贷款交易记录
 * @finish
 * @version 1.0
 * ===============================================
 */

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pay.library.uils.GsonUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.adapter.LoanOrderAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.server.UserLoanDataServer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoanListActivity extends BaseCompatActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.title_check)
    CheckBox titleCheck;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.right_date_image)
    ImageView rightDateImage;
    @BindView(R.id.order_list)
    ListView orderList;
    @BindView(R.id.filter_tx1)
    TextView filterTx1;
    @BindView(R.id.filter_tx2)
    TextView filterTx2;
    @BindView(R.id.filter_tx3)
    TextView filterTx3;
    @BindView(R.id.filter_tx4)
    TextView filterTx4;
    @BindView(R.id.filter_lay)
    LinearLayout filterLay;
    @BindView(R.id.not_order_lay)
    LinearLayout notOrderLay;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;//刷新头部
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    LoanOrderAdapter orderAdapter = null;
    List<LoanApplyBasicInfo> datalist = null;

    int loanType = 0;
    public static int type_List = 0;
    public static int type_repay = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_trade_list);
        ButterKnife.bind(this);

        initViewData();
        loanType = getIntent().getIntExtra("loanType", 0);
        String loanArrayListString =  getIntent().getStringExtra("loan_array");
        Log.e(TAG, "onCreate: "+loanArrayListString );
        if(loanArrayListString!=null){
            Type type = new TypeToken<ArrayList<LoanApplyBasicInfo>>() {}.getType();
            List<LoanApplyBasicInfo> arrayList = new Gson().fromJson(loanArrayListString, type);
            if (arrayList != null) {
                datalist = arrayList;
                orderAdapter.setArrayData(datalist);
                orderAdapter.notifyDataSetChanged();
                notOrderLay.setVisibility(View.GONE);
            }
        } else {
            Log.e(TAG, "type = List  " );
            requestLoanData();
        }
    }

    private void initViewData() {

        titleCheck.setChecked(false);
        titleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    filterLay.setVisibility(View.VISIBLE);
                    Animator animator = AnimatorInflater.loadAnimator(LoanListActivity.this, R.animator.top_menu);
                    animator.setTarget(filterLay);
                    animator.start();
                } else {
                    Animator animator = AnimatorInflater.loadAnimator(LoanListActivity.this, R.animator.top_menu_close);
                    animator.setTarget(filterLay);
                    animator.start();
                }
            }
        });

        datalist = new ArrayList<>();
        orderAdapter = new LoanOrderAdapter(getContext());
        orderAdapter.setArrayData(datalist);
        orderList.setAdapter(orderAdapter);
        orderList.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(onRefreshListene);
    }

    //刷新操作
    OnRefreshListener onRefreshListene = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            setData();
            refreshlayout.finishRefresh(1500);
        }
    };

    private void setData() {
        if (MyApplication.getInstance().getUserBeanData() == null) {
            Toast.makeText(getContext(), "请先登录...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        requestLoanData();
    }

    private void requestLoanData() {
        if (MyApplication.getInstance().getUserBeanData() != null) {
            if (MyApplication.getInstance().getUserBeanData().isRealNameState()) {
                UserLoanDataServer server = new UserLoanDataServer(getContext()) {
                    @Override
                    public void ReceiveMessage(List<LoanApplyBasicInfo> list) {
                        datalist = list;
                        if (loanType == type_repay) {
                            //移除不属于还款条件的订单
                            for (int i = 0; i < datalist.size(); i++) {
                                LoanApplyBasicInfo loan = datalist.get(i);

                                if (!loan.getApplyStatus().equals("400")) {
                                    datalist.remove(i);
                                    i--;
                                }
                            }
                        }
                        if (datalist.size() > 0) {
                            orderAdapter.setArrayData(datalist);
                            orderAdapter.notifyDataSetChanged();
                            notOrderLay.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getContext(), "无贷款条件订单", Toast.LENGTH_SHORT).show();
                            notOrderLay.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void ReceiveError(Object o) {
                        toast_long("数据加载失败~ V ~");
                    }
                };
                server.RefreshUThread();

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.btn_back, R.id.right_date_image, R.id.filter_tx1,
            R.id.filter_tx2, R.id.filter_tx3, R.id.filter_tx4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.right_date_image:
                selectDateDialog();
                break;
            case R.id.filter_tx1:
                selectCheckPress(filterTx1);
                break;
            case R.id.filter_tx2:
                selectCheckPress(filterTx2);
                break;
            case R.id.filter_tx3:
                selectCheckPress(filterTx3);
                break;
            case R.id.filter_tx4:
                selectCheckPress(filterTx4);
                break;
        }
    }

    private void selectDateDialog() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(LoanListActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        toast_short(i + "年" + (i1 + 1) + "月" + i2 + "日");
                    }
                }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        datePickerDialog.setTitle("选择日期");
        datePickerDialog.getDatePicker().setMaxDate((new Date()).getTime());
        datePickerDialog.show();
    }

    private void defaultCheckText() {
        filterTx1.setBackgroundResource(R.drawable.btn_check_default);
        filterTx1.setTextColor(getResources().getColor(R.color.textblack));
        filterTx2.setBackgroundResource(R.drawable.btn_check_default);
        filterTx2.setTextColor(getResources().getColor(R.color.textblack));
        filterTx3.setBackgroundResource(R.drawable.btn_check_default);
        filterTx3.setTextColor(getResources().getColor(R.color.textblack));
        filterTx4.setBackgroundResource(R.drawable.btn_check_default);
        filterTx4.setTextColor(getResources().getColor(R.color.textblack));
    }

    private void selectCheckPress(TextView textView) {
        defaultCheckText();
        textView.setBackgroundResource(R.drawable.btn_check_press);
        textView.setTextColor(getResources().getColor(R.color.green_color));
        titleCheck.setText(textView.getText().toString());
        titleCheck.setChecked(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//       if( datalist.get(position).getApplyStatus().equals("400")) {
//           Intent intent = new Intent(getContext(), RepayTableActivity.class);
//           Bundle bundle = new Bundle();
//           bundle.putSerializable("loanOrder", datalist.get(position));
//           intent.putExtras(bundle);
//           startActivity(intent);
//       }
        Intent intent = new Intent(getContext(), LoanDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("LoanDetails", datalist.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myApplication.getUserBeanData() == null) {
            toast_short("请先登录...");
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
