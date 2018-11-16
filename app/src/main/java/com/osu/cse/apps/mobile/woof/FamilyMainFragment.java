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
    private Button mFamiliesButton;
    private Button mInvitationsButton;
    private Button mCreateNewFamilyButton;

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

        mFamiliesButton = v.findViewById(R.id.families_button);
        mFamiliesButton.setOnClickListener(this);

        mInvitationsButton = v.findViewById(R.id.invitations_button);
        mInvitationsButton.setOnClickListener(this);

        mCreateNewFamilyButton = v.findViewById(R.id.create_new_family_button);
        mCreateNewFamilyButton.setOnClickListener(this);
        enableButtons(true);

        return v;
    }

    public void onClick(View v) {
        enableButtons(false);
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

    @Override
    public void onResume(){
        Log.d(TAG, "Called onResume()");
        super.onResume();
        enableButtons(true);
    }

    private void enableButtons(Boolean enable){
        mCreateNewFamilyButton.setEnabled(enable);
        mInvitationsButton.setEnabled(enable);
        mFamiliesButton.setEnabled(enable);
    }

}
