package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by Elaine on 4/27/2017.
 */

public class EnemyFactory {

    public static Unit getEnemy(int id, BattleScreen screen, Rectangle bounds) {
        switch(id) {
            case 0:
                return new TestEnemy(id, screen, bounds);
            case 1:
                return new TestEnemy(id, screen, bounds);
            case 2:
                return new TestEnemy(id, screen, bounds);
            case 3:
                return new TestEnemy(id, screen, bounds);
            case 4:
                return new TestEnemy(id, screen, bounds);
            case 5:
                return new TestEnemy(id, screen, bounds);
            case 6:
                return new TestEnemy(id, screen, bounds);
            default:
                return new TestEnemy(id, screen, bounds);
        }
    }
}
