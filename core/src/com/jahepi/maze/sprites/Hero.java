package com.jahepi.maze.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	private final static float JUMP = 65.0f;
	private final static float SECONDS_TO_CHANGE_GOD_MODE = 6.0f;
	private float delayChangeGodMode = SECONDS_TO_CHANGE_GOD_MODE;
	
	private Vector2 direction;
	private float velocityX;
	private float velocityY;
	private boolean isJumping, jumpEnemyFlag;
	private Animation runAnimation;
	private Animation jumpAnimation;
	private Animation idleAnimation;
	private Animation deadAnimation;
	private float stateTime;
	private float immunityTime;
	private float teleportSecs = 0f;
	private float godModeSecs = 0f;
	private boolean isMoving = false;
	private Vector2 moveTo;
	private boolean isDead, isGodMode;
	private Resource resource;

	public Hero(World world, float x, float y, Resource resource) {
		super(world, x, y, 0.5f, 1, 0.7f, BodyType.DynamicBody, false, false);
		this.resource = resource;
		velocityX = 12.0f;
		velocityY = 12.0f;
		direction = new Vector2(0, 0);	
		runAnimation = this.resource.getHeroRunAnimation();
		jumpAnimation = this.resource.getHeroJumpAnimation();
		idleAnimation = this.resource.getHeroIdleAnimation();
		deadAnimation = this.resource.getHeroDeadAnimation();
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
		//Gdx.app.log("LINEAR", "" + body.getLinearVelocity().y);

		if (body.getLinearVelocity().y == 0) {
			isJumping = false;
		}

		if (checkMoveTo() || isDead) {
			return;
		}

		jumpOnHeadEnemyHit();


		direction.x *= friction;
		float x = body.getPosition().x + (direction.x * deltatime);
		float y = body.getPosition().y;
		body.setTransform(x, y, 0);
		delayChangeGodMode = MathUtils.clamp(delayChangeGodMode - deltatime, 0, SECONDS_TO_CHANGE_GOD_MODE);
		if (delayChangeGodMode <= 0) {
			isGodMode = false;
		}
	}
	
	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}
	
	public void jump() {
		if (checkMoveTo() || isDead || body.getLinearVelocity().y > 0.5f) {
			return;
		}
		if (!isJumping()) {
			isJumping = true;
			this.resource.getJumpSound().play();
			body.applyLinearImpulse(new Vector2(0.0f, JUMP), body.getPosition(), true);
		}
	}

	private void jumpOnHeadEnemyHit() {
		if (jumpEnemyFlag) {
			body.applyLinearImpulse(new Vector2(0.0f, 50.0f), body.getPosition(), true);
			jumpEnemyFlag = false;
		}
	}

	public void setGodMode() {
		isGodMode = true;
		delayChangeGodMode = SECONDS_TO_CHANGE_GOD_MODE;
	}

	public boolean isGodMode() {
		return isGodMode;
	}

	@Override
	public void render(SpriteBatch batch, float deltatime) {
		stateTime += deltatime;
		teleportSecs += deltatime;
		immunityTime += deltatime;
		godModeSecs += deltatime;
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

		Color color = batch.getColor();
		if (isGodMode) {
			if (godModeSecs < 0.1f) {
				batch.setColor(Color.RED);
			} else if (godModeSecs >= 0.1f && godModeSecs <= 0.2f)  {
				batch.setColor(color);
			} else {
				godModeSecs = 0;
			}
		}
		batch.draw(region, x, y, origX, origY, width, height, scale, scale, rotation);
		batch.setColor(color);
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
			this.resource.getGameOverSound().play();
		}
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public boolean isActive() {
		return teleportSecs > INACTIVE_SECONDS && immunityTime > IMMUNITY_SECONDS && !isGodMode;
	}
	
	public void resetImmunityTime() {
		this.immunityTime = 0;
	}
	
	public class JumpSensor {
		
	}

	public void setJumpEnemyFlag(boolean jumpEnemyFlag) {
		this.jumpEnemyFlag = jumpEnemyFlag;
	}
}
