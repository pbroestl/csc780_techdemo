package com.csc780.eppb.tbd.sprites;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/26/2017.
 */

public class Boy extends Character {

    public Boy(BattleScreen screen, float x, float y) {
        super(screen, x, y);
        defineCharacter(x,y);
    }

    @Override
    protected void defineCharacter(float x , float y) {
        BodyDef  bdef = new BodyDef();
        bdef.position.set(x, y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
