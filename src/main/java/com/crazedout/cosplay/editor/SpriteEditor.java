package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.GamePlay;
import com.crazedout.cosplay.Map;
import com.crazedout.cosplay.Sprite;
import com.crazedout.cosplay.sprites.DefaultActorSprite;
import com.crazedout.cosplay.sprites.DefaultUserSprite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SpriteEditor {

    private static SpriteEditor instance;
    JTextField id,width,height,speed,tileIndex;
    JCheckBox mapDefault, rect;
    JPopupMenu pop;
    Sprite selectedSprite;
    MapPanel mapPanel;
    JComboBox sounds, effects;
    JButton play,play2;

    public SpriteEditor(){

        id = new JTextField(20);
        width = new JTextField(8);
        height = new JTextField(8);
        speed = new JTextField(4);
        tileIndex = new JTextField(8);
        rect = new JCheckBox("Show core rectangle");
        mapDefault = new JCheckBox("Use map default", true);
        mapDefault.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                width.setEditable(!mapDefault.isSelected());
                height.setEditable(!mapDefault.isSelected());
            }
        });

        pop = new JPopupMenu();
        JMenuItem edit = new JMenuItem("Edit...");
        JMenuItem remove = new JMenuItem("Remove");
        pop.add(edit);
        pop.add(remove);

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edit(mapPanel,mapPanel.map, selectedSprite);
            }
        });

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPanel.map.getSprites().remove(selectedSprite);
                mapPanel.repaint();
            }
        });

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
        effects = new JComboBox<>(arr);
        play2 = new JButton("Play sound");
        play2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = effects.getSelectedIndex();
                if(i!=0 && i!=arr.length-1) {
                    int id = GamePlay.getAudio().findId((String)effects.getSelectedItem());
                    GamePlay.getAudio().playSound(id);
                }
            }
        });

    }

    public static SpriteEditor getInstance(){
        if(instance==null){
            instance = new SpriteEditor();
        }
        return instance;
    }

    public void pop(MapPanel panel, Sprite s, int x, int y){
        this.mapPanel = panel;
        this.selectedSprite=s;
        pop.show(panel,x,y);
        mapPanel.repaint();
    }

    public boolean edit(MapPanel mp, Map map, Sprite sprite){

        boolean ok = false;

        Object[] message = {
                "Id:", id,
                "",mapDefault,
                "Width:", width,
                "Height:", height,
                "Pix speed:", speed,
                "Start tile index:", tileIndex,
                "Continuous:", sounds,
                play,
                "On hit:", effects, play2
        };

        if(sprite.getWidth()!=map.getSize() || sprite.getHeight()!=map.getSize()){
            mapDefault.setSelected(false);
        }

        id.setText(sprite.getId());
        rect.setSelected(mp.showCoreRect);
        width.setText("" + map.getSize());
        height.setText(""+map.getSize());
        speed.setText(""+ sprite.getSpeed());
        width.setEditable(!mapDefault.isSelected());
        height.setEditable(!mapDefault.isSelected());
        tileIndex.setText(""+(sprite.getStartTile()==0?map.getStartTile().getIndex():sprite.getStartTile()));

        int option = JOptionPane.showConfirmDialog(mp, message, "Change sprite properties", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double si = map.getSize();
                double sp = Integer.parseInt(speed.getText());
                double res = si/sp;
                double r = res - (int)res;
                if(r>0){
                    CosPlay.showErrorDialog("Error", "Map size: " + map.getSize() + " and sprite pix speed: " +
                            (int)sp +" must be devisible.");
                    ok=false;
                }else {
                    sprite.setId(id.getText());
                    sprite.setStartTile(Integer.parseInt(tileIndex.getText()));
                    sprite.moveToTile(sprite.getStartTile());
                    if(sprite instanceof DefaultUserSprite) {
                        ((DefaultUserSprite) sprite).setWidth(Integer.parseInt(width.getText()));
                        ((DefaultUserSprite) sprite).setHeight(Integer.parseInt(height.getText()));
                    }else{
                        ((DefaultActorSprite) sprite).setWidth(Integer.parseInt(width.getText()));
                        ((DefaultActorSprite) sprite).setHeight(Integer.parseInt(height.getText()));
                    }
                    sprite.setSpeed(Integer.parseInt(speed.getText()));
                    ok = true;
                }
            }catch(Exception ex){
                ex.printStackTrace();
                CosPlay.showErrorDialog("Error","Wrong format in Id, width, height, speed ord tile index.");
            }
        }
        mp.repaint();
        return ok;
    }


}
