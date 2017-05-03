package com.csc780.eppb.tbd.battle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Elaine on 4/27/2017.
 */

public class Enemy {
    @SerializedName("id")
    private int id;
    @SerializedName("position")
    private float[] position;
    @SerializedName("name")
    private String name;
    @SerializedName("hp")
    private int hp;
    @SerializedName("def")
    private int def;
    @SerializedName("attacks")
    private Attack[] attacks;
    @SerializedName("size")
    private float[] size;

    public int getId() {
        return id;
    }

    public float[] getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return hp;
    }

    public int getDef() {
        return def;
    }

    public Attack[] getAttacks() {
        return attacks;
    }

    public float[] getSize() {
        return size;
    }
}
