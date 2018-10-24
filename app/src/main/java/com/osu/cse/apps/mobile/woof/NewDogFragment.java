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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewDogFragment extends Fragment implements View.OnClickListener {

    private EditText mDogNameEditText;
    private String mDogName = "";
    private Spinner mFamilySpinner;
    private String mFamilyId = "";
    private Map<String, Family> mFamilyMap;
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
        getFamilyInformation();
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
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
        //        R.array.family_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, mFamilyNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFamilySpinner.setAdapter(adapter);
        mFamilySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mFamilyId = mFamilyIdList.get(pos);
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

                if (!mDogName.equals("") && !mFamilyId.equals("")) {

                    // add dog to database and add dog ID under family ID corresponding to
                    // selected family name
                    String dogId = mDatabase.child("dogs").push().getKey(); // get new unique ID
                                                                            // from Firebase
                    Dog dog = new Dog(dogId, mDogName, mFamilyId);
                    Family family = mFamilyMap.get(mFamilyId);
                    family.addDogId(dogId);

                    Map<String, Object> childUpdates = new HashMap();
                    childUpdates.put("/dogs/" + dogId, dog.toMap());
                    childUpdates.put("/families/"+ mFamilyId, family.toMap());
                    mDatabase.updateChildren(childUpdates) // update database atomically
                            // print toast and finish activity if database successfully updated
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Successfully added new dog!",
                                        Toast.LENGTH_SHORT);
                                getActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Database update failed",
                                        Toast.LENGTH_SHORT);
                            }
                        });

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
    public void getFamilyInformation() {
        mFamilyMap = CurrentUser.get().getfamilyMap();
        for (Map.Entry<String, Family> entry : mFamilyMap.entrySet()) {
            mFamilyIdList.add(entry.getKey());
            mFamilyNameList.add(entry.getValue().getfamilyName());
        }
    }

}
