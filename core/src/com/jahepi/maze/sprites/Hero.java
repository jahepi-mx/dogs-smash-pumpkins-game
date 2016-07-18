package com.jahepi.maze.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.utils.Box2DUtils;
import com.jahepi.maze.utils.Constant;

public class Hero extends WorldObject {

	private static final float IMMUNITY_SECONDS = 1.0f;
	private static final float INACTIVE_SECONDS = 1.0f;
	private final static float JUMP = 60.0f;
	private final static float SECONDS_TO_CHANGE_GRAVITY = 3.0f;
	private float delayChangeGravity = SECONDS_TO_CHANGE_GRAVITY;
	
	private Vector2 direction;
	private float velocityX;
	private float velocityY;
	private boolean isJumping;
	private Animation runAnimation;
	private Animation jumpAnimation;
	private Animation idleAnimation;
	private Animation deadAnimation;
	private float stateTime;
	private float immunityTime;
	private float teleportSecs = 0f;
	private boolean isMoving = false;
	private Vector2 moveTo;
	private boolean isDead;

	public Hero(World world, float x, float y) {
		super(world, x, y, 0.5f, 1, 0.7f, BodyType.DynamicBody, false, false);
		velocityX = 12.0f;
		velocityY = 12.0f;
		direction = new Vector2(0, 0);	
		runAnimation = Resource.getInstance().getHeroRunAnimation();
		jumpAnimation = Resource.getInstance().getHeroJumpAnimation();
		idleAnimation = Resource.getInstance().getHeroIdleAnimation();
		deadAnimation = Resource.getInstance().getHeroDeadAnimation();
		body.setFixedRotation(true);
	}
	
	public void moveToLeft(float deltatime) {
		if (checkMoveTo() || isDead) {
			return;
		}
		isMoving = true;
		direction.x = -velocityX;
		if (isGravity()) {
			if (Math.abs(Math.round(body.getLinearVelocity().x)) > 0.0f) {
				body.setLinearVelocity(0, body.getLinearVelocity().y);
			}
		} else {
			body.setLinearVelocity(-velocityX, body.getLinearVelocity().y);
		}
	}
	
	public void moveToRight(float deltatime) {
		if (checkMoveTo() || isDead) {
			return;
		}
		isMoving = true;
		direction.x = velocityX;
		if (isGravity()) {
			if (Math.abs(Math.round(body.getLinearVelocity().x)) > 0.0f) {
				body.setLinearVelocity(0, body.getLinearVelocity().y);
			}
		} else {
			body.setLinearVelocity(velocityX, body.getLinearVelocity().y);
		}
	}
	
	public void moveToUp(float deltatime) {
		if (checkMoveTo() || isDead) {
			return;
		}
		direction.y = velocityY;
		body.setLinearVelocity(body.getLinearVelocity().x, velocityY);
	}
	
	public void moveToDown(float deltatime) {
		if (checkMoveTo() || isDead) {
			return;
		}
		direction.y = -velocityY;
		body.setLinearVelocity(body.getLinearVelocity().x, -velocityY);
	}
	
	public void update(float deltatime) {
		if (checkMoveTo() || isDead) {
			return;
		}
		direction.x *= friction;
		float x = body.getPosition().x + (direction.x * deltatime);
		float y = body.getPosition().y;
		body.setTransform(x, y, 0);
		delayChangeGravity = MathUtils.clamp(delayChangeGravity - deltatime, 0, SECONDS_TO_CHANGE_GRAVITY);
		if (delayChangeGravity <= 0) {
			setGravity(true);
		}
	}
	
	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}
	
	public void jump() {
		if (checkMoveTo() || isDead) {
			return;
		}
		if (!isJumping()) {
			isJumping = true;
			Resource.getInstance().getJumpSound().play();
			body.applyLinearImpulse(new Vector2(0.0f, JUMP), body.getPosition(), true);
		}
	}

	@Override
	public void setGravity(boolean active) {
		super.setGravity(active);
		if (!active) {
			delayChangeGravity = SECONDS_TO_CHANGE_GRAVITY;
		}
	}

	public double getDelayChangeGravity() {
		return Math.floor(delayChangeGravity * 10.00f / 10.00f);
	}

	@Override
	public void render(SpriteBatch batch, float deltatime) {
		stateTime += deltatime;
		teleportSecs += deltatime;
		immunityTime += deltatime;
		if (checkMoveTo()) {
			return;
		}
		TextureRegion region;
		
		if (isDead) {
			region = deadAnimation.getKeyFrame(stateTime);
		} else if (isJumping() || !isGravity()) {
			region = jumpAnimation.getKeyFrame(stateTime);
		} else {
			if (!isMoving()) {
				region = idleAnimation.getKeyFrame(stateTime);
			} else {
				region = runAnimation.getKeyFrame(stateTime);
			}
		}
		int factor = 2;
		int flip = direction.x < 0 ? -1 : 1;
		float rotation = body.getAngle() * MathUtils.radiansToDegrees;
		float origX = ((size.x + 0.5f) * factor / 2) * flip;
		float origY = (size.y * factor / 2);
		float x = body.getPosition().x - origX;
		float y = body.getPosition().y - origY;
		float width = (size.x + 0.5f) * factor * flip;
		float height = size.y * factor;
		float scale = 0.9f;
		batch.draw(region, x, y, origX, origY, width, height, scale, scale, rotation);
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	
	public void moveTo(Vector2 to) {
		moveTo = new Vector2(to.x * Constant.BLOCK_SEPARATION, to.y * Constant.BLOCK_SEPARATION);
	}
	
	private boolean checkMoveTo() {
		if (moveTo != null) {
			teleportSecs = 0;
			body.setTransform(moveTo, 0);
			moveTo = null;
			return true;
		}
		return false;
	}

	@Override
	protected void createBody() {
		body = Box2DUtils.createBox(world, position.x, position.y, size.x, size.y, type, friction, density, isSensor);
		body.setUserData(this);
		// Fixture to detect ground collision
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(this.size.x / 4, this.size.y / 4, new Vector2(0f, -1.0f), 0f);
		FixtureDef fix = new FixtureDef();
		fix.shape = shape;
		fix.isSensor = true;
		fix.filter.categoryBits = Enemy.ENEMY_MASK;
		body.createFixture(fix).setUserData(new JumpSensor());
		shape.dispose();
	}
	
	public void die() {
		if (!isDead) {
			stateTime = 0;
			this.isDead = true;
			this.setGravity(true);
			Resource.getInstance().getGameOverSound().play();
		}
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public boolean isActive() {
		return teleportSecs > INACTIVE_SECONDS && immunityTime > IMMUNITY_SECONDS;
	}
	
	public void resetImmunityTime() {
		this.immunityTime = 0;
	}
	
	public class JumpSensor {
		
	}
}
