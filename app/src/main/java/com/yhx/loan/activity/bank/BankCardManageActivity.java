package com.yhx.loan.activity.bank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.hx.view.widget.CustomDialog;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pay.library.config.AppConfig;
import com.pay.library.tool.Logger;
import com.pay.library.uils.GsonUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yhx.loan.R;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.adapter.BankCardAdapter;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.BankCardModel;
import com.yhx.loan.bean.UserBean;
import com.yhx.loan.server.UserDataServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BankCardManageActivity extends BaseCompatActivity implements AdapterView.OnItemLongClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tx_back)
    TextView txBack;
    @BindView(R.id.btn_back)
    LinearLayout btnBack;
    @BindView(R.id.add_bankCard)
    ImageView addBankCard;
    @BindView(R.id.bank_list)
    ListView bankList;
    @BindView(R.id.fresh_header)
    MaterialHeader freshHeader;//刷新头部
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    TextView ListHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_bank_card_manage);
        ButterKnife.bind(this);
        tvTitle.setText("我的银行卡");
        ListHeadView = headerView();
        bankList.addHeaderView(ListHeadView);
        bankCardAdapter = new BankCardAdapter(this, bankListData, 0);
        bankList.setAdapter(bankCardAdapter);
        iniBankList();
        refreshLayout.setOnRefreshListener(onRefreshListene);
        bankList.setOnItemLongClickListener(this);
    }

    //刷新操作
    OnRefreshListener onRefreshListene = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {

            UserDataServer dataServer = new UserDataServer(getContext()) {
                @Override
                public void ReceiveMessage(UserBean userBean) {
                    iniBankList();
                }

                @Override
                public void ReceiveError(Object o) {

                }
                @Override
                public  void pwdError(){
                    MyApplication.getInstance().initUserBeans(null);
                    Intent intent = new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            dataServer.RefreshUThread();
            refreshlayout.finishRefresh(1000);
        }
    };

    BankCardAdapter bankCardAdapter = null;

    List<BankCardModel> bankListData = new ArrayList<BankCardModel>();

    private void iniBankList() {
        bankListData = myApplication.getUserBeanData().getBankCardArray();
        if (bankListData.size() == 0) {
            ListHeadView.setVisibility(View.VISIBLE);
        } else {
            ListHeadView.setVisibility(View.GONE);
            bankCardAdapter = new BankCardAdapter(this, bankListData, 0);
            bankList.setAdapter(bankCardAdapter);
            bankCardAdapter.notifyDataSetChanged();//刷新数据
        }
    }

    Intent intent;

    @OnClick({R.id.btn_back, R.id.add_bankCard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.add_bankCard:
                intent = new Intent(context, AddBankActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myApplication.getUserBeanData() == null) {
            toast_short("请先登录...");
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        iniBankList();

    }

    private TextView headerView() {
        TextView textView = new TextView(this);
        textView.setText("还没有绑定银行卡哦~");
        textView.setTextColor(0xffffffff);
        textView.setTextSize(12);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0, 10, 0, 10);
        textView.setBackgroundColor(0xc1ff9249);
        return textView;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Log.e(TAG, "onItemLongClick: id="+ id);
       final BankCardModel bankCard = bankListData.get(position-1);
        String number = bankCard.getBankCardNumber();
        String name = bankCard.getBankName()+"("+number.substring(number.length()-4)+")";

        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        builder.setTitle("删除银行卡");
        builder.setMessage("您是否要删除银行卡：\n"+name);
        builder.setOkBtn("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                server(bankCard);
            }
        });
        builder.setCancelBtn("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        return false;
    }

    private void server(BankCardModel bankCard) {

        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map.putAll(myApplication.getHttpHeader());
        UserBean userBean = myApplication.getUserBeanData();
        map.put("mobile", userBean.getLoginName());//String	是	手机号(登录名)
        map.put("operateType", BankMap.unBinding);//String	绑定银行卡操作类型
        map.put("realName", userBean.getRealName());//  String(50)	是	真实姓名
        map.put("idCardNumber", userBean.getIdCardNumber());//  String(50)	是	身份证号
        map.put("bankName", bankCard.getBankName());//  String	是	开户行
        map.put("bankCardNumber", bankCard.getBankCardNumber());//	String	是	银行卡号
        map.put("bankCardPLMobile", bankCard.getBankCardPLMobile());//	String	是	银行卡预留电话
        map.put("cardType", "DC");//银行卡类型"CC：贷记卡  DC：借记卡
        //TODO 请求链接
        String url = AppConfig.Bank_Card_binding_url;
        String json = gson.toJson(map);
        showLoadingDialog("提交中...");
        Logger.json("请求数据：", json);
        okGo.<String>post(url)
                .upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                dismissLoadingDialog();

                Logger.json("成功：", response.body());
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    String respCode = jsonObject.getString("respCode");
                    if (respCode.equals("00")) {
                        String result = jsonObject.getString("result");
                        UserBean loginUserBean = GsonUtil.fromJson(result, UserBean.class);
                        myApplication.initUserBeans(loginUserBean);                                                         //设置静态用户数据
                        myApplication.getUserBeanData().saveOrUpdate("loginName=?", loginUserBean.getLoginName());  //储存用户数据
                        iniBankList();

                    } else {
                        toast_long("删除失败");
                    }
                } catch (JSONException e) {
                    showHitDialog("", "删除失败,数据异常");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                showLoadingDialog("提交中...");
            }

            @Override
            public void onFinish() {
                dismissLoadingDialog();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                toast_long("请求异常，请稍后再试！");
            }
        });
    }
}
