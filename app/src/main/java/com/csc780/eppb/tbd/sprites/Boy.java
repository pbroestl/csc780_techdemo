package com.csc780.eppb.tbd.sprites;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/26/2017.
 */

public class Boy extends Character {

    private TextureRegion characterStand;
    private Animation characterRun;
    private Animation characterAttack;

    public enum State {STANDING, RUNNING, ATTACKING};
    private State currentState;
    private State previousState;
    private boolean faceRight;

    public boolean isAttacking;
    private float attackDuration;

    private float stateTimer;

    public Boy(BattleScreen screen, float x, float y, String spriteSheet) {
        super(screen, x, y, spriteSheet);
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0.0f;
        faceRight = true;

        isAttacking = false;
        attackDuration = 0.0f;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0 ; i < 9 ; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("link_attack"), i * 32, 0, 32, 32 ));
        characterAttack = new Animation(0.05f, frames);
        frames.clear();
        for (int i = 0 ; i < 7 ; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("link_run"), i * 32, 0, 32, 32 ));
        characterRun = new Animation(0.05f, frames);

        defineCharacter(x,y);
        characterStand = new TextureRegion (screen.getAtlas().findRegion("link_run"), 32 * 3 , 0 , 32 , 32 );
        setBounds(getX(),getY(),100, 100);
        setRegion(characterStand);
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
        if(faceRight)
            setPosition(b2body.getPosition().x + 20 - getWidth()/2 , b2body.getPosition().y - getHeight() / 2);
        else
            setPosition(b2body.getPosition().x - 20  - getWidth()/2, b2body.getPosition().y - getHeight() / 2);
    }

    public State getState(float dt){
        if (isAttacking) {
            attackDuration -= dt;
            if (attackDuration <= 0) {
                attackDuration = 0.0f;
                isAttacking = false;
            }
            return State.ATTACKING;
        } else if(b2body.getLinearVelocity().x != 0 )
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public TextureRegion getFrame (float dt) {
        currentState = getState(dt);

        TextureRegion region;
        switch (currentState) {
            case ATTACKING:
                region = (TextureRegion)characterAttack.getKeyFrame(stateTimer, false);
                break;
            case RUNNING :
                region = (TextureRegion)characterRun.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = characterStand;
                break;
        }

        if ((b2body.getLinearVelocity().x < 0  || !faceRight) && !region.isFlipX()){
            region.flip(true, false);
            faceRight = false;
        }else if ((b2body.getLinearVelocity().x > 0  || faceRight) && region.isFlipX()){
            region.flip(true, false);
            faceRight = true;
        }

        stateTimer  = currentState == previousState ? stateTimer + dt : 0.0f;
        previousState = currentState;
        return region;
    }

    public void setAttack() {
        if (!isAttacking) {
            isAttacking = true;
            attackDuration = 0.45f;
        }
    }

    @Override
    protected void defineCharacter(float x , float y) {
        BodyDef  bdef = new BodyDef();
        bdef.position.set(x, y );
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

      //  b2body.setLinearDamping(100.0f);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(40);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
