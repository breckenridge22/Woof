package com.osu.cse.apps.mobile.woof;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class NewActivityRecordFragment extends Fragment implements View.OnClickListener{


        private static final String TAG = "NewActivityRecordFragment";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        private List<String> dogIDList;
        private List<String> dogActsList;

        // Views
        private Spinner activity_type_spinner;
        private EditText food_amount_edittext;
        private Spinner food_metrics_spinner;
        private EditText food_brand_edittext;
        private EditText calories_eaten_edittext;
        private Spinner bathroom_type_spinner;
        private EditText vet_address_edittext;
        private EditText vet_reason_edittext;

        private LinearLayout vet_view;
        private LinearLayout bathroom_view;
        private LinearLayout food_view;
        private View view2;
        private View view4;
        private View middle_view;

        private Button submit_button;

        ActivityRecord mActivityRecord;

        public static NewActivityRecordFragment newInstance() {
            Log.d(TAG, "Called newInstance()");
            return new NewActivityRecordFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            Log.d(TAG, "Called onCreate()");
            super.onCreate(savedInstanceState);
            Bundle b = getArguments();
            dogIDList = b.getStringArrayList("DOG_ID_LIST");
            dogActsList = b.getStringArrayList("DOG_ACTS_LIST");
            Log.d(TAG, "Size of dogIDList: " + dogIDList.size());
            Log.d(TAG, "Size of dogActsList: " + dogActsList.size());
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d(TAG, "onCreateView() called");
            View v = inflater.inflate(R.layout.fragment_new_activity, container, false);

            // Prepare the activities map
            if(CurrentUser.getmCurrentActivities()!=null){
                CurrentUser.clearmCurrentActivities();
            }
            CurrentUser.setmCurrentActivities(dogActsList);
            int size = CurrentUser.getmCurrentActivities().size();
            Log.d(TAG, "Dog map ready");
            Log.d(TAG, "Dog map size: " + size);

            Log.d(TAG, "Initializing view elements");
            activity_type_spinner = (Spinner) v.findViewById(R.id.activity_type_spinner);

            // Food
            food_amount_edittext = (EditText) v.findViewById(R.id.food_amount_edittext);
            food_metrics_spinner = (Spinner) v.findViewById(R.id.food_metrics_spinner);
            food_brand_edittext = (EditText) v.findViewById(R.id.food_brand_edittext);
            calories_eaten_edittext = (EditText) v.findViewById(R.id.calories_eaten_edittext);

            // Bathroom
            bathroom_type_spinner = (Spinner) v.findViewById(R.id.bathroom_type_spinner);

            // Vet Visit
            vet_address_edittext = (EditText) v.findViewById(R.id.vet_address_edittext);
            vet_reason_edittext = (EditText) v.findViewById(R.id.vet_reason_edittext);

            submit_button = (Button) v.findViewById(R.id.new_activity_submit);
            submit_button.setEnabled(false);

            vet_view = (LinearLayout) v.findViewById(R.id.vet_view);
            bathroom_view = (LinearLayout) v.findViewById(R.id.bathroom_view);
            food_view = (LinearLayout) v.findViewById(R.id.food_view);
            view2 = (View) v.findViewById(R.id.secondview);
            view4 = (View) v.findViewById(R.id.fourthview);
            middle_view = (View) v.findViewById(R.id.middle_view);


            Log.d(TAG, "Generating Spinner adapters");
            // Generate activity_type_spinner selection values
            ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.activity_types, android.R.layout.simple_spinner_item);
            // WARNING: must call toString() on CharSequence items to compare equality
            activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activity_type_spinner.setAdapter(activityAdapter);

            ArrayAdapter<CharSequence> metricsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.food_metrics, android.R.layout.simple_spinner_item);
            metricsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            food_metrics_spinner.setAdapter(metricsAdapter);

            ArrayAdapter<CharSequence> bathroomAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.bathroom_type, android.R.layout.simple_spinner_item);
            bathroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bathroom_type_spinner.setAdapter(bathroomAdapter);

            activity_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "activity_type_spinner item selected");

                    String activity_type = parent.getItemAtPosition(position).toString();
                    switch(activity_type){
                        case "Walk":
                            // Visible: None
                            setGoneAll();
                            mActivityRecord = new ActivityRecord(ActivityRecord.WALK);
                            break;
                        case "Food":
                            // Visible: food_metrics, food_amount, calories_eaten, food_brand
                            setFoodVisible();
                            mActivityRecord = new ActivityRecord(ActivityRecord.FOOD);
                            break;
                        case "Water":
                            // Visible: None
                            setGoneAll();
                            mActivityRecord = new ActivityRecord(ActivityRecord.WATER);
                            break;
                        case "Bathroom":
                            // Visible: bathroom_type_spinner
                            setBathroomVisible();
                            mActivityRecord = new ActivityRecord(ActivityRecord.BATHROOM);
                            break;
                        case "Vet Visit":
                            // Visible: vet_address, vet_visit_reason
                            setVetVisible();
                            mActivityRecord = new ActivityRecord(ActivityRecord.VETVISIT);
                            break;
                        default:
                            setGoneAll();

                    }
                    enableButtonIfPossible();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Auto-generated stub
                    submit_button.setEnabled(false);
                }
            });


            /* FOOD LISTENERS */
            food_metrics_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "Food Metrics Spinner Item Selected");
                    if(mActivityRecord.getactivity_Type() == ActivityRecord.FOOD){
                        if(parent.getItemAtPosition(position).toString().equals("Oz")){
                            mActivityRecord.setfood_Metric(ActivityRecord.OUNCES);
                        } else {
                            mActivityRecord.setfood_Metric(ActivityRecord.CUPS);
                        }
                    } else{
                        Log.d(TAG, "Error: Expected food activity; Received activity code " + mActivityRecord.getactivity_Type());
                    }
                    enableButtonIfPossible();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Auto-generated stub
                }
            });


            food_amount_edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Auto-generated stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "Food Amount Text Editor modified");
                    mActivityRecord.setfood_Amount(Integer.parseInt(s.toString().trim()));
                    enableButtonIfPossible();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Auto-generated stub
                }

              });

            food_brand_edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Auto-generated stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "Food Brand Text Editor modified");
                    mActivityRecord.setfood_Brand(s.toString().trim());
                    enableButtonIfPossible();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Auto-generated stub
                }
        });

            calories_eaten_edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Auto-generated stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "Calories Eaten Text Editor modified");
                    mActivityRecord.setcalories(Integer.parseInt(s.toString().trim()));
                    enableButtonIfPossible();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Auto-generated stub
                }
            });

            /*  BATHROOM LISTENERS */
            bathroom_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "Bathroom Spinner Item Selected");
                    if(mActivityRecord.getactivity_Type() == ActivityRecord.BATHROOM){
                        if(parent.getItemAtPosition(position).toString().equals("Pee")){
                            mActivityRecord.setbathroom_Type(ActivityRecord.PEE);
                        }
                        else if(parent.getItemAtPosition(position).toString().equals("Poop")){
                            mActivityRecord.setbathroom_Type(ActivityRecord.POO);
                        } else{
                            mActivityRecord.setbathroom_Type(ActivityRecord.BOTH);
                        }
                    }
                    else{
                        Log.d(TAG, "Error: Expected activity_type = BATHROOM");
                    }
                    enableButtonIfPossible();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Auto-generated stub
                }
            });

            /* VET VISIT LISTENERS */
            vet_address_edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Auto-generated stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "VET ADDRESS");
                    Log.d(TAG, s.toString());
                    Log.d(TAG, s.toString().trim());
                    mActivityRecord.setvet_Location(s.toString().trim());
                    enableButtonIfPossible();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Auto-generated stub
                }
            });

            vet_reason_edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Auto-generated stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "VET REASON");
                    Log.d(TAG, s.toString());
                    Log.d(TAG, s.toString().trim());
                    mActivityRecord.setvet_Visit_Reason(s.toString().trim());
                    enableButtonIfPossible();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Auto-generated stub
                }
            });

            submit_button.setOnClickListener(this);

            return v;
        }

        @Override
        public void onClick(View v){
            Log.d(TAG, "submit_button clicked");
            int activity_type = mActivityRecord.getactivity_Type();
            if(activity_type == ActivityRecord.WALK){
                Log.d(TAG, "Starting walk activity");
                Intent intent = MapsActivity.newIntent(getActivity());
                startActivity(intent);
            } else{
                Log.d(TAG, "Saving activity record");
                CurrentUser.addActivity(mActivityRecord);
                CurrentUser.saveActivity();
                Log.d(TAG, "Redirecting to home screen");
                Intent intent = HomeScreenActivity.newIntent(getActivity());
                startActivity(intent);
            }

        }

        private void setGoneAll(){
            Log.d(TAG, "setGoneAll() called.");
            vet_view.setVisibility(View.GONE);
            bathroom_view.setVisibility(View.GONE);
            food_view.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
            middle_view.setVisibility(View.VISIBLE);
        }

        private void setBathroomVisible(){
            Log.d(TAG, "setBathroomVisible() called.");
            vet_view.setVisibility(View.GONE);
            bathroom_view.setVisibility(View.VISIBLE);
            food_view.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
            middle_view.setVisibility(View.GONE);
        }

        private void setFoodVisible(){
            Log.d(TAG, "setFoodVisible() called.");
            vet_view.setVisibility(View.GONE);
            bathroom_view.setVisibility(View.GONE);
            food_view.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
            middle_view.setVisibility(View.GONE);
        }

        private void setVetVisible(){
            Log.d(TAG, "setVetVisible() called.");
            vet_view.setVisibility(View.VISIBLE);
            bathroom_view.setVisibility(View.GONE);
            food_view.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
            middle_view.setVisibility(View.GONE);
        }

        private void enableButtonIfPossible() {
            Log.d(TAG, "enableButtonIfPossible() called.");
            // Button disabled by default
            if (mActivityRecord == null) {
                Log.d(TAG, "Error: unexpected null at mActivityRecord");
            } else {
                submit_button.setEnabled(false);
                switch (mActivityRecord.getactivity_Type()) {
                    case ActivityRecord.WALK:
                        submit_button.setEnabled(true);
                        break;
                    case ActivityRecord.BATHROOM:
                        if (mActivityRecord.getbathroom_Type() != 0) {
                            submit_button.setEnabled(true);
                        }
                        break;
                    case ActivityRecord.WATER:
                        submit_button.setEnabled(true);
                        break;
                    case ActivityRecord.FOOD:
                        // Don't need to require calories
                        if (mActivityRecord.getfood_Amount() > 0) {
                            if (mActivityRecord.getfood_Metric() == ActivityRecord.OUNCES || mActivityRecord.getfood_Metric() == ActivityRecord.CUPS) {
                                submit_button.setEnabled(true);
                            }
                        }
                        break;
                    case ActivityRecord.VETVISIT:
                        Log.d(TAG, "Checking Vet_visit conditions for submit button");
                        if (!mActivityRecord.getvet_Visit_Reason().isEmpty()) {
                            if (!mActivityRecord.getvet_Location().isEmpty()) {
                                submit_button.setEnabled(true);
                            }
                        }
                        break;
                    default:
                        Log.d(TAG, "Error: Unexpected activity value: " + mActivityRecord.getactivity_Type());
                }
            }

        }
}

