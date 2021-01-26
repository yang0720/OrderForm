package com.qingmaiding.orderform.middleman.fragment;


import android.content.Intent;

import com.alpamayo.utils.utils.PrefMethed;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.middleman.MidInfoActivity;
import com.qingmaiding.orderform.middleman.MyMidStoreActivity;
import com.qingmaiding.orderform.middleman.WithDepActivity;
import com.qingmaiding.orderform.shop.MyInfoActivity;
import com.qingmaiding.orderform.shop.MyShopStoreActivity;
import com.qingmaiding.orderform.ui.login.MyLoginActivity;

import butterknife.OnClick;

public class MidFourFragment extends BaseFragment {
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_fourmid;
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }
    @OnClick(R.id.tag3)
    public void tag3(){
        startActivity(new Intent(getActivity(), MyMidStoreActivity.class));
    }
    @OnClick(R.id.tag1)
    public void tag1(){
        startActivity(new Intent(getActivity(), MidInfoActivity.class));
    }
    @OnClick(R.id.tag2)
    public void tag2(){
        startActivity(new Intent(getActivity(), WithDepActivity.class));
    }
    @OnClick(R.id.logout)
    public void logout(){
        PrefMethed.settoken(getActivity(),"");
        PrefMethed.setuser_info(getActivity(),"");
        startActivity(new Intent(getActivity(), MyLoginActivity.class));
        getActivity().finish();
    }
}
