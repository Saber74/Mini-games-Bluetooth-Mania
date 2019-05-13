//May 10, 2019

//update vines as bkg moves
//arrow keys to move player
//sprite frames for megaman

package com.swing.along;

import java.util.*;

import com.badlogic.gdx.ApplicationAdapter;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;


public class SwingAlong extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	ShapeRenderer sr;
	int angle = 90;
	int bkgX1, bkgX2;
	
	boolean animation1 = false;
	boolean animation2 = false;
	
	boolean right = true;//vines start swinging right
	
	Texture bkg;
	Texture platform;
	Texture p1win;
	Texture p2win;
	
	Vine[] vines1;
	Vine[] vines2;

	Player p1, p2;
	
	int vIndex1, vIndex2;
	
	float stateTime1, stateTime2;
	
	@Override
	public void create () {
		Gdx.graphics.setWindowedMode(1000,800);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		
		stateTime1 = 0f;
		stateTime2 = 0f;
		
		vIndex1 = -1;
		vIndex2 = -1;
		
		bkgX1 = 0;
		bkgX2 = 0;
		
		bkg = new Texture("bkg.png");
		platform = new Texture("platform.png");
		p1win = new Texture("p1win.png");
		p2win = new Texture("p2win.png");
		
		vines1 = new Vine[15];
		vines2 = new Vine[15];
		
		//15 vines
		//each vine is 500 pixels apart
		for(int i=0; i<vines1.length; i++){
			vines1[i] = new Vine("vine.png", 250+i*325, 800, 100+Math.random()*50);
		}
		
		for(int i=0; i<vines2.length; i++){
			vines2[i] = new Vine("vine.png", 250+i*325, 400, 100+Math.random()*50);
		}
		
		String[] p1Frames = {"megaman1_1.png","megaman1_2.png","megaman1_3.png","megaman1_4.png"};
		p1 = new Player("megaman1_0.png",p1Frames,0,600);
		

		String[] p2Frames = {"megaman2_1.png","megaman2_2.png","megaman2_3.png","megaman2_4.png"};
		p2 = new Player("megaman2_0.png",p2Frames,0,200);
		
		
		Gdx.input.setInputProcessor(this);
		
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stateTime1 += Gdx.graphics.getDeltaTime();
		stateTime2 += Gdx.graphics.getDeltaTime();
		
		
		//p1 moves onto first vine
		if(vIndex1<vines1.length-1 && p1.getPlayerRect().overlaps(vines1[vIndex1+1].getVineSprite().getBoundingRectangle())){
			vIndex1++;
			p1.setVine(vines1[vIndex1]);
			if(p1.onPlatform){
				p1.setPlatform(false);
			}
		}
		
		//p2 moves onto first vine
		if(vIndex2<vines2.length-1 && p2.getPlayerRect().overlaps(vines2[vIndex2+1].getVineSprite().getBoundingRectangle())){
			vIndex2++;
			p2.setVine(vines2[vIndex2]);
			if(p2.onPlatform){
				p2.setPlatform(false);
			}
		}
		
		batch.begin();
		
		//upper background
		batch.draw(bkg, bkgX1, 400, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);
		batch.draw(platform, bkgX1, 590, 200,10);
		
		//lower background
		batch.draw(bkg, bkgX2, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);
		batch.draw(platform, bkgX2, 190, 200,10);
		updateBkg();
		
		updateAngle();
		
		if(animation1){
			p1.renderAnimation(stateTime1, batch);
			if(p1.isFinishedAnimation(stateTime1)){
				animation1 = false;
			}
		}
		
		else{
			if(p1.onVine()){
				
				p1.setPos(right);
			}
			
			p1.render(batch);
		}
		
		for(int i=0; i<vines1.length; i++){
			
			if(i%2==0){
				vines1[i].setRotation(angle);
			}
			
			else{
				vines1[i].setRotation(-angle);
			}
			
			vines1[i].render(batch);
		}
		
		if(animation2){
			p2.renderAnimation(stateTime2, batch);
			if(p2.isFinishedAnimation(stateTime2)){
				animation2 = false;
			}
		}
		
		else{
			if(p2.onVine()){
				
				p2.setPos(right);
			}
			
			p2.render(batch);
		}
		
		for(int i=0; i<vines2.length; i++){
			
			if(i%2==0){
				vines2[i].setRotation(angle);
			}
			
			else{
				vines2[i].setRotation(-angle);
			}
			
			vines2[i].render(batch);
		}
		
		if(vIndex1+1==vines1.length){
			//vIndex = 0;
			batch.draw(p1win,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		if(vIndex2+1==vines2.length){
			//vIndex = 0;
			batch.draw(p2win,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}

		batch.end();
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.GREEN);
		
		//PENDULUM RECTANGLE 
		//sr.rect(p1.getPosRect().x, p1.getPosRect().y, p1.getPosRect().width, p1.getPosRect().height);
		
		//sr.rect(p1.getOriginX()+p1X, p1.getOriginY()+p1Y, 10, 10);
		
		//Note: position of sprite starts at bottom left
		
		//Rectangle pRect = new Rectangle(p1X,p1Y,p1.getWidth(),p1.getHeight());
		
		//sr.setColor(Color.RED);
		//sr.rect(pRect.x, pRect.y, pRect.width, pRect.height);
		
		sr.end();
		
		
	}
	
	public void restart1(){
		for(int i=0; i<vines1.length; i++){
			vines1[i] = new Vine("vine.png", 250+i*325, 800, 100+Math.random()*50);
		}
		stateTime1 = 0f;
		vIndex1 = -1;
		bkgX1 = 0;
		String[] p1Frames = {"megaman1_1.png","megaman1_2.png","megaman1_3.png","megaman1_4.png"};
		p1 = new Player("megaman1_0.png",p1Frames,0,600);
	}
	
	public void restart2(){
		for(int i=0; i<vines2.length; i++){
			vines2[i] = new Vine("vine.png", 250+i*325, 400, 100+Math.random()*50);
		}
		stateTime2 = 0f;
		vIndex2 = -1;
		bkgX2 = 0;
		String[] p1Frames = {"megaman2_1.png","megaman2_2.png","megaman2_3.png","megaman2_4.png"};
		p2 = new Player("megaman2_0.png",p1Frames,0,200);
	}
	
	public void updateAngle(){
		if(angle==250){
			right = false;
		}
		
		if(angle==110){
			right = true;
		}
		
		if(right){
			angle += 2;
		}
		
		else{
			angle -= 2;
		}
	}
	
	public void updateBkg(){
		if(bkgX1<0){
			for(int i=0; i<bkgX1/-1000+1; i++){
				batch.draw(bkg, 1000*(i+1)+bkgX1, 400, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);		
			}
		}
		
		if(bkgX2<0){
			for(int i=0; i<bkgX2/-1000+1; i++){
				batch.draw(bkg, 1000*(i+1)+bkgX2, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);		
			}
		}
	}

	//implement ALL methods of InputProcessor
	public boolean keyDown(int keycode){
		if(keycode == Keys.RIGHT){

			float px1 = p1.getX()+p1.getWidth();
			float vx1 = (float)(vines1[vIndex1+1].getX()-vines1[vIndex1+1].getHeight()*Math.cos(Math.toRadians(vines1[vIndex1+1].getRotation()-90)));
			System.out.println(Math.abs(px1-vx1));
			if(Math.abs(px1-vx1)<=160){
				animation1 = true;
				stateTime1 = 0f;
			}
			else{
				System.out.println("startover");
				restart1();
			}

		}
		
		if(keycode == Keys.D){
			float px2 = p2.getX()+p2.getWidth();
			float vx2 = (float)(vines2[vIndex2+1].getX()-vines2[vIndex2+1].getHeight()*Math.cos(Math.toRadians(vines2[vIndex2+1].getRotation()-90)));
			System.out.println(Math.abs(px2-vx2));
			if(Math.abs(px2-vx2)<=160){
				animation2 = true;
				stateTime2 = 0f;
			}
			else{
				System.out.println("startover");
				restart2();
			}
		}
		return true;
	}
	
	public boolean keyUp(int keycode){
		if(animation1){
			if(p1.onPlatform){
				bkgX1-=100;
				for(Vine v : vines1){
					v.translateX(-100);
				}
			}
		
			//150*cos(20degrees)
			else{
				bkgX1-=200;
				for(Vine v : vines1){
					v.translateX(-200);
				}
			}
			
		}
		
		if(p1.getX()>500){
			bkgX1-=100;
			for(Vine v : vines1){
				v.translateX(-100);
			}
			p1.translateX(-100);
		}

		
		if(animation2){
			if(p2.onPlatform){
				bkgX2-=100;
				for(Vine v : vines2){
					v.translateX(-100);
				}
			}
		
			//150*cos(20degrees)
			else{
				bkgX2-=200;
				for(Vine v : vines2){
					v.translateX(-200);
				}
			}
			
		}
		
		if(p2.getX()>500){
			bkgX2-=100;
			for(Vine v : vines2){
				v.translateX(-100);
			}
			p2.translateX(-100);
		}
		
		return true;
	}
	
	public boolean keyTyped (char character) {
	    return false;
	}

	public boolean touchDown (int x, int y, int pointer, int button) {
		return false;
	}

	public boolean touchUp (int x, int y, int pointer, int button) {
		return false;
	}

	public boolean touchDragged (int x, int y, int pointer) {
		return false;
	}

	public boolean mouseMoved (int x, int y) {
	    return false;
	}

	public boolean scrolled (int amount) {
	    return false;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}