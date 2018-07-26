package com.example.android.mnnitcentral;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by LENOVO on 3/19/2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    public String title[]={"Academic","Locality","Events"};
    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Academic_Fragment();
        } else if (position == 1){
            return new Locality_Fragment();
        } else {
            return new Events_Fragment();
        }
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
