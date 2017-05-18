package com.csc780.eppb.tbd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.csc780.eppb.tbd.battle.AttackList;
import com.csc780.eppb.tbd.battle.EnemyList;
import com.csc780.eppb.tbd.battle.GsonInstance;
import com.csc780.eppb.tbd.battle.MapList;

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


        Glide.with(this).load(R.drawable.circuit).asBitmap().into(
                new SimpleTarget<Bitmap>(1024, 560) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        LinearLayout background = (LinearLayout) findViewById(R.id.layoutMapSelectionBg);
                        background.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }
                }
        );
        mContext = this;

        GsonInstance.createGson(this);
        MapList.generate(this);
        EnemyList.generate(this);
        AttackList.generate(this);

        final Button mMapEasy = (Button) findViewById(R.id.selectEasy);
        final Button mMapMed = (Button) findViewById(R.id.selectMed);
        final Button mMapHard = (Button) findViewById(R.id.selectHard);

        mMapEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDifficulty = "easy";
                Intent intent = new Intent(mContext, BattleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                intent.putExtra("difficulty", mDifficulty);
                startActivity(intent);
            }
        });

        mMapMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDifficulty = "medium";
                Intent intent = new Intent(mContext, BattleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                intent.putExtra("difficulty", mDifficulty);
                startActivity(intent);
            }
        });


        mMapHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDifficulty = "hard";
                Intent intent = new Intent(mContext, BattleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
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
