package com.csc780.eppb.tbd.battle;

import android.content.Context;

import com.google.gson.Gson;

/**
 * Created by Elaine on 5/2/2017.
 */

public class GsonInstance {
    protected static Gson gson;

    public static void createGson(Context context) {
        gson = new Gson();
    }

}
