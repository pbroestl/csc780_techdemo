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
import com.badlogic.gdx.utils.StringBuilder;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.screens.BattleScreen;
import com.csc780.eppb.tbd.sprites.Enemy;

/**
 * Created by owner on 4/28/2017.
 */

public class Attack extends Sprite {

    protected BattleScreen screen;
    protected World world;
    public Body b2body;

    //Saved for initialization of the Attack body
    private boolean isHero;
    private Rectangle bounds;
    private boolean isAttackSet;

    //Attack Animation variables
    protected float attackDuration;
    protected boolean isAttackFinished;


    protected boolean isFixed;

    //Attack variable
    protected float damage;
    protected String type;  // Change to enum later


    TextureRegion testSprite;

    public Attack (BattleScreen screen, Rectangle bounds,  boolean isHero, String gesture){
        this.screen = screen;
        this.world = screen.getWorld();

        isAttackSet = false;
        this.bounds = bounds;
        this.isHero = isHero;
        //Sprite Loading, and setting
        attackDuration = 0.3f;

        setDamage(gesture);
    }

    public void update (float dt) {
        if(!isAttackSet)
            initializeAttack();

        attackDuration -= dt;
        if(!isAttackFinished && attackDuration <= 0){
            isAttackFinished  = true;
            world.destroyBody(b2body);
        }
    }

    private void initializeAttack() {
        setPosition(bounds.getX(), bounds.getY());
        defineAttack(bounds, isHero);
        setBounds(getX(),getY(), bounds.getWidth(), bounds.getHeight());

        isAttackSet = true;
    }

    protected void defineAttack(Rectangle bounds, boolean isHero) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(getX(), getY() );
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.allowSleep = false;

        b2body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth(),bounds.getHeight());
        fdef.shape = shape;
        fdef.isSensor = true;

        if (isHero) {
            fdef.filter.categoryBits = NeetGame.ATTACK_BIT;
            fdef.filter.maskBits = NeetGame.ENEMY_BIT;
        } else {
            fdef.filter.categoryBits = NeetGame.ENEMYATTACK_BIT;
            fdef.filter.maskBits =  NeetGame.CHARACTER_BIT;
        }

        b2body.createFixture(fdef).setUserData(this);
    }

    public void setDamage(String gesture) {
        switch(gesture) {
            case "Vertical":
                damage = 1;
                break;
            case "Horizontal":
                damage = 1;
                break;
            case "CIRCLE":
                damage = 5;
                break;
            case "TRIANGLE":
                damage = 10;
                break;
            case "CHECK":
                damage = 15;
                break;
            default:
                damage = 0;
        }
    }

    public boolean isAttackFinished(){
        return isAttackFinished;
    }

    public float getDamage() {
        return damage;
    }

}
