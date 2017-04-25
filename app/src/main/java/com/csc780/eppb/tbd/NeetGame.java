package com.csc780.eppb.tbd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by owner on 4/25/2017.
 */

public class NeetGame extends Game {

    public static final int V_WIDTH = 1024;
    public static final int V_HEIGHT = 600;

    private GameScreen screen;
    public SpriteBatch batch ;

    @Override
    public void create(){
        batch = new SpriteBatch();
        screen = new GameScreen(this);
        setScreen(screen);
    }

    public void render () {
        super.render();
    }
    public void setColor (float[] newColor) {
        screen.setColor(newColor);
    }
    public void addCombo(){
        screen.addCombo();
    }
}
