package com.jahepi.maze.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.utils.Box2DUtils;
import com.jahepi.maze.utils.Constant;

public class Enemy extends WorldObject {

	private static final float INACTIVE_SECONDS = 1.0f;
	private static final float MAX_DEAD_LIMIT = 2.0f;
	public final static int ENEMY_CATEGORY = 1;
	public final static int ENEMY_MASK = 1 << 1;
	public static int MIN_VELOCITY = 1;
	public static int MAX_VELOCITY = 3;
	public static int LIFE = 1;
	public final static int WALK_VELOCITY = 6;
	public final static float BLINK_DEAD_TIME = 0.2f;
	
	private Vector2 direction;
	private Vector2 moveTo;
	private float velocity;
	private float jumpSecs = 1.0f;
	private float destroySecs = 0f;
	private float teleportSecs = 0f;
	private boolean isJumping;
	private float stateTime;
	private boolean isDead;
	private int life;
	private float blinkDead;
	private float blinkDeadAlpha;
	private Resource resource;
	
	private Animation runAnimation;
	private Animation jumpAnimation;
	private Animation walkAnimation;
	private Animation deadAnimation;
	
	public Enemy(World world, float x, float y, Resource resource) {
		super(world, x, y, 1.5f, 1.5f, 0.8f, BodyType.DynamicBody, false, false);
		this.resource = resource;
		direction = new Vector2(MathUtils.randomSign(), 1);
		this.velocity = MathUtils.random(MIN_VELOCITY, MAX_VELOCITY);
		this.life = LIFE;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.2f, 0.2f, new Vector2(-0.9f, 0.0f), 0f);
		FixtureDef fix = new FixtureDef();
		fix.filter.categoryBits = ENEMY_CATEGORY;
		fix.filter.maskBits = ENEMY_MASK;
		fix.shape = shape;
		fix.isSensor = true;
		body.createFixture(fix).setUserData(new LeftSideEnemy());
		shape.dispose();
		
		PolygonShape shape1 = new PolygonShape();
		shape1.setAsBox(0.2f, 0.2f, new Vector2(0.9f, 0.0f), 0f);
		FixtureDef fix1 = new FixtureDef();
		fix1.filter.categoryBits = ENEMY_CATEGORY;
		fix1.filter.maskBits = ENEMY_MASK;
		fix1.shape = shape;
		fix1.isSensor = true;
		body.createFixture(fix1).setUserData(new RightSideEnemy());
		shape1.dispose();
		
		runAnimation = this.resource.getEnemyRunAnimation();
		jumpAnimation = this.resource.getEnemyJumpAnimation();
		walkAnimation = this.resource.getEnemyWalkAnimation();
		deadAnimation = this.resource.getEnemyDeadAnimation();
		
		blinkDeadAlpha = 1.0f;
	}

	@Override
	protected void createBody() {
		float realX = position.x;
		float realY = position.y;
		body = Box2DUtils.createEnemy(this.world, realX, realY, size.x - 0.5f, type, 0.1f, 1f, isSensor);
		body.setUserData(this);
	}

	@Override
	public void render(SpriteBatch batch, float deltatime) {
		teleportSecs += deltatime;
		stateTime += deltatime;
		
		if (checkMoveTo()) {
			return;
		}
		
		if (!isDead) {
			jumpSecs += deltatime;
			
			if (Math.round(this.body.getLinearVelocity().y) == 0) {
				isJumping = false;
			}
			
			if (jumpSecs >= 1.0f && Math.round(Math.abs(this.body.getLinearVelocity().x)) <= 0 &&
					Math.round(Math.abs(this.body.getLinearVelocity().y)) <= 0) {
				jump();
			}
			
			body.setLinearVelocity(velocity * direction.x, this.body.getLinearVelocity().y);
		}
		
		TextureRegion region;
		if (isDead) {
			region = deadAnimation.getKeyFrame(stateTime);
		} else if (isJumping) {
			region = jumpAnimation.getKeyFrame(stateTime);
		} else if (this.velocity <= WALK_VELOCITY) {
			region = walkAnimation.getKeyFrame(stateTime);
		} else {
			region = runAnimation.getKeyFrame(stateTime);
		}
		
		int factor = 2;
		int flip = direction.x < 0 ? -1 : 1;
		float rotation = body.getAngle() * MathUtils.radiansToDegrees;
		float origX = (size.x * factor / 2) * flip;
		float origY = (size.y * factor / 2);
		float x = body.getPosition().x - origX;
		float y = body.getPosition().y - origY + 0.7f;
		float width = size.x * factor * flip;
		float height = size.y * factor;
		float scale = 1.0f;
		if (isDead) {
			blinkDead += deltatime;
			if (blinkDead >= BLINK_DEAD_TIME) {
				blinkDead = 0;
				blinkDeadAlpha = blinkDeadAlpha == 1.0f ? 0.5f : 1.0f;
			}
			batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, blinkDeadAlpha);
		}
		batch.draw(region, x, y, origX, origY, width, height, scale, scale, rotation);
		batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1.0f);
		
		if (isDead()) {
			destroySecs += deltatime;
		}
	}
	
	public void jump() {
		if (checkMoveTo()) {
			return;
		}
		if (jumpSecs >= 1.0f) {
			jumpSecs = 0;
			isJumping = true;
			body.applyLinearImpulse(new Vector2(0.01f, 100f), body.getPosition(), true);
		}
	}
	
	public void changeDirection() {
		this.direction.x *= -1;
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
	
	public boolean damage() {
		if (!isDead() && isActive()) {
			life--;
			if (life <= 0) {
				stateTime = 0;
				this.isDead = true;
				this.resource.getEnemyDieSound().play();
			} else {
				this.resource.getHitSound().play();
			}
			return true;
		}
		return false;
	}
	
	public boolean isActive() {
		return (stateTime > INACTIVE_SECONDS && teleportSecs > INACTIVE_SECONDS);
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public boolean isReadyToRemove() {
		if (destroySecs >= MAX_DEAD_LIMIT) {
			this.dispose();
			return true;
		}
		return false;
	}

	public class LeftSideEnemy {
		
	}
	
	public class RightSideEnemy {
		
	}
}
