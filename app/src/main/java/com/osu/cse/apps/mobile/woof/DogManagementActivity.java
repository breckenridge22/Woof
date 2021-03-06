/*
 * Activity class for hosting a DogHomeFragment
 */

package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DogManagementActivity extends AppCompatActivity
    implements DogFragment.Callbacks {

    private String mDogId;
    private String mFamilyId;
    private static final String EXTRA_DOG_ID =
            "com.osu.cse.apps.mobile.woof.DogManagementActivity.dog_id";
    private static final String EXTRA_FRAGMENT_ID =
            "com.osu.cse.apps.mobile.woof.DogManagementActivity.fragment_id";
    private static final String EXTRA_FAMILY_ID =
            "com.osu.cse.apps.mobile.woof.DogManagementActivity.family_id";
    private static final String TAG = "DogManagementActivity";

    // fragment codes to use in intent extras
    public static final int DOG_HOME = 0;
    public static final int DOG_INFORMATION = 1;
    public static final int ACTIVITY_SCHEDULE = 2;
    public static final int ACTIVITY_HISTORY = 3;
    public static final int OWNERS_CARETAKERS = 4;
    public static final int REPORT_LOST = 5;

    public static Intent newIntent(Context packageContext, String dogId, String familyId, int fragmentId) {
        Intent intent = new Intent(packageContext, DogManagementActivity.class);
        intent.putExtra(EXTRA_DOG_ID, dogId);
        intent.putExtra(EXTRA_FAMILY_ID, familyId);
        intent.putExtra(EXTRA_FRAGMENT_ID, fragmentId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
        setContentView(R.layout.activity_dog_management);

        mDogId = getIntent().getStringExtra(EXTRA_DOG_ID);
        mFamilyId = getIntent().getStringExtra(EXTRA_FAMILY_ID);

        FragmentManager fm = getSupportFragmentManager();

        int headerContainerId = R.id.fragment_header_container;
        DogHeaderFragment headerFragment =
                (DogHeaderFragment) fm.findFragmentById(headerContainerId);
        if (headerFragment == null) {
            headerFragment = new DogHeaderFragment();
            headerFragment.setArgs(mDogId, mFamilyId);
            fm.beginTransaction()
                    .add(headerContainerId, headerFragment)
                    .commit();
        }

        int bodyContainerId = R.id.fragment_body_container;
        DogFragment bodyFragment = (DogFragment) fm.findFragmentById(bodyContainerId);
        if (bodyFragment == null) {
            bodyFragment = newBodyFragmentFromId(getFragmentIdFromIntent());
            fm.beginTransaction()
                    .add(bodyContainerId, bodyFragment)
                    .commit();
        }
    }

    private int getFragmentIdFromIntent() {
        Intent intent = getIntent();
        int fragmentId;
        if (intent == null) {
            fragmentId = DOG_HOME;
        }
        else {
            fragmentId = intent.getIntExtra(EXTRA_FRAGMENT_ID, DOG_HOME);
        }
        return fragmentId;
    }

    private void replaceBodyFragment(int fragmentId) {
        FragmentManager fm = getSupportFragmentManager();
        int containerId = R.id.fragment_body_container;
        DogFragment fragment = newBodyFragmentFromId(fragmentId);
        fm.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null) // adds current fragment to back stack, so that when
                                        // back button pressed after new fragment loaded,
                                        // new fragment will be destroyed and current fragment
                                        // will be popped off back stack and loaded in its place
                .commit();
    }

    private DogFragment newBodyFragmentFromId(int fragmentId) {
        DogFragment fragment = null;
        switch (fragmentId) {
            case DOG_HOME:
                fragment = new DogHomeFragment();
                break;
            case DOG_INFORMATION:
                fragment = new DogInformationFragment();
                break;
            case ACTIVITY_SCHEDULE:
                // TODO
                break;
            case ACTIVITY_HISTORY:
                fragment = new ActivityHistoryFragment();
                break;
            case OWNERS_CARETAKERS:
                // TODO
                break;
            case REPORT_LOST:
                // TODO
                break;
        }
        if (fragment != null) {
            fragment.setArgs(mDogId, mFamilyId);
        }
        return fragment;
    }

    /*
     * This method is called by the DogHomeFragment (which is hosted by this activity)
     * when one of its menu buttons is selected.  It loads a new fragment
     * into the body fragment.
     */
    public void onMenuButtonSelected(int fragmentId) {
        replaceBodyFragment(fragmentId);
    }

    // called when dog's value event listener's onDataChange method is called
    public void onDogInfoChanged() {
        FragmentManager fm = getSupportFragmentManager();

        DogHeaderFragment headerFragment = (DogHeaderFragment)
                fm.findFragmentById(R.id.fragment_header_container);
        if (headerFragment != null) {
            headerFragment.updateUI();
        }

        DogFragment bodyFragment = (DogFragment) fm.findFragmentById(R.id.fragment_body_container);
        if (bodyFragment != null) {
            bodyFragment.updateUI();
        }
    }

}
