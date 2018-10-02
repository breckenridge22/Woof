package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DogInformationFragment extends DogManagementFragment {

    private static final String ARG_DOG_ID = "dog_id";

    private Dog mDog;

    /*
    public static Fragment newInstance(UUID dogId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DOG_ID, dogId);

        Fragment fragment = new DogInformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID dogId = (UUID) getArguments().getSerializable(ARG_DOG_ID);
        mDog = CurrentUser.get().getDog(dogId);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog_information, container, false);

        return v;
    }

}
