package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LeaveFamilyFragment extends FamilyFragment implements View.OnClickListener {

    private static final String TAG = "LeaveFamilyFragment";
    private Family mFamily;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leave_family, container, false);

        Button yesButton = v.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(this);

        Button noButton = v.findViewById(R.id.no_button);
        noButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_button:
                CurrentUser.getFamilyIdsFromDatabase(new FamilyIdsCallback() {
                    @Override
                    public void onFamilyIdsRetrieved(List<String> familyIdList) {
                        if (familyIdList.size() <= 1) {
                            Toast.makeText(getActivity(), "Sorry, you can't leave this family since you " +
                                    "don't belong to any other families", Toast.LENGTH_LONG).show();
                            return;
                        }
                        updateFamily();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.d(TAG, errorMessage);
                    }
                });
                break;
            case R.id.no_button:
                Toast.makeText(getActivity(), "Why the cold feet?", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
        }
    }

    private void updateFamily() {
        Family family = getFamily();
        String familyId = getFamily().getfamilyId();
        String currentUserId = CurrentUser.getUserId();

        // prep database updates
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap();

        childUpdates.put("/users/" + currentUserId + "/familyIds/" + familyId, null);

        // prep update for family coordinator user Id if current user is family coordinator
        if (family.getuserIds().size() > 1) {
            childUpdates.put("/families/" + familyId + "/userIds/" + currentUserId, null);
            String familyCoordinatorUserId = family.getcoordinatorUserId();
            if (familyCoordinatorUserId.equals(currentUserId)) {
                String newCoordinatorId = "";
                List<String> userIdList = new ArrayList();
                for (Map.Entry<String, Boolean> entry : family.getuserIds().entrySet()) {
                    userIdList.add(entry.getKey());
                }
                int numMembers = userIdList.size();
                boolean foundNewCoordinator = false;
                Random random = new Random();

                // randomly choose another family member as coordinator
                while (!foundNewCoordinator) {
                    int randIndex = Math.abs(random.nextInt() % numMembers);
                    newCoordinatorId = userIdList.get(randIndex);
                    foundNewCoordinator = !newCoordinatorId.equals(currentUserId);
                }
                childUpdates.put("/families/" + familyId + "/coordinatorUserId", newCoordinatorId);
            }
        }
        else {
            childUpdates.put("families/" + familyId, null);
        }

        // update database atomically
        ref.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Successfully left family", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error updating family database information");
            }
        });

    }
}
