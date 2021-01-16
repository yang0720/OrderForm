package com.qingmaiding.orderform.platform;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qingmaiding.orderform.BaseActivity;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.platform.fragment.PlatFirFragment;
import com.qingmaiding.orderform.platform.fragment.PlatFourFragment;
import com.qingmaiding.orderform.platform.fragment.PlatSecFragment;
import com.qingmaiding.orderform.platform.fragment.PlatThreeFragment;
import com.qingmaiding.orderform.shop.ShopFirFragment;
import com.qingmaiding.orderform.shop.ShopFourFragment;
import com.qingmaiding.orderform.shop.ShopSecFragment;
import com.qingmaiding.orderform.shop.ShopThreeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlatMainActivity extends BaseActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private PlatFirFragment fragment1;
    private PlatSecFragment fragment2;
    private PlatThreeFragment fragment3;
    private PlatFourFragment fragment4;
//    private LiveFragment fragment5;
    private Fragment[] fragments;
    private int lastFragment;//用于记录上个选择的Fragment

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main_home://首页
//                    item.setIcon(R.mipmap.main_icon1_sel);
//                    toast("首页");
                    if(lastFragment!=0)
                    {
                        switchFragment(lastFragment,0);
                        lastFragment=0;
                    }
                    return true;
                case R.id.main_uli_circle://订单
//                    item.setIcon(R.mipmap.main_icon2_sel);
//                    toast("U荔圈");
                    if(lastFragment!=1)
                    {
                        switchFragment(lastFragment,1);
                        lastFragment=1;
                    }
//                    startActivity(new Intent(ShopMainActivity.this, UliMainActivity.class));
                    return true;
                case R.id.main_shop://充值
//                    item.setIcon(R.mipmap.main_icon3_sel);
                    if(lastFragment!=2){
                        switchFragment(lastFragment,2);
                        lastFragment=2;
                    }
//                    startActivity(new Intent(MainActivity.this,ShoppingMainActivity.class));

                    return true;
                case R.id.main_live://我的
//                    item.setIcon(R.mipmap.main_icon4_sel);
                    if(lastFragment!=3)
                    {
                        switchFragment(lastFragment,3);
                        lastFragment=3;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_plat);
        ButterKnife.bind(this);
        //权限检查和申请
        initFragment();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
//        startActivity(new Intent(this, LoginActivity.class));
    }

    private void initFragment() {
        fragment1 = new PlatFirFragment();
        fragment2 = new PlatSecFragment();
        fragment3 = new PlatThreeFragment();
        fragment4 = new PlatFourFragment();
        fragments = new Fragment[]{fragment1,fragment2,fragment3,fragment4};
        lastFragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.lin_lay_fragment,fragment1).show(fragment1).commit();
    }
    //切换Fragment
    private void switchFragment(int lastfragment,int index){
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false){
            transaction.add(R.id.lin_lay_fragment,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

}
