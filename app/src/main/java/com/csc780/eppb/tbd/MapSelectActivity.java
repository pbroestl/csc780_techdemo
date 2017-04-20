package com.csc780.eppb.tbd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Elaine on 4/14/2017.
 */

public class MapSelectActivity extends Activity {
    private static final String TAG = "Map Select";
//    private Button mMapEasy, mMapMed, mMapHard;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mContext = this;

        final Button mMapEasy = (Button) findViewById(R.id.selectEasy);
        final Button mMapMed = (Button) findViewById(R.id.selectMed);
        final Button mMapHard = (Button) findViewById(R.id.selectHard);

        mMapEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDifficulty = "Easy";
                Intent intent = new Intent(mContext, BattleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("difficulty", mDifficulty);
                startActivity(intent);
            }
        });

        mMapMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDifficulty = "Medium";
                Intent intent = new Intent(mContext, BattleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("difficulty", mDifficulty);
                startActivity(intent);
            }
        });


        mMapHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDifficulty = "Hard";
                Intent intent = new Intent(mContext, BattleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("difficulty", mDifficulty);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
