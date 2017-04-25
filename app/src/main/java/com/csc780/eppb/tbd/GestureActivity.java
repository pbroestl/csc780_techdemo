package com.csc780.eppb.tbd;

/**
 * Created by Paul  on 4/18/2017.
 */

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;

import java.util.ArrayList;

public class GestureActivity extends AndroidApplication implements OnGesturePerformedListener{

    private GestureLibrary boiSkillz;
    private GestureLibrary grilSkillz;
    private ArrayList<GestureLibrary> allTehSkillz;
    private int currentPlayer = 0 ;

    private GestureOverlayView gestureView;

    private NeetGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures);

        game = new NeetGame();

        TextView text= new TextView(this);
        text.setText ("Why does this work?");
        text.setTextSize(50.0f);

        boiSkillz =
                GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!boiSkillz.load()) {
            finish();
        }
        grilSkillz =
                GestureLibraries.fromRawResource(this, R.raw.gestures2);
        if (!grilSkillz.load()) {
            finish();
        }
        allTehSkillz = new ArrayList<>();
        allTehSkillz.add(boiSkillz);
        allTehSkillz.add(grilSkillz);

        gestureView =
                (GestureOverlayView) findViewById(R.id.gestureOverlay);
        gestureView.setGestureStrokeAngleThreshold(90.0f);
        gestureView.setGestureStrokeLengthThreshold(100.0f);
        gestureView.setGestureColor(Color.BLUE);

        gestureView.addOnGesturePerformedListener(this);

        //AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View gameView = initializeForView(game);

          gestureView.addView(text);
          gestureView.addView(gameView, 0);

    }

    private String lineOrientationCheck ( Gesture gesture) {
        float[] color = {0,1,0,1};
        float[] color2 = {0,1,1,1};

        float[] points = gesture.getStrokes().get(0).points ;

        float averageSlope = 0.0f;

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
            Log.v("NEET", Short.toString(horizontalPoints));
             Log.v("NEET", Float.toString(totalPoints));
        }

        if((totalPoints - horizontalPoints) > horizontalPoints) {
            game.setColor(color);
            return "Vertical";
        } else {
            game.setColor(color2);
            return " Horizontal";
        }
    }


    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        if(gesture.getLength() >= 5000){
            return;
        }

       // Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT).show();

        ArrayList<Prediction> predictions = allTehSkillz.get(currentPlayer).recognize(gesture);

        if (predictions.size() > 0 && predictions.get(0).score > 2.5) {
            if (currentPlayer == 0) {

                if (predictions.get(0).name.toUpperCase().equals("LINE")) {
                    String lineGestureType = lineOrientationCheck(gesture);
             //       Toast.makeText(getApplicationContext(), lineGestureType, Toast.LENGTH_SHORT).show();
                    game.addCombo();

                } else if (predictions.get(0).name.toUpperCase().equals("LUNK")) {
                    //change the character
                    currentPlayer = 1;
             //       Toast.makeText(getApplicationContext(), "Changing Player to 1", Toast.LENGTH_SHORT).show();
                } else {
                    String action = predictions.get(0).name + " " + predictions.get(0).score;
             //       Toast.makeText(getApplicationContext(), action, Toast.LENGTH_SHORT).show();
                }

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
