package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment implements View.OnClickListener {


    private Button mEmailUpdateButton;
    private Button mPasswordUpdateButton;
    private Button mFNameUpdateButton;
    private Button mLNameUpdateButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mFNameEditText;
    private EditText mLNameEditText;
    private FirebaseUser mUser;
    private static final String TAG = "ProfileFragment";


    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static ProfileFragment newInstance() { return new ProfileFragment(); }

    /*
     * Hosting activity must extend this interface
     */
    public interface Callbacks {
        LayoutInflater getInflater();
    }

    private Callbacks mCallbacks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        mEmailUpdateButton = v.findViewById(R.id.update_email_button);
        mPasswordUpdateButton = v.findViewById(R.id.update_password_button);
        mFNameUpdateButton = v.findViewById(R.id.update_fName_button);
        mLNameUpdateButton = v.findViewById(R.id.update_lName_button);
        mEmailEditText = v.findViewById(R.id.update_email_edit);
        mPasswordEditText = v.findViewById(R.id.update_pass_edit);
        mFNameEditText = v.findViewById(R.id.update_fName_edit);
        mLNameEditText = v.findViewById(R.id.update_lName_edit);

        if (mEmailUpdateButton != null) {
            mEmailUpdateButton.setOnClickListener(this);
        }
        if (mPasswordUpdateButton != null) {
            mPasswordUpdateButton.setOnClickListener(this);
        }
        if (mFNameUpdateButton != null) {
            mFNameUpdateButton.setOnClickListener(this);
        }
        if (mLNameUpdateButton != null) {
            mLNameUpdateButton.setOnClickListener(this);
        }
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        return v;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallbacks = (ProfileFragment.Callbacks) context ;
    }


    public void onClick(View v) {
        int i = v.getId();
        User user = CurrentUser.get();
        switch(i){
            case R.id.update_email_button:
                Log.d(TAG, "Email Update button pressed.");
                mUser.updateEmail(mEmailEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Updated Email", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "User email address updated.");
                                }
                            }
                        });
                break;
            case R.id.update_password_button:
                Log.d(TAG, "Password Update button pressed.");
                mUser.updatePassword(mPasswordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Updated Password", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "User password address updated.");
                                }
                            }
                        });
                break;
            case R.id.update_fName_button:
                Log.d(TAG, "First name Update button pressed.");
                user.changeFirstName(mFNameEditText.getText().toString());
                Toast.makeText(getActivity(), "Updated First Name", Toast.LENGTH_SHORT).show();
                break;
            case R.id.update_lName_button:
                Log.d(TAG, "Last name Update button pressed.");
                user.changeLastName(mLNameEditText.getText().toString());
                Toast.makeText(getActivity(), "Updated Last Name", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
