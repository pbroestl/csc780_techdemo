package com.csc780.eppb.tbd.sprites;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csc780.eppb.tbd.screens.BattleScreen;
import com.csc780.eppb.tbd.tools.Attack;
import com.csc780.eppb.tbd.tools.EnemyAttack;

/**
 * Created by owner on 4/28/2017.
 */

public class TestEnemy extends Enemy {

    private TextureRegion enemyStand;
    private Animation enemyRun;
    private Animation enemyAttack;
    private TextureRegion enemyHit;

    public enum EnemyState {STANDING, RUNNING, ATTACKING, HURTING};
    private EnemyState currentState;
    private EnemyState previousState;
    private boolean faceRight;

    private EnemyAttack currentAttack;
    public boolean isAttacking;
    private float attackDuration;

    private boolean isHurting;
    private float isHurtingCounter;

    private float randAttackTimer;

    private float stateTimer;

    public TestEnemy(BattleScreen screen, Rectangle bounds) {
        super(0, screen, bounds);
        fixture.setUserData(this);

        currentState = EnemyState.STANDING;
        previousState = EnemyState.STANDING;
        stateTimer = 0.0f;
        faceRight = true;

        isAttacking = false;
        attackDuration = 0.0f;

        randAttackTimer = 1.0f + (float)Math.random()*2;

        isHurting = false;
        isHurtingCounter  = 0.0f;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0 ; i < 8 ; i++)
            frames.add(new TextureRegion(screen.bowserAtlas.findRegion("bowser_attack"), i * 64, 0, 64, 64 ));
        enemyAttack = new Animation(0.075f, frames);
        frames.clear();
        for (int i = 0 ; i < 3 ; i++)
            frames.add(new TextureRegion(screen.bowserAtlas.findRegion("bowser_run"), i * 64, 0, 64, 64 ));
        enemyRun = new Animation(0.1f, frames);

        enemyStand = new TextureRegion (screen.bowserAtlas.findRegion("bowser_run"), 64, 0 , 64 , 64 );
        enemyHit = new TextureRegion (screen.bowserAtlas.findRegion("bowser_hit"), 0 , 0 , 64 , 64 );
        setBounds(body.getPosition().x - bounds.getWidth()/2 ,body.getPosition().y - bounds.getHeight()/2 ,bounds.getWidth(), bounds.getHeight());
        setRegion(enemyStand);
    }

    public void update(float dt) {
        setRegion(getFrame(dt));

        randAttackTimer -= dt ;
        if(randAttackTimer <= 0 && !isAttacking) {
            isAttacking = true;
            attackDuration = 0.4f;
            stateTimer = 0.0f;

            if (faceRight)
                currentAttack = new EnemyAttack(screen, new Rectangle(body.getPosition().x + getWidth()/2 -10, body.getPosition().y , 28, 40));
            else
                currentAttack = new EnemyAttack(screen, new Rectangle(body.getPosition().x - getWidth()/2 +10, body.getPosition().y , 28, 40));
        }

       // body.applyLinearImpulse( new Vector2(10,0) ,body.getWorldCenter(), true);

        setPosition(body.getPosition().x  - getWidth()/2, body.getPosition().y - getHeight() / 2);
    }

    public EnemyState getState(float dt){
        if (isAttacking) {
            attackDuration -= dt;
            if (attackDuration <= 0) {
                isAttacking = false;
                randAttackTimer = 1.0f + (float)Math.random()*3;
                world.destroyBody(currentAttack.b2body);
            }
            return EnemyState.ATTACKING;
        } else if(isHurting) {
            isHurtingCounter -= dt;
            if(isHurtingCounter <= 0) {
                isHurting = false;
            }
            return EnemyState.HURTING;
        } else if (body.getLinearVelocity().x != 0 )
            return EnemyState.RUNNING;
        else
            return EnemyState.STANDING;
    }

    public TextureRegion getFrame (float dt) {
        currentState = getState(dt);

        TextureRegion region;
        switch (currentState) {
            case ATTACKING:
                region = (TextureRegion)enemyAttack.getKeyFrame(stateTimer);
                break;
            case RUNNING :
                region = (TextureRegion)enemyRun.getKeyFrame(stateTimer, true);
                break;
            case HURTING:
                region = enemyHit;
                break;
            case STANDING:
            default:
                region = enemyStand;
                break;
        }

        if ( !faceRight && region.isFlipX()){
            region.flip(true, false);
            faceRight = false;
        }else if (faceRight && !region.isFlipX()){
            region.flip(true, false);
            faceRight = true;
        }

        stateTimer  = currentState == previousState ? stateTimer + dt : 0.0f;
        previousState = currentState;
        return region;
    }

//    @Override
    public void onAttackHit() {
        Log.v("hit", "Test enemy hit");
        screen.addCombo();
        isHurtingCounter = 0.25f;
        isHurting = true;
    }
}
