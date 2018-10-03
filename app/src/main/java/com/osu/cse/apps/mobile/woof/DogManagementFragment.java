/*
 * Fragment class for the Dog Management screen.  After selecting a dog, an instance of
 * DogManagementActivity will be created and subsequently instantiate an instance of this
 * fragment.  When the view for this fragment is inflated, it will display the following
 * options:
 *  -Dog Information
 *  -Activity Schedule
 *  -Activity History
 *  -Owners/Caretakers
 *  -Report As Lost
 */

package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

public class DogManagementFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_DOG_ID = "dog_id";
    private static final String TAG = "DogManagementFragment";

    private Dog mDog;

    private TextView mDogNameTextView;

    public static DogManagementFragment newInstance(UUID dogId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DOG_ID, dogId);

        DogManagementFragment fragment = new DogManagementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID dogId = (UUID) getArguments().getSerializable(ARG_DOG_ID);
        mDog = CurrentUser.get().getDog(dogId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog_management, container, false);

        mDogNameTextView = v.findViewById(R.id.dog_name_header);

        Button dogInfoButton = v.findViewById(R.id.dog_information_button);
        dogInfoButton.setOnClickListener(this);

        Button activitySchedButton = v.findViewById(R.id.activity_schedule_button);
        activitySchedButton.setOnClickListener(this);

        Button activityHistButton = v.findViewById(R.id.activity_history_button);
        activityHistButton.setOnClickListener(this);

        Button ownersAndCaretakersButton = v.findViewById(R.id.owners_caretakers_button);
        ownersAndCaretakersButton.setOnClickListener(this);

        Button reportLostButton = v.findViewById(R.id.report_lost_button);
        reportLostButton.setOnClickListener(this);

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        mDogNameTextView.setText(mDog.getName());
    }

    public void onClick(View v) {
        Log.d(TAG, "onClick() called");
        switch (v.getId()) {
            case R.id.dog_information_button:
                Intent intent = DogInformationActivity.newIntent(getActivity(), mDog.getDogId());
                startActivity(intent);
                break;
            case R.id.activity_schedule_button:
                // open activity schedule screen activity
                break;
            case R.id.activity_history_button:
                // open activity history screen activity
                break;
            case R.id.owners_caretakers_button:
                // open owners and caretakers screen activity
                break;
            case R.id.report_lost_button:
                // open report lost screen activity
                break;
        }
    }

}
