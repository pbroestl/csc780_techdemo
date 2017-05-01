package com.csc780.eppb.tbd.tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/28/2017.
 */

public class EnemyAttack extends Sprite{

    protected BattleScreen screen;
    protected World world;
    public Body b2body;


    //Attack Animation variables
    protected float attackDuration;
    protected float castDuration;

    protected boolean isFixed;

    //Attack variable
    protected float damage;
    protected String type;  // Change to enum later


    TextureRegion testSprite;

    public EnemyAttack (BattleScreen screen, Rectangle bounds){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(bounds.getX(), bounds.getY());

        defineAttack(bounds);

        setBounds(getX(),getY(), bounds.getWidth(), bounds.getHeight());
    }


    protected void defineAttack(Rectangle bounds) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY() );
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.allowSleep = false;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(bounds.getWidth(),bounds.getHeight());

        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = NeetGame.ENEMYATTACK_BIT;
        fdef.filter.maskBits =  NeetGame.CHARACTER_BIT;

        b2body.createFixture(fdef).setUserData(this);
    }
}
