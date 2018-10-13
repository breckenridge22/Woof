/*
 * Taken from Android Big Nerd Ranch Guide page 163.  Used as a generic template
 * for activities that host a single fragment.  Activities that host
 * a single fragment should extend this class.
 */

package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();
    private final String TAG = "SingleFragmentActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // Pass any values stored in intent to fragment.
        /*
         * This segment seems to make the app crash
         *
        Bundle args = getIntent().getExtras();
        if (args != null) {
            fragment.setArguments(getIntent().getExtras());
        }
        */

        // Open fragment
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }
}
