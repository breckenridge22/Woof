package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

public class ProfileActivity extends SingleFragmentActivity implements ProfileFragment.Callbacks {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, ProfileActivity.class);
    }

    protected Fragment createFragment() {
        return ProfileFragment.newInstance();
    }

    public LayoutInflater getInflater(){
        return (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
