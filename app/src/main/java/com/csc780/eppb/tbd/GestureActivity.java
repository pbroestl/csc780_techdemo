package com.csc780.eppb.tbd;

/**
 * Created by Paul  on 4/18/2017.
 */

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class GestureActivity extends AppCompatActivity implements OnGesturePerformedListener {

    private GestureLibrary boiSkillz;
    private GestureLibrary grilSkillz;
    private ArrayList<GestureLibrary> allTehSkillz;
    private int currentPlayer = 0 ;

    private GestureOverlayView gestureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestures);

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

        gestureView.addOnGesturePerformedListener(this);

    }

    private String lineOrientationCheck ( Gesture gesture) {
        float[] points = gesture.getStrokes().get(0).points ;

        float averageSlope = 0.0f;
        boolean firstPass = true;
        for (int i = 0; i <points.length - 4 ; i += 2 ){
            float x1 = points[i];
            float y1 = points[i + 1];
            float x2 = points[i + 2];
            float y2 = points[i + 3];

            float slope = Math.abs((y2 - y1 )/(x2 - x1));

            if (firstPass){
                averageSlope =  slope;
                firstPass = false;
            } else {
                averageSlope = (averageSlope + slope)/ 2 ;
            }

            // String coordinates = "x : " + points[i] + "  Y  : " + points[i+1];
            //  Log.v("NEET", coordinates);
            //  Log.v("NEET", Double.toString(averageSlope));
        }

        if(averageSlope > 1.0) {
            return "Vertical";
        } else {
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
                    Toast.makeText(getApplicationContext(), lineGestureType, Toast.LENGTH_SHORT).show();

                } else if (predictions.get(0).name.toUpperCase().equals("LUNK")) {
                    //change the character
                    currentPlayer = 1;
                    Toast.makeText(getApplicationContext(), "Changing Player to 1", Toast.LENGTH_SHORT).show();
                } else {
                    String action = predictions.get(0).name + " " + predictions.get(0).score;
                    Toast.makeText(getApplicationContext(), action, Toast.LENGTH_SHORT).show();
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
