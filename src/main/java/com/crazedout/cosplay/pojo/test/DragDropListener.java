package com.crazedout.cosplay.pojo.test;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

/**
 * Created by Fredrik on 2016-04-18.
 */
public class DragDropListener implements DropTargetListener {

    private TestGame game;

    public DragDropListener(TestGame editor){
        this.game = editor;
    }

    @Override
    public void drop(DropTargetDropEvent event) {

        event.acceptDrop(DnDConstants.ACTION_COPY);

        Transferable transferable = event.getTransferable();

        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        for (DataFlavor flavor : flavors) {

            try {

                if (flavor.isFlavorJavaFileListType()) {
                    List files = (List) transferable.getTransferData(flavor);
                    for (Object file : files) {
                        File f = ((File)file);
                        if(!f.isDirectory()){
                            game.drop(f);
                            break;
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        event.dropComplete(true);

    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }

}
