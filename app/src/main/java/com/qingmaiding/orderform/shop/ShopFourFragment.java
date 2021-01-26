package com.qingmaiding.orderform.shop;


import android.content.Intent;

import com.alpamayo.utils.utils.PrefMethed;
import com.qingmaiding.orderform.BaseFragment;
import com.qingmaiding.orderform.R;
import com.qingmaiding.orderform.ui.login.MyLoginActivity;

import butterknife.OnClick;

public class ShopFourFragment extends BaseFragment {
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_four;
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }
    @OnClick(R.id.tag3)
    public void tag3(){
        startActivity(new Intent(getActivity(), MyShopStoreActivity.class));
    }
    @OnClick(R.id.tag1)
    public void tag1(){
        startActivity(new Intent(getActivity(), MyInfoActivity.class));
    }
    @OnClick(R.id.tag2)
    public void tag2(){
        startActivity(new Intent(getActivity(), MyConsHisActivity.class));
    }
    @OnClick(R.id.tag4)
    public void tag4(){
        startActivity(new Intent(getActivity(), MyExpHisActivity.class));
    }
    @OnClick(R.id.logout)
    public void logout(){
        PrefMethed.settoken(getActivity(),"");
        PrefMethed.setuser_info(getActivity(),"");
        startActivity(new Intent(getActivity(), MyLoginActivity.class));
        getActivity().finish();
    }
}
