package com.jahepi.maze.sprites;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.jahepi.maze.resources.Resource;

public class Crate extends Block {

	public Crate(World world, float x, float y, float size, BodyType type, Resource resource) {
		super(world, x, y, type, size, false);
		this.setGravity(false);
		this.setTextureRegion(resource.getGameAtlas().findRegion("crate"));
	}

}
