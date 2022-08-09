package com.mustafakamber.survivorbird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class HighScoreHandler {

    public static Preferences pref;


    public static void load(){
        pref = Gdx.app.getPreferences("Survivor Bird");

        if(!pref.contains("HighScore")){
            pref.putInteger("HighScore",0);
        }
    }
    public static void setHighScore(int highScore){

        pref.putInteger("HighScore",highScore);
        pref.flush();
    }
    public static int getHighScore(){
        return pref.getInteger("HighScore");
    }

}
