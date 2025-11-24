package com.crazedout.cosplay.editor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {

    private static AboutDialog instance;
    private Image cos;

    public AboutDialog(JFrame parent){
        super(parent,true);
        setSize(400,300);
        setLocationRelativeTo(parent);
        try {
            cos = ImageIO.read(getClass().getResource("/cos.png"));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        JPanel p = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(cos,10,10,this);
                g.setFont(new Font("Arial", Font.BOLD, 22));
                g.drawString("CosPlay (c)", 140,70);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString("cosplay@crazedout.com",100,200);
            }
        };
        add("Center",p);
    }

    public static AboutDialog getInstance(){
        if(instance==null){
            instance = new AboutDialog(CosPlay.frame);
        }
        return instance;
    }

}
