package br.com.finalcraft.unesp.java.pdi.javafx.controller.filemanager;

import br.com.finalcraft.unesp.java.pdi.JavaFXMain;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface FileLoaderHandler {

    public static List<FileLoaderHandler> fileHandlers = new ArrayList<FileLoaderHandler>();

    public static void openFilerLoader(FileLoaderHandler fileLoaderHandler){
        //File Loader não existe ou já está escutando?
        boolean deliveryOnly = fileLoaderHandler == null ? true : !fileHandlers.contains(fileLoaderHandler);

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter(
                        "Alguns tipos de imagens", "*.fcimage", "*.pgm", "*.ppm", "*.png", "*.jpg");

        fileChooser.getExtensionFilters().add(fileExtensions);

        fileChooser.setInitialDirectory(new File("").getAbsoluteFile());
        File loadedFile = fileChooser.showOpenDialog(JavaFXMain.primaryStage);

        if (loadedFile == null) return; //tab closed

        if (!deliveryOnly) fileLoaderHandler.startHearingForFileLoads();
        fileHandlers.forEach(fLoader -> fLoader.onFileLoaded(loadedFile));
        if (!deliveryOnly) fileLoaderHandler.stopHearingForFileLoads();
    }

    public default void startHearingForFileLoads(){
        fileHandlers.add(this);
    }

    public default void stopHearingForFileLoads(){
        fileHandlers.remove(this);
    }

    public void onFileLoaded(File file);

}
