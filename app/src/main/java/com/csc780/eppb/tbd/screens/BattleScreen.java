package com.csc780.eppb.tbd.screens;


import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.battle.EnemyList;
import com.csc780.eppb.tbd.scenes.Hud;
import com.csc780.eppb.tbd.sprites.Boy;
import com.csc780.eppb.tbd.sprites.Enemy;
import com.csc780.eppb.tbd.sprites.EnemyFactory;
import com.csc780.eppb.tbd.sprites.TestEnemy;
import com.csc780.eppb.tbd.sprites.Unit;
import com.csc780.eppb.tbd.tools.WorldContactListener;

import java.util.ArrayList;

/**
 * Created by owner on 4/25/2017.
 */

public class BattleScreen implements Screen {

    private final float MAX_TURN_TIME = 10.0f;
    private NeetGame neetGame;
    private TextureAtlas atlas;
    private TextureAtlas enemyAtlus;
    private TextureAtlas hudAtlus;

    Texture background;

    //temp atlas
    public TextureAtlas bowserAtlas;

    //basic gameScreen variables
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    //Box2d variables
    private World world ;
    private Box2DDebugRenderer b2dr;

    //Player movement
    Vector2 position  = new Vector2();
    Vector2 direction = new Vector2();
    float speed = 200.0f;

    //Sprites
    Unit currentUnitTurn;


    boolean isNextTurn;
    private Unit boy;

    Unit test;
    Unit test2;
    ArrayList<Unit> units;


    //Creating the Gameloop
    private float dimDuration;
    private boolean isDimmed;
    private ShapeRenderer dimScreenRenderer;

    public float heroTurnTimer;


    public BattleScreen(NeetGame game){
        this.neetGame = game;
        atlas = new TextureAtlas("link.txt");
//        enemyAtlus = new TextureAtlas("enemy.txt");
        hudAtlus = new TextureAtlas("hud.txt");
        bowserAtlas  = new TextureAtlas("bowser.txt");

        background = new Texture("forest2.png");

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(NeetGame.V_WIDTH, NeetGame.V_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2, 0);

        world = new World(new Vector2(0,0), true );
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        createBounds();

//        test = new TestEnemy(this, new Rectangle(400, 200, 150, 150));
//        test2 = new TestEnemy(this, new Rectangle(400, 300, 200, 200));
//        boy = new Boy(this, new Rectangle(600,200,0,0));

        loadSprites();


//        units.add(boy);
//        units.add(test);
//        units.add(test2);

        currentUnitTurn = units.get(0); // Unit test
        currentUnitTurn.startTurn();
        isNextTurn = false;
        heroTurnTimer = MAX_TURN_TIME;

        dimScreenRenderer = new ShapeRenderer();
        isDimmed = true;

        hud = new Hud(game.batch, this, units.get(0));
        hud.setCurrentPlayer("Link");
    }

    private void loadSprites() {
        units = new ArrayList<>();
        units.add(new Boy(this, new Rectangle(600,200,0,0)));
        for(int i = 0; i < NeetGame.getGameMapInfo().getEnemyCount(); i++) {
            int enemyId = NeetGame.getGameMapInfo().getEnemyList().get(i).getId();
            float posX = NeetGame.getGameMapInfo().getEnemyList().get(i).getPosition()[0];
            float posY = NeetGame.getGameMapInfo().getEnemyList().get(i).getPosition()[1];
            float sizeX = EnemyList.getEnemy(enemyId).getSize()[0];
            float sizeY = EnemyList.getEnemy(enemyId).getSize()[1];
//            enemies.add(EnemyFactory.getEnemy(enemyId, posX, posY, this));
            units.add(EnemyFactory.getEnemy(enemyId, this, new Rectangle(posX, posY, sizeX, sizeY)));
            Log.d("BattleScreen", EnemyList.getEnemy(enemyId).getName());
        }
    }

    @Override
    public void show() {

    }

    public void handleInput (float dt){
        if (currentUnitTurn.isHero()) {
            if(Gdx.input.isTouched() && heroTurnTimer >= 0.0) {
                position.set(currentUnitTurn.body.getPosition().x, currentUnitTurn.body.getPosition().y);
                direction = hud.getJoypadVector();
                currentUnitTurn.body.applyLinearImpulse(direction.scl(speed), currentUnitTurn.body.getWorldCenter(), true);
            } else {
                currentUnitTurn.body.setLinearVelocity(0.0f, 0.0f);
            }
        }
    }

    public void nextTurn() {
        isNextTurn = true;
    }

    public void update(float dt) {
        if(isNextTurn) {
            units.add(units.remove(0));
            currentUnitTurn = units.get(0);
            currentUnitTurn.startTurn();
            isNextTurn = false;

            if (currentUnitTurn.isHero()){
                heroTurnTimer = MAX_TURN_TIME;
            }
        }
        //accepting input
        handleInput(dt);

        gameCam.update();
        hud.update(dt);


      //  boy.update(dt);
//        test.update(dt);
//        test2.update(dt);
        for(Unit unit : units) {
            unit.update(dt);
        }
        currentUnitTurn.turnUpdate(dt);
 //       dimDuration += dt;


        heroTurnTimer -= dt;
        world.step(1/60f, 30, 30);
    }

    @Override
    public void render(float delta) {
        update(delta);


        Gdx.gl.glClearColor(0,0,0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        neetGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);


        neetGame.batch.begin();

        neetGame.batch.draw(background, 0, 0, NeetGame.V_WIDTH, NeetGame.V_HEIGHT );
//          test.draw(neetGame.batch);
//
//          test2.draw(neetGame.batch);
        for(Unit unit : units) {
            unit.draw(neetGame.batch);
        }


        neetGame.batch.end();

        hud.stage.draw();
        //renderer our Box2DDebugLines
        b2dr.render(world, gameCam.combined );

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
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public TextureAtlas getEnemyAtlus() {
        return enemyAtlus;
    }

    public TextureAtlas getHudAtlus() {
        return hudAtlus;
    }

    public World getWorld () {
        return world;
    }

    public void addCombo(){
        isDimmed = true;
        dimDuration = 0.0f;
        hud.addCombo();
    }

    public void setAttack(String gesture) {
        if(currentUnitTurn.isHero() && heroTurnTimer > 0)
            ((Boy)currentUnitTurn).setAttack(gesture);
    }

    public void createBounds () {
        BodyDef bdef  = new BodyDef ();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //top boundary
        Rectangle rect  = new Rectangle( 0 , NeetGame.V_HEIGHT - 100, NeetGame.V_WIDTH, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(rect.getX() + rect.getWidth() /2, rect.getY() - rect.getHeight()/2);
        body = world.createBody(bdef);
        shape.setAsBox(rect.getWidth() /2 , rect.getHeight()/2);
        fdef.shape = shape;
        fdef.filter.categoryBits = NeetGame.DEFAULT_BIT;
        body.createFixture(fdef);

        //right boundary
        rect  = new Rectangle(NeetGame.V_WIDTH -100, 0, 0, NeetGame.V_HEIGHT);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(rect.getX() - rect.getWidth() /2, rect.getY() + rect.getHeight()/2);
        body = world.createBody(bdef);
        shape.setAsBox(rect.getWidth() /2 , rect.getHeight()/2);
        fdef.shape = shape;
        fdef.filter.categoryBits = NeetGame.DEFAULT_BIT;
        body.createFixture(fdef);

        //Bottom boundary
        rect  = new Rectangle(0, 0, NeetGame.V_WIDTH, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(rect.getX() + rect.getWidth() /2, rect.getY());
        body = world.createBody(bdef);
        shape.setAsBox(rect.getWidth() /2 , rect.getHeight()/2);
        fdef.shape = shape;
        fdef.filter.categoryBits = NeetGame.DEFAULT_BIT;
        body.createFixture(fdef);

        //left boundary
        rect  = new Rectangle(0, 0, 0, NeetGame.V_HEIGHT);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(rect.getX(), rect.getY() + rect.getHeight()/2);
        body = world.createBody(bdef);
        shape.setAsBox(rect.getWidth() /2 , rect.getHeight()/2);
        fdef.shape = shape;
        fdef.filter.categoryBits = NeetGame.DEFAULT_BIT;
        body.createFixture(fdef);
    }

    private void dimScreen(){
        float temp = 0.5f - dimDuration;
        if (temp >= 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            dimScreenRenderer.begin(ShapeRenderer.ShapeType.Filled);
            dimScreenRenderer.setColor(new Color(0, 0, 0, temp));
            dimScreenRenderer.rect(0, 0, gamePort.getScreenWidth() * 2, gamePort.getScreenHeight() * 2);
            dimScreenRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }else {
            isDimmed = false;
        }
    }



}
