package com.jahepi.maze.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;
import com.jahepi.maze.utils.Constant;

abstract public class WorldObject implements Disposable {

	protected World world;
	protected Body body;
	protected Vector2 size;
	protected Vector2 position;
	protected float friction;
	protected float density;
	protected boolean isSensor;
	protected BodyType type;
	protected boolean flipX;
	
	public WorldObject(World world, float x, float y, float width, float height, float friction, BodyType type, boolean isSensor, boolean flipX) {
		this.world = world;
		this.friction = friction;
		this.density = 1.0f;
		size = new Vector2(width, height);
		position = new Vector2(x * Constant.BLOCK_SEPARATION, y * Constant.BLOCK_SEPARATION);
		this.type = type;
		this.isSensor = isSensor;
		this.flipX = flipX;
		createBody();
	}
	
	protected abstract void createBody();

	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public void setGravity(boolean active) {
		body.setGravityScale(active ? 1 : 0);
	}
	
	public float getDistance(Vector2 point) {
		return point.dst(body.getPosition().x, body.getPosition().y);
	}
	
	public boolean isGravity() {
		return body.getGravityScale() == 0 ? false : true;
	}
	
	public boolean isFlipX() {
		return flipX;
	}

	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
	}
	
	public abstract void render(SpriteBatch batch, float deltatime);
	
	@Override
	public void dispose() {
		world.destroyBody(this.body);
		this.body.setUserData(null);
		this.body = null;
	}
}
