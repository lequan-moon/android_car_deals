package com.moudevops.quanlm.cardeal;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.moudevops.quanlm.cardeal.adapter.ViewPagerAdapter;
import com.moudevops.quanlm.cardeal.fragment.DealsFragment;
import com.moudevops.quanlm.cardeal.fragment.MyAccountFragment;
import com.moudevops.quanlm.cardeal.fragment.MyGaraFragment;
import com.moudevops.quanlm.cardeal.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity implements
        MyGaraFragment.OnFragmentInteractionListener,
        DealsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        MyAccountFragment.OnFragmentInteractionListener{

    PagerSlidingTabStrip tabHost;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControl();
        initEvent();
    }

    private void initEvent() {

    }

    private void initControl() {
        tabHost = (PagerSlidingTabStrip) findViewById(R.id.mainContainer);
        pager = (ViewPager) this.findViewById(R.id.pager);

        // init view pager
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabHost.setViewPager(pager);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "URI: " + uri, Toast.LENGTH_SHORT).show();
    }
}
