package com.jahepi.maze.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jahepi.maze.Maze;
import com.jahepi.maze.ads.AdListener;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Maze(new AdListener() {
			@Override
			public void show(boolean active) {

			}

			@Override
			public void showInterstitial() {

			}
		}), config);
	}
}
