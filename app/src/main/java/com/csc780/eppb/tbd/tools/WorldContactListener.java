package com.csc780.eppb.tbd.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.sprites.Enemy;
import com.csc780.eppb.tbd.sprites.Hero;

/**
 * Created by owner on 4/28/2017.
 */

public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case NeetGame.ATTACK_BIT | NeetGame.ENEMY_BIT :
                if(fixA.getFilterData().categoryBits == NeetGame.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).onAttackHit();
                } else if(fixB.getFilterData().categoryBits == NeetGame.ENEMY_BIT) {
                    ((Enemy) fixB.getUserData()).onAttackHit();
                }
                break;
            case NeetGame.ENEMYATTACK_BIT | NeetGame.CHARACTER_BIT :
                if(fixA.getFilterData().categoryBits == NeetGame.CHARACTER_BIT)
                    ((Hero)fixA.getUserData()).onAttackHit();
                else if(fixB.getFilterData().categoryBits == NeetGame.CHARACTER_BIT)
                    ((Hero)fixB.getUserData()).onAttackHit();
                break;
            case NeetGame.TARGET_BIT | NeetGame.CHARACTER_BIT :
                if(fixA.getFilterData().categoryBits == NeetGame.TARGET_BIT)
                    ((Enemy)fixA.getUserData()).targetCharacter((Hero)fixB.getUserData());
                else if(fixB.getFilterData().categoryBits == NeetGame.TARGET_BIT)
                    ((Enemy)fixB.getUserData()).targetCharacter((Hero)fixA.getUserData());
                break;
            case NeetGame.RANGE_BIT | NeetGame.CHARACTER_BIT :
                if(fixA.getFilterData().categoryBits == NeetGame.RANGE_BIT)
                    ((Enemy)fixA.getUserData()).attackCharacter((Hero)fixB.getUserData());
                else if(fixB.getFilterData().categoryBits == NeetGame.RANGE_BIT)
                    ((Enemy)fixB.getUserData()).attackCharacter((Hero)fixA.getUserData());
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
