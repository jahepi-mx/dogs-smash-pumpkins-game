package com.jahepi.maze.net;

import com.badlogic.gdx.utils.Array;

public interface RankingListener {
	void onSaveRanking();
	void onGetRanking(Array<Score> scores);
	void onErrorSaveRanking();
	void onErrorGetRanking();
}