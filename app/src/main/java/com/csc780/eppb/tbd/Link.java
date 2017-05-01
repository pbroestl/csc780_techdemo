package com.csc780.eppb.tbd;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.csc780.eppb.tbd.screens.BattleScreen;

/**
 * Created by owner on 4/25/2017.
 */

public class Link extends Sprite {

    private TextureRegion currentRegion;
    private Animation running;

    private float stateTimer;

    public Link (BattleScreen game){
        super(game.getAtlas().findRegion("link_run"));

        stateTimer = 0.0f;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0 ; i < 9 ; i++)
            frames.add(new TextureRegion(getTexture(),i * 32, 0, 32, 32 ));

        running = new Animation(0.05f, frames);

        currentRegion = new TextureRegion(getTexture(), 0,0,0,0);
        setBounds(0,0, 32 , 32 );
        setRegion(currentRegion);
    }
    public void update (float dt) {
        currentRegion = getFrame(dt);
    }

    public TextureRegion getFrame (float dt) {
        TextureRegion region = (TextureRegion)running.getKeyFrame(stateTimer, true);
        stateTimer += dt;
        return region;
    }

    public TextureRegion getTextureRegion(){
        return currentRegion;
    }
}
