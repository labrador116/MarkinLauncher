package com.development.markin.markinlauncher.view.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.development.markin.markinlauncher.R;
import com.development.markin.markinlauncher.view.fragments.MarkinLauncherFragment;

public class MarkinLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return MarkinLauncherFragment.newInstance();
    }
}
