package com.csc780.eppb.tbd;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Elaine on 4/17/2017.
 */

public class Player {
    public static final int MAX_HEALTH = 100;

    private String mName;
    private int mHealth;
    private Drawable mPicture;
    private Context mContext;

    public Player(Context context) {
        mContext = context;
        mName = context.getResources().getString(R.string.player_name);
        mHealth = MAX_HEALTH;
        mPicture = context.getDrawable(R.drawable.player_placeholder);
    }

    public Player(String name, int health, Drawable picture) {
        mName = name;
        mHealth = health;
        mPicture = picture;
    }

    public String getName() {
        return mName;
    }

    public int getHealth() {
        return mHealth;
    }

    public Drawable getPicture() {
        return mPicture;
    }
}
