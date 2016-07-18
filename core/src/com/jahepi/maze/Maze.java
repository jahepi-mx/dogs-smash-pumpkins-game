package com.jahepi.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.screens.MainScreen;
import com.jahepi.maze.screens.PlayScreen;
import com.jahepi.maze.screens.RankingScreen;

public class Maze extends Game {
	
	private SpriteBatch batch;
	private boolean isPaused;
	
	// Screens
	private MainScreen mainScreen;
	private PlayScreen playScreen;
	private RankingScreen rankingScreen;
	private Screen currentScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		mainScreen = new MainScreen(this);
		playScreen = new PlayScreen(this);
		rankingScreen = new RankingScreen(this);
		this.changeToMainScreen();
	}
	
	@Override
	public void dispose() {
		disposeLastScreen();
		batch.dispose();
		Resource.getInstance().dispose();
	}

	@Override
	public void pause() {
		isPaused = true;
	}

	@Override
	public void resume() {
		isPaused = false;
	}

	@Override
	public void resize(int width, int height) {
		this.screen.resize(width, height);
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public void changeToMainScreen() {
		this.setScreen(mainScreen);
		disposeLastScreen();
		currentScreen = mainScreen;
	}
	
	public void changeToPlayScreen() {
		this.setScreen(playScreen);
		disposeLastScreen();
		currentScreen = playScreen;
	}
	
	public void changeToRankingScreen() {
		this.setScreen(rankingScreen);
		disposeLastScreen();
		currentScreen = rankingScreen;
	}
	
	private void disposeLastScreen() {
		if (currentScreen != null) {
			currentScreen.dispose();
		}
	}
}
