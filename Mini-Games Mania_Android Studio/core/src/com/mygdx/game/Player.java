package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Rectangle;


public class Player {
	//stores player's x and y value
	//stores the vine that the current player is on
	boolean onPlatform;
	Texture startPlayer;
	Texture[] jumpTextures;
	Texture currentFrame;
	float x,y;
	Vine vine;
	
	int frame = 0;
	
	Animation<Texture> jumpAnimation;
	
	//constructor method
	public Player(String name, int n, float x, float y){
		
		onPlatform = true;
		startPlayer = new Texture(String.format("%s%d.png", name,0));
		
		jumpTextures = new Texture[n-1];
		//don't include first image in play
		
		for(int i=0; i<jumpTextures.length; i++){
			String pic = String.format("%s%d.png",name,i+1);
			jumpTextures[i] = new Texture(pic);
		}
		
		//player = new Sprite(playerTextures[0]);
		//player.setCenter(100, 100);
		
		jumpAnimation = new Animation<Texture>(0.12f, jumpTextures);
		
		//starting position on platform
		this.x = x;
		this.y = y;
		
		//player.setPosition(x, y);
	}
	
	public void setPlatform(boolean onPlat){
		onPlatform = onPlat;
	}
	
	public void setVine(Vine v){
		vine = v;
	}
	
	
	public boolean onVine(){
		if(vine!=null){
			return true;
		}
		
		return false;
	}
	
	public Vine getVine(){
		return vine;
	}
	
	//Method overloading	
	public void setPos(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	
	public void setPos(boolean right){
		float width = jumpTextures[0].getWidth();
		float height = jumpTextures[0].getHeight();
		if(vine.getRotation()>0){
			if(right){
				x = (float)(vine.getX()-width*0.72-vine.getHeight()*Math.cos(Math.toRadians(vine.getRotation()-88)));
				y = (float)(vine.getY()-height*0.72-vine.getHeight()*Math.sin(Math.toRadians(vine.getRotation()-88)));
			}
			
			else{
				x = (float)(vine.getX()-width*0.72-vine.getHeight()*Math.cos(Math.toRadians(vine.getRotation()-92)));
				y = (float)(vine.getY()-height*0.72-vine.getHeight()*Math.sin(Math.toRadians(vine.getRotation()-92)));
			}
		}
		
		else{
			if(right){
				x = (float)(vine.getX()-width*0.72-vine.getHeight()*Math.cos(Math.toRadians(vine.getRotation()-92)));
				y = (float)(vine.getY()-height*0.72-vine.getHeight()*Math.sin(Math.toRadians(vine.getRotation()-92)));
			}
			
			else{
				x = (float)(vine.getX()-width*0.72-vine.getHeight()*Math.cos(Math.toRadians(vine.getRotation()-88)));
				y = (float)(vine.getY()-height*0.72-vine.getHeight()*Math.sin(Math.toRadians(vine.getRotation()-88)));
			}
		}
	}
	
	public void translateX(int tx){
		x+=tx;
	}
	
	
	public void render(SpriteBatch batch){
		//player.draw(batch);
		if(onPlatform){
			batch.draw(startPlayer,x,y,110,110);
		}
		else{
			batch.draw(jumpTextures[0],x,y);
		}
		
	}
	
	public void renderAnimation(float time, SpriteBatch batch){
		currentFrame = jumpAnimation.getKeyFrame(time, true);
		/*
		if(onPlatform){
			x+=100;
		}*/
		//x+=100;
		//y+=50;
		
		batch.draw(currentFrame,x,y);
	}
	
	public boolean isFinishedAnimation(float stateTime){
		
		if(jumpAnimation.isAnimationFinished(stateTime)){
			return true;
		}
		return false;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public Rectangle getPlayerRect(){
		return new Rectangle(x,y,jumpTextures[0].getWidth(),jumpTextures[0].getHeight());
	}
	
	public Rectangle getPosRect(){
		return new Rectangle(x,y,10,10);
	}
	
	public float getWidth(){
		return jumpTextures[0].getWidth();
	}
	
}
