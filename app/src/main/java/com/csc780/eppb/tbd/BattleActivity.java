package com.csc780.eppb.tbd;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Created by Elaine on 4/17/2017.
 */

public class BattleActivity extends AndroidApplication {

    private static final String TAG = "Battle Activity";
    private String mDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MyGdxGame(), config);
//        setContentView(R.layout.activity_battle);
//
//        mDifficulty = getIntent().getExtras().getString("difficulty");
//        Log.d(TAG, "You selected difficulty " + mDifficulty);
//        setMap(mDifficulty);
//
//        final Button mHelpButton = (Button) findViewById(R.id.buttonHelp);
//        mHelpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showHelpFragmentDialog();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setMap(String difficulty) {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void showHelpFragmentDialog() {
        DialogFragment dialogFragment = HelpDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(), "dialog");
    }
}
