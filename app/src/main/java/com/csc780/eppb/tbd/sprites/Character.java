package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/26/2017.
 */

public abstract class Character extends Sprite {
    protected BattleScreen screen;
    protected World world;
    public Body b2body;

    //Fields shared between all characters

    public Character (BattleScreen screen, float x , float y ) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x,y);
    }

    protected abstract void defineCharacter(float x , float y);
}
