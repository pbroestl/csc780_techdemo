package com.csc780.eppb.tbd.sprites;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.csc780.eppb.tbd.screens.BattleScreen;
import com.csc780.eppb.tbd.tools.Attack;
import com.csc780.eppb.tbd.tools.EnemyAttack;

/**
 * Created by owner on 4/28/2017.
 */

public class TestEnemy extends Enemy {

    private TextureRegion enemyIdle;
    private Animation enemyMove;
    private Animation enemyAttack;
    private Animation enemyHurt;
    private Animation enemyDying;

    private int currentHP;


    public TestEnemy(int id, BattleScreen screen, Rectangle bounds) {
        super(id, screen, bounds);
        fixture.setUserData(this);

        loadAnimations();

        isDead = false;

        currentState = EnemyState.IDLE;
        previousState = EnemyState.IDLE;
        stateTimer = 0.0f;
        faceRight = false;

        isAttacking = false;
        isAttackSet = false;

        randAttackTimer = 1.0f + (float)Math.random()*8;

        isHurting = false;

        setBounds(body.getPosition().x - bounds.getWidth()/2 ,body.getPosition().y - bounds.getHeight()/2 ,bounds.getWidth(), bounds.getHeight());
        setRegion(enemyIdle);

        currentHP = getMaxHp();
    }

    public void loadAnimations (){
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //loading Idle
        enemyIdle = new TextureRegion (screen.bowserAtlas.findRegion("bowser_run"), 64, 0 , 64 , 64 );
        //loading Moving
        for (int i = 0 ; i < 3 ; i++)
            frames.add(new TextureRegion(screen.bowserAtlas.findRegion("bowser_run"), i * 64, 0, 64, 64 ));
        enemyMove = new Animation(0.1f, frames);
        frames.clear();
        //loading Attacking
        for (int i = 0 ; i < 8 ; i++)
            frames.add(new TextureRegion(screen.bowserAtlas.findRegion("bowser_attack"), i * 64, 0, 64, 64 ));
        enemyAttack = new Animation(0.1f, frames);
        frames.clear();

        //loading Hurting
        frames.add(new TextureRegion (screen.bowserAtlas.findRegion("bowser_hit"), 0 , 0 , 64 , 64 ));
        enemyHurt = new Animation(0.25f, frames);
        frames.clear();
        //loading Dying
        frames.add(new TextureRegion (screen.bowserAtlas.findRegion("bowser_hit"), 0 , 0 , 64 , 64 ));
        frames.add(new TextureRegion (screen.bowserAtlas.findRegion("bowser_hit"), 0 , 0 , 0 , 0 ));
        frames.add(new TextureRegion (screen.bowserAtlas.findRegion("bowser_hit"), 0 , 0 , 64 , 64 ));
        frames.add(new TextureRegion (screen.bowserAtlas.findRegion("bowser_hit"), 0 , 0 , 0 , 0 ));
        frames.add(new TextureRegion (screen.bowserAtlas.findRegion("bowser_hit"), 0 , 0 , 64 , 64 ));
        enemyDying = new Animation (0.1f, frames);
        frames.clear();
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
//        if(isTurn)
//            enemyAI();



//        randAttackTimer -= dt ;
//        if(randAttackTimer <= 0 && !isTargeting) {
//            createTargetSensor();
//            isTargeting = true;
//        }

        setPosition(body.getPosition().x  - getWidth()/2, body.getPosition().y - getHeight() / 2);
    }

    public void turnUpdate(float dt) {
        enemyAI();
    }

    private void enemyAI() {

        //Initializes the target on the first frame of the Enemy turn
        // Allow a physics step to take place before continuing
        if(!isTurnSet){
            createTargetSensor();
            isTurnSet = true;
            isTargeting = true;
            return;
        }

        //Second frame
        if (!isRangeSet && currentTarget == null)
            currentTarget = previousTarget;

        if(!isRangeSet && !isAttacking) {
            if ( currentTarget != null){
//                targetSensor.setUserData(null);
                body.destroyFixture(targetSensor);

                //set the Movement direction relative to currentTarget Position
                heroPosition.set(currentTarget.body.getPosition().x,currentTarget.body.getPosition().y );
                enemyPosition.set(body.getPosition().x, body.getPosition().y);
                movementDirection.set(heroPosition).sub(enemyPosition).nor();

                body.setLinearVelocity(movementDirection.scl(MOVE_SPEED));
                createRangeSensor();
                isRangeSet = true;
            } else {
                isTurnSet = false;
                body.destroyFixture(targetSensor);
                endTurn();
            }
        }

        if(!isAttackSet && isAttacking) {
            isRangeSet = false;
            body.destroyFixture(rangeSensor);

            if (!faceRight)
                currentAttack = new EnemyAttack(screen, new Rectangle(body.getPosition().x + getWidth() / 2 - 10, body.getPosition().y, 28, 40));
            else
                currentAttack = new EnemyAttack(screen, new Rectangle(body.getPosition().x - getWidth() / 2 + 10, body.getPosition().y, 28, 40));
            isAttackSet = true;

        }

    }
    public EnemyState getState(float dt){


        if(isHurting) {
        return EnemyState.HURTING;
    } else if (isAttacking) {
        return EnemyState.ATTACKING;
    } else if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0 )
        return EnemyState.MOVING;
    else
        return EnemyState.IDLE;
    }

    public TextureRegion getFrame (float dt) {
        currentState = getState(dt);

        TextureRegion region;
        switch (currentState) {
            case ATTACKING:
                region = (TextureRegion)enemyAttack.getKeyFrame(stateTimer);
                if (enemyAttack.isAnimationFinished(stateTimer)){
                  world.destroyBody(currentAttack.b2body);
                  isAttackSet = false;
                  isAttacking = false;
                    previousTarget = currentTarget;
                    currentTarget = null;
                    isTurnSet = false;
                    endTurn();
                }
                break;
            case MOVING :
                region = (TextureRegion)enemyMove.getKeyFrame(stateTimer, true);
                break;
            case HURTING:
                region = (TextureRegion) enemyHurt.getKeyFrame(stateTimer);
                if (enemyHurt.isAnimationFinished(stateTimer))
                    isHurting = false ;
                break;
            case IDLE:
            default:
                region = enemyIdle;
                break;
        }

        if ( (body.getLinearVelocity().x < 0 || faceRight) && region.isFlipX()){
            region.flip(true, false);
            faceRight = true;
        }else if ( (body.getLinearVelocity().x > 0 || !faceRight) && !region.isFlipX()){
            region.flip(true, false);
            faceRight = false;
        }

        stateTimer  = currentState == previousState ? stateTimer + dt : 0.0f;
        previousState = currentState;
        return region;
    }

    public void onAttackHit(Attack attack) {
        if(!isHurting) {
            Log.v("hit", "Took " + attack.getDamage() + " and HP is " + currentHP);
            screen.addCombo();
            isHurting = true;
            stateTimer = 0.0f;
            calculateDamage(attack.getDamage());
        }
    }

    private void calculateDamage(float damage) {
        if((currentHP -= damage) <= 0) {
            currentHP = 0;
            isDead = true;
        }
        damageTaken = damage;

    }


}
