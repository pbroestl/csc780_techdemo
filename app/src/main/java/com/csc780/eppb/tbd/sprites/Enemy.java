package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.csc780.eppb.tbd.battle.EnemyList;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/26/2017.
 */

public abstract class Enemy extends Sprite {

    protected BattleScreen screen;
    protected World world;
    public Body body;
    protected Fixture fixture;
    private float posX;
    private float posY;

    // Fields shared between all enemy
    private int id;
    private String name;
    private int maxHp;
    private int def;
//    private String element;
//    private ArrayList<Attack> attacks;

    public Enemy (int id, BattleScreen screen, Rectangle bounds) {
        this.screen = screen;
        this.world  = screen.getWorld();
        setPosition(bounds.getX(),bounds.getY());

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(getX() , getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/4 , bounds.getHeight()/4);
        fdef.shape = shape;
       // fdef.isSensor = true;

        fixture  = body.createFixture(fdef);

        posX = bounds.getX();
        posY = bounds.getY();

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

    // Abstract classes
//    protected abstract void defineEnemy(float x, float y);

    abstract public void onAttackHit();

//    public abstract TextureRegion getTextureRegion();

}
