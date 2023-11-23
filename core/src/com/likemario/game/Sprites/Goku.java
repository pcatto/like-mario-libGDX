package com.likemario.game.Sprites;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.likemario.game.MarioBros;
import com.likemario.game.Screens.PlayScreen;

public class Goku extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion gokuStand;



    public Goku(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("gokuStand"));
        this.world = world;
        defineGoku();
        gokuStand = new TextureRegion(getTexture(), 0, 0, 331,501);
        setBounds(0, 0, 32 / MarioBros.PPM, 50 / MarioBros.PPM);
        setRegion(gokuStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 4);
    }

    public void defineGoku() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / MarioBros.PPM);



        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
