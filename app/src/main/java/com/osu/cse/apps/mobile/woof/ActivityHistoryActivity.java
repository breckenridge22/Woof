package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;


public class ActivityHistoryActivity extends SingleFragmentActivity {

    private static final String EXTRA_DOG_ID =
            "com.osu.cse.apps.mobile.woof.ActivityHistoryActivity";
    private static final String TAG = "ActivityHistoryActivity";



    public static Intent newIntent(Context packageContext) {
        Log.d(TAG, "newIntent called");
        return new Intent(packageContext, ActivityHistoryActivity.class);
    }


    @Override
    protected Fragment createFragment() {
        Log.d(TAG, "createFragment() called");
        return ActivityHistoryFragment.newInstance();
    }

}