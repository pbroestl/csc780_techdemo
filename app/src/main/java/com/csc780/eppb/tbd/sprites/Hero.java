package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/26/2017.
 */

public abstract class Hero extends Unit {

    //
    //Fields shared between all characters

    public Hero(BattleScreen screen, Rectangle bounds) {
        super(screen, bounds);
        defineBody(bounds);

        isHero = true;
    }

    public void setCatagoryFilter(short filterBit ) {
        Filter filter  = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    //Abstract functions
    public abstract void onAttackHit();

    //Override functions
    @Override
    protected void defineBody(Rectangle bounds) {
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
    }

    public int getId(){
        return id;
    }
}
