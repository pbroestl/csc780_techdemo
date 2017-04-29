package com.csc780.eppb.tbd.battle;

import android.content.Context;

import com.csc780.eppb.tbd.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Elaine on 4/27/2017.
 */

public class EnemyList {

    private static BufferedReader reader;
    private static Gson gson;
    private static ArrayList<Enemy> enemyList;

    public static void generate(Context context) {
        Type t = new TypeToken<ArrayList<Enemy>>(){}.getType();
        reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.enemies)));
        gson = new Gson();
        enemyList = gson.fromJson(reader, t);
    }

    public static Enemy getEnemy(int id) {
        return enemyList.get(id);
    }
}
