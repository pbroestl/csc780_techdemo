package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by Elaine on 4/27/2017.
 */

public class EnemyOne extends Enemy {

    private TextureRegion standRegion;
    private Animation animStand;
    private Animation animAttack;
    private Animation animMove;
    private Animation animDefend;

    public enum State {STANDING, RUNNING, ATTACKING};
    private State currentState;
    private State previousState;

    private float stateTimer;

    public EnemyOne(int id, BattleScreen screen, float x, float y) {
        super(id, screen, x, y);
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0.0f;

        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getEnemyAtlus().findRegion("bad"+ id, i+1)));
        }
        animStand = new Animation(0.05f, frames);
        frames.clear();

        defineEnemy(x,y);
        standRegion = new TextureRegion(screen.getEnemyAtlus().findRegion("bad"+id, -1));
        setBounds(getX(), getY(), 50, 50);
        setRegion(standRegion);
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
    }

    public State getState(float dt) {
        return State.STANDING;
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState(dt);
        TextureRegion textureRegion;

        switch(currentState) {
            case STANDING:
            default:
                textureRegion = (TextureRegion)animStand.getKeyFrame(stateTimer, true);
                break;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0.0f;
        return textureRegion;
    }

    protected void defineEnemy(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
    public TextureRegion getTextureRegion() {
        return new TextureRegion(screen.getEnemyAtlus().findRegion("bad"+this.getId()));
    }
}
