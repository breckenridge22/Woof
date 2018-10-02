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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.UUID;

public class DogManagementFragment extends Fragment {

    private static final String ARG_DOG_ID = "dog_id";

    private Dog mDog;

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
        // get dog from dog list singleton (or just have array list of
        // dogs in User class?
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog_management, container, false);

        // set button member variables and button event listeners

        return v;
    }


}
