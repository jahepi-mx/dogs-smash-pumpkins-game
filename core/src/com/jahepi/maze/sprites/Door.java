package com.jahepi.maze.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Door extends Block {

	private Vector2 goTo;
	
	public Door(World world, float x, float y, BodyType type, float size, boolean flipX) {
		super(world, x, y, type, size, flipX);
		goTo = new Vector2();
	}

	@Override
	public void render(SpriteBatch batch, float deltatime) {
		int factor = 1;
		int flip = flipX ? -1 : 1;
		float rotation = body.getAngle() * MathUtils.radiansToDegrees;
		float origX = (size.x * factor / 2) * flip;
		float origY = (size.y);
		float x = body.getPosition().x - origX;
		float y = body.getPosition().y - origY;
		float width = size.x * factor * flip;
		float height = size.y * factor;
		int scale = 1;
		batch.draw(region, x, y, origX, origY, width, height, scale, scale, rotation);
	}
	
	public Vector2 getGoTo() {
		return goTo;
	}

	public void setGoTo(float x, float y) {
		this.goTo.x = x;
		this.goTo.y = y;
	}

}
