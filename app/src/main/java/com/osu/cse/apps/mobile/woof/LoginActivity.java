package com.osu.cse.apps.mobile.woof;

// Woof App for dog activity tracking

import android.support.v4.app.Fragment;

import com.google.firebase.FirebaseApp;

public class LoginActivity extends SingleFragmentActivity implements LoginFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    public void initFireBase(){
        FirebaseApp.initializeApp(this);
    }

}


