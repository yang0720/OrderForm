package com.qingmaiding.orderform.shop;


import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.adapter.LafViewPageAdapter;
import com.qingmaiding.orderform.shop.fragment.ActiveFour;
import com.qingmaiding.orderform.shop.fragment.ActiveOne;
import com.qingmaiding.orderform.shop.fragment.ActiveThree;
import com.qingmaiding.orderform.shop.fragment.ActiveTwo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopSecFragment extends BaseFragment {

    @BindView(R.id.vp_view)
    ViewPager vp_view;
    @BindView(R.id.tableLayouts)
    TabLayout tabs;
    private LafViewPageAdapter viewPagerAdapter;
    private List<Fragment> fragments=new ArrayList<>();
    private String[] titles=new String[]{"全部","待发单","已发出","已完成"};
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_sec;
    }

    @Override
    protected void setUpView() {
//        int tag = getIntent().getIntExtra("activeType",0);
        for (String tab:titles){
            tabs.addTab(tabs.newTab().setText(tab));
        }
        fragments.add(new ActiveOne());
        fragments.add(new ActiveTwo());
        fragments.add(new ActiveThree());
        fragments.add(new ActiveFour());
        viewPagerAdapter=new LafViewPageAdapter(getActivity().getSupportFragmentManager(),titles,fragments);
        vp_view.setAdapter(viewPagerAdapter);
        tabs.setupWithViewPager(vp_view);
//        vp_view.setCurrentItem(tag);
//        tabs.getTabAt(tag).select();
    }
    @BindView(R.id.search_text)
    EditText search_text;
    @OnClick(R.id.search_btn)
    public void search_btn(){
        EventMessage msg = new EventMessage(search_text.getText().toString());
        EventBus.getDefault().post(msg);
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void setUpData() {

    }

}
