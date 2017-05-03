package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 5/1/2017.
 */

    public abstract class Unit extends Sprite {

        // BattleScreen and World
        protected BattleScreen screen;
        protected World world;

        //Box2d body associated with the Sprite
        public Body body;
        Fixture fixture;

        //Booleans Associated with BattleScreen
        protected boolean isTurn;
        protected boolean isDead;
        protected boolean isHero;


        //
        protected int id;

        public Unit(BattleScreen screen, Rectangle bounds ){
            this.screen = screen;
            this.world = screen.getWorld();
            setPosition(bounds.getX(), bounds.getY());

        }

        //Abstract functions
        public abstract void update(float dt);
        public abstract void turnUpdate(float dt);
        protected abstract void defineBody(Rectangle bounds);


        //Shared functions
        public void startTurn () {
            isTurn = true;
        }
        public void endTurn(){
            isTurn = false;
            screen.nextTurn();
        }

        public boolean isDead(){
            return isDead;
        }

        public void destroyBody(){
            world.destroyBody(body);
        }

        public boolean isHero() {
            return isHero;
        }
}
