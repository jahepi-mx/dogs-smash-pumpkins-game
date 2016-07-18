package com.jahepi.maze.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jahepi.maze.Maze;
import com.jahepi.maze.MazeRender;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.screens.interfaces.OnExitListener;
import com.jahepi.maze.utils.Constant;

public class PlayScreen implements Screen, OnExitListener {

	private Maze maze;
	private Stage stage;
	private MazeRender renderObj;
	private Music playMusic;
	private Music mainMusic;
	
	public PlayScreen(Maze mazeParam) {
		maze = mazeParam;
		StretchViewport viewport = new StretchViewport(Constant.UI_WIDTH, Constant.UI_HEIGHT);
		stage = new Stage(viewport, maze.getBatch());
		renderObj = new MazeRender(stage, this);
		playMusic = Resource.getInstance().getPlayMusic();
		mainMusic = Resource.getInstance().getMainMusic();
	}

	@Override
	public void show() {
		mainMusic.stop();
		if (!playMusic.isPlaying()) {
			playMusic.play();
		}
		renderObj.start();
	}

	@Override
	public void render(float delta) {
		this.renderObj.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height);
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
		playMusic.stop();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		renderObj.dispose();
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		maze.changeToMainScreen();
	}

}