package br.com.finalcraft.unesp.java.pdi.javafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class MyFXMLs {

    public static Parent main_screen;
    public static Parent filtragem_espacial_media;
    public static Parent filtragem_espacial_mediana;
    public static Parent filtragem_espacial_laplaceana;
    public static Parent filtragem_espacial_highboost;

    public static void loadUpFiles() throws IOException {
        main_screen = FXMLLoader.load(MyFXMLs.class.getResource("/assets/main_screen.fxml"));
        filtragem_espacial_media = FXMLLoader.load(MyFXMLs.class.getResource("/assets/filstragem/filtragem_espacial_media.fxml"));
        filtragem_espacial_mediana = FXMLLoader.load(MyFXMLs.class.getResource("/assets/filstragem/filtragem_espacial_mediana.fxml"));
        filtragem_espacial_laplaceana = FXMLLoader.load(MyFXMLs.class.getResource("/assets/filstragem/filtragem_espacial_laplaceana.fxml"));
        filtragem_espacial_highboost = FXMLLoader.load(MyFXMLs.class.getResource("/assets/filstragem/filtragem_espacial_highboost.fxml"));
    }

    public static URL getConsoleCSS(){
        return MyFXMLs.class.getResource("/assets/console/console-style.css");
    }
}
