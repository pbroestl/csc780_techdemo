package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.csc780.eppb.tbd.battle.EnemyList;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/26/2017.
 */

public abstract class Enemy extends Sprite {

    protected BattleScreen screen;
    protected World world;
    public Body b2body;
    private float posX;
    private float posY;

    // Fields shared between all enemy
    private int id;
    private String name;
    private int maxHp;
    private int def;
//    private String element;
//    private ArrayList<Attack> attacks;

    public Enemy (int id, BattleScreen screen, float x , float y ) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x,y);

        posX = x;
        posY = y;

        this.id = id;
        name = EnemyList.getEnemy(id).getName();
        maxHp = EnemyList.getEnemy(id).getMaxHp();
        def = EnemyList.getEnemy(id).getDef();
    }

    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public abstract void update(float dt);

    protected abstract void defineEnemy(float x, float y);

    public abstract TextureRegion getTextureRegion();
}
