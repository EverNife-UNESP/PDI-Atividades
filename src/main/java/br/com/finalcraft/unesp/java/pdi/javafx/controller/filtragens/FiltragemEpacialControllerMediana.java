package br.com.finalcraft.unesp.java.pdi.javafx.controller.filtragens;

import br.com.finalcraft.unesp.java.pdi.JavaFXMain;
import br.com.finalcraft.unesp.java.pdi.javafx.controller.MainController;
import br.com.finalcraft.unesp.java.pdi.javafx.view.MyFXMLs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FiltragemEpacialControllerMediana {

    private static FiltragemEpacialControllerMediana instance;
    private Stage stage;

    @FXML
    void initialize() {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        // Defines a modal window that blocks events from being
        // delivered to any other application window.
        stage.initOwner(JavaFXMain.primaryStage);
        instance = this;
    }

    boolean firstRun = true;
    public static void show(){
        if (instance.firstRun){
            Scene newSceneWindow = new Scene(MyFXMLs.filtragem_espacial_mediana);
            instance.stage.setScene(newSceneWindow);
            instance.stage.setResizable(false);
            instance.firstRun = false;
        }
        instance.stage.show();
    }

    @FXML
    private TextField tamanho;

    @FXML
    void onAplicarFiltragem(ActionEvent event) {
        try {
            int tam = Integer.valueOf(tamanho.getText());

            if (tam <= 0 || tam % 2 == 0){
                System.out.println("O tamanho precisa ser IMPAR POSITIVO!");
                return;
            }

            double[][] pesos = new double[tam][tam];
            for (int i = 0; i < pesos.length; i++) {
                for (int j = 0; j < pesos.length; j++) {
                    pesos[i][j] = 1;
                }
            }

            MainController.rightImage = MainController.rightImage.filtragemEspacialMediana(pesos);
            MainController.rightImageBackUp = MainController.rightImage.clone();
            MainController.instance.setLight(0);

            System.out.println("Filtro Espacial da MEDIANA Aplicado:  " + tam  + "x" + tam);
        }catch (Exception e){
            System.out.println("Falha ao ler valores, favor inserir valores numéricos!");
            e.printStackTrace();
        }
        stage.close();
    }

}
