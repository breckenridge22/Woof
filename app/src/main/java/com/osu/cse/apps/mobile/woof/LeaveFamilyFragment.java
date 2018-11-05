package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LeaveFamilyFragment extends FamilyFragment implements View.OnClickListener {

    private static final String TAG = "LeaveFamilyFragment";

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
        switch(v.getId()) {
            case R.id.yes_button:
                // TODO: implement on-click functionality for yes button
                break;
            case R.id.no_button:
                // TODO: implement on-click functionality for no button
                break;
        }
    }
}
