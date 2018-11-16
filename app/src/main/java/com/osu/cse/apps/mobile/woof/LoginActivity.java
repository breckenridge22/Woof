package com.osu.cse.apps.mobile.woof;

// Woof App for dog activity tracking

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.firebase.FirebaseApp;

public class LoginActivity extends SingleFragmentActivity implements LoginFragment.Callbacks {

    public static final String EXTRA_SIGN_OUT =
            "sign_out";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        intent.putExtra(EXTRA_SIGN_OUT, true);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    public void initFireBase() {
        FirebaseApp.initializeApp(this);
    }

}


