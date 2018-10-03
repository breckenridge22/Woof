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

    public static DogSelectionFragment newInstance() {
        return new DogSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dog_selection, container, false);

        mDogRecyclerView = v.findViewById(R.id.dog_recycler_view);
        mDogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        List<Dog> dogs = CurrentUser.get().getDogList();

        if (mAdapter == null) {
            mAdapter = new DogAdapter(dogs);
            mDogRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class DogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Dog mDog;

        private ImageView mDogPictureImageView;
        private TextView mDogNameTextView;

        public DogHolder(LayoutInflater inflater, ViewGroup parent) {
            super (inflater.inflate(R.layout.list_item_dog, parent, false));
            itemView.setOnClickListener(this);

            mDogPictureImageView = itemView.findViewById(R.id.dog_picture);
            mDogNameTextView = itemView.findViewById(R.id.dog_name);
        }

        public void bind(Dog dog) {
            mDog = dog;
            // TODO - set dog picture on bind
            mDogNameTextView.setText(dog.getName());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "dog name is: " + mDog.getName());
            Log.d(TAG, "dog id is: " + mDog.getDogId());
            Intent intent = DogManagementActivity.newIntent(getActivity(), mDog.getDogId());
            startActivity(intent);
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
