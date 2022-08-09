package com.mustafakamber.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {


	SpriteBatch batch;
	Texture background;
	Texture mountain;
	Texture snow;
	Texture dust;
	Texture spring;
	Texture fall;
	Texture blue;
	Texture industrial;
	Texture forest;
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;

	Sound soundSuccess;
	Sound soundFlap;
	Sound soundCrash;

	Random random;
	Circle birdCircle;

	float birdX=0;
	float birdY=0;
	int gameState =0;
	float velocity =0;
	float gravity = 0.4f;
	float enemyVelocity = 6;
	int numberOfEnemies =4;

	float [] enemyX = new float[numberOfEnemies];//Bee Set

	//Randomly sets of bees
	float [] enemyOffSet = new float[numberOfEnemies];
	float [] enemyOffSet2 = new float[numberOfEnemies];
	float [] enemyOffSet3 = new float[numberOfEnemies];

	float distance =0;


	int score =0;
	int scoredEnemy =0;

	ShapeRenderer shapeRenderer;

	Circle[] enemyCircles;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;

	private int highScore;

	boolean input;

	BitmapFont font;
	BitmapFont font2;
	BitmapFont font3;
	@Override
	public void create () {
		// Like onCreate(All initialize process)



		HighScoreHandler.load();

		//Backgrounds
		batch = new SpriteBatch();
		background = new Texture("background.png");
		mountain = new Texture("mountain.png");
		dust = new Texture("dust.png");
		snow = new Texture("snow.png");
        spring = new Texture("spring.png");
		fall = new Texture("fall.png");
        blue = new Texture("blue.png");
		forest = new Texture("forest.png");
		industrial = new Texture("industrial.png");

		//Bird & Bees
		bird = new Texture("bird.png");
		bee1 = new Texture("bee.png");
		bee2 = new Texture("bee.png");
		bee3 = new Texture("bee.png");


		////Distance between two bee sets
		distance = Gdx.graphics.getWidth()/3;



		random = new Random();

		shapeRenderer = new ShapeRenderer();

		//Bird's starting position
		birdX = Gdx.graphics.getWidth()/3-bird.getHeight()/3;
		birdY = Gdx.graphics.getHeight()/3;


		//Sounds Effect
		soundSuccess = Gdx.audio.newSound(Gdx.files.internal("success.ogg"));
        soundCrash = Gdx.audio.newSound(Gdx.files.internal("crash.ogg"));
		soundFlap = Gdx.audio.newSound(Gdx.files.internal("flap.ogg"));

		//Circles
		birdCircle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];

		//To showthe score
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

		//To show the "Game Over"
		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(4);

		//To show the "HighScore"
		font3 = new BitmapFont();
		font3.setColor(Color.WHITE);
		font3.getData().setScale(4);

		//Location of bee sets
		for(int i =0;i<numberOfEnemies;i++){

			//Rastgele ari seti(initialize)
			enemyOffSet[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			enemyOffSet2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			enemyOffSet3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);



			enemyX[i] = Gdx.graphics.getWidth()-bee1.getWidth()/2+i*distance;

			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}

	}


	@Override
	public void render () {
		//All operations while the game is in progress;


		highScore = HighScoreHandler.getHighScore();

		batch.begin();


		if(score < 10){
			//Very easy difficulty level
			batch.draw(spring,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());//Background
			enemyVelocity =6;

		}
		else if(score >= 10 && score < 20){
            //Easy difficulty level
			batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());//Background
			enemyVelocity =7;


		}else if (score >= 20 && score <30){
            //Medium difficulty level
			batch.draw(fall,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());////Background
            enemyVelocity =8;


		}else if( score >= 30 && score <45){
            //Hard difficulty level
			batch.draw(dust,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());////Background
		    enemyVelocity =9;
		}
        else if (score >= 45 && score <55){
			//Hard difficulty level
			batch.draw(blue,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());////Background
			enemyVelocity =10;
		}else if (score >=55 && score <65){
			//Hard+ difficulty level
			batch.draw(snow,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());////Background
			enemyVelocity =11;
		}else if(score >= 65  && score <75){
			//Hard++ difficulty level
			batch.draw(mountain,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());////Background
			enemyVelocity =12;

		}else if(score >= 75 && score <85){
			//Hard+++ difficulty level
			batch.draw(forest,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());////Background
			enemyVelocity =13;
		}else{
			//Hard++++ difficulty level
			batch.draw(industrial,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());////Background
			enemyVelocity =14;
		}





		if(gameState ==1){//Has the game started?

			//Scoring
			if (enemyX[scoredEnemy]<Gdx.graphics.getWidth()/2-bird.getHeight()/2) {
				score++;
				long idSuccess = soundSuccess.play(1.0f);
				soundSuccess.setPitch(idSuccess,2);
				soundSuccess.setLooping(idSuccess,false);
				if(scoredEnemy < numberOfEnemies -1){
					scoredEnemy++;
				}
				else{
					scoredEnemy =0;
				}

			}

			if(Gdx.input.justTouched()){//Did the user touch the screen after the game started?


				//Flying the bird
				velocity = -7.9f;

				long idFlap = soundFlap.play(1.0f);
				soundFlap.setPitch(idFlap,2);
				soundFlap.setLooping(idFlap,false);

			}




			////Location of bee sets
			for(int i =0;i<numberOfEnemies;i++){

				//Decimation of the bee set and the production of new ones
				if(enemyX[i] < Gdx.graphics.getWidth()/15){

					enemyX[i] = enemyX[i] + numberOfEnemies * distance;


					//Random set of Bee
					enemyOffSet[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffSet2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffSet3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);



				}else{
					//The passing speed of the bee set
					enemyX[i] = enemyX[i] - enemyVelocity;
				}



				//Involving bees in the game and their random arrival
				batch.draw(bee1,enemyX[i],+Gdx.graphics.getHeight()/2+enemyOffSet[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/8);
				batch.draw(bee2,enemyX[i],+Gdx.graphics.getHeight()/2+enemyOffSet2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/8);
				batch.draw(bee3,enemyX[i],+Gdx.graphics.getHeight()/2+enemyOffSet3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/8);


				enemyCircles[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,+Gdx.graphics.getHeight()/2+enemyOffSet[i]+Gdx.graphics.getHeight()/16,Gdx.graphics.getWidth()/60);
				enemyCircles2[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,+Gdx.graphics.getHeight()/2+enemyOffSet2[i]+Gdx.graphics.getHeight()/16,Gdx.graphics.getWidth()/60);
				enemyCircles3[i] = new Circle(enemyX[i]+Gdx.graphics.getWidth()/30,+Gdx.graphics.getHeight()/2+enemyOffSet3[i]+Gdx.graphics.getHeight()/16,Gdx.graphics.getWidth()/60);


			}
			if(birdY > 0 && birdY <= Gdx.graphics.getHeight()){//Did the bird fall?

				//Gravity settings
				velocity = velocity + gravity;
				birdY = birdY - velocity;


			}else{
				long id = soundCrash.play(1.0f);
				soundCrash.setPitch(id,2);
				soundCrash.setLooping(id,false);


				if(score > highScore){
					input = true;
					highScore = score;
					HighScoreHandler.setHighScore(highScore);

				}else{
					input = false;
				}

				gameState =2;
			}
			for(int i =0; i<numberOfEnemies; i++){


				//Crashing
				if((Intersector.overlaps(birdCircle,enemyCircles[i])) || Intersector.overlaps(birdCircle,enemyCircles2[i]) || Intersector.overlaps(birdCircle,enemyCircles3[i])){

					long id = soundCrash.play(1.0f);
					soundCrash.setPitch(id,2);
					soundCrash.setLooping(id,false);

					if(score > highScore){
						input = true;
						highScore = score;
						HighScoreHandler.setHighScore(highScore);

					}else{
						input = false;
					}

					gameState =2;
				}

			}
		}else if (gameState ==0){
			batch.draw(spring,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());//Background
			if(Gdx.input.justTouched()){////Did the user touch the screen ?
				gameState=1;//Game has started
			}
		}
		else if(gameState == 2){
			//Game Over

            if(input){
				font3.draw(batch,"High Score!",Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()*7/8);
				font2.draw(batch,"Game Over! Tap to Play Again!",Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()*3/4);
			}
				font2.draw(batch,"Game Over! Tap to Play Again!",Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()*3/4);



			if(Gdx.input.justTouched()){//Did the user touch the screen ?
				gameState=1;//Game has started

				birdY = Gdx.graphics.getHeight()/3;


				for(int i =0;i<numberOfEnemies;i++){

					//Random set of bee
					enemyOffSet[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffSet2[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffSet3[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);



					enemyX[i] = Gdx.graphics.getWidth()-bee1.getWidth()/2+i*distance;

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}
				velocity =0;
				scoredEnemy =0;
				score =0;
			}
		}


		batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/8);//Kusu cizme

		font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()*7.2f/8,Gdx.graphics.getHeight()*7.5f/8);


		birdCircle.set(birdX+Gdx.graphics.getWidth()/30,birdY+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);


		batch.end();
	}


	@Override
	public void dispose () {
           soundFlap.dispose();
		   soundCrash.dispose();
		   soundSuccess.dispose();
		   batch.dispose();
	}
}
