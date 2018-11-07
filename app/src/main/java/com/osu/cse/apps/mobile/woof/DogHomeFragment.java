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
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DogHomeFragment extends DogFragment implements View.OnClickListener {

    private static final String TAG = "DogHomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_dog_home, container, false);

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

        Button deleteDogButton = v.findViewById(R.id.delete_dog_button);
        deleteDogButton.setOnClickListener(this);

        // TODO: Remove button when finished testing
        Button testButton = v.findViewById(R.id.test_button);
        testButton.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        Log.i(TAG, "onClick() called");
        switch (v.getId()) {
            case R.id.dog_information_button:
                getCallbacks().onMenuButtonSelected(DogManagementActivity.DOG_INFORMATION);
                break;
            case R.id.activity_schedule_button:
                // open activity schedule screen activity
                break;
            case R.id.activity_history_button:
                getCallbacks().onMenuButtonSelected(DogManagementActivity.ACTIVITY_HISTORY);
                break;
            case R.id.owners_caretakers_button:
                // open owners and caretakers screen activity
                break;
            case R.id.report_lost_button:
                // open report lost screen activity
                break;
            case R.id.delete_dog_button:
                FirebaseDatabase.getInstance().getReference().child("families").child(getFamilyId())
                        .child("dogs").child(getDogId()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                getActivity().finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Failed to delete dog from database");
                            }
                        });
            case R.id.test_button: // TODO: Remove this when finished testing
                List<ActivityRecord> activityRecords = ActivityRecord.getTestActivityRecords();
                String dogActivityId = getDog().getactivitiesId();  // family Id and dog Id delimited with a colon
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("activities").child(dogActivityId);
                for (ActivityRecord activityRecord : activityRecords) {
                    String key = ref.push().getKey();
                    ref.child(key).setValue(activityRecord);
                }
                break;
        }
    }

}
