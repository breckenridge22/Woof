/*
 * Activity class for hosting a DogHomeFragment
 */

package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.UUID;

public class DogManagementActivity extends AppCompatActivity
    implements DogFragment.Callbacks {

    private Dog mDog;
    private static final String EXTRA_DOG_ID =
            "com.osu.cse.apps.mobile.woof.dogId.DogManagementActivity";
    private static final String TAG = "DogManagementActivity";

    /*
     * Each of these enums corresponds to a fragment that can be loaded in
     * fragment_body_container of the activity_dog_management layout
     */
    public enum Screen {
        DOG_HOME,
        DOG_INFORMATION,
        ACTIVITY_SCHEDULE,
        ACTIVITY_HISTORY,
        OWNERS_CARETAKERS,
        REPORT_LOST
    }

    public static Intent newIntent(Context packageContext, UUID dogId) {
        Intent intent = new Intent(packageContext, DogManagementActivity.class);
        intent.putExtra(EXTRA_DOG_ID, dogId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_dog_management);

        UUID dogId = (UUID) getIntent().getSerializableExtra(EXTRA_DOG_ID);
        mDog = CurrentUser.get().getDog(dogId);
        loadHeaderFragment(DogHeaderFragment.newInstance(new DogHeaderFragment(), mDog.getDogId()));
        loadBodyFragment(DogHomeFragment.newInstance(new DogHomeFragment(), mDog.getDogId()));
    }

    private void loadHeaderFragment(Fragment newFragment) {
        loadFragment(R.id.fragment_header_container, newFragment);

    }

    private void loadBodyFragment(Fragment newFragment) {
        loadFragment(R.id.fragment_body_container, newFragment);
    }

    private void loadFragment(int containerId, Fragment newFragment) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(containerId);
        if (fragment == null) {
            fm.beginTransaction()
                    .add(containerId, newFragment)
                    .commit();
        }
        else {
            fm.beginTransaction()
                    .replace(containerId, newFragment)
                    .commit();
        }
    }

    /*
     * This method is called by the a fragment being hosted by the activity
     * when one of its menu buttons is selected
     */
    public void onMenuButtonSelected(Screen screen) {
        switch (screen) {
            case DOG_INFORMATION:
                loadBodyFragment(DogInformationFragment.newInstance(new DogInformationFragment(),
                        mDog.getDogId()));
                break;
            case ACTIVITY_SCHEDULE:
                // TODO
                break;
            case ACTIVITY_HISTORY:
                // TODO
                break;
            case OWNERS_CARETAKERS:
                // TODO
                break;
            case REPORT_LOST:
                // TODO
                break;
        }
    }

    public void onDogNameChanged() {
        FragmentManager fm = getSupportFragmentManager();
        DogHeaderFragment fragment = (DogHeaderFragment)
                fm.findFragmentById(R.id.fragment_header_container);
        if (fragment != null) {
            fragment.updateUI();
        }
    }

}
