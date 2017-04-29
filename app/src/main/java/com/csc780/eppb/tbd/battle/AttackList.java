package com.csc780.eppb.tbd.battle;

import android.content.Context;

import com.csc780.eppb.tbd.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Elaine on 4/28/2017.
 */

public class AttackList {

    private static BufferedReader reader;
    private static Gson gson;
    private static ArrayList<Attack> attackList;
    private static HashMap<String, ArrayList<Attack>> filteredAttacks;
    private static final String[] attackTypes = { "fire", "ice", "physical" };

    public static void generate(Context context) {
        Type t = new TypeToken<ArrayList<Attack>>(){}.getType();
        reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.attacks)));
        gson = new Gson();
        attackList = gson.fromJson(reader, t);

        filteredAttacks = new HashMap<>();
        for (String attackType : attackTypes)
            filteredAttacks.put(attackType, new ArrayList<Attack>());

        for(int i = 0; i < attackList.size(); i++)
            filteredAttacks.get(attackList.get(i).getType()).add(attackList.get(i));
    }

    public static Attack getAttack(int id) {
        return attackList.get(id);
    }

    public static ArrayList<Attack> getTypedAttacks(String type) {
        return filteredAttacks.get(type);
    }
}
