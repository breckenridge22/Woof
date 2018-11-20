package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFragment extends Fragment implements View.OnClickListener {


    private Button mUpdateButton;
    private Button mDeleteButton;
    private static final String TAG = "SettingsFragment";


    public static FirebaseAuth mAuth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_settings, container, false);

        mUpdateButton = v.findViewById(R.id.update_button);
        mDeleteButton = v.findViewById(R.id.delete_button);


        if (mUpdateButton != null) {
            mUpdateButton.setOnClickListener(this);
        }
        if (mDeleteButton != null) {
            mDeleteButton.setOnClickListener(this);
        }

        return v;
    }

    public void onClick(View v) {
        int i = v.getId();
        Intent intent;
        switch (i) {
            case R.id.update_button:
                if (CurrentUser.isConnectedToDatabase()) {
                    intent = ProfileActivity.newIntent(getActivity());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Cannot update account while offline", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.delete_button:
                if (CurrentUser.isConnectedToDatabase()) {
                    Log.d(TAG, "Delete Button Pressed");
                    intent = DeleteActivity.newIntent(getActivity());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Cannot update account while offline", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
