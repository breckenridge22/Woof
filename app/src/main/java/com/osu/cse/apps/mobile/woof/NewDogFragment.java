package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewDogFragment extends Fragment implements View.OnClickListener {

    private EditText mDogNameEditText;
    private String mDogName = "";
    private Spinner mFamilySpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private String mFamilyId = "";
    private List<String> mFamilyIdList = new ArrayList();
    private List<String> mFamilyNameList = new ArrayList();

    private static final String TAG = "NewDogFragment";

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static NewDogFragment newInstance() {
        return new NewDogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentUser.getFamilyInfoFromDatabase(new FamilyInfoCallback() {
            @Override
            public void onFamilyInfoRetrieved(FamilyInfo familyInfo) {
                parseFamilyInfo(familyInfo);
            }

            @Override
            public void onFailure(String error) {

            }
        });
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

        // Set dropdown menu to choose family
        // Source: https://developer.android.com/guide/topics/ui/controls/spinner#java
        Log.d(TAG, "Size of mFamilyNameList before setting adapter: " + mFamilyNameList.size());
        mAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, mFamilyNameList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFamilySpinner.setAdapter(mAdapter);
        mFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.d(TAG, "onItemSelected() called for dropdown menu");
                mFamilyId = mFamilyIdList.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // required
                Log.d(TAG, "onNothingSelected() called for dropdown menu");
            }
        });
        Log.d(TAG, "Size of mFamilyNameList after setting adapter: " + mFamilyNameList.size());

        Button button = v.findViewById(R.id.add_dog_button);
        button.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        Log.i(TAG, "onClick() called");
        switch (v.getId()) {
            case R.id.add_dog_button:
                if (!mDogName.equals("") && !mFamilyId.equals("")) {

                    // add dog to database and add dog ID under family ID corresponding to
                    // selected family name
                    DatabaseReference ref = mDatabase.child("families").child(mFamilyId)
                            .child("dogs");
                    String dogId = ref.push().getKey(); // get new unique ID from Firebase
                    String activitiesId = mDatabase.child("activities").push().getKey();
                    Dog dog = new Dog(dogId, mDogName, activitiesId);
                    Task updateTask = ref.child(dogId).setValue(dog.toMap());
                    if (CurrentUser.isConnectedToDatabase()) {
                            updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Successfully added new dog!",
                                        Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Database update failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getActivity(), "Successfully added new dog!",
                                Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }
                else {
                    Log.d(TAG, "test");
                    Toast.makeText(getActivity(), "All fields must be populated",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    /*
     * Populate mFamilyIdList and mFamilyNameList for dropdown
     */
    private void parseFamilyInfo(FamilyInfo familyInfo) {
        String familyId = familyInfo.getfamilyId();
        String familyName = familyInfo.getfamilyName();
        Log.d(TAG, "Adding familyId " + familyId + " to list");
        mFamilyIdList.add(familyInfo.getfamilyId());
        Log.d(TAG, "Adding familyName " + familyName + " to list");
        mFamilyNameList.add(familyInfo.getfamilyName());
        Log.d(TAG, "Size of mFamilyNameList before notifying mAdapter that data set changed: " +
                mFamilyNameList.size());
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "Notified adapter that data set changed");
        }
    }

}
