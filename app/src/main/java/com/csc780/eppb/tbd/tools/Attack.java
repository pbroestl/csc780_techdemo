package com.csc780.eppb.tbd.tools;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/28/2017.
 */

public class Attack extends Sprite {

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

    public Attack (BattleScreen screen, Rectangle bounds){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(bounds.getX(), bounds.getY());

        defineAttack(bounds);

        testSprite = new TextureRegion(screen.getAtlas().findRegion("link_run"), 32 * 3 , 0 , 32 , 32 );
        setBounds(getX(),getY(), bounds.getWidth(), bounds.getHeight());

        setPosition(b2body.getPosition().x + 20 - getWidth()/2 , b2body.getPosition().y - getHeight() / 2);
        setRegion(testSprite);
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
        b2body.createFixture(fdef).setUserData("attack");
    }
}
