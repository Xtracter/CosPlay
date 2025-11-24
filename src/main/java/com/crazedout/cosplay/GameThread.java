package com.crazedout.cosplay;

import java.io.Serializable;

public class GameThread implements Runnable, Serializable {

    private Thread runner;
    private long SPEED;
    private GamePanel gp;

    public GameThread(GamePanel gp){
        this(gp,12);
    }

    public GameThread(GamePanel gp, long sleep){
        this.gp=gp;
        this.SPEED=sleep;
    }

    public void start(){
        if(runner==null) {
            this.runner = new Thread(this);
            this.runner.setPriority(Thread.MAX_PRIORITY);
            this.runner.start();
        }
    }

    public boolean isAlive(){
        return runner!=null;
    }

    public long getSpeed(){
        return this.SPEED;
    }

    public void setSpeed(long speed){
        this.SPEED = speed;
    }

    public Thread getThread(){
        return this.runner;
    }

    public void stop(){
        runner=null;
    }

    @Override
    public void run(){
        while(Thread.currentThread()==runner && runner!=null){
            try{
                Thread.sleep(SPEED);
                gp.tick();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

}
