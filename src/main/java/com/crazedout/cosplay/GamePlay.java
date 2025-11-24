package com.crazedout.cosplay;

import java.util.ArrayList;

public abstract class GamePlay implements SpriteListener {

    private java.util.List<GamePlayListener> gamPlayListeners = new ArrayList();

    public enum GameState {
        MARQUEE,
        GAME_STARTING,
        GAME_ON,
        GAME_ENDING,
        GAME_OVER
    }
    protected static Audio audio;
    public static void setAudio(Audio a){
        audio = a;
    }

    public static Audio getAudio(){
        return audio;
    }

    protected GameState gameState = GameState.MARQUEE;
    protected static GamePlay currentGamePlay;

    public GamePlay(){
    }

    public static GamePlay getGamePlay(){
        return currentGamePlay;
    }

    @Override
    public abstract void spriteAction(Map map, Sprite s, Action action);

    public boolean isMarquee(){
        return this.gameState == GameState.MARQUEE;
    }

    public boolean isStarting(){
        return this.gameState==GameState.GAME_STARTING;
    }

    public boolean isPlaying(){
        return this.gameState==GameState.GAME_ON;
    }

    public boolean isEnding(){
        return this.gameState==GameState.GAME_ENDING;
    }

    public boolean isGameOver(){
        return this.gameState==GameState.GAME_OVER;
    }

    public GamePlay(GameState state){
        this.gameState = state;
    }

    public void addGamePlayListener(GamePlayListener l){
        this.gamPlayListeners.add(l);
    }

    public void removeGamePlayListener(GamePlayListener l){
        this.gamPlayListeners.remove(l);
    }

    protected void dispatch(){
        for(GamePlayListener l:gamPlayListeners){
            l.gamePlayChanged(this);
        }
    }

    public GameState getGameState(){
        return this.gameState;
    }

    public void setGameState(GameState state){
        this.gameState = state;
        dispatch();
    }

}
