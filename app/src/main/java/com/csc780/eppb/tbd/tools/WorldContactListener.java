package com.csc780.eppb.tbd.tools;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.csc780.eppb.tbd.sprites.Enemy;

/**
 * Created by owner on 4/28/2017.
 */

public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "attack" || fixB.getUserData() == "attack"){
            Fixture attack = fixA.getUserData() == "attack" ? fixA : fixB;
            Fixture enemy = attack  == fixA ? fixB : fixA ;

            if (enemy.getUserData() instanceof Enemy){
                ((Enemy)enemy.getUserData()).onAttackHit();
            }
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
