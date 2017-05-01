package com.csc780.eppb.tbd.screens;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc780.eppb.tbd.Link;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.battle.EnemyList;
import com.csc780.eppb.tbd.scenes.Hud;
import com.csc780.eppb.tbd.sprites.Boy;
import com.csc780.eppb.tbd.sprites.Enemy;
import com.csc780.eppb.tbd.sprites.EnemyFactory;
import com.csc780.eppb.tbd.sprites.TestEnemy;
import com.csc780.eppb.tbd.tools.Attack;
import com.csc780.eppb.tbd.tools.WorldContactListener;

import java.util.ArrayList;

/**
 * Created by owner on 4/25/2017.
 */

public class BattleScreen implements Screen {

    private NeetGame neetGame;
    private TextureAtlas atlas;
    private TextureAtlas enemyAtlus;
    private TextureAtlas hudAtlus;

    //temp atlas
    public TextureAtlas bowserAtlas;

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
    private ArrayList<Enemy> enemies;

    TestEnemy test;
    TestEnemy test2;

    private Attack attack;

    Texture background;
    float color [] = {0 , 0, 0 , 1};

    public BattleScreen(NeetGame game){
        this.neetGame = game;
        atlas = new TextureAtlas("link.txt");
        enemyAtlus = new TextureAtlas("enemy.txt");
        hudAtlus = new TextureAtlas("hud.txt");
        bowserAtlas  = new TextureAtlas("bowser.txt");

        background = new Texture("forest2.png");

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(neetGame.V_WIDTH , neetGame.V_HEIGHT , gameCam);
        hud = new Hud(game.batch, this);

        gameCam.position.set(gamePort.getWorldWidth() /2, gamePort.getWorldHeight() /2, 0);

        world = new World(new Vector2(0,0), true );
        world.setContactListener(new WorldContactListener());
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef  = new BodyDef ();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //top boundary from upper left corner
        Rectangle rect  = new Rectangle( 0 , NeetGame.V_HEIGHT, NeetGame.V_WIDTH, NeetGame.V_HEIGHT/6);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(rect.getX() + rect.getWidth() /2, rect.getY() - rect.getHeight()/2);
        body = world.createBody(bdef);

        shape.setAsBox(rect.getWidth() /2 , rect.getHeight()/2);
        fdef.shape = shape;
        body.createFixture(fdef);

        //right boundary from lower right corner
        rect  = new Rectangle(NeetGame.V_WIDTH, 0, NeetGame.V_WIDTH/8, NeetGame.V_HEIGHT);
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(rect.getX() - rect.getWidth() /2, rect.getY() + rect.getHeight()/2);
        body = world.createBody(bdef);

        shape.setAsBox(rect.getWidth() /2 , rect.getHeight()/2);
        fdef.shape = shape;
        body.createFixture(fdef);

        player = new Link(this);

        test = new TestEnemy(this, new Rectangle(400, 100, 150, 150));
        test2 = new TestEnemy(this, new Rectangle(400, 300, 200, 200));

        boy = new Boy(this, new Rectangle(600,200,0,0));

        enemies = new ArrayList<>();
        for(int i = 0; i < NeetGame.getGameMapInfo().getEnemyCount(); i++) {
            int enemyId = NeetGame.getGameMapInfo().getEnemyList().get(i).getId();
            float posX = NeetGame.getGameMapInfo().getEnemyList().get(i).getPosition()[0];
            float posY = NeetGame.getGameMapInfo().getEnemyList().get(i).getPosition()[1];
//            enemies.add(EnemyFactory.getEnemy(enemyId, posX, posY, this));
            enemies.add(EnemyFactory.getEnemy(enemyId, new Rectangle(posX, posY, 50, 50), this));
            Log.d("BattleScreen", EnemyList.getEnemy(enemyId).getName());
        }

        hud.setCurrentPlayer("Link");
//        attack = new Attack(this, new Rectangle(550, 50, 100, 100));

    }

    @Override
    public void show() {

    }

    Vector2 position  = new Vector2();
    Vector2 touch  = new Vector2();
    Vector3 temp  = new Vector3();

    Vector2 direction = new Vector2();
    float speed = 100.0f;

    public void handleInput (float dt){
        if (!boy.isAttacking) {
            if(Gdx.input.isTouched()) {

                position.set(boy.b2body.getPosition().x, boy.b2body.getPosition().y);
                gameCam.unproject(temp.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                touch.set(temp.x, temp.y);
                direction.set(touch).sub(position).nor();

                boy.b2body.applyLinearImpulse(direction.scl(speed), boy.b2body.getWorldCenter(), true);

//            if(Gdx.input.getX() > neetGame.V_WIDTH && boy.b2body.getLinearVelocity().x <= 200)
//              boy.b2body.applyLinearImpulse(new Vector2(50f, 0),boy.b2body.getWorldCenter(), true);
//            if(Gdx.input.getX() < neetGame.V_WIDTH && boy.b2body.getLinearVelocity().x >= -200)
//              boy.b2body.applyLinearImpulse(new Vector2(-50f, 0),boy.b2body.getWorldCenter(), true);

            } else {
                boy.b2body.setLinearVelocity(0.0f, 0.0f);
            }
        }
    }

    public void update(float dt) {
        //accepting input
        handleInput(dt);

        world.step(1/60f, 30, 30);

        gameCam.update();
        hud.update(dt);

        boy.update(dt);
        player.update(dt);

        for(int i = 0; i < NeetGame.getGameMapInfo().getEnemyCount(); i++)
            enemies.get(i).update(dt);

        test.update(dt);
        test2.update(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(color[0], color[1], color[2], color[3]);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        hud.stage.draw();
        neetGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        neetGame.batch.begin();

        neetGame.batch.draw(background, 0, 0, NeetGame.V_WIDTH , NeetGame.V_HEIGHT );

       // player.draw(neetGame.batch);
        test.draw(neetGame.batch);
        test2.draw(neetGame.batch);
        boy.draw(neetGame.batch);

        for(int i = 0; i < NeetGame.getGameMapInfo().getEnemyCount(); i++) {
            enemies.get(i).draw(neetGame.batch);
        }

        // attack.draw(neetGame.batch);

        // neetGame.batch.draw(boy.getTextureRegion(), boy.getX(), boy.getY(), boy.getWidth() , boy.getHeight());


        // neetGame.batch.draw(atlas.findRegion("link_run"), 0 ,200, 900 , 100);

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


    public void setColor (float[] newColor) {
        //if (newColor.length == 4)
           // color = newColor;
    }
    public void addCombo(){
        hud.addCombo();
    }

    public void setAttack(String gesture) {
        boy.setAttack(gesture);
    }
}
