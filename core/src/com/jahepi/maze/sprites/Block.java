package com.jahepi.maze.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jahepi.maze.utils.Box2DUtils;

public class Block extends WorldObject {
	
	protected TextureRegion region;
	
	public Block(World world, float x, float y, BodyType type, float size, boolean flipX) {
		super(world, x, y, size, size, 0.8f, type, false, flipX);
	}

	@Override
	public void render(SpriteBatch batch, float deltatime) {
		int factor = 2;
		int flip = flipX ? -1 : 1;
		float rotation = body.getAngle() * MathUtils.radiansToDegrees;
		float origX = (size.x * factor / 2) * flip;
		float origY = (size.y * factor / 2);
		float x = body.getPosition().x - origX;
		float y = body.getPosition().y - origY;
		float width = size.x * factor * flip;
		float height = size.y * factor;
		int scale = 1;
		batch.draw(region, x, y, origX, origY, width, height, scale, scale, rotation);
	}
	
	public void setTextureRegion(TextureRegion region) {
		this.region = region;
	}

	@Override
	protected void createBody() {
		body = Box2DUtils.createBox(world, position.x, position.y, size.x, size.y, type, friction, density, isSensor);
		body.setUserData(this);		
	}
}
