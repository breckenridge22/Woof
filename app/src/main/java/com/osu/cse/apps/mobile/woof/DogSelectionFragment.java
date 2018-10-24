package com.osu.cse.apps.mobile.woof;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DogSelectionFragment extends Fragment {

    private final String TAG = "DogSelectionFragment";

    private RecyclerView mDogRecyclerView;
    private DogAdapter mAdapter;

    private String activity_type;

    public static DogSelectionFragment newInstance() {
        return new DogSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate() called");

        // Retrieve values stored in intent
        Bundle bundle = this.getArguments();
        if(bundle != null){
            activity_type = bundle.getString("activity_type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_dog_selection, container, false);

        mDogRecyclerView = v.findViewById(R.id.dog_recycler_view);
        mDogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() called");
        updateUI();
    }

    private void updateUI() {
        Log.i(TAG, "on updateUI() called");
        List<Dog> dogs = CurrentUser.get().getdogList();

        /*
         * Below code segment doesn't work properly because the data set does doesn't actually change
         * when the user removes a dog from the map in the dogList class.  The user's getdogList() method used above returns
         * an array list that points to all of the dogs.  The elements in the array list still
         * point to a dog object after it is removed from the map in the dogList class.
         *
         * // TODO: use below code segement after refactoring dogList class into user object and removing dogList class altogether
         *
        if (mAdapter == null) {
            mAdapter = new DogAdapter(dogs);
            mDogRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }
        */

        mAdapter = new DogAdapter(dogs);
        mDogRecyclerView.setAdapter(mAdapter);

    }

    private class DogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Dog mDog;

        private ImageView mDogPictureImageView;
        private TextView mDogNameTextView;

        public DogHolder(LayoutInflater inflater, ViewGroup parent)                                                                                                                                                                 {
            super(inflater.inflate(R.layout.list_item_dog, parent, false));
            itemView.setOnClickListener(this);

            mDogPictureImageView = itemView.findViewById(R.id.dog_picture);
            mDogNameTextView = itemView.findViewById(R.id.dog_name);
        }

        public void bind(Dog dog) {
            mDog = dog;
            // TODO - set dog picture on bind
            mDogNameTextView.setText(dog.getdogName());
        }

        @Override
        public void onClick(View v) {
            if (activity_type.equals("manage_dogs")) {
                Intent intent = DogManagementActivity.newIntent(getActivity(), mDog.getdogId(),
                        DogManagementActivity.DOG_HOME);
                startActivity(intent);

            } else if (activity_type.equals("activity_history")) {
                Log.d(TAG, "Starting ActivityHistoryActivity.");
                Intent intent = ActivityHistoryActivity.newIntent(getActivity());
            }
        }
    }

    private class DogAdapter extends RecyclerView.Adapter<DogHolder> {

        private List<Dog> mDogs;

        public DogAdapter(List<Dog> dogs) { mDogs = dogs; }

        @Override
        public DogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new DogHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DogHolder holder, int position) {
            Dog dog = mDogs.get(position);
            holder.bind(dog);
        }

        @Override
        public int getItemCount() { return mDogs.size(); }

    }

}
