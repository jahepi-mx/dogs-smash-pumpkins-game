package com.jahepi.maze.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.jahepi.maze.resources.Resource;
import com.jahepi.maze.utils.Constant;

public class FinishUI implements Disposable {

	public interface FinishUIListener {
		void onTryAgain();
		void onGoToMain();
		void onSendScoreWorldWide(String name, int score);
	}
	
	private Table table;
	private Label scoreLabel;
	private Label statusLabel;
	private int score;
	private TextField textField;
	private FinishUIListener listener;
	private boolean sendingData;
	private Resource resource;
	
	public FinishUI(FinishUIListener listenerParam, Resource resource) {
		this.listener = listenerParam;
		this.resource = resource;
		LabelStyle style0 = new LabelStyle();
		BitmapFont scoreFont = resource.getScoreFont();
		style0.font = scoreFont;
		scoreLabel = new Label("", style0);
		scoreLabel.setColor(Color.WHITE);
		LabelStyle style1 = new LabelStyle();
		BitmapFont uiFont = resource.getUIFont();
		style1.font = uiFont;
		
		Label labelMain = new Label("Go To Main", style1);
		labelMain.setColor(Color.RED);
		labelMain.setFontScale(2.0f);
		Button goToMain = new Button(resource.getSkin());
		goToMain.add(labelMain);
		
		goToMain.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				listener.onGoToMain();
			}
		});
		
		Label labelTry = new Label("Try Again", style1);
		labelTry.setColor(Color.YELLOW);
		labelTry.setFontScale(2.0f);
		Button tryAgain = new Button(resource.getSkin());
		tryAgain.add(labelTry);
		
		tryAgain.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				listener.onTryAgain();
			}
		});
		
		Label myName = new Label("Nickname:", style1);
		myName.setFontScale(2.0f);
		myName.setColor(Color.WHITE);
		
		Label publishLabel = new Label("Publish Score", style1);
		publishLabel.setFontScale(1.5f);
		publishLabel.setColor(Color.WHITE);
		
		statusLabel = new Label("", style1);
		statusLabel.setColor(Color.RED);
		
		textField = new TextField("", resource.getSkin());
		Button saveBtn = new Button(resource.getSkin());
		saveBtn.add(publishLabel);
		saveBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!sendingData) {
					if (getName().equals("")) {
						setStatus("The name is required!");
						return;
					}
					sendingData = true;
					listener.onSendScoreWorldWide(getName(), score);
				}
			}			
		});

		textField.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.getTextInput(new Input.TextInputListener() {
					@Override
					public void input(String text) {
						textField.setText(text);
					}

					@Override
					public void canceled() {

					}
				}, "Set your name to publish", "", "");
			}
		});
		
		table = new Table();
		table.center();
		table.add(scoreLabel).colspan(2).center();
		table.row();
		table.add(goToMain).pad(15).center();
		table.add(tryAgain).pad(15).center();
		table.row().pad(15);
		table.add(myName).left();
		table.add(textField).width(230).height(50).left();
		table.row();
		table.add();
		table.add(saveBtn).center();
		table.row().pad(15);
		table.add(statusLabel).colspan(2).left();
		
		table.setPosition(Constant.UI_WIDTH / 2 - (table.getWidth() / 2), Constant.UI_HEIGHT / 2 - (table.getHeight() / 2));
	}


	public void setScore(int score) {
		this.score = score;
		this.scoreLabel.setText("SCORE: " + score);
	}
	
	public String getName() {
		return this.textField.getText();
	}
	
	public Table getTable() {
		return this.table;
	}
	
	public void setVisible(boolean visible) {
		this.table.setVisible(visible);
	}
	
	public void reset() {
		sendingData = false;
		statusLabel.setText("");
		textField.setText("");
		setVisible(false);
	}
	
	public void setStatus(String status) {
		this.statusLabel.setText(status);
	}

	public boolean isSendingData() {
		return sendingData;
	}

	public void setSendingData(boolean sendingData) {
		this.sendingData = sendingData;
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		table = null;
		scoreLabel = null;
		statusLabel = null;
		textField = null;
		listener = null;
	}
}