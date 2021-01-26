package com.qingmaiding.orderform.platform.fragment;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.shop.adapter.LafViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlatSecFragment extends BaseFragment {

    @BindView(R.id.vp_view)
    ViewPager vp_view;
    @BindView(R.id.tableLayouts)
    TabLayout tabs;
    private LafViewPageAdapter viewPagerAdapter;
    private List<Fragment> fragments=new ArrayList<>();
    private String[] titles=new String[]{"商家列表","中间商列表"};
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_platsec;
    }

    @Override
    protected void setUpView() {
//        int tag = getIntent().getIntExtra("activeType",0);
        for (String tab:titles){
            tabs.addTab(tabs.newTab().setText(tab));
        }
        fragments.add(new PlatSecOne());
        fragments.add(new PlatSecTwo());
        viewPagerAdapter=new LafViewPageAdapter(getActivity().getSupportFragmentManager(),titles,fragments);
//        vp_view.setAdapter(viewPagerAdapter);
        vp_view.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabs.setupWithViewPager(vp_view);
//        vp_view.setCurrentItem(tag);
//        tabs.getTabAt(tag).select();
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
            return 2;
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
