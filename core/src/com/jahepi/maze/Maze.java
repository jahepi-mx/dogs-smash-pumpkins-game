package com.jahepi.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jahepi.maze.ads.AdListener;
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
	private AdListener adListener;
	private Resource resource;

	public Maze(AdListener adListener) {
		this.adListener = adListener;
	}
	
	@Override
	public void create () {
		resource = new Resource();
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
		resource.dispose();
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
		this.adListener.show(true);
		this.setScreen(mainScreen);
		disposeLastScreen();
		currentScreen = mainScreen;
	}
	
	public void changeToPlayScreen() {
		this.adListener.show(true);
		this.setScreen(playScreen);
		disposeLastScreen();
		currentScreen = playScreen;
	}
	
	public void changeToRankingScreen() {
		this.adListener.show(true);
		this.setScreen(rankingScreen);
		disposeLastScreen();
		currentScreen = rankingScreen;
	}
	
	private void disposeLastScreen() {
		if (currentScreen != null) {
			currentScreen.dispose();
		}
	}

	public Resource getResource() {
		return resource;
	}
}
