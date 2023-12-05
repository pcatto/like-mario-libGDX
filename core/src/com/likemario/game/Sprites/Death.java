package com.likemario.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.likemario.game.MarioBros;
import com.likemario.game.Screens.MenuScreen;
import com.likemario.game.Screens.PlayScreen;

public class Death extends InteractiveTileObject{

    PlayScreen screen;
    public Death(PlayScreen screen, Rectangle bounds) {

        super(screen, bounds);

        this.screen = screen;

        fixture.setUserData(this);

        setCategoryFilter(MarioBros.DEATH_BIT);

    }

    @Override
    public void onHeadHit() {
        System.out.println("death hit");
        MenuScreen menuScreen = new MenuScreen(screen.getGame());
        screen.getGame().setScreen(menuScreen);
    }
}
