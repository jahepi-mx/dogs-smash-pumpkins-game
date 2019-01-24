package com.jahepi.maze.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.jahepi.maze.utils.Constant;

public class Resource implements Disposable {
	
	private Skin skin;
	private AssetManager manager = new AssetManager();
	private TextureAtlas gameAtlas;
	private TextureAtlas heroAtlas;
	private TextureAtlas enemyAtlas;
	private Texture backgroundTexture;
	private Texture backgroundGuiTexture;
	private Music mainMusic;
	private Music playMusic;
	private Sound jumpSound;
	private Sound mushroomSound;
	private Sound hitSound;
	private Sound gameOverSound;
	private Sound enemyDieSound;
	private FreeTypeFontGenerator fontGenerator;
	private BitmapFont enemyScoreFont;
	private BitmapFont gameTitleFont;
	private BitmapFont roundFont;
	private BitmapFont fpsFont;
	private BitmapFont scoreFont;
	private BitmapFont UIFont;
	private GlyphLayout glyphLayout;
	private Animation enemyRunAnimation;
	private Animation enemyJumpAnimation;
	private Animation enemyWalkAnimation;
	private Animation enemyDeadAnimation;
	private Animation heroRunAnimation;
	private Animation heroJumpAnimation;
	private Animation heroIdleAnimation;
	private Animation heroDeadAnimation;
	private ShaderProgram monochromeShader;

	public Resource() {
		
		skin = new Skin(Gdx.files.internal(Constant.SKIN_PATH));
		backgroundTexture = new Texture(Gdx.files.internal("BG.png"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		backgroundGuiTexture = new Texture(Gdx.files.internal("GUIBG.png"));
		backgroundGuiTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		manager.load("game.pack", TextureAtlas.class);
		manager.load("hero.pack", TextureAtlas.class);
		manager.load("enemy.pack", TextureAtlas.class);
		manager.finishLoading();
		heroAtlas = manager.get("hero.pack");
		enemyAtlas = manager.get("enemy.pack");
		gameAtlas = manager.get("game.pack");
		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/main.mp3"));
	    playMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/gameplay.mp3"));
	    jumpSound = Gdx.audio.newSound(Gdx.files.internal("sound/jump.wav"));
	    mushroomSound = Gdx.audio.newSound(Gdx.files.internal("sound/mushroom.wav"));
	    hitSound = Gdx.audio.newSound(Gdx.files.internal("sound/hit.wav"));
	    gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sound/death.wav"));
	    enemyDieSound = Gdx.audio.newSound(Gdx.files.internal("sound/mutantdie.wav"));
	    mainMusic.setLooping(true);
	    playMusic.setLooping(true);
	    
	    enemyRunAnimation = new Animation(1.0f/6.0f, getEnemyAtlas().findRegions("Run"), Animation.PlayMode.LOOP);
		enemyJumpAnimation = new Animation(1.0f/6.0f, getEnemyAtlas().findRegions("Jump"), Animation.PlayMode.LOOP);
		enemyWalkAnimation = new Animation(1.0f/8.0f, getEnemyAtlas().findRegions("Walk"), Animation.PlayMode.LOOP);
		enemyDeadAnimation = new Animation(1.0f/6.0f, getEnemyAtlas().findRegions("Dead"), Animation.PlayMode.NORMAL);
		
		heroRunAnimation = new Animation(1.0f/8.0f, getHeroAtlas().findRegions("Run"), Animation.PlayMode.LOOP);
		heroJumpAnimation = new Animation(1.0f/8.0f, getHeroAtlas().findRegions("Jump"), Animation.PlayMode.LOOP);
		heroIdleAnimation = new Animation(1.0f/6.0f, getHeroAtlas().findRegions("Idle"), Animation.PlayMode.LOOP);
		heroDeadAnimation = new Animation(1.0f/6.0f, getHeroAtlas().findRegions("Dead"), Animation.PlayMode.NORMAL);
		
	    fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/game.ttf"));
	    
	    FreeTypeFontParameter parameters = new FreeTypeFontParameter();
	    parameters.size = 150;
	    parameters.shadowOffsetX = 1;
	    parameters.shadowOffsetY = 1;
	    parameters.borderWidth = 5;
	    parameters.borderColor = Color.ORANGE;
	    parameters.color = Color.WHITE;
	    gameTitleFont = fontGenerator.generateFont(parameters);
	    
	    FreeTypeFontParameter parameters2 = new FreeTypeFontParameter();
	    parameters2.size = 40;
	    parameters2.shadowOffsetX = 1;
	    parameters2.shadowOffsetY = 1;
	    parameters2.color = Color.WHITE;
	    UIFont = fontGenerator.generateFont(parameters2);
	    
	    FreeTypeFontParameter parameters3 = new FreeTypeFontParameter();
	    parameters3.size = 250;
	    parameters3.shadowOffsetX = 1;
	    parameters3.shadowOffsetY = 1;
	    parameters3.color = Color.WHITE;
	    roundFont = fontGenerator.generateFont(parameters3);
	    
	    FreeTypeFontParameter parameters4 = new FreeTypeFontParameter();
	    parameters4.size = 300;
	    parameters4.shadowOffsetX = 3;
	    parameters4.shadowOffsetY = 3;
	    parameters4.color = Color.ORANGE;
	    scoreFont = fontGenerator.generateFont(parameters4);
	    
	    FreeTypeFontParameter parameters5 = new FreeTypeFontParameter();
	    parameters5.size = 120;
	    parameters5.color = Color.GREEN;
		parameters5.shadowOffsetX = 2;
		parameters5.shadowOffsetY = 2;
	    fpsFont = fontGenerator.generateFont(parameters5);
	    
	    FreeTypeFontParameter parameters6 = new FreeTypeFontParameter();
	    parameters6.size = 80;
	    parameters6.shadowOffsetX = 1;
	    parameters6.shadowOffsetY = 1;
	    parameters6.color = Color.GREEN;
		enemyScoreFont = fontGenerator.generateFont(parameters6);
	    
	    glyphLayout = new GlyphLayout();
	    
	    monochromeShader = new ShaderProgram(Gdx.files.internal("shader/monochrome.vs"), Gdx.files.internal("shader/monochrome.fs"));
	}

	public Skin getSkin() {
		return skin;
	}
	
	public TextureAtlas getHeroAtlas() {
		return heroAtlas;
	}

	public void setHeroAtlas(TextureAtlas heroAtlas) {
		this.heroAtlas = heroAtlas;
	}
	
	public TextureAtlas getEnemyAtlas() {
		return enemyAtlas;
	}

	public void setEnemyAtlas(TextureAtlas enemyAtlas) {
		this.enemyAtlas = enemyAtlas;
	}

	public Texture getBackgroundTexture() {
		return backgroundTexture;
	}

	public void setBackgroundTexture(Texture backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}

	public TextureAtlas getGameAtlas() {
		return gameAtlas;
	}

	public void setGameAtlas(TextureAtlas gameAtlas) {
		this.gameAtlas = gameAtlas;
	}

	public Texture getBackgroundGuiTexture() {
		return backgroundGuiTexture;
	}

	public void setBackgroundGuiTexture(Texture backgroundGuiTexture) {
		this.backgroundGuiTexture = backgroundGuiTexture;
	}

	public BitmapFont getEnemyScoreFont() {
		return enemyScoreFont;
	}

	public void setEnemyScoreFont(BitmapFont enemyScoreFont) {
		this.enemyScoreFont = enemyScoreFont;
	}

	public Music getMainMusic() {
		return mainMusic;
	}

	public void setMainMusic(Music mainMusic) {
		this.mainMusic = mainMusic;
	}

	public Music getPlayMusic() {
		return playMusic;
	}

	public void setPlayMusic(Music playMusic) {
		this.playMusic = playMusic;
	}

	public Sound getJumpSound() {
		return jumpSound;
	}

	public void setJumpSound(Sound jumpSound) {
		this.jumpSound = jumpSound;
	}

	public Sound getMushroomSound() {
		return mushroomSound;
	}

	public void setMushroomSound(Sound mushroomSound) {
		this.mushroomSound = mushroomSound;
	}

	public Sound getHitSound() {
		return hitSound;
	}

	public void setHitSound(Sound hitSound) {
		this.hitSound = hitSound;
	}

	public Sound getGameOverSound() {
		return gameOverSound;
	}

	public void setGameOverSound(Sound gameOverSound) {
		this.gameOverSound = gameOverSound;
	}

	public Sound getEnemyDieSound() {
		return enemyDieSound;
	}

	public void setEnemyDieSound(Sound enemyDieSound) {
		this.enemyDieSound = enemyDieSound;
	}

	public BitmapFont getGameTitleFont() {
		return gameTitleFont;
	}

	public void setGameTitleFont(BitmapFont gameTitleFont) {
		this.gameTitleFont = gameTitleFont;
	}

	public GlyphLayout getGlyphLayout() {
		return glyphLayout;
	}

	public void setGlyphLayout(GlyphLayout glyphLayout) {
		this.glyphLayout = glyphLayout;
	}

	public BitmapFont getRoundFont() {
		return roundFont;
	}

	public void setRoundFont(BitmapFont roundFont) {
		this.roundFont = roundFont;
	}

	public BitmapFont getFpsFont() {
		return fpsFont;
	}

	public void setFpsFont(BitmapFont fpsFont) {
		this.fpsFont = fpsFont;
	}

	public BitmapFont getScoreFont() {
		return scoreFont;
	}

	public void setScoreFont(BitmapFont scoreFont) {
		this.scoreFont = scoreFont;
	}

	public Animation getEnemyRunAnimation() {
		return enemyRunAnimation;
	}

	public void setEnemyRunAnimation(Animation enemyRunAnimation) {
		this.enemyRunAnimation = enemyRunAnimation;
	}

	public Animation getEnemyJumpAnimation() {
		return enemyJumpAnimation;
	}

	public void setEnemyJumpAnimation(Animation enemyJumpAnimation) {
		this.enemyJumpAnimation = enemyJumpAnimation;
	}

	public Animation getEnemyWalkAnimation() {
		return enemyWalkAnimation;
	}

	public void setEnemyWalkAnimation(Animation enemyWalkAnimation) {
		this.enemyWalkAnimation = enemyWalkAnimation;
	}

	public Animation getEnemyDeadAnimation() {
		return enemyDeadAnimation;
	}

	public void setEnemyDeadAnimation(Animation enemyDeadAnimation) {
		this.enemyDeadAnimation = enemyDeadAnimation;
	}

	public Animation getHeroRunAnimation() {
		return heroRunAnimation;
	}

	public void setHeroRunAnimation(Animation heroRunAnimation) {
		this.heroRunAnimation = heroRunAnimation;
	}

	public Animation getHeroJumpAnimation() {
		return heroJumpAnimation;
	}

	public void setHeroJumpAnimation(Animation heroJumpAnimation) {
		this.heroJumpAnimation = heroJumpAnimation;
	}

	public Animation getHeroIdleAnimation() {
		return heroIdleAnimation;
	}

	public void setHeroIdleAnimation(Animation heroIdleAnimation) {
		this.heroIdleAnimation = heroIdleAnimation;
	}

	public Animation getHeroDeadAnimation() {
		return heroDeadAnimation;
	}

	public void setHeroDeadAnimation(Animation heroDeadAnimation) {
		this.heroDeadAnimation = heroDeadAnimation;
	}
	
	public ShaderProgram getMonochromeShader() {
		return monochromeShader;
	}

	public void setMonochromeShader(ShaderProgram monochromeShader) {
		this.monochromeShader = monochromeShader;
	}

	public BitmapFont getUIFont() {
		return UIFont;
	}

	public void setRankingFont(BitmapFont UIFont) {
		this.UIFont = UIFont;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		skin.dispose();
		backgroundTexture.dispose();
		backgroundGuiTexture.dispose();
		gameAtlas.dispose();
		heroAtlas.dispose();
		enemyAtlas.dispose();
		manager.dispose();
		enemyScoreFont.dispose();
		mainMusic.dispose();
		playMusic.dispose();
		jumpSound.dispose();
		mushroomSound.dispose();
		hitSound.dispose();
		gameOverSound.dispose();
		enemyDieSound.dispose();
		fontGenerator.dispose();
		gameTitleFont.dispose();
		roundFont.dispose();
		fpsFont.dispose();
		scoreFont.dispose();
		UIFont.dispose();
		glyphLayout = null;
		monochromeShader.dispose();
	}
}