package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteFragment extends Fragment implements View.OnClickListener {


    private Button mYesButton;
    private Button mNoButton;
    private static final String TAG = "DeleteFragment";



    public static FirebaseAuth mAuth;

    public static DeleteFragment newInstance() { return new DeleteFragment(); }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View v;
        v = inflater.inflate(R.layout.fragment_delete_confirm, container, false);

        mAuth = FirebaseAuth.getInstance();

        mYesButton = v.findViewById(R.id.yes_button);
        mNoButton = v.findViewById(R.id.no_button);


        if (mYesButton != null) {
            mYesButton.setOnClickListener(this);
        }
        if (mNoButton != null) {
            mNoButton.setOnClickListener(this);
        }

        return v;
    }

    public void onClick(View v) {
        int i = v.getId();
        Intent intent;
        FirebaseUser aUser = mAuth.getCurrentUser();
        switch(i){
            case R.id.yes_button:
                aUser.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User account deleted.");
                                User dbUser = CurrentUser.get();
                                dbUser.deleteUser();
                                CurrentUser.setNull();
                                Intent intent = LoginActivity.newIntent(getActivity());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText(getActivity(), "Deleted Account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                break;
            case R.id.no_button:
                intent = SettingsActivity.newIntent(getActivity());
                startActivity(intent);
                break;
        }
    }
}
