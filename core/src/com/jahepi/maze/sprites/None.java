package com.jahepi.maze.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class None extends WorldObject {

	protected TextureRegion region;
	
	public None(float x, float y, float size, boolean flipX) {
		super(null, x, y, size, size, 0.0f, null, false, flipX);
	}

	@Override
	protected void createBody() {
	}

	@Override
	public void render(SpriteBatch batch, float deltatime) {
		// TODO Auto-generated method stub
		int factor = 2;
		int flip = flipX ? -1 : 1;
		float rotation = 0 * MathUtils.radiansToDegrees;
		float origX = (size.x * factor / 2) * flip;
		float origY = (size.y * factor / 2);
		float x = position.x - origX;
		float y = position.y - origY;
		float width = size.x * factor * flip;
		float height = size.y * factor;
		int scale = 1;
		batch.draw(region, x, y, origX, origY, width, height, scale, scale, rotation);
	}
	
	public void setTextureRegion(TextureRegion region) {
		this.region = region;
	}

	@Override
	public void dispose() {
	}
	
}
