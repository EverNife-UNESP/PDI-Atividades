package br.com.finalcraft.unesp.java.pdi.javafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class MyFXMLs {

    public static Parent main_screen;

    public static void loadUpFiles() throws IOException {
        main_screen = FXMLLoader.load(MyFXMLs.class.getResource("/assets/main_screen.fxml"));
    }

    public static URL getConsoleCSS(){
        return MyFXMLs.class.getResource("/assets/console/console-style.css");
    }
}
