package com.jahepi.maze.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jahepi.maze.Maze;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.utils.Constant;

public class MainScreen implements Screen {
	
	private final static float BLINK_START_INTERVAL = 0.1f;
	private final static float SCALE_INTERVAL = 1.0f;
	private final static float MAX_SCALE = 1.0f;
	private final static float MIN_SCALE = 0.5f;
	
	private Maze maze;
	private Label startBtn;
	private Label rankingBtn;
	private Label exiBtn;
	private Label creditsLabel;
	private Stage stage;
	private TextureRegion backgroundRegion;
	private Vector2 scaleFrom;
	private Vector2 scaleTo;
	private float blinkStartTime;
	private Color blinkStartColor = Color.RED;
	private BitmapFont gameTitle;
	private float scaleTime;
	private GlyphLayout glyphLayout;
	private Music mainMusic;
	private Resource resource;
	
	public MainScreen(Maze mazeParam) {

		maze = mazeParam;
		resource = mazeParam.getResource();

		StretchViewport viewport = new StretchViewport(Constant.UI_WIDTH, Constant.UI_HEIGHT);
		stage = new Stage(viewport, maze.getBatch());
		
		glyphLayout = resource.getGlyphLayout();
		gameTitle = resource.getGameTitleFont();
		mainMusic = resource.getMainMusic();
		backgroundRegion = new TextureRegion(resource.getBackgroundTexture());
		
		scaleFrom = new Vector2(MIN_SCALE, 0);
		scaleTo = new Vector2(MAX_SCALE, 0);
		
	}
	
	private void buildStage() {
		
		LabelStyle style = new LabelStyle();
		BitmapFont start = resource.getUIFont();
		style.font = start;
		startBtn = new Label("Start", style);
		startBtn.setFontScale(2.0f);
		startBtn.setColor(Color.RED);
		
		startBtn.setPosition(Constant.UI_WIDTH / 2 - (startBtn.getWidth() * startBtn.getFontScaleX() / 2), Constant.UI_HEIGHT / 2 - (startBtn.getHeight() * startBtn.getFontScaleY() / 2));
		startBtn.setOriginX(startBtn.getWidth() / 2);
		startBtn.setOriginY(startBtn.getHeight() / 2);
		startBtn.scaleBy(2.0f);
		this.stage.addActor(startBtn);
		
		startBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				maze.changeToPlayScreen();
			}
			
		});
		
		LabelStyle style2 = new LabelStyle();
		BitmapFont ranking = resource.getUIFont();
		style2.font = ranking;
		
		rankingBtn = new Label("WorldWide Ranking", style2);
		rankingBtn.setFontScale(2.0f);
		rankingBtn.setColor(Color.WHITE);
		rankingBtn.scaleBy(2.0f);
		rankingBtn.setPosition(Constant.UI_WIDTH / 2 - (rankingBtn.getWidth() * rankingBtn.getFontScaleX() / 2), Constant.UI_HEIGHT / 2 - (rankingBtn.getHeight() * rankingBtn.getFontScaleY() * 2.0f));
		rankingBtn.setOriginX(rankingBtn.getWidth() / 2);
		rankingBtn.setOriginY(rankingBtn.getHeight() / 2);
		this.stage.addActor(rankingBtn);
		
		rankingBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				maze.changeToRankingScreen();
			}
			
		});

		exiBtn = new Label("Exit", style2);
		exiBtn.setFontScale(2.0f);
		exiBtn.setColor(Color.WHITE);
		exiBtn.scaleBy(2.0f);
		exiBtn.setPosition(Constant.UI_WIDTH / 2 - (exiBtn.getWidth() * exiBtn.getFontScaleX() / 2), Constant.UI_HEIGHT / 2 - (exiBtn.getHeight() * exiBtn.getFontScaleY() * 3.4f));
		exiBtn.setOriginX(exiBtn.getWidth() / 2);
		exiBtn.setOriginY(exiBtn.getHeight() / 2);
		this.stage.addActor(exiBtn);

		exiBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}

		});

		creditsLabel = new Label("Game Sprites www.gameart2d.com \n Music & Sound http://opengameart.org", style2);
		creditsLabel.setFontScale(1.2f);
		creditsLabel.setColor(Color.GOLD);
		creditsLabel.scaleBy(1.2f);
		creditsLabel.setPosition(Constant.UI_WIDTH / 2 - (creditsLabel.getWidth() * creditsLabel.getFontScaleX() / 2), 10);
		creditsLabel.setOriginX(creditsLabel.getWidth() / 2);
		creditsLabel.setOriginY(creditsLabel.getHeight() / 2);
		this.stage.addActor(creditsLabel);
	}

	@Override
	public void show() {
		this.buildStage();
		Gdx.input.setInputProcessor(stage);
		if (!mainMusic.isPlaying()) {
			mainMusic.play();
		}
	}

	@Override
	public void render(float delta) {
		
		blinkStartTime += delta;
		scaleTime += delta;
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		scaleFrom.lerp(scaleTo, delta);
		if (scaleTime >= SCALE_INTERVAL) {
			scaleTime = 0;
			scaleTo.x = scaleTo.x == MAX_SCALE ? MIN_SCALE : MAX_SCALE;
		}
		
		if (blinkStartTime >= BLINK_START_INTERVAL) {
			blinkStartTime = 0;
			blinkStartColor = blinkStartColor == Color.RED ? Color.WHITE : Color.RED;
			startBtn.setColor(blinkStartColor);
		}
		
		maze.getBatch().begin();
		maze.getBatch().draw(backgroundRegion, (stage.getWidth() / 2) - (Constant.UI_WIDTH / 2), (stage.getHeight() / 2) - (Constant.UI_HEIGHT / 2), Constant.UI_WIDTH / 2, Constant.UI_HEIGHT / 2, Constant.UI_WIDTH, Constant.UI_HEIGHT, 1 + scaleFrom.x, 1 + scaleFrom.x, 270.0f, false);
		glyphLayout.setText(gameTitle, "PUMPKIN DOG KILLER");
		gameTitle.draw(maze.getBatch(), glyphLayout, (stage.getWidth() / 2) - (glyphLayout.width / 2), (stage.getHeight() / 2) + (glyphLayout.height * 3));
		maze.getBatch().end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		this.stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		startBtn = null;
		rankingBtn = null;
		exiBtn = null;
	}
}