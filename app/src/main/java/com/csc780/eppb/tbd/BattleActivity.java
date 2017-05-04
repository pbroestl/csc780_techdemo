package com.csc780.eppb.tbd;

import android.app.DialogFragment;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.csc780.eppb.tbd.battle.Map;
import com.csc780.eppb.tbd.battle.MapList;

import java.util.ArrayList;

/**
 * Created by Elaine on 4/17/2017.
 */

public class BattleActivity extends AndroidApplication implements OnGesturePerformedListener {

    private static final String TAG = "Battle Activity";

    private GestureLibrary skillsBoy;
    private GestureLibrary skillsGirl;
    private ArrayList<GestureLibrary> skills;
    private int currentPlayer = 0;

    private GestureOverlayView gestureView;
    View gameView;

    private NeetGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        Map map = MapList.getMap(this.getIntent().getStringExtra("difficulty"));
        game = new NeetGame(map);

        RelativeLayout uiView = (RelativeLayout) findViewById(R.id.layoutBattleInterface);
        gameView = initializeForView(game);
        uiView.addView(gameView, 0);

        skillsBoy = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if(!skillsBoy.load())
            finish();

        skillsGirl = GestureLibraries.fromRawResource(this, R.raw.gestures2);
        if(!skillsGirl.load())
            finish();

        skills = new ArrayList<>();
        skills.add(skillsBoy);
        skills.add(skillsGirl);

        gestureView = (GestureOverlayView) findViewById(R.id.gestureOverlay);
        gestureView.setGestureStrokeAngleThreshold(90.0f);
        gestureView.setGestureStrokeLengthThreshold(100.f);
        gestureView.setGestureColor(Color.WHITE);
        gestureView.addOnGesturePerformedListener(this);


        final Button mHelpButton = (Button) findViewById(R.id.buttonHelp);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpFragmentDialog();
            }
        });

        final Button mRunButton = (Button) findViewById(R.id.buttonRun);
        mRunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BattleActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event ) {
        Log.v(TAG, "Working");
        gameView.onTouchEvent(event);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
    }

    public void showHelpFragmentDialog() {
        DialogFragment dialogFragment = HelpDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    private String lineOrientationCheck ( Gesture gesture) {
        float[] color = {0,1,0,1};
        float[] color2 = {0,0,1,1};

        float[] points = gesture.getStrokes().get(0).points ;

        short totalPoints = 0;
        short horizontalPoints = 0;

        boolean firstPass = true;
        for (int i = 0; i <points.length - 4 ; i += 2 ){
            float x1 = points[i];
            float y1 = points[i + 1];
            float x2 = points[i + 2];
            float y2 = points[i + 3];

            float slope = Math.abs((y2 - y1 )/(x2 - x1));

            if(slope <= 1.0f )
                horizontalPoints++;

            totalPoints++;
            // String coordinates = "x : " + points[i] + "  Y  : " + points[i+1];
            // Log.v("NEET", coordinates);
            Log.d(TAG, Short.toString(horizontalPoints));
            Log.d(TAG, Float.toString(totalPoints));
        }

        if((totalPoints - horizontalPoints) > horizontalPoints) {
            return "Vertical";
        } else {

            return "Horizontal";

        }
    }


    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        if(gesture.getLength() >= 5000){
            return;
        }

        ArrayList<Prediction> predictions = skills.get(currentPlayer).recognize(gesture);
        String gestureString;

        if (predictions.size() > 0 && predictions.get(0).score > 2.5) {
            if (currentPlayer == 0) {

                if (predictions.get(0).name.toUpperCase().equals("LINE"))
                    gestureString = lineOrientationCheck(gesture);
                else
                    gestureString = predictions.get(0).name.toUpperCase();
                game.setAttack(gestureString);

            } else if(currentPlayer == 1 ){

                if (predictions.get(0).name.toUpperCase().equals("OMEGA")) {
                    //change the character
                    currentPlayer = 0;
                    Toast.makeText(getApplicationContext(), "Changing Player to 0", Toast.LENGTH_SHORT).show();
                } else {
                    String action = predictions.get(0).name + " " + predictions.get(0).score;
                    Toast.makeText(getApplicationContext(), action, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
