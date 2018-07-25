package com.yhx.loan.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hx.view.widget.MyGridView;
import com.pay.library.uils.GsonUtil;
import com.yhx.loan.R;
import com.yhx.loan.activity.loan.LoanAuthActivity;
import com.yhx.loan.activity.login.LoginActivity;
import com.yhx.loan.adapter.LoanMenuAdapter;
import com.yhx.loan.base.MyApplication;
import com.yhx.loan.bean.LoanMenu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;

public class HomeFragment extends Fragment implements BGABanner.Delegate<ImageView, String>, BGABanner.Adapter<ImageView, String>, AdapterView.OnItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.loan_Range)
    TextView loanRange;
    @BindView(R.id.loan_small_btn)
    Button loanSmallBtn;
    @BindView(R.id.xffqd_lay)
    RelativeLayout xffqdLay;
    @BindView(R.id.gxd_lay)
    RelativeLayout gxdLay;
    @BindView(R.id.bdd_layout)
    RelativeLayout bddLayout;
    @BindView(R.id.fcygd_layout)
    RelativeLayout fcygdLayout;
    @BindView(R.id.qcjr_layout)
    RelativeLayout qcjrLayout;
    @BindView(R.id.zxd_layout)
    RelativeLayout zxdLayout;
    @BindView(R.id.more_layout)
    LinearLayout moreLayout;
    @BindView(R.id.banner_home_zoomFade)
    BGABanner banner;
    @BindView(R.id.loan_menu)
    MyGridView gridView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        loadBannerData();
        SetMenuData();
        gridView.setOnItemClickListener(this);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        banner.setMinimumHeight(width / 3);

        if(MyApplication.getInstance().getUserBeanData() != null){
            loanSmallBtn.setText("测试贷款");
        }else {
            loanSmallBtn.setText("未登录");
        }
        return rootView;
    }

    public List<String> imgs = new ArrayList<>();
    public List<String> tips = new ArrayList<>();


    private void loadBannerData() {
        imgs.add("file:///android_asset/image/banner_01.png");
        imgs.add("file:///android_asset/image/banner_02.png");
        imgs.add("file:///android_asset/image/banner_03.png");
//        imgs.add("http://pic29.nipic.com/20130530/6434097_113007064309_2.jpg");
//        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523815350480&di=0acc823e2141c6feeca38a51f0ca6365&imgtype=0&src=http%3A%2F%2Fpic.97uimg.com%2Fback_pic%2F00%2F04%2F13%2F75%2Fb6c3d3579f6747dff7cf9bd2d0fe57ba.jpg");
//        imgs.add("http://pic2.ooopic.com/10/58/70/05b1OOOPIC96.jpg");
        tips.add("");
        tips.add("");
        tips.add("");
//        tips.add("");
//        tips.add("");
//        tips.add("");

        /**
         * 设置是否开启自动轮播，需要在 setData 方法之前调用，并且调了该方法后必须再调用一次 setData 方法
         * 例如根据图片当图片数量大于 1 时开启自动轮播，等于 1 时不开启自动轮播
         */
        banner.setAutoPlayAble(imgs.size() > 1);
        banner.setAdapter(this);
        banner.setDelegate(this);
        banner.setData(imgs, tips);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
        Log.d("itemView", "onBannerItemClick: " + position);
//        Toast.makeText(banner.getContext(), "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
        Glide.with(itemView.getContext())
                .load(model)
                .apply(new RequestOptions().placeholder(R.drawable.banner_03).error(R.drawable.banner_03).dontAnimate().centerCrop())
                .into(itemView);
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


    @OnClick({R.id.loan_small_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loan_small_btn:
                if (MyApplication.getInstance().getUserBeanData() == null) {
                    startActivity(LoginActivity.class);
                    break;
                }
                MyApplication.getInstance().LOAN_productID = "1001";
                startActivity(LoanAuthActivity.class);
                break;
        }
    }

    Intent intent;

    private void startActivity(Class<?> cls) {
        intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取主界面显示的数据
     */
    LoanMenuAdapter mAdapter;
    List<LoanMenu> loanMenus = new ArrayList<>();

    public void SetMenuData() {
        try {
            InputStream is = getActivity().getResources().getAssets().open("data/LoanJson");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader buffReader = new BufferedReader(reader);
            String content = "";
            String line = "";
            while ((line = buffReader.readLine()) != null) {
                content += line;
            }
            List<LoanMenu> menus = GsonUtil.jsonToArrayList(content, LoanMenu.class);
            for (LoanMenu loan : menus) {
                if (loan.isState())
                    loanMenus.add(loan);
            }
            mAdapter = new LoanMenuAdapter(getActivity(), loanMenus);
            gridView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LoanMenu loanMenu = loanMenus.get(i);
        if (loanMenu.isEnable()) {
            MyApplication.getInstance().LOAN_productID = loanMenu.getCode();
            startActivity(LoanAuthActivity.class);
        } else {
            toast("此专案暂无开发！");
        }
    }
}
