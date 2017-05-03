package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/26/2017.
 */

public abstract class Hero extends Sprite {
    protected BattleScreen screen;
    protected World world;
    public Body body;

    protected int id;

    Fixture fixture;

    //Fields shared between all characters

    public Hero(BattleScreen screen, Rectangle bounds) {
        //super(screen.getAtlas().findRegion(spriteSheet));
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(bounds.getX(), bounds.getY());
        id = 1;

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.allowSleep = false;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(35);
        fdef.filter.categoryBits = NeetGame.CHARACTER_BIT;
        fdef.filter.maskBits =  NeetGame.DEFAULT_BIT | NeetGame.ENEMY_BIT |NeetGame.ENEMYATTACK_BIT |NeetGame.TARGET_BIT | NeetGame.RANGE_BIT ;
        fdef.shape = shape;

        fixture  = body.createFixture(fdef);
        fixture.setUserData(this);

    }

    public void setCatagoryFilter(short filterBit ) {
        Filter filter  = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }


    public abstract void onAttackHit();

    public int getId(){
        return id;
    }
}
