package com.csc780.eppb.tbd.battle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Elaine on 4/28/2017.
 */

public class Attack {

    private int id;
    @SerializedName("attack")
    private String name;
    private String target;
    private float range;
    private String type;
    private int damage;

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getTarget() {
        return target;
    }
    public float getRange() {
        return range;
    }
    public String getType() {
        return type;
    }
    public int getDamage() {
        return damage;
    }
}
