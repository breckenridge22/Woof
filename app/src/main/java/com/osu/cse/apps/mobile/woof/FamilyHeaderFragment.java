package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FamilyHeaderFragment extends FamilyFragment {

    private TextView mFamilyNameTextView;
    private static final String TAG = "FamilyHeaderFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_family_header, container, false);
        mFamilyNameTextView = v.findViewById(R.id.family_name_header);

        return v;
    }

    @Override
    public void updateUI() {
        Log.d(TAG, "updateUI() called");
        Family family = getFamily();
        if (family == null) {
            return;
        }
        mFamilyNameTextView.setText(family.getfamilyName());
    }

}
