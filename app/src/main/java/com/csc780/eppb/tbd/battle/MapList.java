package com.csc780.eppb.tbd.battle;

import android.content.Context;

import com.csc780.eppb.tbd.R;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Elaine on 4/27/2017.
 */

public class MapList {

    private static BufferedReader reader;
    private static ArrayList<Map> maps;
    private static HashMap<String, ArrayList<Map>> filteredMaps;

    public static void generate(Context context) {
        Type t = new TypeToken<ArrayList<Map>>(){}.getType();
        reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.maps)));
        maps = GsonInstance.gson.fromJson(reader, t);

        filteredMaps = new HashMap<>();
        filteredMaps.put("easy", new ArrayList<Map>());
        filteredMaps.put("medium", new ArrayList<Map>());
        filteredMaps.put("hard", new ArrayList<Map>());

        for(int i = 0; i < maps.size(); i++) {
            filteredMaps.get(maps.get(i).getDifficulty()).add(maps.get(i));
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map getMap(String difficulty) {
        return filteredMaps.get(difficulty).get(0);
    }
}
