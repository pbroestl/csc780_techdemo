package com.csc780.eppb.tbd;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc780.eppb.tbd.NeetGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by owner on 4/25/2017.
 */

public class GameScreen implements Screen {

    private NeetGame neetGame;
    float color [] = {0 , 0, 0 , 1};

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    public GameScreen (NeetGame game){
        this.neetGame = game;

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(neetGame.V_WIDTH, neetGame.V_HEIGHT, gameCam);
        hud = new Hud(game.batch);

        gameCam.position.set(gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2, 0);

    }
    @Override
    public void show() {

    }

    public void handleInput (){

    }

    public void update(float dt) {
        //input
        gameCam.update();
        hud.update(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(color[0], color[1], color[2], color[3]);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

    }

    public void setColor (float[] newColor) {
        if (newColor.length == 4)
            color = newColor;
    }
    public void addCombo(){
        hud.addCombo();
    }
}
