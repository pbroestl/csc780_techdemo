package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/26/2017.
 */

public abstract class Enemy extends Sprite {
    protected BattleScreen screen;
    public Body b2body;

    //Fields shared between all characters

    public Enemy (BattleScreen screen, float x , float y ) {
        this.screen = screen;
        setPosition(x,y);
    }

    protected abstract void defineEnemy();
}
