package com.jahepi.maze.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.utils.Box2DUtils;

public class GravityItem extends WorldObject {

	private static final float SPAWN_TIME = 10.0f;
	
	private float spawnSecs;
	private boolean visible;
	private TextureRegion region;
	
	public GravityItem(World world, float x, float y) {
		super(world, x, y, 1, 1, 0.9f, BodyType.StaticBody, true, false);
		visible = true;
		region = new TextureRegion(Resource.getInstance().getGameAtlas().findRegion("mushroom2"));
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		if (!this.visible) {
			Resource.getInstance().getMushroomSound().play();
		}
	}

	@Override
	public void render(SpriteBatch batch, float deltatime) {
		if (isVisible()) {
			int factor = 1;
			float rotation = (body.getAngle() * MathUtils.radiansToDegrees);
			float origX = (size.x * factor / 2);
			float origY = (size.y * factor / 2);
			float x = body.getPosition().x - origX;
			float y = body.getPosition().y - origY;
			float width = size.x * factor;
			float height = size.y * factor;
			int scale = 1;
			batch.draw(region, x, y, origX, origY, width, height, scale, scale, rotation);
		} else {
			spawnSecs += deltatime;
		}
		if (spawnSecs >= SPAWN_TIME) {
			spawnSecs = 0;
			setVisible(true);
		}
	}

	@Override
	protected void createBody() {
		body = Box2DUtils.createBox(world, position.x, position.y, size.x, size.y, type, friction, density, isSensor);
		body.setUserData(this);	
	}
}
