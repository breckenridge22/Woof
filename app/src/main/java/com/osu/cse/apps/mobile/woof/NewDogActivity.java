package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class NewDogActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, NewDogActivity.class);
    }

    protected Fragment createFragment() {
        return NewDogFragment.newInstance();
    }


}
