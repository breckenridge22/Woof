package com.osu.cse.apps.mobile.woof;

// Woof App for dog activity tracking

import android.support.v4.app.Fragment;

public class LoginActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}


