package com.likemario.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.likemario.game.MarioBros;
import com.likemario.game.Sprites.Goku;

public class MenuScreen implements Screen {
    public Stage stage;

    private MarioBros game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
//    private Hud hud;

    //tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private Goku player;
    private Table table;
    Label title;
    Label mensagem;

    public MenuScreen(MarioBros game) {
        this.game = game;

        stage = new Stage();

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label.LabelStyle titleStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        titleStyle.font.getData().setScale(3.0f);

        title = new Label("MAKU", titleStyle);

        Label.LabelStyle mensagemStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        mensagem = new Label("PRESSIONE ENTER PARA CONTINUAR!", mensagemStyle);

        table.add(title).expandX().padTop(16);
        table.row();
        table.add(mensagem).expandX().padTop(16);

        stage.addActor(table);
    }


    @Override
    public void show() {

    }
    public void handleInput(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(new PlayScreen(game));
            dispose();
        }


    }

    public void update (float dt) {
        handleInput(dt);

    }
    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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
}
