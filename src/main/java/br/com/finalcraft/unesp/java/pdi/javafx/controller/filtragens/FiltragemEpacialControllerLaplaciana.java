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

public class FiltragemEpacialControllerLaplaciana {

    private static FiltragemEpacialControllerLaplaciana instance;
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
            Scene newSceneWindow = new Scene(MyFXMLs.filtragem_espacial_laplaceana);
            instance.stage.setScene(newSceneWindow);
            instance.stage.setResizable(false);
            instance.firstRun = false;
        }
        instance.stage.show();
    }

    @FXML
    private TextField modelo;

    @FXML
    void onAplicarFiltragem(ActionEvent event) {
        try {
            int mode = Integer.valueOf(modelo.getText());

            if (mode < 1 || mode > 4){
                System.out.println("O modelo precisa ser um numero entre 1 e 4!");
                return;
            }

            double[][] pesos = null;

            switch (mode){
                case 1:{
                    pesos = new double[][]{
                            {0,1,0},
                            {1,-4,1},
                            {0,1,0},
                    };
                    break;
                }
                case 2:{
                    pesos = new double[][]{
                            {1,1,1},
                            {1,-8,1},
                            {1,1,1},
                    };
                    break;
                }
                case 3:{
                    pesos = new double[][]{
                            {0,-1,0},
                            {-1,4,-1},
                            {0,-1,0},
                    };
                    break;
                }
                case 4:{
                    pesos = new double[][]{
                            {-1,-1,-1},
                            {-1,8,-1},
                            {-1,-1,-1},
                    };
                    break;
                }
            }

            MainController.rightImage = MainController.rightImage.filtragemEspacialLaplaciana(pesos);
            MainController.rightImageBackUp = MainController.rightImage.clone();
            MainController.instance.setLight(0);
            System.out.println("Filtro Espacial da LAPLACEANO Aplicado no modelo:  " + 3  + "x" + 3);
            System.out.println(String.format("[%s][%s][%s]", pesos[0][0],pesos[0][1], pesos[0][2]));
            System.out.println(String.format("[%s][%s][%s]", pesos[1][0],pesos[1][1], pesos[1][2]));
            System.out.println(String.format("[%s][%s][%s]", pesos[2][0],pesos[2][1], pesos[2][2]));
        }catch (Exception e){
            System.out.println("Falha ao ler valores, favor inserir valores numéricos!");
            e.printStackTrace();
        }
        stage.close();
    }

}
