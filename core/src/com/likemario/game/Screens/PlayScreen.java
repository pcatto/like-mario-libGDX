package com.likemario.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.likemario.game.MarioBros;
import com.likemario.game.Scenes.Hud;
import com.likemario.game.Sprites.Goku;

public class PlayScreen implements Screen {
    private MarioBros game;
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

        player = new Goku(world);

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // ground
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / MarioBros.PPM, (rect.getY() + rect.getHeight()/2)/ MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/ MarioBros.PPM, (rect.getHeight()/2)/ MarioBros.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // pipe
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / MarioBros.PPM , (rect.getY() + rect.getHeight()/2)/ MarioBros.PPM );

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/ MarioBros.PPM, (rect.getHeight()/2)/ MarioBros.PPM) ;
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // brick
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / MarioBros.PPM , (rect.getY() + rect.getHeight()/2)/ MarioBros.PPM );

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/ MarioBros.PPM, (rect.getHeight()/2)/ MarioBros.PPM) ;
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // coin
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / MarioBros.PPM , (rect.getY() + rect.getHeight()/2)/ MarioBros.PPM );

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/ MarioBros.PPM, (rect.getHeight()/2)/ MarioBros.PPM) ;
            fdef.shape = shape;
            body.createFixture(fdef);
        }


    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.b2body.applyLinearImpulse(new Vector2(0, 2f), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 0.2f), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.b2body.applyLinearImpulse(new Vector2(1f, 0), player.b2body.getWorldCenter(), true);
        }

    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1/60f, 6, 2);

        float targetX = (float) 0.4 + player.b2body.getPosition().x;
        float targetY = (float) 0.3 + player.b2body.getPosition().y;

        float lerpSpeed = 3.0f;

        // Interpola a posição da câmera suavemente
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

        ScreenUtils.clear(0, 0, 0, 1);

        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        game.batch.begin();


        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        gameCam.update();
        game.batch.setProjectionMatrix(gameCam.combined);
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
