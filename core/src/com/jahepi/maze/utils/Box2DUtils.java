package com.jahepi.maze.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jahepi.maze.sprites.Enemy;

public class Box2DUtils {

	public Box2DUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static Body createBox(World world, float x, float y, float width, float height, BodyType type, float friction, float density, boolean isSensor) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		body.setGravityScale(0);
		body.setSleepingAllowed(false);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		FixtureDef fix = new FixtureDef();
		fix.shape = shape;
		fix.density = density;
		fix.friction = friction;
		fix.isSensor = isSensor;
		fix.filter.categoryBits = Enemy.ENEMY_MASK;
		body.createFixture(fix);
		shape.dispose();
		return body;
	}
	
	public static Body createTriangle(World world, float x, float y, float size, BodyType type, float friction, float density, boolean isSensor, boolean flip) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		body.setGravityScale(0);
		body.setSleepingAllowed(false);
		PolygonShape shape = new PolygonShape();
		if (flip) {
			Vector2[] vertices = new Vector2[3];
			vertices[0] = new Vector2(-size, -size);
			vertices[1] = new Vector2(-size, size);
			vertices[2] = new Vector2(size, -size);
			shape.set(vertices);
		} else {
			Vector2[] vertices = new Vector2[3];
			vertices[0] = new Vector2(size, -size);
			vertices[1] = new Vector2(size, size);
			vertices[2] = new Vector2(-size, -size);
			shape.set(vertices);
		}
		
		FixtureDef fix = new FixtureDef();
		fix.shape = shape;
		fix.density = density;
		fix.friction = friction;
		fix.isSensor = isSensor;
		fix.filter.categoryBits = Enemy.ENEMY_MASK;
		body.createFixture(fix);
		shape.dispose();
		return body;
	}

	public static Body createEnemy(World world, float x, float y, float size, BodyType type, float friction, float density, boolean isSensor) {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(x, y);
		Body body = world.createBody(bodyDef);
		body.setGravityScale(1);
		body.setFixedRotation(true);
		body.setSleepingAllowed(false);
		CircleShape shape = new CircleShape();
		shape.setRadius(size);
		FixtureDef fix = new FixtureDef();
		fix.shape = shape;
		fix.filter.categoryBits = Enemy.ENEMY_CATEGORY;
		fix.filter.maskBits = Enemy.ENEMY_MASK;
		fix.density = density;
		fix.friction = friction;
		fix.isSensor = isSensor;
		body.createFixture(fix);
		shape.dispose();
		return body;
	}
}
