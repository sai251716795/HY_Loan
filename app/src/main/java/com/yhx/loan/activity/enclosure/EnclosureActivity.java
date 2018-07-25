package com.yhx.loan.activity.enclosure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hx.view.wedget.flashview.DepthPageTransformer;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.pay.library.uils.DensityUtil;
import com.yhx.loan.R;
import com.yhx.loan.adapter.StringArray;
import com.yhx.loan.base.BaseCompatActivity;
import com.yhx.loan.view.CustomViewPager;
import com.yhx.loan.view.SelectDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnclosureActivity extends BaseCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.idCardBt)
    TextView idCardBt;
    @BindView(R.id.bankCardBt)
    TextView bankCardBt;
    @BindView(R.id.pcreditBt)
    TextView pcreditBt;
    @BindView(R.id.rightViewPager)
    CustomViewPager rightViewPager;
    @BindView(R.id.leftMenuLayout)
    LinearLayout leftMenuLayout;
    @BindView(R.id.add_other)
    Button addOther;
    private List<Fragment> mfragments;
    private FragmentPagerAdapter madapter;
    RightBankCardFragment bankCardFragment;
    RightIDCardFragment idCardFragment;
    RightOtherFragment otherFragment;
    RightPCreditFragment pCreditFragment;

    private List<String> selectList = new ArrayList<>();

    public static final String FILE_TYPE_IDCARD = "01"; //身份证
    public static final String FILE_TYPE_BANK = "02"; //银行卡
    public static final String FILE_TYPE_PCREDIT = "03";//个人征信

    public List<TextView> menuBtnList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        setContentView(R.layout.activity_enclosure);
        ButterKnife.bind(this);
        tvTitle.setText("附件上传");
        rightViewPager.setScanScroll(false);
        initSelectList();
        initView();
        setSelect(0);
    }

    private void initView() {
        menuBtnList.add(idCardBt);
        menuBtnList.add(bankCardBt);
        menuBtnList.add(pcreditBt);

        mfragments = new ArrayList<>();
        idCardFragment = new RightIDCardFragment();
        bankCardFragment = new RightBankCardFragment();
        pCreditFragment = new RightPCreditFragment();
        mfragments.add(idCardFragment);
        mfragments.add(bankCardFragment);
        mfragments.add(pCreditFragment);

        madapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = mfragments.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", "" + position);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return mfragments.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment fragment = (Fragment) super.instantiateItem(container, position);
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
                return fragment;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = mfragments.get(position);
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
//                 super.destroyItem(container, position, object);
            }
        };

        rightViewPager.setPageTransformer(true, new DepthPageTransformer());
        rightViewPager.setAdapter(madapter);

    }

    private void setSelect(int i) {
        rightViewPager.setCurrentItem(i);
    }

    @OnClick({R.id.btn_back, R.id.idCardBt, R.id.bankCardBt, R.id.pcreditBt, R.id.add_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.idCardBt:
                setSelect(0);
                selectBt(idCardBt);
                break;
            case R.id.bankCardBt:
                setSelect(1);
                selectBt(bankCardBt);
                break;
            case R.id.pcreditBt:
                setSelect(2);
                selectBt(pcreditBt);
                break;
            case R.id.add_other:
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        toast_short(selectList.get(position));
                        newSelectMenuButton(mfragments.size(), selectList.get(position));
                        selectList.remove(position);
                    }
                }, selectList);
                break;
        }
    }

    void initBtBackGround() {
        for (TextView view : menuBtnList) {
            view.setBackgroundColor(getResources().getColor(R.color.white));
            view.setTextColor(getResources().getColor(R.color.textblack));
        }
    }

    /**
     * 给选中的textbtn 设置文字颜色与背景颜色，初始化其他控件
     */
    void selectBt(TextView view) {
        initBtBackGround();
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        view.setTextColor(getResources().getColor(R.color.white));
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 初始化案件类型
     */
    private void initSelectList() {
        selectList.add("医社保公积金");
        selectList.add("房产证明");
        selectList.add("婚姻证明");
        selectList.add("工作证明");
        selectList.add("车辆证明");
        selectList.add("收入证明");
        selectList.add("银行流水");
        selectList.add("户口本");
        selectList.add("通话详单");
        selectList.add("营业执照");
        selectList.add("其他附件");
    }

    /**
     * 新建按钮
     */
    private void newSelectMenuButton(int tag, String text) {
        // 创建一个文本按钮，放到leftMenuLayout中
        final TextView button = new TextView(getContext());
        button.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        button.setText(text);
        button.setTag(tag);
        int dp_10 = DensityUtil.dp2px(getContext(), 10);
        int dp_5 = DensityUtil.dp2px(getContext(), 5);
        button.setPadding(dp_10, dp_5, 0, dp_5);
        button.setTextSize(14);
        button.setBackgroundColor(getResources().getColor(R.color.white));
        button.setTextColor(getResources().getColor(R.color.textblack));
        //给button添加点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "new button onClick: tag :" + ((int) v.getTag()));
                setSelect((int) v.getTag());
                selectBt(button);
            }
        });
        // 加入布局
        leftMenuLayout.addView(button);
        // 加入到菜单集合中
        menuBtnList.add(button);
        // 添加新的RightOtherFragment 加入到madapter中，并通知事务更新。
        RightOtherFragment newOtherFragment = new RightOtherFragment();
        newOtherFragment.setSelectType(StringArray.getMapKey(RightOtherFragment.typeNameMap, text));
        mfragments.add(newOtherFragment);
        madapter.notifyDataSetChanged();
        // 展示新添加的子页面
        setSelect(tag);
        selectBt(button);

    }
}
