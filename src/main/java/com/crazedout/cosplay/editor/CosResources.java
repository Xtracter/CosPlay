package com.crazedout.cosplay.editor;

import com.crazedout.cosplay.pojo.ImageBitmap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CosResources {

    private List<CosImageBitmap> images;
    private List<String> imgId;
    private Properties props;
    private LoadDialog loader;
    private boolean exportRes;
    File userDir,mapsDir,soundDir;

    private static CosResources instance = null;

    private CosResources(){
        loader = new LoadDialog(CosPlay.frame);
        imgId = new ArrayList<>();
        images = new ArrayList<>();
        try{
            props = new Properties();
            props.load(getClass().getResourceAsStream("/cosplay.properties"));
        }catch(Exception ex){
            ex.printStackTrace();
            CosPlay.showErrorDialog("Error", ex.getMessage());
        }
        try{
            images.add(
                    new CosImageBitmap(
                            ImageIO.read(getClass().getResource("/blank.png")
                            ),new File("/blank.png"))
            );
            imgId.add("blank.png");
        }catch(Exception ex){
            ex.printStackTrace();
            CosPlay.showErrorDialog("Error", ex.getMessage());
        }
        if(!checkForUserDir()) {
            showLoader();
        }
        loadResorces();
        loader.setVisible(false);
    }

    private boolean checkForUserDir(){
        userDir = new File(CosPlay.userHome + File.separatorChar + "cosplay");
        mapsDir = new File(CosPlay.userHome + File.separatorChar + "cosplay" + File.separatorChar + "maps");
        soundDir = new File(CosPlay.userHome + File.separatorChar + "cosplay" + File.separatorChar + "sound");
        File spriteDir = new File(CosPlay.userHome + File.separatorChar + "cosplay" + File.separatorChar + "sprites");
        File mapsDir = new File(CosPlay.userHome + File.separatorChar + "cosplay" + File.separatorChar + "maps");
        if(userDir.exists()) return true;
        int n = JOptionPane.showConfirmDialog(
                null,
                "Creating directory: '" + CosPlay.userHome + File.separatorChar + "cosplay'",
                "Create Directory?",
                JOptionPane.YES_NO_OPTION);
        if(n==JOptionPane.YES_OPTION){
            try {
                userDir.mkdir();
                spriteDir.mkdir();
                mapsDir.mkdir();
                soundDir.mkdir();
                exportRes=true;
            }catch(Exception ex){
                CosPlay.showErrorDialog("Error",ex.getMessage());
            }
        }
        return false;
    }

    public void showLoader(){
        Thread r = new Thread(new Runnable() {
            @Override
            public void run() {
                loader.setLocationRelativeTo(CosPlay.frame);
                loader.setVisible(true);
            }
        });
        r.start();
    }

    public void hideLoader(){
        loader.setVisible(false);
    }

    public static List<CosImageBitmap> getImages(){
        return getInstance().images;
    }

    public static ImageBitmap loadFile(File file) throws IOException {
        Image i = ImageIO.read(new FileInputStream(file));
        return new ImageBitmap(i,file);
    }

    public void addFile(String file) {
        try {
            Image i = ImageIO.read(new FileInputStream(file));
            images.add(new CosImageBitmap(i, new File(file)));
            imgId.add(new File(file).getName());
        }catch(Exception ex){
            ex.printStackTrace();
            CosPlay.showErrorDialog("Error", ex.getMessage());
        }
    }

    public void addResourceFile(String file) {
        try {
            if(exportRes) {
                System.out.println(file);
                InputStream in = getClass().getResourceAsStream("/" + file);
                FileOutputStream fos = new FileOutputStream(userDir.getAbsolutePath() + File.separatorChar + file);
                byte[] buf = new byte[8192];
                int length;
                while ((length = in.read(buf)) != -1) {
                    fos.write(buf, 0, length);
                }
                in.close();
                fos.flush();
                fos.close();
            }
            if(!file.endsWith(".au")) {
                Image i = ImageIO.read(getClass().getResource("/" + file));
                images.add(new CosImageBitmap(i, new File(userDir.getAbsolutePath() + File.separatorChar + file)));
                imgId.add(new File(file).getName());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void loadResorces(){
            try {
                if(exportRes) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/files.txt")));
                    String line = null;
                    while ((line = r.readLine()) != null) {
                        addResourceFile(line);
                    }
                    r.close();
                }else{
                    loadAll(userDir.getAbsolutePath());
                }
        }catch(Exception ex){
            ex.printStackTrace();
            CosPlay.showErrorDialog("Error", ex.getMessage());
        }
    }

    public void reload(File file) throws Exception {
        CosImageBitmap blank = images.get(0);
        images.clear();
        imgId.clear();
        images.add(blank);
        imgId.add("blank.png");
        loadAll(file.getAbsolutePath());
    }

    private void loadAll(String path) throws Exception {

        String files[] = new File(path).list();
        for(int i = 0; i < files.length; i++){
            if(files[i].endsWith(".png")||files[i].endsWith(".jpg") ||
                    files[i].endsWith(".gif") || files[i].endsWith(".jpeg") || files[i].endsWith(".au")) {
                addFile(path + File.separatorChar + files[i]);
            }
        }
    }

    public static CosResources getInstance(){
        if(instance==null){
            instance = new CosResources();
        }
        return instance;
    }

    public static ImageBitmap getImage(String id){
        return getInstance().images.get(getInstance().imgId.indexOf(id));
    }

}
