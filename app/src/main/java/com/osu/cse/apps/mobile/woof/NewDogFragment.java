package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class NewDogFragment extends Fragment implements View.OnClickListener {

    private EditText mDogNameEditText;
    private String mDogName = "";
    private Spinner mFamilySpinner;
    private String mFamilyName = "";
    private static final String TAG = "NewDogFragment";

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static NewDogFragment newInstance() {
        return new NewDogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_new_dog, container, false);

        mDogNameEditText = v.findViewById(R.id.dog_name);
        mDogNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // No implementation.  Required to declare this method.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDogName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No implementation.  Required to declare this method.
            }
        });

        mFamilySpinner = v.findViewById(R.id.family_spinner);
        // Source: https://developer.android.com/guide/topics/ui/controls/spinner#java
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.family_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFamilySpinner.setAdapter(adapter);
        mFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mFamilyName = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // required
            }
        });

        Button button = v.findViewById(R.id.add_dog_button);
        button.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        Log.i(TAG, "onClick() called");
        switch (v.getId()) {
            case R.id.add_dog_button:
                if (!mDogName.equals("") && !mFamilyName.equals("")) {
                    String dogId = mDatabase.child("dogs").push().getKey();
                    String familyId = getFamilyId();
                    Dog dog = new Dog(dogId, mDogName, familyId);
                    mDatabase.child("dogs").child(dogId).setValue(dog);
                    mDatabase.child("families/" + familyId + "/dogs").child(dogId).setValue(true);
                }
                else {
                    Log.d(TAG, "test");
                    Toast.makeText(getActivity(), "All fields must be populated",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    public String getFamilyId() {
        // TODO: get family UUID from list of families that user belongs
        // TODO: to based on selected family name
        return mFamilyName; // placeholder
    }

    public String createDogUUID(String familyUUID) {
        UUID tmpUUID = UUID.randomUUID();
        return familyUUID + "." + tmpUUID.toString();
    }

}
