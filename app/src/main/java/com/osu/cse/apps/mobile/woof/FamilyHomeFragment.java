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

public class FamilyHomeFragment extends FamilyFragment implements View.OnClickListener {

    private static final String TAG = "FamilyHomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_family_home, container, false);

        Button familyMembersButton = v.findViewById(R.id.family_members_button);
        familyMembersButton.setOnClickListener(this);

        Button dogsButton = v.findViewById(R.id.dogs_button);
        dogsButton.setOnClickListener(this);

        Button inviteNewMemberButton = v.findViewById(R.id.invite_new_member_button);
        inviteNewMemberButton.setOnClickListener(this);

        Button editFamilyInfoButton = v.findViewById(R.id.edit_family_info_button);
        editFamilyInfoButton.setOnClickListener(this);

        Button leaveFamilyButton = v.findViewById(R.id.leave_family_button);
        leaveFamilyButton.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        Log.i(TAG, "onClick() called");
        switch (v.getId()) {
            case R.id.family_members_button:
                getCallbacks().onMenuButtonSelected(FamilyManagementActivity.FAMILY_MEMBERS);
                break;
            case R.id.dogs_button:
                // TODO: intent to run DogSelectionActivity (can't use onMenuButtonSelected)
                break;
            case R.id.invite_new_member_button:
                getCallbacks().onMenuButtonSelected(FamilyManagementActivity.INVITE_NEW_MEMBER);
                break;
            case R.id.edit_family_info_button:
                getCallbacks().onMenuButtonSelected(FamilyManagementActivity.EDIT_FAMILY_INFO);
                break;
            case R.id.leave_family_button:
                getCallbacks().onMenuButtonSelected(FamilyManagementActivity.LEAVE_FAMILY);
                break;
        }
    }

}
