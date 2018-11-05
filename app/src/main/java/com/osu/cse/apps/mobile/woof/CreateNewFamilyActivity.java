package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class CreateNewFamilyActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, CreateNewFamilyActivity.class);
    }

    protected Fragment createFragment() {
        return CreateNewFamilyFragment.newInstance();
    }

}
