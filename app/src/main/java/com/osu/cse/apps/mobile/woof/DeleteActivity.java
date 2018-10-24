package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class DeleteActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, DeleteActivity.class);
    }

    protected Fragment createFragment() {
        return DeleteFragment.newInstance();
    }

}