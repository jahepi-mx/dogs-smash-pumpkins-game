package com.jahepi.maze.levels;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Level {

	private Array<Tile[][]> levels;
	
	public class Tile {
		
		private String image;
		private boolean flipX;
		
		public Tile() {}
		
		public Tile(String image, boolean flipX) {
			this.image = image;
			this.flipX = flipX;
		}

		public String getImage() {
			return image;
		}

		public Tile setImage(String image) {
			this.image = image;
			return this;
		}

		public boolean isFlipX() {
			return flipX;
		}

		public Tile setFlipX(boolean flipX) {
			this.flipX = flipX;
			return this;
		}
	}
	
	public class Block extends Tile {
		public Block(String image, boolean flipX) {
			super(image, flipX);
		}
	}
	
	public class Slope extends Tile {
		public Slope(String image, boolean flipX) {
			super(image, flipX);
		}
	}
	
	public class Hero extends Tile {
		public Hero(String image, boolean flipX) {
			super(image, flipX);
		}
	}
	
	public class Empty extends Tile {
	}
	
	public class None extends Tile {
		public None(String image, boolean flipX) {
			super(image, flipX);
		}
	}
	
	public class Crate extends Tile {
		public Crate(String image, boolean flipX) {
			super(image, flipX);
		}
	}
	
	public class Mushroom extends Tile {
		public Mushroom(String image, boolean flipX) {
			super(image, flipX);
		}
	}
	
	public class StaticCrate extends Tile {
		public StaticCrate(String image, boolean flipX) {
			super(image, flipX);
		}
	}
	
	public class Door extends Tile {
		private Vector2 goTo;
		public Door(String image, boolean flipX, float x, float y) {
			super(image, flipX);
			this.goTo = new Vector2(x, y);
		}
		public Vector2 getGoTo() {
			return goTo;
		}

		public void setGoTo(float x, float y) {
			this.goTo.x = x;
			this.goTo.y = y;
		}
	}
	
	private Tile empty = new Empty();
	private Tile sand1 = new Block("sand1", false);
	private Tile sand2 = new Block("sand2", false);
	private Tile sand3 = new Block("sand3", false);
	private Tile sand3Flip = new Block("sand3", true);
	private Tile sand4 = new Block("sand4", false);
	private Tile sand5 = new Block("sand5", false);
	private Tile sand4Flip = new Block("sand4", true);
	private Tile sand7 = new Block("sand7", false);
	private Tile sand7Flip = new Block("sand7", true);
	private Tile sand8 = new Block("sand8", false);
	private Tile sand9 = new Block("sand9", false);
	private Tile sand9Flip = new Block("sand9", true);
	private Tile sand10 = new Block("sand10", false);
	private Tile slope = new Slope("sand12", false);
	private Tile slopeFlip = new Slope("sand12", true);
	private Tile hero = new Hero("hero", false);
	private Tile crate = new Crate("crate", false);
	private Tile staticCrate = new StaticCrate("crate", false);
	private Tile mushroom = new Mushroom("mushroom2", false);
	private Tile door1 = new Door("sign", true, 6, 6);
	private Tile door2 = new Door("sign", false, 1, 2);
	private Tile door3 = new Door("sign", false, 11, 2);
	private Tile door4 = new Door("sign", false, 9, 7);
	private Tile door5 = new Door("sign", false, 15, 2);
	private Tile door6 = new Door("sign", false, 11, 7);
	private Tile none = new None("sand5", false);
	
	private Tile[][] level1 = {
		{sand4,  sand10, sand10, sand10, sand10,     sand10,  sand10,     sand4Flip},
		{sand4,  empty,  empty,  empty,  empty,      empty,   empty,      sand4Flip},
		{sand4,  empty,  empty,  empty,  empty,      empty,   empty,      door2},
		{sand4,  empty,  sand7,  sand8,  sand7Flip,  empty,   sand7,      sand4Flip},
		{sand4,  empty,  crate,  empty,  empty,      empty,   empty,      sand4Flip},
		{sand4,  empty,  empty,  empty,  empty,      empty,   mushroom,   sand4Flip},
		{door1,  hero,   slope,  sand5,  slopeFlip,  empty,   empty,      sand4Flip},
		{sand4,  sand3,  sand2,  sand2,  sand2,      sand2,   sand3Flip,  sand4Flip},	
		{sand9,  sand10, sand10, sand10, sand10,     sand10,  sand10,     sand9Flip}
	};
	
	private Tile[][] level2 = {
		{sand4,  sand10, sand10, sand10,       sand10,     sand10,        sand10,         sand10,        sand10,     sand10,      sand10,sand10,     sand4Flip},
		{sand4,  empty,  empty,  empty,        empty,      empty,         empty,          empty,         empty,      empty,       empty, empty,      sand4Flip},
		{sand4,  empty,  empty,  empty,        empty,      empty,         empty,          empty,         empty,      empty,       empty, empty,      sand4Flip},
		{sand4,  empty,  empty,  empty,        empty,      empty,         empty,          empty,         empty,      empty,       empty, empty,      sand4Flip},
		{sand4,  empty,  crate,  empty,        crate,      empty,         mushroom,       crate,         empty,      empty,       empty, empty,      sand4Flip},
		{sand4,  empty,  empty,  empty,        empty,      empty,         empty,          empty,         empty,      empty,       empty, empty,      sand4Flip},
		{door3,  hero,   empty,  staticCrate,  empty,      empty,         staticCrate,    empty,         empty,      staticCrate, empty, empty,      door2},
		{sand4,  sand3,  sand2,  sand2,  	   sand2,      sand2,         sand2,          sand2,         sand2,      sand2,       sand2, sand3Flip,  sand4Flip},
		{sand9,  sand10, sand10, sand10,       sand10,     sand10,        sand10,         sand10,        sand10,     sand10,      sand10,sand10,     sand9Flip}
	};

	private Tile[][] level3 = {
			{sand4,  sand10, sand10, sand10,       sand10,     sand10,        sand10,         sand10,        sand10,      	sand10, 	 sand10,     	sand10, sand10, 	sand4Flip},
			{sand4,  empty,  empty,  empty,        empty,      empty,         empty,    	  empty,         empty,       	empty, 		 door2, 		empty,  empty, 		sand4Flip},
			{sand4,  empty,  empty,  empty,        empty,      empty,         mushroom,    	  empty,         sand7,  	    sand8,       sand7Flip,  	empty,  empty, 		sand4Flip},
			{sand4,  empty,  empty,  hero,         empty,      empty,         empty,    	  empty,         empty,       	empty, 		 empty,  		empty,  empty, 		sand4Flip},
			{sand4,  empty,  empty,  crate,        empty,      empty,         empty,    	  staticCrate,   empty,       	empty, 		 empty,  		empty,  empty, 		sand4Flip},
			{sand4,  empty,  empty,  crate,        empty,      empty,         staticCrate,    staticCrate,   staticCrate, 	empty,       empty,  		empty,  empty, 		sand4Flip},
			{door4,  empty,  empty,  empty,  	   empty,      staticCrate,   staticCrate,    staticCrate,   staticCrate, 	staticCrate, empty,  		empty,  empty, 		sand4Flip},
			{sand4,  sand3,  sand2,  sand2,  	   sand2,      sand2,         sand2,    	  sand2,         sand2,   		sand2, 	   	 sand2,  		sand2,  sand3Flip, 	sand4Flip},
			{sand9,  sand10, sand10, sand10,       sand10,     sand10,        sand10,   	  sand10,        sand10,      	sand10,      sand10, 		sand10, sand10, 	sand9Flip}
	};

	private Tile[][] level4 = {
			{sand4,  sand10,     sand10,     sand10,       sand10,     sand10,        sand10,         sand10,        sand10,      	sand10, 	 sand10, sand10, sand10, sand10,     	sand10,       sand10, 	    sand4Flip},
			{sand4,  empty,      empty,      empty,        empty,      empty,         empty,    	  empty,         empty,       	empty, 		 empty,  empty,  empty,  empty, 		empty,        empty, 		sand4Flip},
			{sand4,  empty,      empty,      empty,        empty,      empty,         empty,    	  empty,         empty,       	empty, 		 empty,  empty,  empty,  empty, 		empty,        empty, 		sand4Flip},
			{sand4,  empty,      empty,      empty,        empty,      empty,         empty,    	  empty,         empty,       	empty, 		 empty,  empty,  empty,  empty, 		empty,        empty, 		sand4Flip},
			{sand4,  empty,      empty,      empty,        empty,      mushroom,      empty,    	  empty,         empty,  	    empty,       empty,  empty,  empty,  empty,  	    empty,        empty, 		sand4Flip},
			{sand4,  empty,      hero,       empty,        empty,      empty,         empty,    	  empty,         empty,       	staticCrate, empty,  empty,  empty,  empty,  		empty,        empty, 		sand4Flip},
			{sand4,  empty,      empty,      empty,        empty,      empty,         empty,    	  empty,         staticCrate,   sand5, 		 empty,  crate,  empty,  empty, 		empty,  	  empty, 		sand4Flip},
			{sand4,  empty,      sand7,      sand8,        sand7Flip,  empty,         empty,          staticCrate,   sand5, 	    sand5,       empty,  empty,  empty,  empty,  		empty,  	  empty, 	    sand4Flip},
			{door5,  empty,      empty,      empty,  	   empty,      empty,         staticCrate,    sand5,         sand5, 	    sand5,       empty,  empty,  slope,  sand5,         slopeFlip,    empty, 	    door2},
			{sand4,  sand3,      sand2,      sand2,  	   sand2,      sand2,         sand2,    	  sand2,         sand2,   		sand2, 	   	 sand2,  sand2,  sand2,  sand2, 		sand2,  	  sand3Flip, 	sand4Flip},
			{sand9,  sand10,     sand10,     sand10,       sand10,     sand10,        sand10,   	  sand10,        sand10,      	sand10,      sand10, sand10, sand10, sand10,		sand10, 	  sand10, 	    sand9Flip}
	};

	private Tile[][] level5 = {
			{sand4,  sand10, sand10,     sand10,       sand10,     sand10,        sand10,         sand10,        sand10,      	sand10, 	 sand10,     	sand10,       sand10, 	    sand4Flip},
			{sand4,  empty,  empty,      empty,        empty,      empty,         empty,    	  empty,         empty,       	empty, 		 empty, 		empty,        door2, 		sand4Flip},
			{sand4,  empty,  empty,      empty,        empty,      empty,         empty,    	  empty,         empty,  	    empty,       sand7,  	    sand8,        sand7Flip, 	sand4Flip},
			{sand4,  empty,  empty,      empty,        empty,      empty,         empty,    	  empty,         empty,         empty, 		 mushroom,  	empty,        empty, 		sand4Flip},
			{sand4,  empty,  empty,      hero,         empty,      slope,         sand5,    	  slopeFlip,     empty,         empty, 		 empty,  		empty,  	  empty, 		sand4Flip},
			{sand4,  empty,  empty,      crate,        slope,      sand5,         sand5,          sand5,         staticCrate, 	empty,       empty,  		empty,  	  slope, 	    sand4Flip},
			{door6,  empty,  empty,      slope,  	   sand5,      sand5,         sand5,          sand5,         sand5, 	    slopeFlip,   crate,         slope,        sand5, 		sand4Flip},
			{sand4,  sand3,  sand2,      sand2,  	   sand2,      sand2,         sand2,    	  sand2,         sand2,   		sand2, 	   	 sand2,  		sand2,  	  sand3Flip, 	sand4Flip},
			{sand9,  sand10, sand10,     sand10,       sand10,     sand10,        sand10,   	  sand10,        sand10,      	sand10,      sand10, 		sand10, 	  sand10, 	    sand9Flip}
	};

	public Level() {
		this.levels = new Array<Tile[][]>();
		this.levels.add(level1);
		this.levels.add(level2);
		this.levels.add(level3);
		this.levels.add(level4);
		this.levels.add(level5);
	}
	
	public Tile[][] getLevel() {
		int index = MathUtils.random(0, levels.size - 1);
		return this.levels.get(index);
	}

}
