package com.likemario.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.likemario.game.MarioBros;
import com.likemario.game.Screens.PlayScreen;

public class Brick extends InteractiveTileObject{
    public Brick(PlayScreen screen, Rectangle bounds) {

        super(screen, bounds);

        fixture.setUserData(this);

        setCategoryFilter(MarioBros.BRICK_BIT);

    }

    @Override
    public void onHeadHit() {
        System.out.println("brick hit");
        setCategoryFilter(MarioBros.DESTROYED_BIT);
//        getCell().setTile(null);
    }
}
