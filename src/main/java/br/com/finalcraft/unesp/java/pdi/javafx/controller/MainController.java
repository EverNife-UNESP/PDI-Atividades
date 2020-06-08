package br.com.finalcraft.unesp.java.pdi.javafx.controller;

import br.com.finalcraft.unesp.java.pdi.data.image.FileHelper;
import br.com.finalcraft.unesp.java.pdi.data.image.ImageHelper;
import br.com.finalcraft.unesp.java.pdi.data.wrapper.ImgWrapper;
import br.com.finalcraft.unesp.java.pdi.javafx.controller.consoleview.ConsoleView;
import br.com.finalcraft.unesp.java.pdi.javafx.controller.filemanager.FileLoaderHandler;

import br.com.finalcraft.unesp.java.pdi.javafx.controller.filemanager.FileSaverHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.awt.image.BufferedImage;
import java.io.File;

public class MainController implements FileLoaderHandler, FileSaverHandler {

    private static MainController instance;

    public ImgWrapper leftImage;
    public ImgWrapper rightImage;
    public ImgWrapper rightImageBackUp;

    @FXML
    void initialize() {
        instance = this;
    }

    public void updateImagesBeingDisplayed(){
        if (leftImage != null){
            BufferedImage bufferedImage = ImageHelper.convertToBufferedImage(leftImage);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            leftImageViwer.setImage(image);
        }
        if (rightImage != null){
            BufferedImage bufferedImage = ImageHelper.convertToBufferedImage(rightImage);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            rightImageViwer.setImage(image);
        }
    }

    @Override
    public void onFileLoaded(File file) {
        ImgWrapper imgWrapper = FileHelper.readAndCreateImageWrapper(file);
        if (imgWrapper != null){
            leftImage = imgWrapper;
            rightImage = imgWrapper.clone();
            rightImageBackUp = imgWrapper.clone();

            brightSlider.setDisable(false);
            brightTextField.setDisable(false);
            buttonInvertColors.setDisable(false);
            buttomRotateLeft.setDisable(false);
            buttomRotateRight.setDisable(false);
        }
        setLight(0);
    }

    @Override
    public void onFileSaved(File file) {
        FileHelper.export(file, rightImage);
        System.out.println("RightImage exporatada pada: \n" + file);
    }

    @FXML
    void onConsoleOpen() {
        ConsoleView.initialize();
    }

    @FXML
    private Slider brightSlider;

    @FXML
    private TextField brightTextField;

    @FXML
    private ImageView leftImageViwer;

    @FXML
    private ImageView rightImageViwer;

    @FXML
    private Button buttonInvertColors;

    @FXML
    private Button buttomRotateLeft;

    @FXML
    private Button buttomRotateRight;

    @FXML
    private Button restaurarButtom;

    public void updateLight(){
        rightImage = rightImageBackUp.setBright(currentbright);
        updateImagesBeingDisplayed();
    }

    public void setLight(int value){
        currentbright = value;
        brightTextField.clear();
        brightTextField.appendText(String.valueOf(currentbright));
        brightSlider.setValue(value);
        updateLight();
    }
    int currentbright = 0;
    @FXML
    void onBrightManual(KeyEvent event) {
        try {
            int integer = (int) Double.parseDouble(brightTextField.getCharacters().toString());
            if (integer > brightSlider.getMax()) integer = (int) brightSlider.getMax();
            if (integer < brightSlider.getMin()) integer = (int) brightSlider.getMin();
            setLight(integer);
            System.out.println("Bright set to: " + currentbright);
        }catch (Exception e){
            brightTextField.clear();
            brightTextField.appendText(String.valueOf(currentbright));
            System.out.println("Failed to set bright to non Int");
        }
    }

    @FXML
    void onDragSlider() {
        setLight((int) brightSlider.getValue());
        System.out.println("Bright set to: " + currentbright);
    }

    @FXML
    void onInvertColors() {
        rightImage = rightImage.inverse();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Colors Inverted");
    }

    @FXML
    void onRotateLeft() {
        this.rightImage = this.rightImage.rotateLeft();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Rotated to Left");
    }

    @FXML
    void onRotateRight() {
        this.rightImage = this.rightImage.rotateRight();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Rotated to Right");
    }

    @FXML
    void onOpenFile(){
        startHearingForFileLoads();
        FileLoaderHandler.openFilerLoader(this);
        stopHearingForFileLoads();
    }

    @FXML
    void onExport(){
        startHearingForFileSaves();
        FileSaverHandler.openFilerLoader(this);
        stopHearingForFileSaves();
    }

    @FXML
    void onRestaurar(){
        this.rightImage = this.leftImage.clone();
        this.rightImageBackUp = this.leftImage.clone();
        setLight(0);
    }
}