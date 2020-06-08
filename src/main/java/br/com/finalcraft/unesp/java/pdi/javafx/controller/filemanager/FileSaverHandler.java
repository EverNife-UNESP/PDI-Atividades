package br.com.finalcraft.unesp.java.pdi.javafx.controller.filemanager;

import br.com.finalcraft.unesp.java.pdi.JavaFXMain;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface FileSaverHandler {

    public static List<FileSaverHandler> fileHandlers = new ArrayList<FileSaverHandler>();

    public static void openFilerLoader(FileSaverHandler fileLoaderHandler){
        //File Loader não existe ou já está escutando?
        boolean deliveryOnly = fileLoaderHandler == null ? true : !fileHandlers.contains(fileLoaderHandler);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Arquivo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("FinalCraft image", "*.fcimage"),
                new FileChooser.ExtensionFilter("Netpbm grayscale image", "*.pgm"),
                new FileChooser.ExtensionFilter("Netpbm color image", "*.ppm"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));

        fileChooser.setInitialDirectory(new File("").getAbsoluteFile());
        fileChooser.setInitialFileName("MinhaImagem.fcimage"); //set the default name for file to be saved

        File saveTargetFile = fileChooser.showSaveDialog(JavaFXMain.primaryStage);

        if (saveTargetFile == null) return; //tab closed

        if (!deliveryOnly) fileLoaderHandler.startHearingForFileSaves();
        fileHandlers.forEach(fLoader -> fLoader.onFileSaved(saveTargetFile));
        if (!deliveryOnly) fileLoaderHandler.stopHearingForFileSaves();
    }

    public default void startHearingForFileSaves(){
        fileHandlers.add(this);
    }

    public default void stopHearingForFileSaves(){
        fileHandlers.remove(this);
    }

    public void onFileSaved(File file);

}
