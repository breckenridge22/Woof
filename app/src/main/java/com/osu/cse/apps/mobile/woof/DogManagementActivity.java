/*
 * Activity class for hosting a DogManagementFragment
 */

package com.osu.cse.apps.mobile.woof;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DogManagementActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DogManagementFragment();
    }

}
