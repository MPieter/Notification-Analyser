package com.tierep.notificationanalyser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by pieter on 18/09/14.
 */
public class NavigationTabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment fragment;
    private Activity hostActivity;
    private int fragmentId;
    private Class<T> fragClass;

    public NavigationTabListener(Activity hostActivity, int fragmentId, Class<T> fragClass) {
        this.hostActivity = hostActivity;
        this.fragmentId = fragmentId;
        this.fragClass = fragClass;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (fragment == null) {
            this.fragment = Fragment.instantiate(hostActivity, fragClass.getName());
            fragmentTransaction.add(fragmentId, fragment);
        } else {
            fragmentTransaction.attach(fragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (fragment != null) {
            fragmentTransaction.detach(fragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // User selected the already selected tab. Usually do nothing.
    }
}
