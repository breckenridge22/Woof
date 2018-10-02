package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

public class DogInformationFragment extends Fragment {

    private static final String ARG_DOG_ID = "dog_id";
    private static final String TAG = "DogInformationFragment";

    private Dog mDog;

    private TextView mDogNameTextView;
    private TextView mDogIdTextView;
    private EditText mDogNameEditText;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog_information, container, false);

        mDogNameTextView = v.findViewById(R.id.dog_name_header);

        mDogIdTextView = v.findViewById(R.id.dog_id);

        mDogNameEditText = v.findViewById(R.id.dog_name);
        mDogNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Took this from CrimeFragment.java in BNRG.  Code should go here?
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                mDog.setName(s.toString());
                Log.d(TAG, "onTextChanged() called");
                updateUI();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Took this from CrimeFragment.java in BNRG.  Code should go here?
            }
        });

        mDogNameEditText.setText(mDog.getName());
        updateUI();

        return v;
    }

    public void updateUI() {
        Log.d(TAG, "updateUI() called");
        mDogNameTextView.setText(mDog.getName());
        mDogIdTextView.setText(mDog.getDogId().toString());
        //mDogNameEditText.setText(mDog.getName());
        Log.d(TAG, "updateUI() finished");
    }

}
