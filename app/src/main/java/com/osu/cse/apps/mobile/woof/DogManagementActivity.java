/*
 * Activity class for hosting a DogManagementFragment
 */

package com.osu.cse.apps.mobile.woof;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class DogManagementActivity extends SingleFragmentActivity {

    private static final String EXTRA_DOG_ID =
            "com.osu.cse.apps.mobile.woof.dogId.DogManagementActivity";

    public static Intent newIntent(Context packageContext, UUID dogId) {
        Intent intent = new Intent(packageContext, DogManagementActivity.class);
        intent.putExtra(EXTRA_DOG_ID, dogId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID dogId = (UUID) getIntent().getSerializableExtra(EXTRA_DOG_ID);
        return DogManagementFragment.newInstance(dogId);
    }

}
