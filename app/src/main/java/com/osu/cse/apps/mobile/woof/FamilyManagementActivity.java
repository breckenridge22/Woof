package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class FamilyManagementActivity extends AppCompatActivity
        implements FamilyFragment.Callbacks {

    private String mFamilyId;
    private static final String EXTRA_FAMILY_ID =
            "com.osu.cse.apps.mobile.woof.FamilyManagementActivity.family_id";
    private static final String EXTRA_FRAGMENT_ID =
            "com.osu.cse.apps.mobile.woof.FamilyManagementActivity.fragment_id";

    private static final String TAG = "FamilyManagementActivity";

    // fragment codes to use in intent extras
    public static final int FAMILY_HOME = 0;
    public static final int FAMILY_MEMBERS = 1;
    public static final int DOGS = 2;
    public static final int INVITE_NEW_MEMBER = 3;
    public static final int EDIT_FAMILY_INFO = 4;
    public static final int LEAVE_FAMILY = 5;

    public static Intent newIntent(Context packageContext, String familyId, int fragmentId) {
        Intent intent = new Intent(packageContext, FamilyManagementActivity.class);
        intent.putExtra(EXTRA_FAMILY_ID, familyId);
        intent.putExtra(EXTRA_FRAGMENT_ID, fragmentId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
        setContentView(R.layout.activity_family_management);

        mFamilyId = getIntent().getStringExtra(EXTRA_FAMILY_ID);

        FragmentManager fm = getSupportFragmentManager();

        int headerContainerId = R.id.fragment_header_container;
        FamilyHeaderFragment headerFragment =
                (FamilyHeaderFragment) fm.findFragmentById(headerContainerId);
        if (headerFragment == null) {
            headerFragment = new FamilyHeaderFragment();
            headerFragment.setArgs(mFamilyId);
            fm.beginTransaction()
                    .add(headerContainerId, headerFragment)
                    .commit();
        }

        int bodyContainerId = R.id.fragment_body_container;

        // need to handle case where family selection screen is opened separately since it requires
        // a different activity to run
        FamilyFragment bodyFragment = (FamilyFragment) fm.findFragmentById(bodyContainerId);
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
            fragmentId = FAMILY_HOME;
        } else {
            fragmentId = intent.getIntExtra(EXTRA_FRAGMENT_ID, FAMILY_HOME);
        }
        return fragmentId;
    }

    private void replaceBodyFragment(int fragmentId) {
        FragmentManager fm = getSupportFragmentManager();
        int containerId = R.id.fragment_body_container;
        FamilyFragment fragment = newBodyFragmentFromId(fragmentId);
        fm.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null) // adds current fragment to back stack, so that when
                // back button pressed after new fragment loaded,
                // new fragment will be destroyed and current fragment
                // will be popped off back stack and loaded in its place
                .commit();
    }

    private FamilyFragment newBodyFragmentFromId(int fragmentId) {
        FamilyFragment fragment = null;
        switch (fragmentId) {
            case FAMILY_HOME:
                fragment = new FamilyHomeFragment();
                break;
            case FAMILY_MEMBERS:
                fragment = new FamilyMembersFragment();
                break;
            case DOGS:
                Log.d(TAG, "Error: Dog Selection screen is not a valid body fragment for " +
                        "FamilyManagementActivity");
                break;
            case INVITE_NEW_MEMBER:
                fragment = new InviteUserToFamilyFragment();
                break;
            case EDIT_FAMILY_INFO:
                fragment = new EditFamilyInfoFragment();
                break;
            case LEAVE_FAMILY:
                fragment = new LeaveFamilyFragment();
                break;
        }
        if (fragment != null) {
            fragment.setArgs(mFamilyId);
        }
        return fragment;
    }

    /*
     * This method is called by the FamilyHomeFragment (which is hosted by this activity)
     * when one of its menu buttons is selected.  It loads a new fragment
     * into the body fragment.
     */
    public void onMenuButtonSelected(int fragmentId) {
        replaceBodyFragment(fragmentId);
    }

    // called when family's value event listener's onDataChange method is called
    public void onFamilyInfoChanged() {
        Log.d(TAG, "onFamilyInfoChanged() called");
        FragmentManager fm = getSupportFragmentManager();

        FamilyHeaderFragment headerFragment = (FamilyHeaderFragment)
                fm.findFragmentById(R.id.fragment_header_container);
        if (headerFragment != null) {
            Log.d(TAG, "Calling updateUI() for header fragment");
            headerFragment.updateUI();
        }

        FamilyFragment bodyFragment = (FamilyFragment) fm.findFragmentById(R.id.fragment_body_container);
        if (bodyFragment != null) {
            Log.d(TAG, "Calling updateUI() for body fragment");
            bodyFragment.updateUI();
        }
    }


}
