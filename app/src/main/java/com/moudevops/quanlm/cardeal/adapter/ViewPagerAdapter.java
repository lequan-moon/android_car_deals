package com.moudevops.quanlm.cardeal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.moudevops.quanlm.cardeal.fragment.DealsFragment;
import com.moudevops.quanlm.cardeal.fragment.MyAccountFragment;
import com.moudevops.quanlm.cardeal.fragment.MyGaraFragment;
import com.moudevops.quanlm.cardeal.fragment.SearchListFragment;
import com.moudevops.quanlm.cardeal.fragment.SettingsFragment;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0:
                fragment = SearchListFragment.newInstance("param1", "param2");
                break;
            case 1:
                fragment = MyGaraFragment.newInstance("", "");
                break;
            case 2:
                fragment = DealsFragment.newInstance("", "");
                break;
            case 3:
                fragment = MyAccountFragment.newInstance("", "");
                break;
            case 4:
                fragment = SettingsFragment.newInstance("", "");
                break;
            default:
                fragment = SearchListFragment.newInstance("param1", "param2");
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Tìm kiếm";
            case 1:
                return "Gara của tôi";
            case 2:
                return "Deals";
            case 3:
                return "Tài khoản";
            case 4:
                return "Thiết lập";
            default:
                return "Tìm kiếm";
        }
    }

}
