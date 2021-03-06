package com.osu.cse.apps.mobile.woof;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import static com.osu.cse.apps.mobile.woof.CurrentUser.getDogMap;

public class MultiDogSelectionActivity extends AppCompatActivity implements MultiSelectClick {

    private static final String TAG = "MultiDogSelectionActivity";

    private RecyclerView mRecyclerView;
    private Button mContinueButton;
    private RecyclerView.Adapter mAdapter;
    private List<DogInfo> mDogInfoList;
    private Map<String, Boolean> mDogSelected;
    private List<String> mSelectedDogs;

    public static Intent newIntent(Context packageContext) {
        Log.d(TAG, "newIntent() called.");
        return new Intent(packageContext, MultiDogSelectionActivity.class);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("selected_dogs", MultiselectRecyclerViewAdapter.getSelected());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called.");
        super.onCreate(savedInstanceState);


        mDogInfoList=new ArrayList<>();
        mDogSelected = new HashMap<>();
        mSelectedDogs = new ArrayList();

        if( savedInstanceState !=null && !savedInstanceState.isEmpty()) {
            ArrayList<String> selected_dogs = savedInstanceState.getStringArrayList("selected_dogs");

            if (selected_dogs.size() > 0) {
                for (String dog_id : selected_dogs) {
                    mDogSelected.put(dog_id, true);
                    mSelectedDogs.add(dog_id);
                }
            }
        }

        // Retrieve list of dogs
        Log.d(TAG, "Getting CurrentUser's Dogs.");

            // populate dog info list from database
            CurrentUser.getDogInfoFromDatabase(new DogInfoCallback() {
                @Override
                public void onDogInfoRetrieved(DogInfo dogInfo) {
                    mDogInfoList.add(dogInfo);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    String dogID = dogInfo.getdogId();
                    if(!mDogSelected.containsKey(dogID)) {
                        mDogSelected.put(dogID, false);
                    }

                }

                @Override
                public void onFailure(String error) {
                    Log.d(TAG, error);
                }
            });

        setContentView(R.layout.multi_select_dog);

        mContinueButton = (Button) findViewById(R.id.multi_select_continue_button);
        if(mSelectedDogs.size()>0){
            mContinueButton.setEnabled(true);
        } else {
            mContinueButton.setEnabled(false);
        }
        mContinueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "Calling newIntent() from NewActivityRecordActivity");
                Intent intent = NewActivityRecordActivity.newIntent(MultiDogSelectionActivity.this, mSelectedDogs, mDogInfoList);
                startActivity(intent);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        Log.d(TAG, "Getting MultiselectRecyclerViewAdapter");
        Log.d(TAG, mDogSelected.toString());
        mAdapter = new MultiselectRecyclerViewAdapter(mDogInfoList, mDogSelected, this);
        LinearLayoutManager manager = new LinearLayoutManager(MultiDogSelectionActivity.this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);
        Log.d(TAG, "Setting Adapter: Line 78");
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(Map<String, Boolean> dogSelected){
        Log.d(TAG, "Called onClick() (implemented interface");
        mSelectedDogs = new ArrayList();
        for(String key:dogSelected.keySet()){
            if(dogSelected.containsKey(key) && dogSelected.get(key) == true){
                mSelectedDogs.add(key);
            }
            if(mSelectedDogs.size() > 0){
                mContinueButton.setEnabled(true);
            } else{
                mContinueButton.setEnabled(false);
            }
        }
    }
}

