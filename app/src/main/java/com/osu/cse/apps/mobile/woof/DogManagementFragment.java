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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

public class DogManagementFragment extends Fragment {

    private static final String ARG_DOG_ID = "dog_id";

    private Dog mDog;

    private TextView mDogNameTextView;
    private Button mDogInfoButton;
    private Button mActivitySchedButton;
    private Button mActivityHistButton;
    private Button mOwnersAndCaretakersButton;
    private Button mReportLostButton;

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

        //***remove below code after setting up intents***
        User testUser = CurrentUser.get();
        Dog testDog = testUser.getDogList().get(0);
        dogId = testDog.getDogId();
        //***remove above code after setting up intents***

        mDog = CurrentUser.get().getDog(dogId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog_management, container, false);

        mDogNameTextView = v.findViewById(R.id.dog_name);
        mDogNameTextView.setText(mDog.getName());

        // onClick listener for below buttons automatically set to (this)
        mDogInfoButton = v.findViewById(R.id.dog_information_button);
        mActivitySchedButton = v.findViewById(R.id.activity_schedule_button);
        mActivityHistButton = v.findViewById(R.id.activity_history_button);
        mOwnersAndCaretakersButton = v.findViewById(R.id.owners_caretakers_button);
        mReportLostButton = v.findViewById(R.id.report_lost_button);

        return v;
    }

    public void onClick(View v) {
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
