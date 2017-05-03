package com.csc780.eppb.tbd;

import android.view.MotionEvent;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csc780.eppb.tbd.battle.Map;
import com.csc780.eppb.tbd.screens.BattleScreen;
import com.csc780.eppb.tbd.battle.Enemy;

import java.util.ArrayList;

/**
 * Created by owner on 4/25/2017.
 */

public class NeetGame extends Game {

    public static final int V_WIDTH = 1024;
    public static final int V_HEIGHT = 560;
    //Pixels per Meter, used for scaling in relation to B2Body
    public static final float PPM  = 100.0f;

    //Collision bits for filtering game Objects;
    public static final short DEFAULT_BIT = 1;
    public static final short CHARACTER_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short ATTACK_BIT = 8;
    public static final short ENEMYATTACK_BIT = 16;
    public static final short TARGET_BIT = 32;
    public static final short RANGE_BIT = 64;

    private BattleScreen screen;
    public SpriteBatch batch ;

    private static Map map;

    public NeetGame() {
        super();
    }

    public NeetGame(Map map) {
        super();
        NeetGame.map = map;
    }

    @Override
    public void create(){
        batch = new SpriteBatch();
        screen = new BattleScreen(this);
        setScreen(screen);
    }

    public void render () {
        super.render();
    }

    public void setColor (float[] newColor) {
        screen.setColor(newColor);
    }

    public void addCombo(){
        screen.addCombo();
    }

    public static Map getGameMapInfo() {
        return map;
    }

    public void setAttack(String gesture) {
        screen.setAttack(gesture);
    }

}
