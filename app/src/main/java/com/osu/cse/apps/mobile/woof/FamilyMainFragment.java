package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FamilyMainFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "FamilyMainFragment";

    public static FamilyMainFragment newInstance() {
        return new FamilyMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");

        View v = inflater.inflate(R.layout.fragment_family_main, container, false);

        Button familiesButton = v.findViewById(R.id.families_button);
        familiesButton.setOnClickListener(this);

        Button invitationsButton = v.findViewById(R.id.invitations_button);
        invitationsButton.setOnClickListener(this);

        Button createNewFamilyButton = v.findViewById(R.id.create_new_family_button);
        createNewFamilyButton.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.families_button:
                intent = FamilySelectionActivity.newIntent(getActivity());
                startActivity(intent);
                break;
            case R.id.invitations_button:
                intent = FamilyInvitationsActivity.newIntent(getActivity());
                startActivity(intent);
                break;
            case R.id. create_new_family_button:
                intent = CreateNewFamilyActivity.newIntent(getActivity());
                startActivity(intent);
                break;
        }
    }

}
