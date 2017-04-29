package com.csc780.eppb.tbd.sprites;

import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by Elaine on 4/27/2017.
 */

public class EnemyFactory {

    public static Enemy getEnemy(int id, float x, float y, BattleScreen screen) {
        switch(id) {
            case 0:
                return new EnemyOne(id, screen, x, y);
            case 1:
                return new EnemyOne(id, screen, x, y);
            case 2:
                return new EnemyOne(id, screen, x, y);
            case 3:
                return new EnemyOne(id, screen, x, y);
            case 4:
            default:
                return new EnemyOne(id, screen, x, y);
        }
    }
}
