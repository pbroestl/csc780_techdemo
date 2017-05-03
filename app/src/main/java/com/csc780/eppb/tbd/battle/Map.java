package com.csc780.eppb.tbd.battle;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Elaine on 4/26/2017.
 */


public class Map {

    private String difficulty;
    @SerializedName("enemy_count")
    private int enemyCount;
    @SerializedName("enemies")
    private ArrayList<Enemy> enemyList;

    public String getDifficulty() {
        return difficulty;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public ArrayList<Enemy> getEnemyList() {
        return enemyList;
    }


}

