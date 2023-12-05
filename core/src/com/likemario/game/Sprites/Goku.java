package com.likemario.game.Sprites;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.likemario.game.MarioBros;
import com.likemario.game.Screens.PlayScreen;

public class Goku extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, ATTACKING, RUNNINGATTACKING};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private TextureRegion gokuStand;

    private Animation gokuRun;

    private Animation gokuAtack;
    private Animation gokuRunningAtack;
    private Animation gokuJump;
    private float stateTimer;
    public boolean runningRight;
    public boolean atacking;


    public Goku(PlayScreen screen) {
        super(screen.getAtlas().findRegion("standing"));
        this.world = screen.getWorld();

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
        frames.add(new TextureRegion(screen.getAtlas().findRegion("jumping1")));


        gokuJump = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack1")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack-stand")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack2")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack3")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack6")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack7")));

        gokuAtack = new Animation(0.1f,frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack-running1")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack1")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack-running2")));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("kick1")));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("kick2")));

//        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack-stand2")));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("kick-stand")));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("kick3")));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack-stand3")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack3")));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack4")));
//        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack5")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack6")));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("atack7")));




        gokuRunningAtack = new Animation(0.15f,frames);

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
            case ATTACKING:
                region = (TextureRegion) gokuAtack.getKeyFrame(stateTimer, true);
                break;
            case RUNNINGATTACKING:
                region = (TextureRegion) gokuRunningAtack.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = gokuStand;
                break;
        }

        if(currentState == State.RUNNING || currentState == State.RUNNINGATTACKING){
            setBounds(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2, 40 / MarioBros.PPM, 40 / MarioBros.PPM);
        }
        else if(currentState == State.ATTACKING) {
            setBounds(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 2 / MarioBros.PPM, 40 / MarioBros.PPM, 40 / MarioBros.PPM);
        }
        else{
            setBounds(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2, 26 / MarioBros.PPM, 40 / MarioBros.PPM);
        }




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
        if (b2body.getLinearVelocity().x != 0 && atacking) {
            return State.RUNNINGATTACKING;
        } else if (atacking) {
            return State.ATTACKING;
        } else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
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
        shape.setAsBox(5 / MarioBros.PPM, 18 / MarioBros.PPM);

        fdef.filter.categoryBits = MarioBros.GOKU_BIT;
        fdef.filter.maskBits = MarioBros.DEFAULT_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT;



        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 19 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 19 / MarioBros.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");

    }
}
