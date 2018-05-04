package com.yhx.loan.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import com.lzy.okgo.utils.OkLogger;
import com.pay.library.uils.Logger;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.activity.order.LoanDetailsActivity;
import com.yhx.loan.adapter.LoanOrderAdapter;
import com.yhx.loan.adapter.OrderAdapter;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.LoanApplyBasicInfo;
import com.yhx.loan.bean.TradeOrder;
import com.yhx.loan.server.UserLoanDataServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.title_check)
    CheckBox titleCheck;
    @BindView(R.id.right_date_image)
    ImageView rightDateImage;
    @BindView(R.id.left_loan_btn)
    TextView leftLoanBtn;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;
    @BindView(R.id.order_list)
    ListView orderList;
    @BindView(R.id.not_order_lay)
    LinearLayout notOrderLay;
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
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    LoanOrderAdapter orderAdapter = null;
    List<LoanApplyBasicInfo> datalist = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initViewData();
        requestLoanData();
        return rootView;
    }

    private void initViewData() {
        titleCheck.setChecked(false);
        titleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    filterLay.setVisibility(View.VISIBLE);
                    Animator animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.top_menu);
                    animator.setTarget(filterLay);
                    animator.start();
                } else {
                    Animator animator = AnimatorInflater.loadAnimator(getActivity(), R.animator.top_menu_close);
                    animator.setTarget(filterLay);
                    animator.start();
                }
            }
        });

        datalist = new ArrayList<>();
        orderAdapter = new LoanOrderAdapter(getActivity(), datalist);
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
            Toast.makeText(getActivity(), "请先登录...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        requestLoanData();
    }

    private void requestLoanData() {
        if (MyApplication.getInstance().getUserBeanData() != null) {
            if (MyApplication.getInstance().getUserBeanData().isRealNameState()) {
                UserLoanDataServer server = new UserLoanDataServer(getActivity()) {
                    @Override
                    public void ReceiveMessage(List<LoanApplyBasicInfo> list) {
                        datalist = list;
                        orderAdapter = new LoanOrderAdapter(getActivity(), datalist);
                        orderList.setAdapter(orderAdapter);
                        orderAdapter.notifyDataSetChanged();
                        if (datalist.size() > 0) {
                            notOrderLay.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getContext(),"无记录",Toast.LENGTH_SHORT).show();
                            notOrderLay.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void ReceiveError(Object o) {

                    }
                };
                server.RefreshUThread();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.right_date_image, R.id.left_loan_btn, R.id.filter_tx1,
            R.id.filter_tx2, R.id.filter_tx3, R.id.filter_tx4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_date_image:
                selectDateDialog();
                break;
            case R.id.left_loan_btn:
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Toast.makeText(getActivity(), i + "年" + (i1 + 1) + "月" + i2 + "日", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getActivity(), LoanDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("LoanDetails", datalist.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestLoanData();
    }
}
