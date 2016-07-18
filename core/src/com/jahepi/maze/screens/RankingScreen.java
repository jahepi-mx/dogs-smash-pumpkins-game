package com.jahepi.maze.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jahepi.maze.Maze;
import com.jahepi.maze.net.Ranking;
import com.jahepi.maze.net.RankingListener;
import com.jahepi.maze.net.Score;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.utils.Constant;

public class RankingScreen implements Screen, RankingListener {

	private Maze maze;
	private Stage stage;
	private SpriteBatch batch;
	private TextureRegion backgroundRegion;
	private Ranking ranking;
	private Button exitBtn;
	private Table table;
	private BitmapFont rankingFont;
	private Label statusLabel;
	private Resource resource;
	
	public RankingScreen(Maze mazeParam) {
		maze = mazeParam;
		resource = mazeParam.getResource();
		backgroundRegion = new TextureRegion(resource.getBackgroundTexture());
		
		batch = maze.getBatch();
		StretchViewport viewport = new StretchViewport(Constant.UI_WIDTH, Constant.UI_HEIGHT);
		stage = new Stage(viewport, batch);
		
		ranking = new Ranking(this);
		rankingFont = resource.getUIFont();
	}
	
	private void buildStage() {
		
		LabelStyle style = new LabelStyle();
		style.font = rankingFont;
		statusLabel = new Label("", style);
		statusLabel.setFontScale(1.0f);
		statusLabel.setColor(Color.RED);
		statusLabel.setPosition(Constant.UI_WIDTH / 2 - (statusLabel.getWidth() * statusLabel.getFontScaleX() / 2), Constant.UI_HEIGHT / 2 - (statusLabel.getHeight() * statusLabel.getFontScaleY() / 2));
		statusLabel.setAlignment(Align.center);
		
		TextureRegion region = resource.getGameAtlas().findRegion("home");
		exitBtn = new Button(new TextureRegionDrawable(region));
		exitBtn.setPosition(-30, Constant.UI_HEIGHT - (exitBtn.getHeight() + 10));
		exitBtn.setTransform(true);
		exitBtn.setOriginX(exitBtn.getWidth() / 2);
		exitBtn.setOriginY(exitBtn.getHeight() / 2);
		exitBtn.setScale(0.4f);
		
		exitBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				maze.changeToMainScreen();
			}		
		});
		Gdx.input.setInputProcessor(stage);
		table = new Table();
		stage.addActor(exitBtn);
		stage.addActor(statusLabel);
	}

	@Override
	public void show() {
		buildStage();
		this.showMessage("Loading data ...");
		ranking.getRanking();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(backgroundRegion, (stage.getWidth() / 2) - (Constant.UI_WIDTH / 2), (stage.getHeight() / 2) - (Constant.UI_HEIGHT / 2), Constant.UI_WIDTH / 2, Constant.UI_HEIGHT / 2, Constant.UI_WIDTH, Constant.UI_HEIGHT, 1.5f, 1.5f, 270.0f, false);
		batch.end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		this.stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {	
	}

	@Override
	public void resume() {		
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		exitBtn = null;
		table = null;
		statusLabel = null;
		// Gdx.input.setInputProcessor(null);
	}

	@Override
	public void onSaveRanking() {
	}

	@Override
	public void onGetRanking(Array<Score> scores) {
		if (table == null) {
			return;
		}
		table.clear();
		Iterator<Score> iterator = scores.iterator();
		int i = 0;
		
		LabelStyle style = new LabelStyle();
		style.font = rankingFont;
		
		Label labelTop = new Label("TOP 10 PLAYER LIST", style);
		labelTop.setColor(Color.WHITE);
		labelTop.setFontScale(2.0f);
		table.add(labelTop).colspan(2).pad(1.0f);
		table.row();
		
		Label label = new Label("Player", style);
		table.add(label).pad(10).left();
		label.setColor(Color.RED);
		label = new Label("Score", style);
		label.setColor(Color.RED);
		table.add(label).pad(10).center();
		
		while (iterator.hasNext()) {
			Score score = iterator.next();
			table.row();
			label = new Label((++i) + " " + score.getName(), style);
			table.add(label).pad(5).left();
			label = new Label(Integer.toString(score.getScore()), style);
			label.setColor(Color.ORANGE);
			table.add(label).pad(5).center();
		}
		statusLabel.setText("");
		stage.addActor(table);
		centerTable();
	}

	@Override
	public void onErrorSaveRanking() {	
	}

	@Override
	public void onErrorGetRanking() {
		this.showMessage("Verify your internet connection, could not retrieve scores.");
	}
	
	private void showMessage(String message) {
		statusLabel.setText(message);
	}
	
	private void centerTable() {
		table.setPosition(Constant.UI_WIDTH / 2 - (table.getWidth() / 2), Constant.UI_HEIGHT / 2 - (table.getHeight() / 2));
	}
}