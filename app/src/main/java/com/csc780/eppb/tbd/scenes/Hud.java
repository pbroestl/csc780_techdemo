package com.csc780.eppb.tbd.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    Label mapDifficultyText;

    public Hud(SpriteBatch sb) {
        attackTimer = 8.00f;
        timeCount = 0;
        combo = 0;

        viewport = new FitViewport(NeetGame.V_WIDTH, NeetGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        attackTimerText = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        attackTimerCount = new Label(String.format("%.2f", attackTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));;
        comboText = new Label("COMBO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));;
        comboCount = new Label(String.format("%3d", combo), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        mapDifficultyText = new Label(String.format(
                "%s", NeetGame.getGameMapInfo().getDifficulty().toUpperCase()),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(attackTimerText).expandX().padTop(10);
        table.add(comboText).expandX().padTop(10);
        table.row();
        table.add(attackTimerCount).expandX();
        table.add(comboCount).expandX();
        table.row();
        table.add(mapDifficultyText).expandX();

        stage.addActor(table);
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
}
