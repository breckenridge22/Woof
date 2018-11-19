package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HomeScreenFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeScreenFragment";
    private Button mFinishWalkButton;
    private Button mNewActivityButton;
    private Button mActivityHistoryButton;
    private Button mManageDogsButton;
    private Button mAddDogButton;
    private Button mFamilyButton;
    private Button mSettingsButton;
    private Button mLogOutButton;

    public static HomeScreenFragment newInstance() {
        return new HomeScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
        CurrentUser.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");

        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        mFinishWalkButton = v.findViewById(R.id.finish_walk_button);
        if(CurrentUser.getmCurrentActivities().size()>0){
            mFinishWalkButton.setVisibility(View.VISIBLE);
        }
        mFinishWalkButton.setOnClickListener(this);

        mNewActivityButton = v.findViewById(R.id.new_activity_button);
        mNewActivityButton.setOnClickListener(this);

        mActivityHistoryButton = v.findViewById(R.id.activity_history_button);
        mActivityHistoryButton.setOnClickListener(this);

        mManageDogsButton = v.findViewById(R.id.manage_dogs_button);
        mManageDogsButton.setOnClickListener(this);

        mAddDogButton = v.findViewById(R.id.add_dog_button);
        mAddDogButton.setOnClickListener(this);

        mFamilyButton = v.findViewById(R.id.family_button);
        mFamilyButton.setOnClickListener(this);

        mSettingsButton = v.findViewById(R.id.settings_button);
        mSettingsButton.setOnClickListener(this);

        mLogOutButton = v.findViewById(R.id.log_out_button);
        mLogOutButton.setOnClickListener(this);

        enableButtons(true);

        return v;

    }

    @Override
    public void onClick(View v) {
        enableButtons(false);
        Intent intent;
        switch (v.getId()) {
            case R.id.finish_walk_button:
                CurrentUser.saveActivity();
                mFinishWalkButton.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Walk Completed.", Toast.LENGTH_SHORT).show();
                enableButtons(true);
                break;
            case R.id.new_activity_button:
                Log.d(TAG, "Routing to MultiDogSelectionActivity");
                intent = MultiDogSelectionActivity.newIntent(getActivity());
                intent.putExtra("activity_type", "new_activity");
                startActivity(intent);
                break;
            case R.id.activity_history_button:
                Log.d(TAG, "Routing to activity history");
                intent = DogSelectionActivity.newIntent(getActivity());
                intent.putExtra("activity_type", "activity_history");
                startActivity(intent);
                break;
            case R.id.manage_dogs_button:
                Log.d(TAG, "Routing to manage dogs");
                intent = DogSelectionActivity.newIntent(getActivity());
                intent.putExtra("activity_type", "manage_dogs");
                startActivity(intent);
                break;
            case R.id.add_dog_button:
                Log.d(TAG, "Routing to add dog");
                intent = NewDogActivity.newIntent(getActivity());
                startActivity(intent);
                break;
            case R.id.family_button:
                intent = FamilyMainActivity.newIntent(getActivity());
                startActivity(intent);
                break;
            case R.id.settings_button:
                intent = SettingsActivity.newIntent(getActivity());
                startActivity(intent);
                break;
            case R.id.log_out_button:
                Log.d(TAG, "Routing to Log out");
                CurrentUser.setNull();
                LoginFragment.signOut();
                intent = LoginActivity.newIntent(getActivity());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

        }
    }

    private void enableButtons(Boolean enable){
        mFinishWalkButton.setEnabled(enable);
        mNewActivityButton.setEnabled(enable);
        mActivityHistoryButton.setEnabled(enable);
        mManageDogsButton.setEnabled(enable);
        mAddDogButton.setEnabled(enable);
        mFamilyButton.setEnabled(enable);
        mSettingsButton.setEnabled(enable);
        mLogOutButton.setEnabled(enable);
    }

    @Override
    public void onResume(){
        super.onResume();
        enableButtons(true);
    }

}