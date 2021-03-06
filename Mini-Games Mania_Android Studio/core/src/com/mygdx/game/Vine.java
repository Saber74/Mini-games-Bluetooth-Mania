package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Vine {
	//stores coordinates of vine, angle of rotation
	//setOrigin
	//setPos
	
	Sprite vine;
	double height;
	float x,y;
	int angle;
	
	public Vine(String file, float x, float y, double h){
		vine = new Sprite(new Texture(file));
		height = h;
		vine.setSize(vine.getWidth(), (float)height);
		//System.out.println(length);
		vine.setOrigin(0,0);
		angle = 90;
		
		this.x = x;
		this.y = y;
	}
	
	public void setPos(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setRotation(int angle){
		this.angle = angle;
		vine.setRotation(angle);
	}
	
	public void translateX(int tx){
		x += tx;
	}
	
	
	public void render(SpriteBatch batch){
		vine.setPosition(x, y);
		vine.draw(batch);
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getWidth(){
		return vine.getWidth();
	}
	
	public float getHeight(){
		return vine.getHeight();
	}
	
	public int getRotation(){
		return angle;
	}
	
	public Sprite getVineSprite(){
		return vine;
	}
	
}
