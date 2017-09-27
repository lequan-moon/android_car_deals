package com.example.quanlm.cardeal;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.quanlm.cardeal.adapter.ViewPagerAdapter;
import com.example.quanlm.cardeal.fragment.DealsFragment;
import com.example.quanlm.cardeal.fragment.MyAccountFragment;
import com.example.quanlm.cardeal.fragment.MyGaraFragment;
import com.example.quanlm.cardeal.fragment.SearchListFragment;
import com.example.quanlm.cardeal.fragment.SettingsFragment;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements MaterialTabListener,
        MyGaraFragment.OnFragmentInteractionListener,
        DealsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        MyAccountFragment.OnFragmentInteractionListener{

    MaterialTabHost tabHost;
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
        tabHost = (MaterialTabHost) findViewById(R.id.mainContainer);
        pager = (ViewPager) this.findViewById(R.id.pager);

        // init view pager
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "URI: " + uri, Toast.LENGTH_SHORT).show();
    }
}
