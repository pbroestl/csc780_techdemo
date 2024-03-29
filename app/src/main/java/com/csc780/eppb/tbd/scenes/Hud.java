package com.csc780.eppb.tbd.scenes;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc780.eppb.tbd.NeetGame;
import com.csc780.eppb.tbd.screens.BattleScreen;
import com.csc780.eppb.tbd.sprites.Boy;
import com.csc780.eppb.tbd.sprites.Unit;

import java.util.Locale;

/**
 * Created by owner on 4/25/2017.
 */

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Boy player;
    private BattleScreen screen;

    private Float attackTimer;
    private float timeCount;
    private Integer combo;
    private float health;
    private float maxHealth;

    Label attackTimerText;
    Label attackTimerCount;
    Label comboText;
    Label comboCount;
    Label mapDifficultyText;
    Label currentPlayerText;
    Label hpText;

    TextureRegion healthBar;
    TextureRegion healthFill;
    Sprite healthBarSprite;
    Sprite healthFillSprite;
    Image healthFillImg;
    float healthMaxWidth;

    private Touchpad joypad;
    private Touchpad.TouchpadStyle joypadStyle;
    private Skin joypadSkin;

    private Vector2 moveVector;

    public Hud(SpriteBatch sb, BattleScreen screen, Unit player) {
        this.player = (Boy) player;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("prstartk.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        attackTimer = 10.00f;

        timeCount = 0;
        combo = 0;
        health = ((Boy) player).getCurrentHealth();
        maxHealth = ((Boy) player).getMAX_HEALTH();

        viewport = new FitViewport(NeetGame.V_WIDTH, NeetGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        joypadSkin = new Skin();
        joypadSkin.add("touchBackground", new Texture ("joystick_background.png"));
        joypadSkin.add("touchKnob", new Texture("joystick_knob.png"));

        joypadStyle =  new Touchpad.TouchpadStyle();
        joypadStyle.background = joypadSkin.getDrawable("touchBackground");
        joypadStyle.knob = joypadSkin.getDrawable("touchKnob");

        joypad = new Touchpad(10 , joypadStyle);
        joypad.setBounds(10 ,10, 175, 175);

        moveVector = new Vector2();

        Table table = new Table();
        table.setFillParent(true);

//        currentPlayerText = new Label("PLAYER", new Label.LabelStyle(font, Color.WHITE));
//        currentPlayerText.setText(String.format(Locale.ENGLISH, "%3s's Turn", ((Boy) player).getName()));
        mapDifficultyText = new Label(String.format(
                Locale.ENGLISH, "%s", NeetGame.getGameMapInfo().getDifficulty().toUpperCase()),
                new Label.LabelStyle(font, Color.WHITE));
//        hpText = new Label(String.format(
//                Locale.ENGLISH, "HP %1f/%1f", health, MAX_HEALTH),
//                new Label.LabelStyle(font, Color.WHITE));
        attackTimerText = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        attackTimerCount = new Label(String.format(Locale.ENGLISH, "%.2f", attackTimer), new Label.LabelStyle(font, Color.WHITE));
        comboText = new Label("COMBO", new Label.LabelStyle(font, Color.WHITE));
        comboCount = new Label(String.format(Locale.ENGLISH, "%3d", combo), new Label.LabelStyle(font, Color.WHITE));

        table.top();
        table.add(mapDifficultyText).expandX().left().pad(10);
        table.add(comboText).expandX().right().pad(5);
        table.add(comboCount).right().pad(5);
        table.row();
//        table.add(currentPlayerText).expandX().left().padLeft(10);
//        table.row();
//        table.add(hpText).expandX().left().padLeft(10);
        healthBar = new TextureRegion(screen.getHudAtlus().findRegion("health_bar"));
        healthBarSprite = new Sprite(healthBar);
        healthFill = new TextureRegion(screen.getHudAtlus().findRegion("health_fill"));
        healthFillSprite = new Sprite(healthFill);
        Image healthBarImg = new Image(healthBarSprite);
        healthFillImg = new Image(healthFillSprite);
        healthBarImg.setScale(2f, 1);
        healthFillImg.setScale(2f, 1);
        healthMaxWidth = healthFillImg.getWidth();
        Log.v("Hud", ""+healthFillImg.getWidth());
        Stack stack = new Stack();
        stack.add(healthFillImg);
        stack.add(healthBarImg);


        table.add(stack).expandX().left().pad(10);
        table.add(attackTimerText).expandX().right().padRight(20);
        table.add(attackTimerCount).right().padTop(5);

        stage.addActor(joypad);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    public void update (float dt){
        updateTime(dt);

        updateHealth();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void updateTime( float dt){

        if(!player.isTurn()) {
            attackTimer = 8.0f;
            combo = 0;
            comboCount.setText(String.format("%3d", combo));
            return;
        }

        if(attackTimer == 0.0f)
            return;

        if((attackTimer -= dt) < 0.0f)
            attackTimer = 0.0f;

        attackTimerCount.setText(String.format("%.2f", attackTimer));
    }


    public void updateHealth() {
//        if(player == null)
//            return;

        if (health == 0.0)
            return;

        health = player.getCurrentHealth();
        float healthRatio = health / maxHealth;
        healthFillImg.setWidth(healthMaxWidth * healthRatio);
        if (healthFillImg.getWidth() < 0)
            healthFillImg.setWidth(0);
    }

    public Vector2 getJoypadVector(){
        moveVector.set(joypad.getKnobPercentX(), joypad.getKnobPercentY());
        return moveVector;
    }

    public void addCombo(){
        if (attackTimer == 0.0)
            return;
        combo++;
        updateHealth();
        comboCount.setText(String.format("%3d", combo));
    }

}
