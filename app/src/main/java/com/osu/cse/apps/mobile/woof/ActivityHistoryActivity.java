package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.UUID;


public class ActivityHistoryActivity extends AppCompatActivity implements ActivityFragment.Callbacks {

    private static final String EXTRA_DOG_ID =
            "com.osu.cse.apps.mobile.woof.ActivityHistoryActivity";
    private static final String TAG = "ActivityHistoryActivity";



    public static Intent newIntent(Context packageContext, UUID dogId) {
        Intent intent = new Intent(packageContext, DogManagementActivity.class);
        intent.putExtra(EXTRA_DOG_ID, dogId);
        return intent;
    }
    private Dog mDog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");

        setContentView(R.layout.fragment_activity_history);

        UUID dogId = (UUID) getIntent().getSerializableExtra(EXTRA_DOG_ID);
        mDog = CurrentUser.get().getDog(dogId);

        FragmentManager fm = getSupportFragmentManager();

        int headerContainerId = R.id.fragment_header_container;
        DogHeaderFragment headerFragment =
                (DogHeaderFragment) fm.findFragmentById(headerContainerId);
        if (headerFragment == null) {
            headerFragment = new DogHeaderFragment();
            headerFragment.setArgs(dogId);
            fm.beginTransaction()
                    .add(headerContainerId, headerFragment)
                    .commit();
        }

        int bodyContainerId = R.id.fragment_body_container;
        ActivityFragment bodyFragment = (ActivityFragment) fm.findFragmentById(bodyContainerId);
        if (bodyFragment == null) {
            bodyFragment = new ActivityHistoryFragment;
            fm.beginTransaction()
                    .add(bodyContainerId, bodyFragment)
                    .commit();
        }
    }

    @Override
    protected Fragment createFragment() {
        UUID dogId = (UUID) getIntent().getSerializableExtra(EXTRA_DOG_ID);
        DogHomeFragment fragment = new DogHomeFragment();
        fragment.setArgs(dogId);
        return fragment;
    }

}
