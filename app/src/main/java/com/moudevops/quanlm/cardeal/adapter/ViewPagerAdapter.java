package com.example.quanlm.cardeal.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.example.quanlm.cardeal.fragment.DealsFragment;
import com.example.quanlm.cardeal.fragment.MyAccountFragment;
import com.example.quanlm.cardeal.fragment.MyGaraFragment;
import com.example.quanlm.cardeal.fragment.SearchListFragment;
import com.example.quanlm.cardeal.fragment.SettingsFragment;

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
