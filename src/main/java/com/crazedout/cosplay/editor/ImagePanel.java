package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.pojo.ImageBitmap;
import com.crazedout.cosplay.Tile;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class ImagePanel extends JPanel {

    class PopListener extends MouseAdapter {
        Component ip;
        public PopListener(Component c){
            this.ip=c;
        }
        @Override
        public void mousePressed(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getButton() == 3) {
                list.setSelectedIndex(list.locationToIndex(e.getPoint()));
                pop.show(list, e.getX(), e.getY());
            }
        }
    }

    class Renderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            return new JPanel(){
                public void paintComponent(Graphics g){
                    g.drawImage(((ImageBitmap)value).getImage(),2,2,64,64,this);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("TimesRoman", Font.BOLD, 12));
                    g.drawString(((ImageBitmap)value).getName(),(getWidth()/2)+4,getHeight()/2);
                    if(isSelected){
                        g.setColor(Color.RED);
                        g.drawRect(0,0,getWidth()-1,getHeight()-1);
                    }
                }
                public synchronized Dimension getPreferredSize(){
                    return new Dimension(68,68);
                }
            };
        }
    }

    JList list;
    List<CosImageBitmap> imageList;
    JPopupMenu pop;
    ImagePanel ip;
    CosImageBitmap selectedImage;
    JFileChooser fc = new JFileChooser();
    CosPlay cosPlay;

    public ImagePanel(CosPlay cosPlay){
        super(new BorderLayout());
        this.cosPlay = cosPlay;
        this.ip = this;
        imageList = CosResources.getImages();
        Object[] iList = imageList.toArray();
        list = new JList(iList);
        list.setToolTipText("Right click for properties menu");
        list.setCellRenderer(new Renderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);
        add("Center", scrollPane);
        pop = new JPopupMenu();
        addToPop();
        list.addMouseListener(new PopListener(list));
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(list.getSelectedValue()!=null) {
                    selectedImage = (CosImageBitmap) list.getSelectedValue();
                }
            }
        });
        JButton openBtn = new JButton("New image source...");
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File(".")); // start at application current directory
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showSaveDialog(CosPlay.frame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File yourFolder = fc.getSelectedFile();
                    reload(yourFolder);
                    CosPlay.userHome = yourFolder.getAbsolutePath();
                    CosPlay.getInstance().setTitle("CosPlay - " + CosPlay.userHome);
                }
            }
        });
        //add("South", openBtn);
    }

    public void reload(File file){
        try {
            CosResources.getInstance().showLoader();
            CosResources.getInstance().reload(file);
            List<CosImageBitmap> images = CosResources.getImages();
            list.setListData(images.toArray());
            list.invalidate();
            list.validate();
            CosResources.getInstance().hideLoader();
        }catch(Exception ex){
            ex.printStackTrace();
            CosPlay.showErrorDialog("Error", ex.getMessage());
        }
    }

    JMenu m = new JMenu("Properties..");
    JCheckBoxMenuItem wall,exit,grave,ladder,defaultTile,mainBg,bgTile;
    JMenuItem file,gun;

    @Override
    public synchronized Dimension getPreferredSize(){
        return new Dimension(180,100);
    }

    private void addToPop(){

        pop.add(m);
        m.add((mainBg=new JCheckBoxMenuItem("Main background")));
        mainBg.setToolTipText("Set background image");
        m.addSeparator();
        m.add((defaultTile=new JCheckBoxMenuItem("Default tile image")));
        defaultTile.setToolTipText("Adds image to all tiles");
        m.add((bgTile=new JCheckBoxMenuItem("Default background tile image")));
        bgTile.setToolTipText("Adds image as background for all tiles");
        m.addSeparator();
        m.add((wall=new JCheckBoxMenuItem("Wall tile")));
        wall.setToolTipText("Set this tile to be a wall");
        m.add((exit=new JCheckBoxMenuItem("Enter/Exit tile")));
        exit.setToolTipText("Set this tile to be a enter or exit tile");
        m.add((grave=new JCheckBoxMenuItem("Spawn sprite tile")));
        grave.setToolTipText("Set this tile to be a place for spawning of new sprites");
        m.add((ladder=new JCheckBoxMenuItem("Ladder tile")));
        ladder.setToolTipText("Set this tile to be a ladder");
        m.addSeparator();
        m.add((gun=new JMenuItem("Item1 tile")));

        m.addSeparator();
        m.add((file=new JMenuItem("")));

        ladder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem mi = (JCheckBoxMenuItem)e.getSource();
                selectedImage.ladder=(mi.getState());
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                for(Tile t:mp.map.grid){
                    if(selectedImage!=null && t.getFile()!=null && t.getFile().equals(selectedImage.file)) {
                        ((CosTile)t).setLadder(mi.getState());
                    }
                }
                mp.repaint();
                unset(ladder);
            }
        });

        wall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem mi = (JCheckBoxMenuItem)e.getSource();
                selectedImage.wall=(mi.getState());
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                for(Tile t:mp.map.grid){
                    if(selectedImage!=null && t.getFile()!=null && t.getFile().equals(selectedImage.file)) {
                        ((CosTile)t).setWall(mi.getState());
                    }
                }
                mp.repaint();
                unset(wall);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                JCheckBoxMenuItem mi = (JCheckBoxMenuItem)e.getSource();
                selectedImage.exit = mi.getState();
                for(Tile t:mp.map.grid){
                    if(selectedImage!=null && t.getFile()!=null && t.getFile().equals(selectedImage.file)) {
                       // ((CosTile)t).setExit(mi.getState());
                    }
                }
                mp.repaint();
                unset(exit);
            }
        });
        grave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem mi = (JCheckBoxMenuItem)e.getSource();
                selectedImage.grave = mi.getState();
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                for(Tile t:mp.map.grid){
                    if(selectedImage!=null && t.getFile()!=null && t.getFile().equals(selectedImage.file)){
                        //((CosTile)t).setGrave(mi.getState());
                    }
                }
                mp.repaint();
                unset(grave);
            }
        });
        gun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem mi = (JMenuItem)e.getSource();
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                ItemEditor.getInstance().edit(mp,selectedImage);
                mp.repaint();
            }
        });

        mainBg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem mi = (JCheckBoxMenuItem)e.getSource();
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                selectedImage.defBg = mi.getState();
                if(selectedImage.defBg) mp.map.bgImage=selectedImage;
                else mp.map.bgImage=null;
                unset(mainBg);
                mp.repaint();
            }
        });

        defaultTile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem mi = (JCheckBoxMenuItem)e.getSource();
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                selectedImage.defTile = mi.getState();
                if(selectedImage.defTile) mp.map.setDefaultImage(selectedImage);
                else mp.map.setDefaultImage(null);
                mp.repaint();
                unset(defaultTile);
            }
        });

        bgTile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem mi = (JCheckBoxMenuItem)e.getSource();
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                selectedImage.tileBg = mi.getState();
                if(selectedImage.tileBg) mp.map.setDefaultTileBgImage(selectedImage);
                else mp.map.setDefaultTileBgImage(null);
                mp.repaint();
                unset(bgTile);
            }
        });

        pop.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                m.setEnabled(selectedImage!=null);
                wall.setState(selectedImage.wall);
                exit.setState(selectedImage.exit);
                grave.setState(selectedImage.grave);
                ladder.setState(selectedImage.ladder);
                defaultTile.setState(selectedImage.defTile);
                mainBg.setState(selectedImage.defBg);
                bgTile.setState(selectedImage.tileBg);
                file.setText(selectedImage.file);
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                mp.repaint();
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });

    }

    public void unset(JCheckBoxMenuItem item){
        if(item!=bgTile) {
            selectedImage.tileBg = false;
            bgTile.setState(false);
        }
        if(item!=wall) {
            selectedImage.wall = false;
            wall.setState(false);
        }
        if(item!=ladder) {
            selectedImage.ladder = false;
            ladder.setState(false);
        }
        if(item!=exit) {
            selectedImage.exit = false;
            exit.setState(false);
        }
        if(item!=grave) {
            selectedImage.grave = false;
            grave.setState(false);
        }
        /*
        if(i tem!=gun) {
            selectedImage.gun = false;
            gun.setState(false);
        }*/
        if(item!=mainBg) {
            selectedImage.defBg = false;
            mainBg.setState(false);
        }
    }
}
