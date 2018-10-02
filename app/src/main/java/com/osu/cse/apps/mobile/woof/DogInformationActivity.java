package com.osu.cse.apps.mobile.woof;

import android.support.v4.app.Fragment;

import java.util.UUID;

public class DogInformationActivity extends DogManagementActivity {

    private static final String EXTRA_DOG_ID =
            "com.osu.cse.apps.mobile.woof.DogInformationActivity";

    @Override
    protected Fragment createFragment() {
        UUID dogId = (UUID) getIntent().getSerializableExtra(EXTRA_DOG_ID);
        return DogInformationFragment.newInstance(dogId);
    }

}
