package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreenFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeScreenFragment";
    private FirebaseAuth mAuth;

    public static HomeScreenFragment newInstance() {
        return new HomeScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");


        //***remove below code after setting up login screen
        // TODO
        mAuth = FirebaseAuth.getInstance();
        //CurrentUser.get();
        //***remove above code after setting up login screen
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");

        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        Button mapButton = v.findViewById(R.id.map_button);
        mapButton.setOnClickListener(this);

        Button finishWalkButton = v.findViewById(R.id.finish_walk_button);
        finishWalkButton.setOnClickListener(this);

        Button newActivityButton = v.findViewById(R.id.new_activity_button);
        newActivityButton.setOnClickListener(this);

        Button activityHistoryButton = v.findViewById(R.id.activity_history_button);
        activityHistoryButton.setOnClickListener(this);

        Button manageDogsButton = v.findViewById(R.id.manage_dogs_button);
        manageDogsButton.setOnClickListener(this);

        Button addDogButton = v.findViewById(R.id.add_dog_button);
        addDogButton.setOnClickListener(this);

        Button friendsFamilyButton = v.findViewById(R.id.friends_family_button);
        friendsFamilyButton.setOnClickListener(this);

        Button settingsButton = v.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

        Button logOutButton = v.findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.map_button:
                // TODO
                break;
            case R.id.finish_walk_button:
                // TODO
                break;
            case R.id.new_activity_button:
                intent = DogSelectionActivity.newIntent(getActivity());
                intent.putExtra("activity_type", "new_activity");
                startActivity(intent);
                break;
            case R.id.activity_history_button:
                intent = DogSelectionActivity.newIntent(getActivity());
                intent.putExtra("activity_type", "activity_history");
                startActivity(intent);
                break;
            case R.id.manage_dogs_button:
                intent = DogSelectionActivity.newIntent(getActivity());
                intent.putExtra("activity_type", "manage_dogs");
                startActivity(intent);
                break;
            case R.id.add_dog_button:
                // TODO
                break;
            case R.id.friends_family_button:
                // TODO
                break;
            case R.id.settings_button:
                // TODO
                break;
            case R.id.log_out_button:
                LoginFragment.signOut();
                intent = LoginActivity.newIntent(getActivity());
                startActivity(intent);
                break;
        }
    }

}
