package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class DogInformationActivity extends SingleFragmentActivity {

    private static final String EXTRA_DOG_ID =
            "com.osu.cse.apps.mobile.woof.DogInformationActivity";

    public static Intent newIntent(Context packageContext, UUID dogId) {
        Intent intent = new Intent(packageContext, DogInformationActivity.class);
        intent.putExtra(EXTRA_DOG_ID, dogId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID dogId = (UUID) getIntent().getSerializableExtra(EXTRA_DOG_ID);
        return DogInformationFragment.newInstance(dogId);
    }

}
