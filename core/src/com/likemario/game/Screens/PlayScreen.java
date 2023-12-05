package com.likemario.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.likemario.game.MarioBros;
import com.likemario.game.Scenes.Hud;
import com.likemario.game.Sprites.Goku;
import com.likemario.game.Tools.B2WorldCreator;
import com.likemario.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {

    private MarioBros game;
    private TextureAtlas atlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    //tiled map
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private Goku player;


    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("goku.atlas");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(10 / MarioBros.PPM, -500 / MarioBros.PPM), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        player = new Goku(this);

        world.setContactListener(new WorldContactListener());




    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if(player.currentState != Goku.State.JUMPING && player.currentState != Goku.State.FALLING){
                player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(player.currentState == Goku.State.JUMPING || player.currentState == Goku.State.FALLING)
                player.b2body.applyLinearImpulse(new Vector2(0, 0.15f), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)) {
            if(player.runningRight){
                player.b2body.applyLinearImpulse(new Vector2(1f, 0), player.b2body.getWorldCenter(), true);
            }
            else {
                player.b2body.applyLinearImpulse(new Vector2(-1f, 0), player.b2body.getWorldCenter(), true);
            }
        }

    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);

        float targetX = (float) 0.4 + player.b2body.getPosition().x;
        float targetY = (float) 0.3 + player.b2body.getPosition().y;

        float distance = Vector2.dst(gameCam.position.x, gameCam.position.y, targetX, targetY);

        float lerpSpeed = 2.0f + distance * 10.0f;

        float newX = MathUtils.lerp(gameCam.position.x, targetX, lerpSpeed * dt);
        float newY = MathUtils.lerp(gameCam.position.y, targetY, lerpSpeed * dt);

        gameCam.position.x = newX;
        gameCam.position.y = newY;



        gameCam.update();
        renderer.setView(gameCam);
    }



    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear( (float)(0.3705), (float)(0.6862), 1, 1);

        renderer.render();

        b2dr.render(world, gameCam.combined);



        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        player.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        gameCam.update();
        game.batch.setProjectionMatrix(gameCam.combined);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
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

        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();


    }
}
