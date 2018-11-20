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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateNewFamilyFragment extends Fragment {

    private static final String TAG = "CreateNewFamilyFragment";
    private static final String KEY_FAMILY_NAME = "family_name";
    private String mFamilyName;

    public static CreateNewFamilyFragment newInstance() {
        return new CreateNewFamilyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");
        if (savedInstanceState != null) {
            mFamilyName = savedInstanceState.getString(KEY_FAMILY_NAME, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_new_family, container, false);

        EditText familyNameEditText = v.findViewById(R.id.family_name_edit_text);
        if (mFamilyName != null) {
            familyNameEditText.setText(mFamilyName);
        }
        familyNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // required
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "onTextChanged() called");
                mFamilyName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // required
            }
        });


        Button createButton = v.findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFamilyName == null || mFamilyName.length() == 0) {
                    Toast.makeText(getActivity(),
                            "\"Enter family name field\" must not be blank", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    String familyId = ref.child("families").push().getKey();
                    String userId = CurrentUser.getUserId();
                    Family newFamily = new Family(familyId, mFamilyName, userId);

                    // atomically create new family and add new familyId to user's list
                    Map<String, Object> childUpdates = new HashMap();
                    childUpdates.put("/families/" + familyId, newFamily.toMap());
                    childUpdates.put("/users/" + userId + "/familyIds/" + familyId, true);
                    Task updateTask = ref.updateChildren(childUpdates);
                    if (CurrentUser.isConnectedToDatabase()) {
                        updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "New family successfully created", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Failed to create new family", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "New family successfully created", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState() called");
        if (mFamilyName != null) {
            savedInstanceState.putString(KEY_FAMILY_NAME, mFamilyName);
        }
    }

}
