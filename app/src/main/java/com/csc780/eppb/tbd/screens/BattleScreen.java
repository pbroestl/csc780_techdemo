package com.csc780.eppb.tbd.screens;

import android.util.Log;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc780.eppb.tbd.scenes.Hud;
import com.csc780.eppb.tbd.Link;
import com.csc780.eppb.tbd.NeetGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.csc780.eppb.tbd.sprites.Boy;

/**
 * Created by owner on 4/25/2017.
 */

public class BattleScreen implements Screen {

    private NeetGame neetGame;
    private TextureAtlas atlas;

    //basic gameScreen variables
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    //Box2d variables
    private World world ;
    private Box2DDebugRenderer b2dr;

    //Sprites
    private Link player;
    private Boy  boy;

    float color [] = {0 , 0, 0 , 1};

    public BattleScreen(NeetGame game){
        this.neetGame = game;
        atlas = new TextureAtlas("link.txt");

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(neetGame.V_WIDTH, neetGame.V_HEIGHT, gameCam);
        hud = new Hud(game.batch);

        gameCam.position.set(gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2, 0);

        world = new World(new Vector2(0,0), true );
        b2dr = new Box2DDebugRenderer();

        player = new Link(this);
        boy = new Boy(this, 600, 200);

    }

    @Override
    public void show() {

    }

    public void handleInput (float dt){
        if(Gdx.input.isTouched()) {
            gameCam.position.x += 100 * dt;
           // Log.v("Neet", "Oh, touch me more!");
        }
    }

    public void update(float dt) {
        //accepting input
        handleInput(dt);

        world.step(1/60f,6, 2);

        gameCam.update();
        hud.update(dt);
        player.update(dt);


    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(color[0], color[1], color[2], color[3]);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        neetGame.batch.begin();
       // player.draw(neetGame.batch);
        neetGame.batch.draw(player.getTextureRegion(), 100,100, 100 , 100);
        neetGame.batch.end();

        //renderer our Box2DDebugLines
        b2dr.render(world,gameCam.combined );

        neetGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hud.dispose();
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public World getWorld () {
        return world;
    }


    public void setColor (float[] newColor) {
        if (newColor.length == 4)
            color = newColor;
    }
    public void addCombo(){
        hud.addCombo();
    }
}
