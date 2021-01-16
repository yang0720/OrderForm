package com.qingmaiding.orderform.middleman.fragment;


import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.EventMessage;
import com.qingmaiding.orderform.shop.adapter.LafViewPageAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MidSecFragment extends BaseFragment {

    @BindView(R.id.vp_view)
    ViewPager vp_view;
    @BindView(R.id.tableLayouts)
    TabLayout tabs;
    private LafViewPageAdapter viewPagerAdapter;
    private List<Fragment> fragments=new ArrayList<>();
    private String[] titles=new String[]{"代签收","未发出","已发出","已驳回"};
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
        fragments.add(new MidOne());
        fragments.add(new MidTwo());
        fragments.add(new MidThree());
        fragments.add(new MidFour());
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
    protected void setUpData() {

    }



}
