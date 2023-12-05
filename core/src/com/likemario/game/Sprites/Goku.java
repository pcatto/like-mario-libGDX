package com.likemario.game.Sprites;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.likemario.game.MarioBros;
import com.likemario.game.Screens.PlayScreen;

public class Goku extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private TextureRegion gokuStand;

    private Animation gokuRun;
    private Animation gokuJump;
    private float stateTimer;
    public boolean runningRight;


    public Goku(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("standing"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("running1")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("running2")));

        gokuRun = new Animation(0.2f, frames);
        frames.clear();


        frames.add(new TextureRegion(screen.getAtlas().findRegion("jumping1")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("jumping2")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("jumping3")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("jumping4")));


        gokuJump = new Animation(0.1f, frames);

        gokuStand = new TextureRegion(screen.getAtlas().findRegion("standing"));


        defineGoku();
        setBounds(0, 0, 26 / MarioBros.PPM, 40 / MarioBros.PPM);
        setRegion(gokuStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) gokuJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) gokuRun.getKeyFrame(stateTimer);
                break;
            case FALLING:
            case STANDING:
            default:
                region = gokuStand;
                break;
        }

        if(currentState == State.RUNNING){
            setBounds(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2, 40 / MarioBros.PPM, 40 / MarioBros.PPM);
        }
        else
            setBounds(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2, 26 / MarioBros.PPM, 40 / MarioBros.PPM);



        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void defineGoku() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(6 / MarioBros.PPM, 18 / MarioBros.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();


    }
}
