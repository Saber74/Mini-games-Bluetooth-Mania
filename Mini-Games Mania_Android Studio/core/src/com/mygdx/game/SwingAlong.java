


//May 10, 2019

//update vines as bkg moves
//arrow keys to move player
//sprite frames for megaman


package com.mygdx.game;

//import java.util.*;


import com.ClientRead;
import com.badlogic.gdx.ApplicationAdapter;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.swing.along.ActionResolver;

public class SwingAlong extends ScreenAdapter {

	private OrthographicCamera cam;
	ClientRead client;
	MyGdxGame game;
	String fromserver;
	SpriteBatch batch;
	ShapeRenderer sr;
	int angle = 90;
	int bkgX1, bkgX2;
	
	boolean animation1 = false;
	boolean animation2 = false;
	
	boolean right = true;//vines start swinging right
	
	Texture bkg;
	Texture platform;
	Texture p1Mark;
	Texture p2Mark;
	Texture p1win;
	Texture p2win;
	
	Vine[] vines1;
	Vine[] vines2;

	Player p1, p2;
	float x1, x2;
	
	int vIndex1, vIndex2;
	
	float stateTime1, stateTime2;


	public SwingAlong(MyGdxGame game){

		//Gdx.graphics.setWindowedMode(1000,800);
		//resize(2000,800);
		this.game = game;
		batch = game.batch;
		sr = new ShapeRenderer();
		client=game.client;
		//https://github.com/libgdx/libgdx/wiki/Orthographic-camera
		// Constructs a new OrthographicCamera, using the given viewport width and height
		// Height is multiplied by aspect ratio.
		cam = new OrthographicCamera(1000,800);

		cam.position.set(500,400,0);
		cam.update();


		//https://code.google.com/archive/p/libgdx-users/wikis/IntegratingAndroidNativeUiElements3TierProjectSetup.wiki

		stateTime1 = 0f;
		stateTime2 = 0f;
		
		vIndex1 = -1;
		vIndex2 = -1;
		
		bkgX1 = 0;
		bkgX2 = 0;
		
		x1 = 0;
		x2 = 0;
		
		bkg = new Texture("android/assets/SwingAlong/bkg.png");
		platform = new Texture("android/assets/SwingAlong/platform.png");
		p1Mark = new Texture("android/assets/SwingAlong/p1head.png");
		p2Mark = new Texture("android/assets/SwingAlong/p2head.png");
		p1win = new Texture("android/assets/SwingAlong/p1win.png");
		p2win = new Texture("android/assets/SwingAlong/p2win.png");
		
		vines1 = new Vine[15];
		vines2 = new Vine[15];
		
		//15 vines
		//each vine is 500 pixels apart
		for(int i=0; i<vines1.length; i++){
			vines1[i] = new Vine("android/assets/SwingAlong/vine.png", 275+i*350, 800, 100+Math.random()*50);
		}
		
		for(int i=0; i<vines2.length; i++){
			vines2[i] = new Vine("android/assets/SwingAlong/vine.png", 275+i*350, 400, 100+Math.random()*50);
		}
		
		p1 = new Player("android/assets/SwingAlong/megaman1_",5,0,600);
		
		p2 = new Player("android/assets/SwingAlong/megaman2_",5,0,200);

		
	}

	@Override
	public void render (float delta) {
//		fromserver=client.read();
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stateTime1 += Gdx.graphics.getDeltaTime();
		stateTime2 += Gdx.graphics.getDeltaTime();
		
		//p1 moves onto first vine
		
		if(vIndex1<vines1.length-1 && p1.getPlayerRect().overlaps((vines1[vIndex1+1].getVineSprite()).getBoundingRectangle())){
			vIndex1++;
			p1.setVine(vines1[vIndex1]);
			if(p1.onPlatform){
				p1.setPlatform(false);
			}
			updateMarker1();
		}
		
		//p2 moves onto first vine
		if(vIndex2<vines2.length-1 && p2.getPlayerRect().overlaps(vines2[vIndex2+1].getVineSprite().getBoundingRectangle())){
			vIndex2++;
			p2.setVine(vines2[vIndex2]);
			if(p2.onPlatform){
				p2.setPlatform(false);
			}
			updateMarker2();
		}
		
		batch.begin();
		
		//upper background
		batch.draw(bkg, bkgX1, 400, 1000, 400);
		batch.draw(platform, bkgX1, 590, 200,10);
		
		//lower background
		batch.draw(bkg, bkgX2, 0, 1000, 400);
		batch.draw(platform, bkgX2, 190, 200,10);
		
		updateBkg();
		updateAngle();
		
		batch.draw(p1Mark, x1, 400, 40,40);
		batch.draw(p2Mark, x2, 350, 40,40);
		
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
			batch.draw(p1win,0,0,1000, 800);
		}
		
		if(vIndex2+1==vines2.length){
			//vIndex = 0;
			batch.draw(p2win,0,0,1000, 800);
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
			vines1[i] = new Vine("android/assets/SwingAlong/vine.png", 250+i*325, 800, 100+Math.random()*50);
		}
		stateTime1 = 0f;
		vIndex1 = -1;
		bkgX1 = 0;
		p1 = new Player("android/assets/SwingAlong/megaman1_",5,0,600);
		x1 = 0;
	}
	
	public void restart2(){
		for(int i=0; i<vines2.length; i++){
			vines2[i] = new Vine("android/assets/SwingAlong/vine.png", 250+i*325, 400, 100+Math.random()*50);
		}
		stateTime2 = 0f;
		vIndex2 = -1;
		bkgX2 = 0;
		p2 = new Player("android/assets/SwingAlong/megaman2_",5,0,200);
		x2 = 0;
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
				batch.draw(bkg, 1000*(i+1)+bkgX1, 400, 1000,400);
			}
		}
		
		if(bkgX2<0){
			for(int i=0; i<bkgX2/-1000+1; i++){
				batch.draw(bkg, 1000*(i+1)+bkgX2, 0, 1000, 400);
			}
		}
	}
	
	public void updateMarker1(){
		x1 = 1000/vines1.length*(vIndex1+1);
	}
	
	public void updateMarker2(){
		x2 = 1000/vines2.length*(vIndex2+1);
	}

	@Override
	public void show(){
		Gdx.input.setInputProcessor(new InputAdapter(){
			//implement ALL methods of InputProcessor
			public boolean keyDown(int keycode){
				if(keycode == Keys.ESCAPE){
					game.setScreen(new GamePickScreen(game));
				}
				if(keycode == Keys.RIGHT){ //||fromserver.equals("JUMP")

					float px1 = p1.getX()+p1.getWidth();
					float vx1 = (float)(vines1[vIndex1+1].getX()-vines1[vIndex1+1].getHeight()*Math.cos(Math.toRadians(vines1[vIndex1+1].getRotation()-90)));
					//System.out.println(Math.abs(px1-vx1));
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
					//System.out.println(Math.abs(px2-vx2));
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
						bkgX1-=70;
						for(Vine v : vines1){
							v.translateX(-70);
						}
					}

					else{
						bkgX1-=200;
						for(Vine v : vines1){
							v.translateX(-200);
						}
					}

				}

				if(p1.getX()>400){
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

					else{
						bkgX2-=200;
						for(Vine v : vines2){
							v.translateX(-200);
						}
					}

				}

				if(p2.getX()>400){
					bkgX2-=100;
					for(Vine v : vines2){
						v.translateX(-100);
					}
					p2.translateX(-100);
				}

				return true;
			}
		});
	}



	
	@Override
	public void hide () {
		Gdx.input.setInputProcessor(null);
	}

}