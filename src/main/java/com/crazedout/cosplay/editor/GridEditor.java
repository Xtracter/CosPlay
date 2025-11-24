package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.GameType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridEditor  {

    JTextField cols, rows, size, aniSpeed;
    JComboBox<GameType> gravity;
    private static GridEditor instance = null;

    private GridEditor(){
        GameType[] gr = {GameType.UP_DOWN_PERSPECTIVE,GameType.SIDE_VIEW_PERSPECTIVE};
        gravity = new JComboBox<GameType>(gr);
        cols = new JTextField();
        rows = new JTextField();
        size = new JTextField();
        aniSpeed = new JTextField();

    }

    public static GridEditor getInstance(){
        if(instance==null){
            instance = new GridEditor();
        }
        return instance;
    }

    public boolean pop(MapPanel panel){

        boolean ok = false;

        Object[] message = {
                "Game type:", gravity,
                "Columns:", cols,
                "Rows:", rows,
                "Grid size:", size,
                "Animation speed:", aniSpeed
        };
        gravity.setSelectedItem(panel.map.getGameType());
        cols.setText("" + panel.map.cols);
        rows.setText("" + panel.map.rows);
        size.setText("" + panel.map.size);
        aniSpeed.setText("" + panel.t.getSpeed());

        int option = JOptionPane.showConfirmDialog(panel, message, "Change game properties", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                panel.map.update(
                        (GameType)gravity.getSelectedItem(),
                        Integer.parseInt(cols.getText()),
                        Integer.parseInt(rows.getText()),
                        Integer.parseInt(size.getText()));
                panel.t.setSpeed(Long.parseLong(aniSpeed.getText()));
                ok = true;
            } catch (Exception ex) {
                CosPlay.showErrorDialog("Error", "Wrong format in Columns, Rows, Size or Animation speed");
            }
        }


        return ok;
    }

}
