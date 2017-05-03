package com.csc780.eppb.tbd.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc780.eppb.tbd.NeetGame;

/**
 * Created by owner on 4/25/2017.
 */

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Float attackTimer;
    private float timeCount;
    private Integer combo;

    Label attackTimerText;
    Label attackTimerCount;
    Label comboText;
    Label comboCount;

    private Touchpad joypad;
    private Touchpad.TouchpadStyle joypadStyle;
    private Skin joypadSkin;

    private Vector2 moveVector;


    public Hud(SpriteBatch sb) {
        attackTimer = 10.00f;
        timeCount = 0;
        combo = 0;

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
        table.top();
        table.setFillParent(true);

        attackTimerText = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        attackTimerCount = new Label(String.format("%.2f", attackTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));;
        comboText = new Label("COMBO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));;
        comboCount = new Label(String.format("%3d", combo), new Label.LabelStyle(new BitmapFont(), Color.WHITE));;

        table.add(attackTimerText).expandX().padTop(10);
        table.add(comboText).expandX().padTop(10);
        table.row();
        table.add(attackTimerCount).expandX();
        table.add(comboCount).expandX();

        stage.addActor(joypad);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }
    public void update (float dt){
        updateTime(dt);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void updateTime( float dt){
        if (attackTimer <= 0.0f)
            return;

        attackTimer -= dt ;
        if (attackTimer < 0.0f)
            attackTimer = 0.0f;
        attackTimerCount.setText(String.format("%.2f", attackTimer));
    }

    public Vector2 getJoypadVector(){
        moveVector.set(joypad.getKnobPercentX(), joypad.getKnobPercentY());
        return moveVector;
    }

    public void addCombo(){
        if (attackTimer == 0.0)
            return;
        combo++;
        comboCount.setText(String.format("%3d", combo));
    }
}
