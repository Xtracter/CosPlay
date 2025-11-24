package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.ViewPort;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewPortEditor {

    JTextField left,top,width,height;
    JCheckBox scrollable;

    private static ViewPortEditor instance = null;

    private ViewPortEditor() {

        scrollable = new JCheckBox("Use view port scrolling", false);
        left = new JTextField("" + 0);
        top = new JTextField("" + 0);
        width = new JTextField("" + 0);
        height = new JTextField("" + 0);

        scrollable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                left.setEditable(scrollable.isSelected());
                top.setEditable(scrollable.isSelected());
                width.setEditable(scrollable.isSelected());
                height.setEditable(scrollable.isSelected());
            }
        });
    }

    public static ViewPortEditor getInstance() {
        if (instance == null) {
            instance = new ViewPortEditor();
        }
        return instance;
    }

    public boolean pop(MapPanel panel) {

        boolean ok = false;

        Object[] message = {
                "",scrollable,
                "Left tile:", left,
                "Top tile:", top,
                "Tile width:", width,
                "Tile height:", height
        };

        if(panel.map.viewPort!=null){
            left.setText(""+panel.map.viewPort.tileLeft);
            top.setText(""+panel.map.viewPort.tileTop);
            width.setText(""+panel.map.viewPort.tileWidth);
            height.setText(""+panel.map.viewPort.tileHeight);
        }

        left.setEditable(scrollable.isSelected());
        top.setEditable(scrollable.isSelected());
        width.setEditable(scrollable.isSelected());
        height.setEditable(scrollable.isSelected());

        int option = JOptionPane.showConfirmDialog(panel, message, "Enable view port scrolling", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                if(scrollable.isSelected()) {
                    panel.map.viewPort = new ViewPort(panel,
                            Integer.parseInt(left.getText()),
                            Integer.parseInt(top.getText()),
                            Integer.parseInt(width.getText()),
                            Integer.parseInt(height.getText()),
                            Integer.parseInt("" + panel.map.size));
                }else{
                    panel.map.viewPort=null;
                }
                ok=true;
            }catch(Exception ex){
                CosPlay.showErrorDialog("Error","Wrong format in input");
            }
        }
        return ok;
    }
}
