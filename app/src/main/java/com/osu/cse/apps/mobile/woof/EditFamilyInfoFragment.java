package com.osu.cse.apps.mobile.woof;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class EditFamilyInfoFragment extends FamilyFragment {

    private static final String TAG = "EditFamilyInfoFragment";
    private String mFamilyName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");

        View v = inflater.inflate(R.layout.fragment_edit_family_info, container, false);

        EditText familyNameEditText = v.findViewById(R.id.family_name_edit_text);
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

        Button saveChangesButton = v.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick() called");
                if (mFamilyName == null || mFamilyName.length() == 0) {
                    Toast.makeText(getActivity(), "Family name field is blank", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                            .child("families").child(getFamilyId()).child("familyName");
                    Task updateTask = ref.setValue(mFamilyName);
                    if (CurrentUser.isConnectedToDatabase()) {
                        updateTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error saving changes", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }

}
