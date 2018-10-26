package com.osu.cse.apps.mobile.woof;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NewActivityRecordFragment extends Fragment implements View.OnClickListener {


        private static final String TAG = "NewActivityRecordFragment";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        public static NewActivityRecordFragment newInstance() {
            return new NewActivityRecordFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }


//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            Log.d(TAG, "onCreateView() called");
//            // TODO
//
//        }

        public void onClick(View v) {
            // TODO
        }


    }

