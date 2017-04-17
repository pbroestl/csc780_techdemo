package com.csc780.eppb.tbd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Elaine on 4/7/2017.
 */

public class TitleScreenActivity extends Activity {
    private static final String TAG = "Title Screen";
    private TextView mTapMsg;
    private LinearLayout mLinearLayout;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        mContext = this;

        mTapMsg = (TextView)  findViewById(R.id.tapMsg);
        mTapMsg.setAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));

        mLinearLayout = (LinearLayout) findViewById(R.id.layoutTitle);
        mLinearLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(mContext, MapSelectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }
        });

//        FrameLayout frameLayout;
//        frameLayout = (FrameLayout) findViewById(R.id.grid);
//        Log.v(TAG, "Width: " + frameLayout.getWidth());

//        GridFourView grid = new GridFourView(this.getApplicationContext(), this);
//        grid.drawGrid();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
