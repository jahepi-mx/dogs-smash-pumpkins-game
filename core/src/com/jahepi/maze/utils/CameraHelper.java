package com.jahepi.maze.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {

	private final static float MAX_ZOOM = 1.5f;
	private final static float MIN_ZOOM = 1.0f;
	private final static float ZOOM_INTERVAL = 0.1f;
	
	private Vector2 zoom;
	private Vector2 zoomTo;
	private float rotation;
	private Vector2 position;
	
	public CameraHelper() {
		// TODO Auto-generated constructor stub
		position = new Vector2();
		zoom = new Vector2(MIN_ZOOM, 0);
		zoomTo = new Vector2(MIN_ZOOM, 0);
	}
	
	public void setPosition(float x, float y) {
		Vector2 tmpPosition = new Vector2(x, y);
		position.lerp(tmpPosition, 0.02f);
	}
	
	public void updateZoom() {
		zoom.lerp(zoomTo, 0.02f);
	}
	
	public void zoomIn() {
		this.zoomTo.x -= ZOOM_INTERVAL;
		this.zoomTo.x = MathUtils.clamp(this.zoomTo.x, MIN_ZOOM, MAX_ZOOM);
	}
	
	public void zoomOut() {
		this.zoomTo.x += ZOOM_INTERVAL;
		this.zoomTo.x = MathUtils.clamp(this.zoomTo.x, MIN_ZOOM, MAX_ZOOM);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}

	public Vector2 getZoom() {
		return zoom;
	}

	public float getRotation() {
		return rotation;
	}	
}
