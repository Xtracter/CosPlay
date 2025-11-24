package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.GridListener;
import com.crazedout.cosplay.Sprite;
import com.crazedout.cosplay.pojo.test.TestGame;
import com.crazedout.cosplay.sprites.DefaultUserSprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Stack;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class CosPlay extends JFrame implements GridListener {

    public static JFrame frame;
    public static String userHome = System.getProperty("user.home");

    public static java.util.Stack<File> recentFiles = new Stack<>();

    public static CosPlay instance;
    CosMap map;
    MapPanel panel;
    File settingFile = new File(userHome + File.separatorChar + "cosplay" +
            File.separatorChar + "maps" + File.separatorChar + "settings.properties");

    java.util.List<CosMap> mapList = new ArrayList<>();

    class PopListener extends MouseAdapter {
        Component ip;
        ImagePanel panel;
        public PopListener(Component c, ImagePanel panel){
            this.panel = panel;
            this.ip=c;
        }
        @Override
        public void mousePressed(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getButton() == 3) {
                panel.list.setSelectedIndex(panel.list.locationToIndex(e.getPoint()));
                panel.pop.show(ip, e.getX(), e.getY());
            }
        }
    }


    class MarginPanel extends JPanel {
        int margin;
        MarginPanel(int margin){
            this.margin=margin;
        }
        @Override
        public synchronized Dimension getPreferredSize(){
            return new Dimension(margin,margin);
        }
    }
    JTabbedPane mapTabs;
    ImagePanel imagePanel;
    JPanel statusPanel;


    private CosPlay() {
        frame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Image icon = ImageIO.read(getClass().getResource("/cos.png"));
            setIconImage(icon);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try{
                    FileOutputStream fos = new FileOutputStream(settingFile);
                    fos.write("files=".getBytes());
                    int n = 0;
                    for(File fi:recentFiles){
                        fos.write((fi.getAbsolutePath().replace("\\","/") + ",").getBytes());
                        if(n++>7) break;
                    }
                    fos.write("\n".getBytes());
                    fos.close();
                }catch(Exception ex){
                    ex.printStackTrace();;
                }

            }
        });

        map = new CosMap();
        map.addGridListener(this);
        panel = new MapPanel(map);
        JTabbedPane tabs = new JTabbedPane();
        mapTabs = new JTabbedPane();
        StatusBar statusBar = new StatusBar(panel);
        imagePanel = new ImagePanel(this);
        panel.setStatusBar(statusBar);
        panel.setImagePanel(imagePanel);
        add("West", new MarginPanel(12));
        add("North", new MarginPanel(12));
        mapTabs = new JTabbedPane();
        mapList.add(map);
        //mapTabs.add("Map" + mapList.size(), new JScrollPane(panel));
        mapTabs.add("Map" + mapList.size(), panel);
        map.name = "Map" + mapList.size();
        add("Center", mapTabs);
        CardLayout cards = new CardLayout();
        statusPanel = new JPanel(cards);
        statusPanel.add(""+statusBar.myId,statusBar);
        add("South", statusPanel);
        tabs.add("Map images", imagePanel);
        SpritePanel spritePanel = new SpritePanel(this);
        tabs.add("Sprites", spritePanel);
        add("East", tabs);
        setResizable(false);
        setTitle("CosPlay - " + userHome);

        try{
            Properties props = new Properties();
            props.load(new FileInputStream(settingFile));
            if(props.getProperty("files")!=null){
                String files[] = props.getProperty("files").split(",");
                for(String file:files){
                    recentFiles.push(new File(file));
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        addMenu();

        mapTabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MapPanel mp = (MapPanel)mapTabs.getSelectedComponent();
                ((CardLayout)statusPanel.getLayout()).show(statusPanel,""+mp.statusBar.myId);
            }
        });


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    JFileChooser fc = new JFileChooser();
    File currentSavedFile=null;

    public static String getLicense(){
        return "Freeware";
    }

    void addMenu(){

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu aboutMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About...");
        JMenuItem lic = new JMenuItem("License...");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog.getInstance().setVisible(true);
            }
        });
        JMenuItem tut = new JMenuItem("Tutorial...");
        aboutMenu.add(tut);
        aboutMenu.addSeparator();
        aboutMenu.add(lic);
        aboutMenu.addSeparator();
        aboutMenu.add(about);

        tut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "https://crazedout.com/cosplay/");
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        JMenuItem edit = new JMenuItem("Change game properties...");
        JMenuItem view = new JMenuItem("Enable view port scrolling...");

        JMenuItem newMap = new JMenuItem("New...");
        JMenuItem name = new JMenuItem("Change map name...");

        JMenuItem load = new JMenuItem("Open...");
        JMenu openRecent = new JMenu("Open recent");
        openRecent.setEnabled(recentFiles.size()!=0);
        for(File f:recentFiles){
            JMenuItem fi = new JMenuItem(f.getAbsolutePath());
            fi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JMenuItem mi = (JMenuItem)e.getSource();
                    setResizable(true);
                    MapPanel panel = (MapPanel)mapTabs.getSelectedComponent();
                    CosMapFactory.importEditableMap(mi.getText(),panel,CosMap.class);
                    for(Sprite s:panel.map.getSprites()){
                        if(s instanceof DefaultUserSprite){
                            panel.keyControls.setSprite(s);
                            s.addSpriteListener(panel.map);
                        }
                    }
                    panel.map.initSprites();
                    setResizable(false);
                }
            });
            openRecent.add(fi);
        }

        JMenuItem closeMap = new JMenuItem("Close map");
        closeMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mapTabs.getTabCount()>1) {
                    mapTabs.removeTabAt(mapTabs.getSelectedIndex());
                }
            }
        });

        JMenuItem saveAs = new JMenuItem("Save as...");
        JMenuItem save = new JMenuItem("Save");

        JMenuItem exp = new JMenuItem("Export...");

        JMenuItem clear = new JMenuItem("Clear map");
        JMenuItem color = new JMenuItem("Set background color");

        JCheckBoxMenuItem grid = new JCheckBoxMenuItem("Show grid",true);
        JCheckBoxMenuItem walls = new JCheckBoxMenuItem("Show walls");

        JCheckBoxMenuItem tool = new JCheckBoxMenuItem("Show bottom toolbar",true);
        JMenuItem game = new JMenuItem("Open in Game mode...");

        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(newMap);
        fileMenu.addSeparator();
        fileMenu.add(load);
        fileMenu.add(openRecent);
        fileMenu.addSeparator();
        fileMenu.add(closeMap);
        fileMenu.addSeparator();
        //fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.addSeparator();
        fileMenu.add(exp);
        fileMenu.addSeparator();
        fileMenu.add(clear);
        fileMenu.addSeparator();
        fileMenu.add(name);
        //fileMenu.add(color);
        //fileMenu.add(color);
        fileMenu.addSeparator();
        fileMenu.add(grid);
        fileMenu.add(walls);
        fileMenu.addSeparator();
        fileMenu.add(edit);
        fileMenu.addSeparator();
        fileMenu.add(view);
        fileMenu.addSeparator();
        fileMenu.add(game);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);

        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel mp = (MapPanel)mapTabs.getSelectedComponent();
                if(ViewPortEditor.getInstance().pop(mp)){
                        if(panel.map.viewPort!=null){
                            System.out.println(panel.map.viewPort.getPixLeft() + " " + panel.map.viewPort.getPixTop());
                        }
                        panel.map.sendGridHasChanged();
                        panel.invalidate();
                }
            }
        });

        name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel mp = (MapPanel)mapTabs.getSelectedComponent();
                JTextField text = new JTextField(mp.map.name);
                Object message[] = {"Name:",text};
                int option = JOptionPane.showConfirmDialog(panel, message, "New map name", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        if(text.getText().length()>0) {
                            panel.map.name = text.getText();
                            int i = mapTabs.getSelectedIndex();
                            mapTabs.setTitleAt(i, panel.map.name);
                        }else{
                            showErrorDialog("Error","Bad map name: '" + text.getText() + "'");
                        }
                    }catch(Exception ex){
                        CosPlay.showErrorDialog("Error","Wrong format in Columns, Rows or Size.");
                    }
                }
            }
        });

        newMap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CosMap m = new CosMap();
                mapList.add(m);
                MapPanel mp = new MapPanel(m);
                StatusBar status = new StatusBar(mp);
                mapTabs.add("Map" + mapList.size(), mp);
                mp.map.name="Map" + mapList.size();
                mp.setStatusBar(status);
                mp.setImagePanel(imagePanel);
                statusPanel.add(""+status.myId, status);
                mapTabs.setSelectedIndex(mapTabs.getTabCount()-1);
                ((CardLayout)statusPanel.getLayout()).show(statusPanel,""+status.myId);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowEvent winClosing;
                winClosing = new WindowEvent(frame,WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosing);
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fc.setCurrentDirectory(CosResources.getInstance().mapsDir);
                fc.setSelectedFile(new File("cosplay_map.cos"));
                int returnVal = fc.showOpenDialog(CosPlay.frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    setResizable(true);
                    File file = fc.getSelectedFile();
                    MapPanel panel = (MapPanel)mapTabs.getSelectedComponent();
                    CosMapFactory.importEditableMap(file.getAbsolutePath(),panel,CosMap.class);
                    for(Sprite s:panel.map.getSprites()){
                        if(s instanceof DefaultUserSprite){
                            panel.keyControls.setSprite(s);
                            s.addSpriteListener(panel.map);
                        }
                    }
                    panel.map.initSprites();
                    setResizable(false);
                    recentFiles.push(file);
                }
                panel.repaint();
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel panel = (MapPanel)mapTabs.getSelectedComponent();
                CosMapFactory.exportEditableMap(panel,true);
            }
        });

        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel panel = (MapPanel)mapTabs.getSelectedComponent();
                CosMapFactory.exportEditableMap(panel, true);
            }
        });

        exp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CosMapFactory.exportAllGameMaps(mapList, true);
                }catch (Exception ex){
                    ex.printStackTrace();
                    showErrorDialog("Error", ex.getMessage());
                }
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel panel = (MapPanel)mapTabs.getSelectedComponent();
                panel.map.clear();
                panel.repaint();
            }
        });

        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel panel = (MapPanel)mapTabs.getSelectedComponent();
                panel.showGrid=grid.isSelected();
                panel.repaint();
            }
        });

        walls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel panel = (MapPanel)mapTabs.getSelectedComponent();
                panel.showWalls=walls.isSelected();
                panel.repaint();
            }
        });

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(GridEditor.getInstance().pop(panel)) {
                    setResizable(true);
                    MapPanel panel = (MapPanel)mapTabs.getSelectedComponent();
                    panel.invalidate();
                    panel.validate();
                    pack();
                    setResizable(false);
                    setLocationRelativeTo(null);
                }
            }
        });

        game.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel mp = (MapPanel)mapTabs.getSelectedComponent();
                mp.t.stop();
                mp.statusBar.animate.setSelected(false);
                Object[] obj = TestGame.openGameMode(mp.map);
                TestGame tg = (TestGame)obj[1];
            }
        });

    }

    @Override
    public void gridChanged() {

        setResizable(true);
        panel.invalidate();
        panel.validate();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);

    }

    public static CosPlay getInstance(){
        if(instance==null){
            instance = new CosPlay();
        }
        return instance;
    }


    public static void showErrorDialog(String title, String message){
        JOptionPane.showMessageDialog(CosPlay.frame, message, title, ERROR_MESSAGE);
    }


    public static void main(String argv[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
            CosPlay.showErrorDialog("Error", ex.getMessage());
        }
        CosPlay.getInstance();
    }
}
