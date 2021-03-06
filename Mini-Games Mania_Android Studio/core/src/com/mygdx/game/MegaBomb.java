
package com.mygdx.game;
import java.io.*;
import java.net.*;

//6 wires: 1 wire will defuse the bomb. The first person to choose the correct wire WINS.
//OR the person who is up to choose once the time runs out loses

import com.ClientRead;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.MyGdxGame;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import sun.util.resources.cldr.ro.TimeZoneNames_ro;

//instead of ApplicationAdapter, use ScreenAdapter
//class set up is same as create()

public class MegaBomb extends ScreenAdapter{
	ClientRead client;
	OrthographicCamera cam;
	MyGdxGame game;
	SpriteBatch batch;

	public static final int UNCUT = 11;
	public static final int CUT = 10;
	public static final int P1 = 1;
	public static final int P2 = 2;

	int player;

	ShapeRenderer sr;
	BitmapFont fontPlayer;
	BitmapFont fontTime;
	float stateTime;

	Texture bomb;
	Sprite lightSprite;
	int[] wireStatus;
	int[][] wireCoords;
	Texture[] wiresUncut;
	Texture[] wiresCut;

	String timeStamp;
	int time;
	String fromserver;
	int explode;

	Animation<Texture> explodeAnimation;
	Texture currentFrame;

	public MegaBomb(MyGdxGame game) {

		cam = new OrthographicCamera(800,600);

		cam.position.set(400,300,0);
		cam.update();


		this.game = game;
		batch = game.batch;
		client=game.client;
		sr = new ShapeRenderer();
		fontPlayer = new BitmapFont(Gdx.files.internal("android/assets/IntroScreen/Intro.fnt")); //description font
		fontTime = new BitmapFont();
		fontTime.getData().setScale(2f);

		stateTime = 0f;

		player = P1;

		bomb = new Texture("android/assets/MegaBomb/bomb.png");
		lightSprite = new Sprite(new Texture("android/assets/MegaBomb/light.png"));
		lightSprite.setSize(100,100);
		lightSprite.setPosition(380,290);

		wireStatus = new int[]{UNCUT, UNCUT, UNCUT, UNCUT, UNCUT, UNCUT};
		wireCoords = new int[][]{{180,280}, {200,310},{270,310},{280,140},{235,100},{160,50}};


		wiresUncut = new Texture[wireStatus.length];
		wiresCut = new Texture[wireStatus.length];

		for(int i=0; i<wireStatus.length; i++){
			wiresUncut[i] = new Texture(String.format("android/assets/MegaBomb/wire%d_colour.png",i+1));
			wiresCut[i] = new Texture(String.format("android/assets/MegaBomb/wire%d_grey.png",i+1));
		}

		Texture[] explodeTextures = new Texture[73];

		for(int i=0; i<explodeTextures.length; i++){
			explodeTextures[i] = new Texture(String.format("android/assets/MegaBomb/EXPLOSION/%d.png",i));
		}

		explodeAnimation = new Animation<Texture>(0.12f, explodeTextures);

		explode = (int)(Math.random()*wireStatus.length);
		System.out.println(explode);

		/*time = 10;//10 seconds
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				if(time>0) {
					time--;
					int min = (int) (time / 60);
					int sec = (int) (time - min * 60);
					timeStamp = String.format("%d:0%d", min, sec);
				}
			}
		};*/

		time = 15000;//10 seconds - 600 seconds -> 10 minutes
		//599000
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				if(time>0) {
					time--;
					int min = (int) (time / 60000);
					int sec = (int) ((time - min * 60000)/1000);
					timeStamp = String.format("%d:%d", min, sec);
					if(sec<10){
						timeStamp = String.format("%d:0%d", min, sec);
					}
				}
			}
		};
		timer.scheduleAtFixedRate(task,0,1);

	}

	//go through the status and if int==1, batch.draw the coloured wire. else, batch.draw the grey wire

	@Override
	public void render (float delta) {
//		fromServer=client.read();
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



		batch.begin();

		batch.draw(bomb, 250, 225, 300,150);

		for(int i=0; i<wireStatus.length; i++){
			float width = wiresUncut[i].getWidth();
			float height = wiresUncut[i].getHeight();
			if(wireStatus[i]==UNCUT){
				batch.draw(wiresUncut[i], wireCoords[i][0], wireCoords[i][1],(float)(width*0.3), (float)(height*0.3));
				//batch.draw(wiresUncut[i], 300, 600-80*i, (float)(width*0.3), (float)(height*0.3));
			}

			else{
				batch.draw(wiresCut[i], wireCoords[i][0], wireCoords[i][1],(float)(width*0.3), (float)(height*0.3));
			}
		}

		if(time>0) {
			fontPlayer.draw(batch, String.format("Player %d's turn",player), 10, 550);
			fontTime.draw(batch, timeStamp, 350, 310);
			if(time<5000 && (time/20)%10 == 0){
				lightSprite.setAlpha(1f);
			}
			else if(time<10000 && (time/100)%10==0){
				lightSprite.setAlpha(1f);
			}
			else{
				lightSprite.setAlpha(0f);
			}
			lightSprite.draw(batch);
		}


		if(wireStatus[explode] == CUT){
			time = -1;
			fontTime.getData().setScale(1.5f);
			fontTime.draw(batch, "DEFUSED", 325, 305);
			fontPlayer.draw(batch,"Player "+player+" wins!",30,550);

			wireStatus = new int[]{CUT,CUT,CUT,CUT,CUT,CUT};
		}

		batch.end();


		if(time==0){
			sr.begin(ShapeRenderer.ShapeType.Filled);
			sr.setColor(Color.BLACK);

			sr.rect(0,0,800,600);
			sr.end();
			batch.begin();
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = explodeAnimation.getKeyFrame(stateTime, false);
			batch.draw(currentFrame,100,70,500,500);
			batch.end();
		}


	}

	@Override
	public void show(){
		Gdx.input.setInputProcessor(new InputAdapter(){
			public boolean keyDown(int keycode) {
				if(keycode == Input.Keys.ESCAPE){
					game.setScreen(new GamePickScreen(game));
				}
				if (keycode == Input.Keys.NUM_1){ //||fromserer.equals("1")
					wireStatus[0] = CUT;
				}
				if (keycode == Input.Keys.NUM_2) { //||fromserer.equals("2")
					wireStatus[1] = CUT;
				}
				if (keycode == Input.Keys.NUM_3) { //||fromserer.equals("3")
					wireStatus[2] = CUT;
				}
				if (keycode == Input.Keys.NUM_4) { //||fromserer.equals("4")
					wireStatus[3] = CUT;
				}
				if (keycode == Input.Keys.NUM_5) { //||fromserer.equals("5")
					wireStatus[4] = CUT;
				}
				if (keycode == Input.Keys.NUM_6) { //||fromserer.equals("6")
					wireStatus[5] = CUT;
				}

				if (wireStatus[explode] == UNCUT) {
					player = player == P1 ? P2 : P1;
				}

				return true;
			}

		});
	}


	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}
}


