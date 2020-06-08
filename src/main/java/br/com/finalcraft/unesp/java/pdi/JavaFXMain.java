package br.com.finalcraft.unesp.java.pdi;

import br.com.finalcraft.unesp.java.pdi.data.image.FileHelper;
import br.com.finalcraft.unesp.java.pdi.data.ImgMatrix;
import br.com.finalcraft.unesp.java.pdi.data.image.ImageHelper;
import br.com.finalcraft.unesp.java.pdi.javafx.controller.consoleview.ConsoleView;
import br.com.finalcraft.unesp.java.pdi.javafx.view.MyFXMLs;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;

public class JavaFXMain extends Application {

    public static Stage primaryStage;
    public static BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyFXMLs.loadUpFiles();

        JavaFXMain.primaryStage = primaryStage;
        JavaFXMain.primaryStage.setTitle("Processamento Digital de Imagens  - 2020");

        initRootLayout();

        //Iniciando todos as abas para deixar no grau!
    }

    public void initRootLayout()  throws Exception{
        //Carrega o root layout do arquivo fxml.
        rootLayout = (BorderPane) MyFXMLs.main_screen;

        // Mostra a scene (cena) contendo oroot layout.
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        //Ter certeza de que as janelas serão fechadas corretamente!
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                shutDown();
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        // bellow is the default testing
        try {
            //BufferedImage originalImage = ImageHelper.readImage(new File("tests/Lena 256x256.png"));

           /// ImgMatrix imgMatrix = FileHelper.readAndCreateMatrix(new File("tests/Lena 256x256.png"));
            //imgMatrix = ImgMatrix.fromString(imgMatrix.toString());
            //FileHelper.export(new File("tests/lena256x256"), imgMatrix, FileHelper.ExtensionTypes.PGM);

            //ImgMatrix finalImg = FileHelper.readAndCreateMatrix(new File("tests/lena256x256222.pgm"));
            //BufferedImage inverseBufferedImage = ImageHelper.convertToBufferedImage(finalImg);
            //ImageHelper.showImage(imgMatrix);
            //ImageHelper.showImage(imgMatrix);
        }catch (Throwable t){
            t.printStackTrace();
        }
        launch(args);

    }

    public static void shutDown(){
        primaryStage.close();
        Platform.exit();
        System.exit(0);
    }
}
