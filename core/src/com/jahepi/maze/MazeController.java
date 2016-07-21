package com.jahepi.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.jahepi.maze.levels.Level;
import com.jahepi.maze.levels.Level.Tile;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.sprites.Block;
import com.jahepi.maze.sprites.Crate;
import com.jahepi.maze.sprites.Door;
import com.jahepi.maze.sprites.Enemy;
import com.jahepi.maze.sprites.GravityItem;
import com.jahepi.maze.sprites.Hero;
import com.jahepi.maze.sprites.None;
import com.jahepi.maze.sprites.Slope;
import com.jahepi.maze.sprites.WorldObject;
import com.jahepi.maze.utils.CameraHelper;
import com.jahepi.maze.utils.Constant;

public class MazeController implements Disposable, ContactListener {
	
	private World world;
	private Box2DDebugRenderer debug;
	private CameraHelper cameraHelper;
	private Hero hero;
	private Level level;
	private Array<WorldObject> blocks;
	private Array<Enemy> enemies;
	private Array<Block> gravityBlocks;
	private Array<GravityItem> gravityItems;
	private Array<Vector2> spawnPoints;
	private int numberOfEnemies;
	private EventListener listener;
	private boolean onKillEnemiesFlag;
	private int score;
	private int roundNumber;
	private float startRoundSecs;
	private float heroDeadTime;
	private boolean loadNextLevel;
	private Resource resource;
	
	public MazeController(EventListener listener, Resource resource) {
		level = new Level();
		this.listener = listener;
		cameraHelper = new CameraHelper();
		this.resource = resource;
	}
	
	public void start() {
		enemies = new Array<Enemy>();
		blocks = new Array<WorldObject>();
		gravityBlocks = new Array<Block>();
		gravityItems = new Array<GravityItem>();
		spawnPoints = new Array<Vector2>();
		debug = new Box2DDebugRenderer();
		world = new World(new Vector2(0, -98f), true);
		numberOfEnemies = Constant.DEFAULT_NUMBER_ENEMIES;
		Enemy.MIN_VELOCITY = Constant.DEFAULT_ENEMY_MIN_VELOCITY;
		Enemy.MAX_VELOCITY = Constant.DEFAULT_ENEMY_MAX_VELOCITY;
		Enemy.LIFE = Constant.DEFAULT_ENEMY_LIFE;
		score = 0;
		roundNumber = 1;
		startRoundSecs = 0;
		heroDeadTime = 0;
		onKillEnemiesFlag = true;
		loadLevel();	
		world.setContactListener(this);
	}
	
	private void rebuild() {
		dispose();
		enemies = new Array<Enemy>();
		blocks = new Array<WorldObject>();
		gravityBlocks = new Array<Block>();
		gravityItems = new Array<GravityItem>();
		spawnPoints = new Array<Vector2>();
		debug = new Box2DDebugRenderer();
		world = new World(new Vector2(0, -98f), true);
		loadLevel();
		world.setContactListener(this);
	}
	
	public void update(float deltatime) {
		
		if (loadNextLevel) {
			loadNextLevel = false;
			rebuild();
		}
		
		startRoundSecs += deltatime;
		if (hero.isDead()) {
			heroDeadTime += deltatime;
		}
		updateEnemies();
		world.step(deltatime, 6, 2);
		updateHeroPosition(deltatime);
		updateCameraHelper();
	}
	
	private void updateEnemies() {
		for (Enemy enemy : enemies) {
			if (enemy.isReadyToRemove()) {
				enemies.removeValue(enemy, true);
			}
		}	
		if (enemies.size == 0 && !onKillEnemiesFlag) {
			onKillEnemiesFlag = true;
			roundNumber++;
			startRoundSecs = 0;
			loadNextLevel = true;
			listener.onKillAllEnemies();
		}
	}
	
	public void createEnemies() {
		if (onKillEnemiesFlag) {
			if (this.roundNumber > 1) {
				Enemy.MIN_VELOCITY++;
				Enemy.MAX_VELOCITY++;
				Enemy.LIFE++;
				this.numberOfEnemies++;
			}
			for (int i = 0; i < numberOfEnemies; i++) {
				Vector2 vector = spawnPoints.get(MathUtils.random(spawnPoints.size - 1));
				Enemy enemy = new Enemy(world, vector.x , vector.y, resource);
				enemies.add(enemy);
			}
			onKillEnemiesFlag = false;
		}
	}
	
	private void updateCameraHelper() {
		float x = hero.getPosition().x;
		float y = hero.getPosition().y;
		cameraHelper.setPosition(x, y);
		cameraHelper.updateZoom();
	}
	
	public void zoomIn() {
		this.cameraHelper.zoomIn();
	}
	
	public void zoomOut() {
		this.cameraHelper.zoomOut();
	}
	
	private void updateHeroPosition(float deltatime) {
		
		if (Gdx.input.isKeyPressed(Keys.A)) {
			this.heroLeft(deltatime);
		}
		
		if (Gdx.input.isKeyPressed(Keys.D)) {
			this.heroRight(deltatime);
		}
		
		if (Gdx.input.isKeyPressed(Keys.W)) {
			this.heroUp(deltatime);
		}
		
		if (Gdx.input.isKeyPressed(Keys.S)) {
			this.heroDown(deltatime);
		}

		hero.update(deltatime);
		
		if (hero.getPosition().y <= 0) {
			cleanLevel();
			start();
		}
	}
	
	public void heroLeft(float deltatime) {
		hero.moveToLeft(deltatime);
	}
	
	public void heroRight(float deltatime) {
		hero.moveToRight(deltatime);
	}
	
	public void heroUp(float deltatime) {
		if (hero.isGravity()) {
			hero.jump();
		} else {
			hero.moveToUp(deltatime);
		}
	}
	
	public void heroDown(float deltatime) {
		if (!hero.isGravity()) {
			hero.moveToDown(deltatime);
		}
	}
	
	public void setMovingHero(boolean moving) {
		this.hero.setMoving(moving);
	}
	
	public void debug(Matrix4 matrix) {
		debug.render(world, matrix);
	}
	
	public CameraHelper getCameraHelper() {
		return cameraHelper;
	}

	private void loadLevel() {
		Tile[][] data = level.getLevel();
		int mirrorY = data.length;
		for (int y = 0; y < data.length; y++) {
			mirrorY--;
			for (int x = 0; x < data[y].length; x++) {
				if (data[y][x] instanceof Level.Crate) {
					Crate block = new Crate(world, x , mirrorY, 1.5f, BodyType.DynamicBody, resource);
					gravityBlocks.add(block);
				} else if (data[y][x] instanceof Level.StaticCrate) {
					Crate block = new Crate(world, x , mirrorY, 2.0f, BodyType.StaticBody, resource);
					blocks.add(block);
				} else if (data[y][x] instanceof Level.Hero) {
					hero = new Hero(world, x, mirrorY, resource);
					hero.setGravity(true);
				} else if (data[y][x] instanceof Level.Mushroom) {
					GravityItem item = new GravityItem(world, x, mirrorY, resource);
					gravityItems.add(item);
				} else if (data[y][x] instanceof Level.Slope) {
					Slope slope = new Slope(world, x , mirrorY, 2.0f, data[y][x].isFlipX());
					slope.setTextureRegion(resource.getGameAtlas().findRegion(data[y][x].getImage()));
					blocks.add(slope);
				} else if (data[y][x] instanceof Level.Door) {
					Level.Door levelDoor = (Level.Door) data[y][x];
					Door item = new Door(world, x , mirrorY, BodyType.StaticBody, 2.0f, levelDoor.isFlipX());
					item.setGoTo(levelDoor.getGoTo().x, levelDoor.getGoTo().y);
					item.setTextureRegion(resource.getGameAtlas().findRegion(levelDoor.getImage()));
					blocks.add(item);
				} else if(data[y][x] instanceof Level.Empty) {
					spawnPoints.add(new Vector2(x, mirrorY));
				} else if(data[y][x] instanceof Level.None) {
					None none = new None(x , mirrorY, 2.0f, data[y][x].isFlipX());
					none.setTextureRegion(resource.getGameAtlas().findRegion(data[y][x].getImage()));
					blocks.add(none);
				} else {
					Block block = new Block(world, x , mirrorY, BodyType.StaticBody, 2.0f, data[y][x].isFlipX());
					block.setTextureRegion(resource.getGameAtlas().findRegion(data[y][x].getImage()));
					blocks.add(block);
				}
			}
		}
	}

	@Override
	public void beginContact(Contact contact) {
		
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		if (fixtureA.getBody().getUserData() instanceof Crate
				&& fixtureB.getBody().getUserData() instanceof Hero) {
			Crate box = (Crate) fixtureA.getBody().getUserData();
			box.setGravity(true);
		}
		
		if (fixtureB.getBody().getUserData() instanceof Crate
				&& fixtureA.getBody().getUserData() instanceof Hero) {
			Crate box = (Crate) fixtureB.getBody().getUserData();
			box.setGravity(true);
		}
		
		if (fixtureA.getUserData() instanceof Hero.JumpSensor 
				|| fixtureB.getUserData() instanceof Hero.JumpSensor) {
			if (fixtureA.getBody().getUserData() instanceof Block 
					|| fixtureB.getBody().getUserData() instanceof Block) {
				hero.setJumping(false);
			}
			if (fixtureA.getBody().getUserData() instanceof Enemy 
					|| fixtureB.getBody().getUserData() instanceof Enemy) {
				hero.setJumping(false);
			}
		}
		
		if (fixtureA.getUserData() instanceof Hero.JumpSensor 
				|| fixtureB.getUserData() instanceof Hero.JumpSensor) {
			if (fixtureA.getBody().getUserData() instanceof Enemy) {
				Enemy enemy = (Enemy) fixtureA.getBody().getUserData();
				if (enemy.damage()) {
					score++;
					this.listener.onPoint();
					hero.resetImmunityTime();
					hero.setJumpFlag(true);
				}
			}
			if (fixtureB.getBody().getUserData() instanceof Enemy) {
				Enemy enemy = (Enemy) fixtureB.getBody().getUserData();
				if (enemy.damage()) {
					score++;
					this.listener.onPoint();
					hero.resetImmunityTime();
					hero.setJumpFlag(true);
				}
			}
		}
		
		if (fixtureA.getUserData() instanceof Enemy.LeftSideEnemy) {
			Enemy enemy = (Enemy) fixtureA.getBody().getUserData();
			if (!checkCrateCollision(enemy, fixtureB)) {
				if (fixtureB.getBody().getUserData() instanceof Door == false) {
					enemy.changeDirection();
				}
			}
		}
		if (fixtureA.getUserData() instanceof Enemy.RightSideEnemy) {
			Enemy enemy = (Enemy) fixtureA.getBody().getUserData();
			if (!checkCrateCollision(enemy, fixtureB)) {
				if (fixtureB.getBody().getUserData() instanceof Door == false) {
					enemy.changeDirection();
				}
			}
		}
		
		if (fixtureB.getUserData() instanceof Enemy.LeftSideEnemy) {
			Enemy enemy = (Enemy) fixtureB.getBody().getUserData();
			if (!checkCrateCollision(enemy, fixtureA)) {
				if (fixtureA.getBody().getUserData() instanceof Door == false) {
					enemy.changeDirection();
				}
			}
		}
		if (fixtureB.getUserData() instanceof Enemy.RightSideEnemy) {
			Enemy enemy = (Enemy) fixtureB.getBody().getUserData();
			if (!checkCrateCollision(enemy, fixtureA)) {
				if (fixtureA.getBody().getUserData() instanceof Door == false) {
					enemy.changeDirection();
				}
			}
		}
		
		if (fixtureA.getBody().getUserData() instanceof GravityItem
				&& fixtureB.getBody().getUserData() instanceof Hero) {
			GravityItem item = (GravityItem) fixtureA.getBody().getUserData();
			if (item.isVisible()) {
				item.setVisible(false);
				hero.setGodMode();
			}
		}
		if (fixtureB.getBody().getUserData() instanceof GravityItem
				&& fixtureA.getBody().getUserData() instanceof Hero) {
			GravityItem item = (GravityItem) fixtureB.getBody().getUserData();
			if (item.isVisible()) {
				item.setVisible(false);
				hero.setGodMode();
			}
		}
		
		if (fixtureA.getBody().getUserData() instanceof Hero &&
				fixtureB.getUserData() instanceof Enemy.LeftSideEnemy) {
			Enemy enemy = (Enemy) fixtureB.getBody().getUserData();
			if (!enemy.isDead() && enemy.isActive() && hero.isActive()) {
				hero.die();
			}
		}
		
		if (fixtureB.getBody().getUserData() instanceof Hero &&
				fixtureA.getUserData() instanceof Enemy.LeftSideEnemy) {
			Enemy enemy = (Enemy) fixtureA.getBody().getUserData();
			if (!enemy.isDead() && enemy.isActive() && hero.isActive()) {
				hero.die();
			}
		}
		
		if (fixtureA.getBody().getUserData() instanceof Hero &&
				fixtureB.getUserData() instanceof Enemy.RightSideEnemy) {
			Enemy enemy = (Enemy) fixtureB.getBody().getUserData();
			if (!enemy.isDead() && enemy.isActive() && hero.isActive()) {
				hero.die();
			}
		}
		
		if (fixtureB.getBody().getUserData() instanceof Hero &&
				fixtureA.getUserData() instanceof Enemy.RightSideEnemy) {
			Enemy enemy = (Enemy) fixtureA.getBody().getUserData();
			if (!enemy.isDead() && enemy.isActive() && hero.isActive()) {
				hero.die();
			}
		}
		
		if (fixtureA.getBody().getUserData() instanceof Door) {
			Door door = (Door) fixtureA.getBody().getUserData();
			Vector2 to = door.getGoTo();
			if (fixtureB.getBody().getUserData() instanceof Hero) {
				hero.moveTo(to);
			}
			if (fixtureB.getBody().getUserData() instanceof Enemy) {
				Enemy enemy = (Enemy) fixtureB.getBody().getUserData();
				enemy.moveTo(to);
			}
		}
		
		if (fixtureB.getBody().getUserData() instanceof Door) {
			Door door = (Door) fixtureB.getBody().getUserData();
			Vector2 to = door.getGoTo();
			if (fixtureA.getBody().getUserData() instanceof Hero) {
				hero.moveTo(to);
			}
			if (fixtureA.getBody().getUserData() instanceof Enemy) {
				Enemy enemy = (Enemy) fixtureA.getBody().getUserData();
				enemy.moveTo(to);
			}
		}
	}
	
	private boolean checkCrateCollision(Enemy enemy, Fixture fixture) {
		if (fixture.getBody().getUserData() instanceof Crate) {
			enemy.setJumpFlag(true);
			return true;
		}
		return false;
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
		
	public Hero getHero() {
		return hero;
	}
	
	public boolean isHeroDead() {
		return hero.isDead();
	}

	public boolean isGodMode() {
		return hero.isGodMode();
	}
	
	public Array<GravityItem> getGravityItems() {
		return gravityItems;
	}
	
	public Array<WorldObject> getBlocks() {
		return blocks;
	}
	
	public Array<Enemy> getEnemies() {
		return enemies;
	}
	
	public Array<Block> getGravityBlocks() {
		return gravityBlocks;
	}
	
	public int getScore() {
		return this.score;
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public float getStartRoundSecs() {
		return startRoundSecs;
	}
	
	public float getHeroDeadTime() {
		return heroDeadTime;
	}

	private void cleanLevel() {
		try {
			debug.dispose();
		} catch (Exception e) {
			
		}
		try {
			world.dispose();
		} catch (Exception e) {
			
		}
		
		if (blocks != null) {
			blocks.clear();
			blocks = null;
			gravityBlocks.clear();
			gravityBlocks = null;
			gravityItems.clear();
			gravityItems = null;
			enemies.clear();
			enemies = null;
			spawnPoints.clear();
			spawnPoints = null;
		}
		hero = null;
	}
	
	@Override
	public void dispose() {
		cleanLevel();
	}
}