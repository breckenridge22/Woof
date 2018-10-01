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

public class DogManagementFragment extends Fragment {

    private Button mDogInfoButton;
    private Button mActivitySchedButton;
    private Button mActivityHistButton;
    private Button mOwnersAndCaretakersButton;
    private Button mReportLostButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get info (i.e., Dog object) from intent that was sent to parent activity
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog_management, container, false);

        // set button member variables and button event listeners

        return v;
    }


}
