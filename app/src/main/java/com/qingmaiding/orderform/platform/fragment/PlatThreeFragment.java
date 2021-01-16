package com.qingmaiding.orderform.platform.fragment;


import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.EventMessage;
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

public class PlatThreeFragment extends BaseFragment {

    @BindView(R.id.vp_view)
    ViewPager vp_view;
    @BindView(R.id.tableLayouts)
    TabLayout tabs;
    private LafViewPageAdapter viewPagerAdapter;
    private List<Fragment> fragments=new ArrayList<>();
    private String[] titles=new String[]{"全部","待付款","未发出","已发出","已驳回"};
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
        fragments.add(new PlatOne());
        fragments.add(new PlatTwo());
        fragments.add(new PlatThree());
        fragments.add(new PlatFour());
        fragments.add(new PlatFive());
        viewPagerAdapter=new LafViewPageAdapter(getActivity().getSupportFragmentManager(),titles,fragments);
        vp_view.setAdapter(new MyAdapter(getChildFragmentManager()));
//        vp_view.setAdapter(viewPagerAdapter);
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

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
