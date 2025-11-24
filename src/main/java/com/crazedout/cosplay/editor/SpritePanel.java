package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.GamePlay;
import com.crazedout.cosplay.Sprite;
import com.crazedout.cosplay.pojo.ImageBitmap;
import com.crazedout.cosplay.sprites.DefaultActorSprite;
import com.crazedout.cosplay.sprites.DefaultUserSprite;
import com.crazedout.cosplay.sprites.DefaultZombieSprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SpritePanel extends JPanel {

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
    SpritePanel sp;
    CosImageBitmap selectedImage;
    JFileChooser fc = new JFileChooser();
    CosPlay cosPlay;

    public SpritePanel(CosPlay cosPlay){
        super(new BorderLayout());
        this.cosPlay=cosPlay;
        this.sp = this;
        cosPlay = cosPlay;
        imageList = getImages();
        Object[] iList = imageList.toArray();
        list = new JList(iList);
        list.setToolTipText("Right click for properties menu");
        list.setCellRenderer(new SpritePanel.Renderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);
        add("Center", scrollPane);
        pop = new JPopupMenu();
        addToPop();
        list.addMouseListener(new SpritePanel.PopListener(list));
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
                    //reload(yourFolder);
                    CosPlay.userHome = yourFolder.getAbsolutePath();
                    CosPlay.getInstance().setTitle("CosPlay - " + CosPlay.userHome);
                }
            }
        });
        //add("South", openBtn);
    }

    void addToPop(){
        JMenu menu = new JMenu("Properties");
        JMenuItem user = new JMenuItem("Add as user sprite");
        user.setToolTipText("User sprite can be controlled by keyboard or mouse etc.");
        JMenuItem actor = new JMenuItem("Add as actor sprite");
        actor.setToolTipText("Actor sprite moves around automatically depending on implementation");
        JMenuItem removeUser = new JMenuItem("Remove user sprite");
        menu.add(user);
        menu.add(actor);
        //menu.addSeparator();
        //menu.add(removeUser);
        pop.add(menu);

        user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                DefaultUserSprite s = new DefaultUserSprite(mp.map);
                CosImageBitmap i = (CosImageBitmap)list.getSelectedValue();
                s.setImageMap(new File(i.file).getName());
                s.loadImages();
                if(SpriteEditor.getInstance().edit(mp,mp.map,s)) {
                    mp.map.addSprite(s);
                    s.addSpriteListener(mp.map);
                }
                mp.keyControls.setSprite(s,mp,mp.map);
                mp.repaint();
            }
        });

        actor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                CosImageBitmap i = (CosImageBitmap)list.getSelectedValue();
                DefaultActorSprite s = null;
                if(i.getFile().indexOf("z")>0){
                    int index = 0;
                    if(i.getName().indexOf("z1_")>-1) index = 0;
                    else if(i.getName().indexOf("z2_")>-1) index = 1;
                    else if(i.getName().indexOf("z3_")>-1) index = 2;
                    DefaultZombieSprite z = new DefaultZombieSprite(mp.map,index);
                    if(SpriteEditor.getInstance().edit(mp,mp.map,z)) {
                        mp.map.addSprite(z);
                    }
                }else {
                    s = new DefaultActorSprite(mp.map);
                    s.setImageMap(new File(i.file).getName());
                    s.loadImages();
                    if(SpriteEditor.getInstance().edit(mp,mp.map,s)) {
                        mp.map.addSprite(s);
                    }
                }
                mp.repaint();
            }
        });


        removeUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sprite s = null;
                MapPanel mp = (MapPanel)cosPlay.mapTabs.getSelectedComponent();
                for(Sprite t:mp.map.sprites){
                    if(t instanceof DefaultUserSprite) s=t;
                }
                mp.map.sprites.remove(s);
            }
        });
    }

    List<CosImageBitmap> getImages(){
        List<CosImageBitmap> list = new ArrayList<>();
        String[] files = new String[]{"ett.png","tva.png","tre.png","fyra.png","fem.png","sex.png"};
        try{
            for(String file:files){
                CosImageBitmap bm = loadImage(this.getClass().getResourceAsStream("/sprites/" + file),file,0,0);
                list.add(bm);
            }
            CosImageBitmap bm = new CosImageBitmap(ImageIO.read(this.getClass().getResourceAsStream("/sprites/z1_right.png")),new File("z1_right.png"));
            list.add(bm);
            bm = new CosImageBitmap(ImageIO.read(this.getClass().getResourceAsStream("/sprites/z2_right.png")),new File("z2_right.png"));
            list.add(bm);
            bm = new CosImageBitmap(ImageIO.read(this.getClass().getResourceAsStream("/sprites/z3_right.png")),new File("z3_right.png"));
            list.add(bm);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
    public CosImageBitmap loadImage(InputStream in, String imageFile, int row, int col){
        try {
            BufferedImage i = ImageIO.read(in);
            if(row>-1 && col>-1){
                int imgWidth = 128;
                int imgHeight = 192;
                int x = col*imgWidth/4;
                int y = row*imgHeight/4;
                return new CosImageBitmap(i.getSubimage(x, y, imgWidth / 4, imgHeight / 4), new File(imageFile));
            }else{
                return new CosImageBitmap(i,new File(imageFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
