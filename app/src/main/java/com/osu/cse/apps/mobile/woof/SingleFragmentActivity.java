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
        Log.i(TAG, "onCreate() called");
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // Retrieve stored values in intent.
        Bundle args = getIntent().getExtras();
        Log.d(TAG, "GetIntent().getExtras()");

        // Open fragment
        if (fragment == null) {
            fragment = createFragment();

            // Pass the stored values to fragment
            if (args != null) {
                Log.d(TAG, "inserting extras into fragment");
                fragment.setArguments(args);
            }

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }
}
