package com.jahepi.maze.sprites;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.jahepi.maze.utils.Box2DUtils;

public class Slope extends Block {
	
	public Slope(World world, float x, float y, float size, boolean flipX) {
		super(world, x, y, BodyType.StaticBody, size, flipX);
	}

	@Override
	protected void createBody() {
		body = Box2DUtils.createTriangle(world, position.x, position.y, size.x, type, friction, density, isSensor, flipX);
		body.setUserData(this);	
	}
}
