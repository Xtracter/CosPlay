package com.crazedout.cosplay.editor;

import javax.swing.*;
import java.awt.*;

public class LoadDialog extends JDialog {

    JProgressBar progressBar;
    JLabel text;

    public LoadDialog(JFrame frame){
        super(frame);
        setModal(true);
        setSize(400,100);
        setResizable(false);

        setTitle("Loading resources");
        setLayout(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setBorderPainted(true);
        progressBar.setIndeterminate(true);
        text = new JLabel("Loading resources...");
        add("South", text);
        add("Center", progressBar);
    }

}
