package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class StatusBar extends JPanel {

    JLabel tileLabel;
    JFileChooser fc = new JFileChooser();

    static int id;

    int myId;
    MapPanel mapPanel;
    public JCheckBox animate;

    public StatusBar(MapPanel mapPanel){
        super(new FlowLayout(FlowLayout.LEFT));
        myId = ++id;
        this.mapPanel=mapPanel;
        tileLabel = new JLabel("Index:       x:       y:      ");

        JButton bgImageBtn = new JButton("Add bg image");
        bgImageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    int returnVal = fc.showOpenDialog(CosPlay.frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        mapPanel.map.setBgImage(file);
                        mapPanel.repaint();
                    }
            }
        });


        JButton clearBtn = new JButton("Clear map");
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPanel.map.clear();
                mapPanel.repaint();
                mapPanel.grabFocus();
            }
        });

        JButton colorBtn = new JButton("Set bg color");
        colorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(
                        CosPlay.frame,
                        "Choose Background Color",
                        mapPanel.getBackground());
                if(newColor!=null){
                    mapPanel.map.setBackgroundColor(newColor);
                    mapPanel.repaint();
                }
                mapPanel.grabFocus();
            }
        });

        final JCheckBox walls = new JCheckBox("Show walls, ladders and items");
        walls.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPanel.showWalls=walls.isSelected();
                mapPanel.repaint();
                mapPanel.grabFocus();
            }
        });

        final JCheckBox grid = new JCheckBox("Show grid",true);
        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPanel.showGrid=grid.isSelected();
                mapPanel.repaint();
                mapPanel.grabFocus();
            }
        });

        animate = new JCheckBox("Animate",false);
        animate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(animate.isSelected()){
                    mapPanel.t.start();
                }else{
                    mapPanel.t.stop();
                }
                mapPanel.grabFocus();
            }

        });

        add(clearBtn);
        add(colorBtn);
        add(walls);
        add(grid);
        add(animate);
        add(tileLabel);
    }

    public void setTile(Tile t){
        if(t==null) tileLabel.setText("Index:       x:       y:      ");
        else this.tileLabel.setText("Index: " + t.getIndex() + " x: " + t.x + " y: " + t.y);
    }

    @Override
    public synchronized Dimension getPreferredSize(){
        return new Dimension(100,60);
    }

}
