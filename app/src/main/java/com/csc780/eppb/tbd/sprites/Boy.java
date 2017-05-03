package com.csc780.eppb.tbd.sprites;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.csc780.eppb.tbd.screens.BattleScreen;
import com.csc780.eppb.tbd.tools.Attack;

/**
 * Created by owner on 4/26/2017.
 */

public class Boy extends Hero {

    private TextureRegion characterStand;
    private Animation characterRun;
    private Animation characterAttack;
    private TextureRegion characterHurt;

    public enum PlayerState {STANDING, RUNNING, ATTACKING, HURTING};
    private PlayerState currentState;
    private PlayerState previousState;
    private boolean faceRight;

    private Attack currentAttack;
    public boolean isAttacking;
    private float attackDuration;

    private boolean isHurting;
    private float isHurtingCounter;

    private float stateTimer;
    private boolean isPaused;

    private final float MAX_HEALTH = 100;
    private float currentHealth;

    public Boy(BattleScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);

        currentState = PlayerState.STANDING;
        previousState = PlayerState.STANDING;
        stateTimer = 0.0f;
        isPaused = false;

        faceRight = true;

        isAttacking = false;
        isHurting = false ;
        isHurtingCounter = 0.0f;

        attackDuration = 0.0f;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0 ; i < 9 ; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("link_attack"), i * 32, 0, 32, 32 ));
        characterAttack = new Animation(0.05f, frames);
        frames.clear();
        for (int i = 0 ; i < 7 ; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("link_run"), i * 32, 0, 32, 32 ));
        characterRun = new Animation(0.05f, frames);

        characterStand = new TextureRegion (screen.getAtlas().findRegion("link_run"), 32 * 3 , 0 , 32 , 32 );
        characterHurt = new TextureRegion (screen.getAtlas().findRegion("link_run"), 32 * 3 , 0 , 32 , 32 );
        setBounds(getX(),getY(),100, 100);
        setRegion(characterStand);

        currentHealth = MAX_HEALTH;
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
//        if(screen.heroTurnTimer <= 0)
//            endTurn();

        if(faceRight)
            setPosition(body.getPosition().x + 20 - getWidth()/2 , body.getPosition().y - getHeight() / 2);
        else
            setPosition(body.getPosition().x - 20  - getWidth()/2, body.getPosition().y - getHeight() / 2);
    }

    public void turnUpdate(float dt) {
        if(!isAttacking && screen.heroTurnTimer <= 0) {
            endTurn();
        }
    }

    public PlayerState getState(float dt){
        if(isHurting) {
            isHurtingCounter -= dt;
            if (isHurtingCounter <= 0) {
                isHurting = false;
            }
            return PlayerState.HURTING;

        } else if (isAttacking) {
            return PlayerState.ATTACKING;
        } else if(body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0)
            return PlayerState.RUNNING;
        else
            return PlayerState.STANDING;
    }

    public TextureRegion getFrame (float dt) {
        currentState = getState(dt);

        TextureRegion region;
        switch (currentState) {
            case ATTACKING:
                region = (TextureRegion)characterAttack.getKeyFrame(stateTimer);
                if(characterAttack.isAnimationFinished(stateTimer)) {
                    world.destroyBody(currentAttack.b2body);
                    isAttacking = false;
                }
                break;
            case RUNNING :
                region = (TextureRegion)characterRun.getKeyFrame(stateTimer, true);
                break;
            case HURTING:
                this.setColor(1,0,0,1);
                region = characterStand;
                break;
            case STANDING:
            default:
                region = characterStand;
                break;
        }
        if (previousState == PlayerState.HURTING && currentState != PlayerState.HURTING)
            this.setColor(1,1,1,1);

        if ((body.getLinearVelocity().x < 0  || !faceRight) && !region.isFlipX()){
            region.flip(true, false);
            faceRight = false;
        }else if ((body.getLinearVelocity().x > 0  || faceRight) && region.isFlipX()){
            region.flip(true, false);
            faceRight = true;
        }

        if(!isPaused) {
                stateTimer  = currentState == previousState ? stateTimer + dt : 0.0f;
        }
        previousState = currentState;
        return region;
    }

    public void setAttack(String gesture) {
        //check if the player is already attacking
        if (!isAttacking) {
            stateTimer = 0.0f;
            isAttacking = true;
            attackDuration = 0.45f;

            if (faceRight)
                currentAttack = new Attack(screen, new Rectangle(body.getPosition().x + 40, body.getPosition().y , 28, 40), gesture);
            else
                currentAttack = new Attack(screen, new Rectangle(body.getPosition().x - 40, body.getPosition().y , 28, 40), gesture);

        }
    }
    @Override
    public void onAttackHit() {
        Log.v("hit", "Player Hit health: " + currentHealth);
        if((currentHealth -= 5) < 0 )
            currentHealth = 0;
        isHurtingCounter = 0.25f;
        isHurting = true;

    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getMAX_HEALTH() {
        return MAX_HEALTH;
    }
}
