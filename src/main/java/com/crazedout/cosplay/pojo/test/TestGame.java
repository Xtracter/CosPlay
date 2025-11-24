package com.crazedout.cosplay.pojo.test;

import com.crazedout.cosplay.*;
import com.crazedout.cosplay.Scrollable;
import com.crazedout.cosplay.pojo.*;
import com.crazedout.cosplay.editor.CosPlay;
import com.crazedout.cosplay.sprites.DefaultUserSprite;
import com.crazedout.cosplay.sprites.DefaultZombieSprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class TestGame extends JPanel implements GamePanel, GridListener {

    List<Tile> grid;
    Map map;
    String userDir = System.getProperty("user.home") + File.separatorChar + "cosplay";
    public GameThread runner;
    POJOMapLoader pojoLoader;
    File dropFile;
    DefaultGamePlay gamePlay;

    public TestGame(){
        super(null);
        map = new DefaultMap(16,17,32);
        setFocusable(true);
        grabFocus();
        KeyControls keyControls = new KeyControls(map.getUserSprite());
        keyControls.setFireKey1(KeyEvent.VK_P);
        addKeyListener(keyControls);
        keyControls.setStopKey(KeyEvent.VK_SPACE);
        pojoLoader = new POJOMapLoader();
        DragDropListener myDragDropListener = new DragDropListener(this);
        new DropTarget(this, myDragDropListener);
    }

    public TestGame(Map map){

        super(null);
        this.map = map;
        setFocusable(true);
        grabFocus();
        DefaultUserSprite usp = (DefaultUserSprite)map.getUserSprite();

        gamePlay = new DefaultGamePlay();
        if(map.getSprites().size()>0) {
            KeyControls keyControls = new KeyControls(usp);
            keyControls.setFireKey1(KeyEvent.VK_P);
            keyControls.setJumpKey(KeyEvent.VK_SPACE);
            keyControls.setStopKey(KeyEvent.VK_ESCAPE);
            addKeyListener(keyControls);
            usp.addSpriteListener(gamePlay);
            //gamePlay.addActorSprites(map.getSprites());
        }
        if(map.getBackgroundColor()!=null) {
            setBackground((Color)map.getBackgroundColor());
        }
        runner = new GameThread(this,12);
        runner.start();
    }

    public void setCosPack(String file){
        try {
            ZipResource res = new ZipResource(file);
            List<String> maps = res.getMapNames();
            gamePlay = new DefaultGamePlay();
            map = new DefaultMap(res);
            GamePlay.setAudio(new POJOAudio(map.getResources()));
            ((DefaultMap)map).addGridListener(this);
            pojoLoader.loadMap(map, maps.get(0), res);
            Sprite userSprite = map.getUserSprite();
            KeyControls keyControls = new KeyControls(userSprite, this, map);
            keyControls.setFireKey1(KeyEvent.VK_P);
            keyControls.setJumpKey(KeyEvent.VK_SPACE);
            keyControls.setStopKey(KeyEvent.VK_ESCAPE);
            addKeyListener(keyControls);
            if(userSprite!=null) userSprite.addSpriteListener(gamePlay);

            gamePlay.addGamePlayListener((DefaultUserSprite)userSprite);
            runner = new GameThread(this,12);
            runner.start();
            if(map.getBackgroundColor()!=null) {
                setBackground((Color)map.getBackgroundColor());
            }
            map.initSprites();
        }catch(Exception ex){
            ex.printStackTrace();
            showErrorDialog("Error", ex.getMessage());
        }
    }

    public void drop(File file){
        dropFile=file;
        setCosPack(file.getAbsolutePath());
        requestFocus();
    }

    @Override
    public synchronized Dimension getPreferredSize(){
        Dimension dim = null;
        if(((Scrollable)map).getViewPort()==null) {
            int c = map.getCols();
            int r = map.getRows();
            int w = c * map.getSize();
            int h = r * map.getSize();
            dim = new Dimension(w+12,h+2);
        }else{
            ViewPort r = ((Scrollable)map).getViewPort();
            dim = new Dimension(r.getPixWidth(),r.getPixHeight());
        }
        return dim;
    }

    public static Object[] openGameMode(Map map){
        JFrame f = new JFrame("CosPlay Test Game");
        try {
            Image icon = ImageIO.read(f.getClass().getResource("/cos.png"));
            f.setIconImage(icon);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final TestGame tg = new TestGame(map);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                tg.runner.stop();
            }
        });

        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_F1){
                    map.getSprites().get(0).snapToGrid(map);
                    tg.repaint();
                }
            }
        });

        f.add(tg);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        Object[] obj = new Object[]{f,tg};
        return obj;
    }

    public static void showErrorDialog(String title, String message){
        JOptionPane.showMessageDialog(CosPlay.frame, message, title, ERROR_MESSAGE);
    }

    @Override
    public void tick() {
        for(Sprite s:map.getSprites()){
            s.move(map);
        }
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        POJOGraphics pg = new POJOGraphics(g,this);
        if(this.map!=null){
            this.map.draw(pg);
        }
    }

    static JFrame frame;
    public static void main(String argv[]){
        JFrame t = new JFrame("CosPlay Test Game");
        try {
            Image icon = ImageIO.read(t.getClass().getResource("/cos.png"));
            t.setIconImage(icon);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        frame=t;
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TestGame tg = new TestGame();
        t.add(tg);
        t.pack();
        t.setLocationRelativeTo(null);
        t.setVisible(true);
        tg.setCosPack(System.getProperty("user.home") + File.separatorChar + "cosplay" +
                File.separatorChar + "maps" + File.separatorChar + "cosplay.zip");
    }

    @Override
    public void gridChanged() {
        frame.setResizable(true);
        invalidate();
        validate();
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }
}
