package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.battle.EnemyList;
import com.csc780.eppb.tbd.screens.BattleScreen;
import com.csc780.eppb.tbd.tools.Attack;

/**
 * Created by owner on 4/26/2017.
 */


public abstract class Enemy extends Unit {
    //Constant Variables
    protected final float MOVE_SPEED = 70.0f;

    // The possible animation states an enemy can exhibit
    public enum EnemyState {
        IDLE, MOVING, ATTACKING, HURTING, DYING, TARGETING, DEAD
    }

    ;

    private float posX;
    private float posY;

    // Enemy Stats
    protected float hp;
    protected float damage;
    protected float defense;

    //used to start the Enemy AI at the beginning of the turn
    protected boolean isTurnSet;

    //keeping track recent state
    protected EnemyState currentState;
    protected EnemyState previousState;
    protected boolean faceRight;

    protected boolean isHurting;

    protected float randAttackTimer;
    protected float stateTimer;


    public boolean isAttackSet;

    //Targeting and movement variables
    protected Fixture targetSensor;
    protected Fixture rangeSensor;

    protected boolean isTargeting;
    protected boolean isRangeSet;

    protected Hero previousTarget;
    protected Hero currentTarget;

    protected Vector2 movementDirection;
    protected float distanceFromTarget;

    protected Vector2 enemyPosition;
    protected Vector2 enemyStartPosition;
    protected Vector2 heroPosition;

    // Fields shared between all enemies
    private int id;
    private String name;
    private int maxHp;
    private int def;
//    private String element;
//    private ArrayList<Attack> attacks;

    public float health;

    public float damageTaken;


    public Enemy(int id, BattleScreen screen, Rectangle bounds) {
        super(screen, bounds);
        defineBody(bounds);

        //From Json file
        this.id = id;
        name = EnemyList.getEnemy(id).getName();
        maxHp = EnemyList.getEnemy(id).getMaxHp();
        def = EnemyList.getEnemy(id).getDef();

        isHero = false;

        //vectors for calculating movement/Targeting
        enemyPosition = new Vector2();
        enemyStartPosition = new Vector2();
        heroPosition = new Vector2();
        movementDirection = new Vector2();

        isTurnSet = false;
        isTargeting = false;
        isRangeSet = false;
    }

    //Override functions
    @Override
    protected void defineBody(Rectangle bounds) {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        CircleShape circle = new CircleShape();

        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        bdef.allowSleep = false;
        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 4, bounds.getHeight() / 4);
        circle.setRadius(bounds.getWidth()/4);
//        fdef.shape = shape;
        fdef.shape = circle;

        fdef.filter.categoryBits = NeetGame.ENEMY_BIT;
        fdef.filter.maskBits = NeetGame.DEFAULT_BIT | NeetGame.CHARACTER_BIT | NeetGame.ATTACK_BIT;

//        fdef.isSensor = true;
        fixture = body.createFixture(fdef);

        posX = bounds.getX();
        posY = bounds.getY();

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


    abstract public void onAttackHit(Attack attack);

    public void createRangeSensor() {
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius((getWidth() / 3));

        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = NeetGame.RANGE_BIT;
        fdef.filter.maskBits = NeetGame.CHARACTER_BIT;

        rangeSensor = body.createFixture(fdef);
        rangeSensor.setUserData(this);
    }

    public void createTargetSensor() {
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();

        shape.setRadius((getWidth()) * 1.5f);

        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = NeetGame.TARGET_BIT;
        fdef.filter.maskBits = NeetGame.CHARACTER_BIT;

        targetSensor = body.createFixture(fdef);
        targetSensor.setUserData(this);
    }


    public void targetCharacter(Hero character) {
        //setting the vectors for calculating the direction and distance between the target
        heroPosition.set(character.body.getPosition().x, character.body.getPosition().y);
        enemyPosition.set(body.getPosition().x, body.getPosition().y);
        float tempDistance = enemyPosition.dst(heroPosition);

        currentTarget = character;
        movementDirection.set(heroPosition).sub(enemyPosition).nor();

        enemyStartPosition = enemyPosition;
        distanceFromTarget = tempDistance;
    }

    public void attackCharacter(Hero character) {

        if (currentTarget.getId() == character.getId()) {
            isTargeting = false;
//            isRangeSet = false;

            body.setLinearVelocity(0, 0);

            isAttacking = true;
            stateTimer = 0.0f;
            randAttackTimer = 1.0f + (float) Math.random() * 8;

        }
    }

    public int getMaxHp() {
        return maxHp;
    }
}
