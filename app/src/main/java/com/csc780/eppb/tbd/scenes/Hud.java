package com.csc780.eppb.tbd.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc780.eppb.tbd.NeetGame;

import java.util.Locale;

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
    Label currentPlayerText;

    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;




    public Hud(SpriteBatch sb) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("prstartk.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        attackTimer = 8.00f;
        timeCount = 0;
        combo = 0;

        viewport = new FitViewport(NeetGame.V_WIDTH, NeetGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture ("joystick_background.png"));
        touchpadSkin.add("touchKnob", new Texture("joystick_knob.png"));

        touchpadStyle =  new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchpadSkin.getDrawable("touchBackground");
        touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");

        touchpad = new Touchpad(10 , touchpadStyle);
        touchpad.setBounds(10 ,10,175, 175);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.setDebug(true);

        currentPlayerText = new Label("PLAYER", new Label.LabelStyle(font, Color.WHITE));
        attackTimerText = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        attackTimerCount = new Label(String.format(Locale.ENGLISH, "%.2f", attackTimer), new Label.LabelStyle(font, Color.WHITE));
        comboText = new Label("COMBO", new Label.LabelStyle(font, Color.WHITE));
        comboCount = new Label(String.format(Locale.ENGLISH, "%3d", combo), new Label.LabelStyle(font, Color.WHITE));

        table.add(currentPlayerText).expandX().left();
        table.add(comboText).expandX().right().pad(5);
        table.row();
        table.add();
        table.add(comboCount).expandX().right().pad(5);
        table.row();
        table.add();
        table.add(attackTimerText).expandX().right().padTop(5);
        table.row();
        table.add();
        table.add(attackTimerCount).expandX().right().padTop(5);
//        table.add(comboCount).expandX();


        stage.addActor(touchpad);
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

    public void addCombo(){
        if (attackTimer == 0.0)
            return;
        combo++;
        comboCount.setText(String.format("%3d", combo));
    }

    public void setCurrentPlayer(String name) {
        currentPlayerText.setText(String.format(Locale.ENGLISH, "%3s's Turn", name));
    }
}
