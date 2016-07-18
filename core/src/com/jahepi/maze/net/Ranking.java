package com.jahepi.maze.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;

public class Ranking {
	
	private final static String URL = "http://blog.jahepi.net/";
	
	private RankingListener listener;
	
	public Ranking(RankingListener listener) {
		this.listener = listener;
	}
	
	public boolean saveRanking(String name, int score) {
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl(URL + "save.php");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.setContent("name=" + name + "&score=" + score);
		Gdx.net.sendHttpRequest(httpPost, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				listener.onSaveRanking();
			}
			
			@Override
			public void failed(Throwable t) {
				listener.onErrorSaveRanking();
			}
			
			@Override
			public void cancelled() {
				
			}
		});
		return true;
	}
	
	public void getRanking() {
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl(URL + "get.php");
		httpPost.setHeader("Content-Type", "application/json");
		Gdx.net.sendHttpRequest(httpPost, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				String data = httpResponse.getResultAsString();
				JsonValue json = new JsonReader().parse(data);
				JsonValue scores = json.get("scores");
				JsonIterator iterator = scores.iterator();
				Array<Score> scoresCol = new Array<Score>();
				while (iterator.hasNext()) {
					JsonValue value = iterator.next();
					Score score = new Score();
					score.setName(value.getString("name"));
					score.setScore(value.getInt("score"));
					scoresCol.add(score);
				}
				listener.onGetRanking(scoresCol);
			}
			
			@Override
			public void failed(Throwable t) {
				listener.onErrorGetRanking();
			}
			
			@Override
			public void cancelled() {
				
			}
		});
	}
}