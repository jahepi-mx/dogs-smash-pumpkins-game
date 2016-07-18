package com.jahepi.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.jahepi.maze.net.Ranking;
import com.jahepi.maze.net.RankingListener;
import com.jahepi.maze.net.Score;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.screens.interfaces.OnExitListener;
import com.jahepi.maze.sprites.Block;
import com.jahepi.maze.sprites.Enemy;
import com.jahepi.maze.sprites.GravityItem;
import com.jahepi.maze.sprites.WorldObject;
import com.jahepi.maze.ui.FinishUI;
import com.jahepi.maze.ui.FinishUI.FinishUIListener;
import com.jahepi.maze.utils.Constant;

public class MazeRender implements Disposable, EventListener, FinishUIListener, RankingListener {

	private static final float SECONDS_TO_START_MATCH = 3.0f;
	private static final float SCORE_POINT_TIME = 0.1f;
	private static final float SECONDS_SHOW_DEAD_SCREEN = 2f;
	private static final boolean DEBUG_BOX2D = false;
	
	private OrthographicCamera cameraGUI;
	private OrthographicCamera camera;
	private Stage stage;
	private MazeController controller;
	private BitmapFont fpsFont;
	private Button exitBtn;
	private Button zoomInBtn;
	private Button zoomOutBtn;
	private Button arrowLeftBtn;
	private Button arrowRightBtn;
	private Button arrowUpBtn;
	private Button arrowDownBtn;
	private Label scoreFontLabel;
	private OnExitListener exitListener;
	private TextureRegion backgroundRegion;
	private BitmapFont startRoundFont;
	private float scorePointTime;
	private SpriteBatch batch;
	private GlyphLayout glyphLayout;
	private BitmapFont scoreEnemyFont;
	private Vector2 scoreEnemyFontPos;
	private ShaderProgram monochromeShader;
	private FinishUI finishUI;
	private Ranking ranking;
	private boolean isLeft;
	private boolean isRight;
	private boolean isUp;
	private boolean isDown;
	
	public MazeRender(Stage stageParam, OnExitListener exitListenerParam) {
		stage = stageParam;
		batch = (SpriteBatch) stage.getBatch();
		exitListener = exitListenerParam;
		ranking = new Ranking(this);
		backgroundRegion = new TextureRegion(Resource.getInstance().getBackgroundTexture());
		startRoundFont = Resource.getInstance().getRoundFont();
		monochromeShader = Resource.getInstance().getMonochromeShader();
		glyphLayout = Resource.getInstance().getGlyphLayout();
		controller = new MazeController(this);
		fpsFont = Resource.getInstance().getFpsFont();
		scoreEnemyFont = Resource.getInstance().getEnemyScoreFont();
		scoreEnemyFontPos = new Vector2(0, Constant.UI_HEIGHT);
		
		camera = new OrthographicCamera(Constant.WORLD_CAMERA_WIDTH, Constant.WORLD_CAMERA_HEIGHT);
		camera.position.set(Constant.WORLD_CAMERA_WIDTH / 2, Constant.WORLD_CAMERA_HEIGHT / 2, 0);
		cameraGUI = new OrthographicCamera(Constant.UI_WIDTH, Constant.UI_HEIGHT);
		cameraGUI.position.set(Constant.UI_WIDTH / 2, Constant.UI_HEIGHT / 2, 0);
	}
	
	private void buildStage() {
		scoreEnemyFontPos.y = Constant.UI_HEIGHT;
		TextureRegion region = Resource.getInstance().getGameAtlas().findRegion("home");
		exitBtn = new Button(new TextureRegionDrawable(region));
		exitBtn.setPosition(-30, Constant.UI_HEIGHT - (exitBtn.getHeight() - 40));
		exitBtn.setTransform(true);
		exitBtn.setOriginX(exitBtn.getWidth() / 2);
		exitBtn.setOriginY(exitBtn.getHeight() / 2);
		exitBtn.setScale(0.4f);
		stage.addActor(exitBtn);
		
		TextureRegion regionZoomIn = Resource.getInstance().getGameAtlas().findRegion("zoomin");
		TextureRegion regionZoomInOver = Resource.getInstance().getGameAtlas().findRegion("zoominover");
		ButtonStyle style = new ButtonStyle();
		style.up = new TextureRegionDrawable(regionZoomIn);
		style.down = new TextureRegionDrawable(regionZoomInOver);
		zoomInBtn = new Button(style);
		zoomInBtn.setPosition(60, Constant.UI_HEIGHT - (exitBtn.getHeight() - 40));
		zoomInBtn.setTransform(true);
		zoomInBtn.setOriginX(zoomInBtn.getWidth() / 2);
		zoomInBtn.setOriginY(zoomInBtn.getHeight() / 2);
		zoomInBtn.setScale(0.4f);
		stage.addActor(zoomInBtn);
		
		TextureRegion regionZoomOut = Resource.getInstance().getGameAtlas().findRegion("zoomout");
		TextureRegion regionZoomOutOver = Resource.getInstance().getGameAtlas().findRegion("zoomoutover");
		ButtonStyle style2 = new ButtonStyle();
		style2.up = new TextureRegionDrawable(regionZoomOut);
		style2.down = new TextureRegionDrawable(regionZoomOutOver);
		zoomOutBtn = new Button(style2);
		zoomOutBtn.setPosition(150, Constant.UI_HEIGHT - (exitBtn.getHeight() - 40));
		zoomOutBtn.setTransform(true);
		zoomOutBtn.setOriginX(zoomOutBtn.getWidth() / 2);
		zoomOutBtn.setOriginY(zoomOutBtn.getHeight() / 2);
		zoomOutBtn.setScale(0.4f);
		stage.addActor(zoomOutBtn);
		
		TextureRegion regionArrow = Resource.getInstance().getGameAtlas().findRegion("arrow");
		TextureRegion regionArrowOver = Resource.getInstance().getGameAtlas().findRegion("arrowover");
		ButtonStyle style3 = new ButtonStyle();
		style3.up = new TextureRegionDrawable(regionArrow);
		style3.down = new TextureRegionDrawable(regionArrowOver);
		arrowLeftBtn = new Button(style3);
		arrowLeftBtn.setPosition(-30, -10);
		arrowLeftBtn.setTransform(true);
		arrowLeftBtn.setOriginX(arrowLeftBtn.getWidth() / 2);
		arrowLeftBtn.setOriginY(arrowLeftBtn.getHeight() / 2);
		arrowLeftBtn.setScale(0.5f);
		stage.addActor(arrowLeftBtn);
		
		arrowLeftBtn.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				isLeft = true;
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				isLeft = false;
				controller.setMovingHero(false);
			}			
		});
		
		arrowRightBtn = new Button(style3);
		arrowRightBtn.setPosition(85, -10);
		arrowRightBtn.setTransform(true);
		arrowRightBtn.setOriginX(arrowRightBtn.getWidth() / 2);
		arrowRightBtn.setOriginY(arrowRightBtn.getHeight() / 2);
		arrowRightBtn.setScale(0.5f);
		arrowRightBtn.rotateBy(180.0f);
		stage.addActor(arrowRightBtn);
		
		arrowRightBtn.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				isRight = true;
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				isRight = false;
				controller.setMovingHero(false);
			}			
		});
		
		arrowUpBtn = new Button(style3);
		arrowUpBtn.setPosition(Constant.UI_WIDTH - 275, -10);
		arrowUpBtn.setTransform(true);
		arrowUpBtn.setOriginX(arrowUpBtn.getWidth() / 2);
		arrowUpBtn.setOriginY(arrowUpBtn.getHeight() / 2);
		arrowUpBtn.setScale(0.5f);
		arrowUpBtn.rotateBy(270.0f);
		stage.addActor(arrowUpBtn);
		
		arrowUpBtn.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				isUp = true;
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				isUp = false;
			}			
		});
		
		arrowDownBtn = new Button(style3);
		arrowDownBtn.setPosition(Constant.UI_WIDTH - 155, -10);
		arrowDownBtn.setTransform(true);
		arrowDownBtn.setOriginX(arrowUpBtn.getWidth() / 2);
		arrowDownBtn.setOriginY(arrowUpBtn.getHeight() / 2);
		arrowDownBtn.setScale(0.5f);
		arrowDownBtn.rotateBy(90.0f);
		stage.addActor(arrowDownBtn);
		
		arrowDownBtn.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				isDown = true;
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				isDown = false;
			}			
		});
		
		LabelStyle style4 = new LabelStyle();
		style4.font = Resource.getInstance().getScoreFont();
		scoreFontLabel = new Label("0", style4);	
		scoreFontLabel.setPosition(Constant.UI_WIDTH - scoreFontLabel.getWidth() - 50.0f, Constant.UI_HEIGHT - scoreFontLabel.getHeight());
		stage.addActor(scoreFontLabel);
		
		stage.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.A ||  keycode == Keys.D) {
					controller.setMovingHero(false);
				}
				return true;
			}
			
		});
		
		exitBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				batch.setShader(null);
				finishUI.reset();
				exitListener.onExit();
			}		
		});
		
		zoomInBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				controller.zoomIn();
			}		
		});
		
		zoomOutBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				controller.zoomOut();
			}		
		});
		
		finishUI = new FinishUI(this);
		finishUI.setVisible(false);
		stage.addActor(finishUI.getTable());
	}
	
	public void start() {
		buildStage();
		controller.start();
		Gdx.input.setInputProcessor(stage);
	}
	
	private void drawBackground(SpriteBatch batch, float deltatime) {
		float degrees = 270f;
		batch.draw(backgroundRegion, camera.position.x - (Constant.WORLD_CAMERA_WIDTH * 1.5f / 2), camera.position.y - (Constant.WORLD_CAMERA_HEIGHT * 1.5f / 2), (Constant.WORLD_CAMERA_WIDTH * 1.5f / 2), (Constant.WORLD_CAMERA_HEIGHT * 1.5f / 2), Constant.WORLD_CAMERA_WIDTH * 1.5f, Constant.WORLD_CAMERA_HEIGHT * 1.5f, 1, 1, degrees, false);
	}
	
	public void render(float deltatime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.controller.update(deltatime);
		batch.setProjectionMatrix(camera.combined);
		updateCamera();
		
		batch.begin();
		if (controller.isHeroDead()) {
			if (controller.getHeroDeadTime() >= SECONDS_SHOW_DEAD_SCREEN) {
				batch.setShader(monochromeShader);
				monochromeShader.setUniformf("u_amount", 1.0f);
				arrowDownBtn.setVisible(false);
				arrowUpBtn.setVisible(false);
				arrowLeftBtn.setVisible(false);
				arrowRightBtn.setVisible(false);
				scoreFontLabel.setVisible(false);
				exitBtn.setVisible(false);
				zoomInBtn.setVisible(false);
				zoomOutBtn.setVisible(false);
				finishUI.setVisible(true);
				finishUI.setScore(controller.getScore());
			}
		}
		this.drawBackground(batch, deltatime);
		
		if (isLeft) {
			this.controller.heroLeft(deltatime);
		}
		
		if (isRight) {
			this.controller.heroRight(deltatime);
		}
		
		if (isUp) {
			this.controller.heroUp(deltatime);
		}
		
		if (isDown) {
			this.controller.heroDown(deltatime);
		}
		
		this.controller.getHero().render(batch, deltatime);
		Array<GravityItem> gravityItems = controller.getGravityItems();
		for (GravityItem item : gravityItems) {
			item.render(batch, deltatime);
		}
		Array<WorldObject> blocks = controller.getBlocks();
		for (WorldObject block : blocks) {
			block.render(batch, deltatime);
		}
		Array<Block> gravityBlocks = controller.getGravityBlocks();
		for (Block block : gravityBlocks) {
			block.render(batch, deltatime);
		}
		if (controller.getStartRoundSecs() > SECONDS_TO_START_MATCH) {
			this.controller.createEnemies();
			Array<Enemy> enemies = controller.getEnemies();
			for (WorldObject enemy : enemies) {
				enemy.render(batch, deltatime);
			}
		}
		batch.end();
		// System.out.println("Calls: " + batch.renderCalls);
		if (DEBUG_BOX2D) {
			batch.begin();
			this.controller.debug(camera.combined);
			batch.end();
		}
		System.out.println("Heap: " + (Gdx.app.getJavaHeap() / 1048576f));
		renderGui(deltatime);
	}
	
	private void updateCamera() {
		this.camera.position.x = controller.getCameraHelper().getX();
		this.camera.position.y = controller.getCameraHelper().getY();
		this.camera.zoom = controller.getCameraHelper().getZoom().x;
		camera.update();
	}
	
	private void renderGui(float deltatime) {
		float margin = 40.0f;
		batch.setProjectionMatrix(cameraGUI.combined);
		cameraGUI.update();
		batch.begin();
		
		glyphLayout.setText(fpsFont, "Fps: " + Gdx.graphics.getFramesPerSecond());
		fpsFont.draw(batch, glyphLayout, Constant.UI_WIDTH - glyphLayout.width - margin, margin);
		
		if (scorePointTime > 0) {
			scorePointTime -= deltatime;
			scoreFontLabel.setFontScale(2.0f);
		} else {
			scoreFontLabel.setFontScale(1.0f);
		}
		
		if (controller.getStartRoundSecs() <= SECONDS_TO_START_MATCH) {
			glyphLayout.setText(startRoundFont, "Round " + controller.getRoundNumber());
			startRoundFont.draw(batch, glyphLayout, (stage.getWidth() / 2) - (glyphLayout.width / 2), (stage.getHeight() / 2) + (glyphLayout.height / 2));
		}
		if (scoreEnemyFontPos.y < Constant.UI_HEIGHT) {
			scoreEnemyFontPos.y += (100.0f * deltatime);
			scoreEnemyFont.draw(batch, "-1", scoreEnemyFontPos.x, scoreEnemyFontPos.y);
		}
		batch.end();
		batch.setShader(null);
		stage.act(deltatime);
		stage.draw();
	}

	@Override
	public void dispose() {
		controller.dispose();
		finishUI.dispose();
		finishUI = null;
		exitBtn = null;
		zoomInBtn = null;
		zoomOutBtn = null;
		arrowLeftBtn = null;
		arrowRightBtn = null;
		arrowUpBtn = null;
		arrowDownBtn = null;
		scoreFontLabel = null;
		isLeft = false;
		isRight = false;
		isUp = false;
		isDown = false;
		stage.dispose();
	}

	@Override
	public void onKillAllEnemies() {
		
	}

	@Override
	public void onPoint() {
		scorePointTime = SCORE_POINT_TIME;
		scoreEnemyFontPos.x = (Constant.UI_WIDTH / camera.position.x / 2) * camera.position.x;
		scoreEnemyFontPos.y = (Constant.UI_HEIGHT / camera.position.y / 2) * camera.position.y;
		scoreFontLabel.setText(Integer.toString(this.controller.getScore()));
	}

	@Override
	public void onTryAgain() {
		scoreFontLabel.setText("0");
		arrowDownBtn.setVisible(true);
		arrowUpBtn.setVisible(true);
		arrowLeftBtn.setVisible(true);
		arrowRightBtn.setVisible(true);
		scoreFontLabel.setVisible(true);
		exitBtn.setVisible(true);
		zoomInBtn.setVisible(true);
		zoomOutBtn.setVisible(true);
		finishUI.reset();
		stage.setKeyboardFocus(null);
		controller.dispose();
		controller.start();
	}

	@Override
	public void onGoToMain() {
		batch.setShader(null);
		finishUI.reset();
		stage.setKeyboardFocus(null);
		exitListener.onExit();
	}

	@Override
	public void onSendScoreWorldWide(String name, int score) {
		ranking.saveRanking(name, score);
	}

	@Override
	public void onSaveRanking() {
		if (finishUI != null) {
			finishUI.setStatus("Your score was published successfully!");
		}
	}

	@Override
	public void onGetRanking(Array<Score> scores) {
		
	}

	@Override
	public void onErrorSaveRanking() {
		if (finishUI != null) {
			finishUI.setSendingData(false);
			finishUI.setStatus("Your score could not be published, verify your internet connection!");
		}
	}

	@Override
	public void onErrorGetRanking() {
	}
}