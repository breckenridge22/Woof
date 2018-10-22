package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment implements View.OnClickListener {


    private Button mEmailUpdateButton;
    private Button mPasswordUpdateButton;
    private Button mNameUpdateButton;
    private Button mDNameUpdateButton;
    private static final String TAG = "ProfileFragment";
    private PopupWindow pw;
    private View mView;
    LayoutInflater mPopupInflater;
    View mPwView;


    public static FirebaseAuth mAuth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static FirebaseUser user;

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
        //View v;
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mEmailUpdateButton = mView.findViewById(R.id.update_email_button);
        mPasswordUpdateButton = mView.findViewById(R.id.update_password_button);
        mNameUpdateButton = mView.findViewById(R.id.update_name_button);
        mDNameUpdateButton = mView.findViewById(R.id.update_dName_button);

        if (mEmailUpdateButton != null) {
            mEmailUpdateButton.setOnClickListener(this);
        }
        if (mPasswordUpdateButton != null) {
            mPasswordUpdateButton.setOnClickListener(this);
        }
        if (mNameUpdateButton != null) {
            mPasswordUpdateButton.setOnClickListener(this);
        }
        if (mDNameUpdateButton != null) {
            mDNameUpdateButton.setOnClickListener(this);
        }

        return mView;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mCallbacks = (ProfileFragment.Callbacks) context ;
    }


    public void onClick(View v) {
        int i = v.getId();
        switch(i){
            case R.id.update_email_button:
                Log.d(TAG, "Creating Popup");
                mPopupInflater = mCallbacks.getInflater();
                Log.d(TAG, "Created inflater");
                mPwView = mPopupInflater.inflate(R.layout.popup_update_email, null, false);
                Log.d(TAG, "Created view");
                pw = new PopupWindow(mPwView, 100, 100, true);
                Log.d(TAG, "Created popup");
                pw.showAtLocation(mView.findViewById(R.id.main), Gravity.CENTER, 0, 0);
                Log.d(TAG, "Showing Popup");
                break;
            case R.id.update_password_button:
                break;
            case R.id.update_name_button:
                break;
            case R.id.update_dName_button:
                break;
        }
    }


}
