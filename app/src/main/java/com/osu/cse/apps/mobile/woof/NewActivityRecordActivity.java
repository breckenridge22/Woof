package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class NewActivityRecordActivity {

    private static final String TAG = "NewActivityRecordActivity";
    private static final String EXTRA_DOG_ID = "DOG_ID";

    public static Intent newIntent(Context packageContext, String dogID) {
        Log.d(TAG, "newIntent() called.");
        Intent intent = new Intent(packageContext, NewActivityRecordActivity.class);
        intent.putExtra(EXTRA_DOG_ID, dogID);
        return intent;
    }

    protected Fragment createFragment() {
        Log.d(TAG, "createFragment() called");
        return NewActivityRecordFragment.newInstance();
    }


}
