package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.GamePlay;
import com.crazedout.cosplay.Item;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ItemEditor  {

    JComboBox types;
    JComboBox<String> sounds;
    JTextField name;
    JButton play;
    private static int n = 0;
    private static ItemEditor instance;

    private ItemEditor(){
        types = new JComboBox(Item.Type.values());
        name = new JTextField("Item " + (++n));

        File dir = new File(CosPlay.userHome + File.separatorChar + "cosplay" + File.separatorChar + "sound");
        String[] files = dir.list();
        String[] arr = new String[files.length+2];
        arr[0] = "<Default>";
        for(int i = 0; i < files.length;i++){
            arr[i+1] = files[i];
        }
        arr[arr.length-1] = "<No Sound>";
        sounds = new JComboBox<>(arr);
        play = new JButton("Play sound");
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = sounds.getSelectedIndex();
                if(i!=0 && i!=arr.length-1) {
                    int id = GamePlay.getAudio().findId((String)sounds.getSelectedItem());
                    GamePlay.getAudio().playSound(id);
                }
            }
        });
    }

    public static ItemEditor getInstance(){
        if(instance==null){
            instance = new ItemEditor();
        }
        return instance;
    }


    public void edit(MapPanel mp, CosImageBitmap bm){

        Object[] message = {
                "Id:", name,
                "Type:",types,
                "Sound", sounds,
                "", play
        };
        if(bm.item!=null) {
            name.setText(bm.item.getItemId());
            types.setSelectedItem(bm.item.getType());
            sounds.setSelectedIndex(bm.item.getSoundId());
        }else{
            name.setText("Item " + (++n));
            types.setSelectedIndex(0);
            sounds.setSelectedIndex(0);
        }

        int option = JOptionPane.showConfirmDialog(mp, message, "Set item properties", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            bm.setItem(new Item((Item.Type)types.getSelectedItem(),name.getText(),sounds.getSelectedIndex()));
        }

    }


}
