package com.csc780.eppb.tbd.screens;


import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import com.csc780.eppb.tbd.MapSelectActivity;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.battle.EnemyList;
import com.csc780.eppb.tbd.scenes.Hud;
import com.csc780.eppb.tbd.sprites.Boy;
import com.csc780.eppb.tbd.sprites.EnemyFactory;
import com.csc780.eppb.tbd.sprites.Unit;
import com.csc780.eppb.tbd.tools.Attack;
import com.csc780.eppb.tbd.tools.WorldContactListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by owner on 4/25/2017.
 */

public class BattleScreen implements Screen {

    private final float MAX_TURN_TIME = 8.0f;
    private NeetGame neetGame;
    private TextureAtlas atlas;
    private TextureAtlas deadAtlas;
    private TextureAtlas hudAtlus;

//    Texture background;

    Texture base;
    Texture hourglass;
    Texture outerRotation;
    Texture innerRotation;
    float rotation;

    float hourRotation;
    float hourTimer;
    boolean hourPause;


    TextureRegion transitionBackground;
    TextureRegion transitionForeground;

    TextureRegion profileBackground;
    TextureRegion profileForeground;

    //temp atlas
    public TextureAtlas bowserAtlas;
    public BitmapFont font;

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

    ArrayList<Unit> units;
    ArrayList<Attack> attacks;


    //Creating the Gameloop
    private float dimDuration;
    private ShapeRenderer dimScreenRenderer;
    private boolean isVictory;
    private boolean isGameOver;

    private boolean isTransition;
    private float transitionDuration;

    private boolean isPaused;

    public float heroTurnTimer;


    public BattleScreen(NeetGame game){
        this.neetGame = game;
        atlas = new TextureAtlas("link.txt");
        deadAtlas = new TextureAtlas("link_dead.txt");
        hudAtlus = new TextureAtlas("hud.txt");
        bowserAtlas  = new TextureAtlas("bowser.txt");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("trench100free.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 35;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter);
        generator.dispose();

        hourglass = new Texture("hourglass.png");
        base = new Texture("base.png");
        outerRotation = new Texture("outer_rotation.png");
        innerRotation = new Texture("inner_rotation.png");

        hourPause  = false;
        hourRotation = 0;

        transitionBackground = new TextureRegion(getHudAtlus().findRegion("transition_background"));
        transitionForeground = new TextureRegion(getHudAtlus().findRegion("transition_foreground"));

        profileBackground  = new TextureRegion(getHudAtlus().findRegion("profile_background"));
        profileForeground = new TextureRegion(getHudAtlus().findRegion("profile_foreground"));

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(NeetGame.V_WIDTH, NeetGame.V_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2, 0);

        world = new World(new Vector2(0,0), true );
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        createBounds();

        loadSprites();
        attacks = new ArrayList<Attack>();
        isNextTurn = true;
        currentUnitTurn = units.get(0); // Unit test
        currentUnitTurn.startTurn();
        isNextTurn = false;
        heroTurnTimer = MAX_TURN_TIME;
        isPaused = true;
        isTransition = true;
        transitionDuration = 1.0f;


        dimScreenRenderer = new ShapeRenderer();

        dimDuration =0.0f ;

        hud = new Hud(game.batch, this, currentUnitTurn);

        isVictory = false;
        isGameOver = false;
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
            units.add(EnemyFactory.getEnemy(enemyId, this, new Rectangle(posX, posY, sizeX, sizeY)));
            Log.d("BattleScreen", EnemyList.getEnemy(enemyId).getName());
        }
    }

    @Override
    public void show() {

    }

    public void handleInput (float dt){
        if(Gdx.input.isTouched() && (isVictory || isGameOver)) {
            Intent intent = new Intent(neetGame.getActivity(), MapSelectActivity.class);
            neetGame.getActivity().startActivity(intent);
        }

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
        if (units.size() == 1 && !isVictory){
            isVictory = true;
        }

        if(isNextTurn && !isVictory) {
            units.add(units.remove(0));
            currentUnitTurn = units.get(0);
            currentUnitTurn.startTurn();
            isNextTurn = false;

            if (currentUnitTurn.isHero()){
                heroTurnTimer = MAX_TURN_TIME;
            }

            isTransition = true;
            transitionDuration = 1.0f;

        }

        if(isTransition){
            if(transitionDuration <= 0){
                isTransition = false;
                isPaused = false;
            }
            transitionDuration -= dt;
        }

        //accepting input
        handleInput(dt);

        rotation += dt * 100;

        gameCam.update();
        hud.update(dt);

        Iterator<Unit> iter = units.iterator();
        while(iter.hasNext()) {
            Unit unit = iter.next();
            if(!unit.isDead())
                unit.update(dt);
            else{
                if (unit.isTurn())
                    nextTurn();
                iter.remove();
                world.destroyBody(unit.body);
            }
        }

        //updating the attacks set to the world
        for(Attack attack : attacks) {
            if (!attack.isAttackFinished())
                attack.update(dt);
            else {
                attacks.remove(attack);
            }
        }

        if(isTransition && ! isPaused)
            isPaused = true;

        if (!currentUnitTurn.isDying() && !isPaused)
            currentUnitTurn.turnUpdate(dt);

        if(!isPaused)
         heroTurnTimer -= dt;


        // spinning Time Animation
        if(!hourPause) {
            hourRotation += dt * 300;
            if (hourRotation >= 180) {
                hourPause = true;
                hourRotation = 180;
            }
        } else {
            hourTimer += dt ;
            if (hourTimer >= 1){
                hourPause = false;
                hourTimer = 0 ;
                hourRotation = 0;
            }
        }



        world.step(1/60f, 30, 30);
    }

    @Override
    public void render(float delta) {
        update(delta);


        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        neetGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);


        neetGame.batch.begin();

        for(Unit unit : units) {
            unit.draw(neetGame.batch);
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        neetGame.batch.draw(profileBackground, 10 , NeetGame.V_HEIGHT - 110);
        neetGame.batch.draw(profileForeground, 10 , NeetGame.V_HEIGHT- 110);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        neetGame.batch.draw(new TextureRegion(base), NeetGame.V_WIDTH - 250, 0, 100,100 ,200, 200 , 1f , 1f , 0);
        neetGame.batch.draw(new TextureRegion(innerRotation), NeetGame.V_WIDTH - 250, 0, 100, 100, 200, 200 , 1f , 1f, -rotation);
        neetGame.batch.draw(new TextureRegion(outerRotation), NeetGame.V_WIDTH - 250, 0, 100, 100, 200, 200 , 1f , 1f , rotation);
        neetGame.batch.draw(new TextureRegion(hourglass), NeetGame.V_WIDTH - 250, 0, 100 ,100 , 200, 200, 0.75f, .75f, -hourRotation);

        neetGame.batch.end();

        hud.stage.draw();

        if(isTransition){
            neetGame.batch.begin();
            neetGame.batch.draw(transitionBackground, NeetGame.V_WIDTH/2 -300, NeetGame.V_HEIGHT/2 - 100 );
            font.draw(neetGame.batch, currentUnitTurn.getName() + "'s Turn", NeetGame.V_WIDTH/2 - 100, NeetGame.V_HEIGHT/2 + 10  );
            neetGame.batch.draw(transitionForeground, NeetGame.V_WIDTH/2 - 300, NeetGame.V_HEIGHT/2 - 100);
            neetGame.batch.end();
        }

        if(isVictory){
            neetGame.batch.begin();
            neetGame.batch.draw(transitionBackground, NeetGame.V_WIDTH/2 -300, NeetGame.V_HEIGHT/2 - 100 );
            font.draw(neetGame.batch, "Victory", NeetGame.V_WIDTH/2 - 80, NeetGame.V_HEIGHT/2 + 20  );
            neetGame.batch.draw(transitionForeground, NeetGame.V_WIDTH/2 - 300, NeetGame.V_HEIGHT/2 - 100);
            neetGame.batch.end();
        }

        if(isGameOver) {
            neetGame.batch.begin();
            font.draw(neetGame.batch, "Game Over", NeetGame.V_WIDTH/2 - 100, NeetGame.V_HEIGHT/2  );
            neetGame.batch.end();
        }
        //renderer our Box2DDebugLines
       // b2dr.render(world, gameCam.combined );

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

    public void createNewAttack( Rectangle bounds , boolean isHero, String gesture){
        attacks.add(new Attack(this, bounds, isHero, gesture));
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public TextureAtlas getHudAtlus() {
        return hudAtlus;
    }

    public TextureAtlas getDeadAtlas() {
        return deadAtlas;
    }

    public World getWorld () {
        return world;
    }

    public boolean isPaused() {return isPaused;}

    public void addCombo(){
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
        float temp = dimDuration;
        if (temp >= 0.5 )
            temp = 0.5f ;
            Gdx.gl.glEnable(GL20.GL_BLEND);
            dimScreenRenderer.begin(ShapeRenderer.ShapeType.Filled);
            dimScreenRenderer.setColor(new Color(0, 0, 0, temp));
            dimScreenRenderer.rect(0, 0, gamePort.getScreenWidth() * 2, gamePort.getScreenHeight() * 2);
            dimScreenRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void setGameOver() {
        isGameOver = true;
    }

}
