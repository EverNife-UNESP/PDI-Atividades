package br.com.finalcraft.unesp.java.pdi.javafx.controller;

import br.com.finalcraft.unesp.java.pdi.data.image.FileHelper;
import br.com.finalcraft.unesp.java.pdi.data.image.ImageHelper;
import br.com.finalcraft.unesp.java.pdi.data.wrapper.ImgWrapper;
import br.com.finalcraft.unesp.java.pdi.javafx.controller.consoleview.ConsoleView;
import br.com.finalcraft.unesp.java.pdi.javafx.controller.filemanager.FileLoaderHandler;

import br.com.finalcraft.unesp.java.pdi.javafx.controller.filemanager.FileSaverHandler;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.RangeSlider;

import java.awt.image.BufferedImage;
import java.io.File;

public class MainController implements FileLoaderHandler, FileSaverHandler {

    private static MainController instance;

    public ImgWrapper leftImage;
    public ImgWrapper rightImage;
    public ImgWrapper rightImageBackUp;

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
            System.out.println("Loading image: " + file.getAbsolutePath());

            leftImage = imgWrapper;
            rightImage = imgWrapper.clone();
            rightImageBackUp = imgWrapper.clone();

            brightSlider.setDisable(false);
            brightTextField.setDisable(false);
            buttonInvertColors.setDisable(false);
            buttomRotateLeft.setDisable(false);
            buttomRotateRight.setDisable(false);
            buttomFlipHorizonttal.setDisable(false);
            buttomFlipVertical.setDisable(false);
            rangeSlider.setDisable(false);
            brightRangeMin.setDisable(false);
            brightRangeMax.setDisable(false);

            zoomProperty.set(leftImage.getRed().getWidth() / 4);
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
    private Slider brightSliderRangeFake;

    private RangeSlider rangeSlider;

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
    private Button buttomFlipHorizonttal;

    @FXML
    private Button buttomFlipVertical;

    @FXML
    private Button restaurarButtom;

    @FXML
    private TextField brightRangeMin;

    @FXML
    private TextField brightRangeMax;

    @FXML
    private ScrollPane scrollPaneLeft;

    @FXML
    private ScrollPane scrollPaneRight;

    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(100);

    @FXML
    void initialize() {
        instance = this;

        rangeSlider = new RangeSlider(0, 255, 0, 255);
        rangeSlider.setDisable(true);

        GridPane gridPane = (GridPane) brightSliderRangeFake.getParent();
        gridPane.add(rangeSlider, 0, GridPane.getRowIndex(brightSliderRangeFake));
        brightSliderRangeFake.setVisible(false);
        brightSliderRangeFake.setDisable(true);

        rangeSlider.setShowTickMarks(true);
        rangeSlider.setShowTickLabels(true);
        rangeSlider.setMajorTickUnit(16);
        rangeSlider.setMinorTickCount(1);
        rangeSlider.setSnapToTicks(true);

        // Set the socket values whenever the range changes
        this.rangeSlider.lowValueProperty().addListener(o -> {
            this.brightRangeMin.setText(String.valueOf((int)rangeSlider.getLowValue()));
            this.onBrightManualMin();
            this.updateLight();
        });
        this.rangeSlider.highValueProperty().addListener(o -> {
            this.brightRangeMax.setText(String.valueOf((int)rangeSlider.getHighValue()));
            this.onBrightManualHigh();
            this.updateLight();
        });

        leftImageViwer.setPreserveRatio(true);
        rightImageViwer.setPreserveRatio(true);

        zoomProperty.addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                leftImageViwer.setFitWidth(zoomProperty.get() * 4);
                leftImageViwer.setFitHeight(zoomProperty.get() * 3);
                rightImageViwer.setFitWidth(zoomProperty.get() * 4);
                rightImageViwer.setFitHeight(zoomProperty.get() * 3);
            }
        });

        EventHandler<ScrollEvent> scrollEventEventHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                    System.out.println("zoomProperty: " + zoomProperty);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                    System.out.println("zoomProperty: " + zoomProperty);
                }
            }
        };

        scrollPaneLeft.addEventFilter(ScrollEvent.ANY, scrollEventEventHandler);
        scrollPaneRight.addEventFilter(ScrollEvent.ANY, scrollEventEventHandler);
    }

    public void updateLight(){
        rightImage = rightImageBackUp.setBright(currentbright, currentLowerBound, currentHigherBound);
        updateImagesBeingDisplayed();
    }

    public void setLight(int value){
        currentbright = value;
        brightTextField.setText(String.valueOf(currentbright));
        brightSlider.setValue(value);
        updateLight();
    }
    int currentbright = 0;

    @FXML
    void onBrightManual(KeyEvent event) {
        try {
            int integer = (int) Double.parseDouble(brightTextField.getText());
            if (integer > brightSlider.getMax()){
                integer = (int) brightSlider.getMax();
                brightSlider.setValue(integer);
            }
            if (integer < brightSlider.getMin()){
                integer = (int) brightSlider.getMin();
                brightSlider.setValue(integer);
            }
            currentbright = integer;
            updateLight();
            System.out.println("Bright set to: " + currentbright);
        }catch (Exception e){
            brightTextField.setText("" + currentbright);
            System.out.println("Failed to set bright to non Int");
        }
    }

    int currentLowerBound = 0;
    int currentHigherBound = 255;

    @FXML
    void onBrightManualMin() {
        try {
            int integer = (int) Double.parseDouble(brightRangeMin.getText());
            if (integer > rangeSlider.getHighValue()){
                integer = (int) rangeSlider.getHighValue();
            }else if (integer < rangeSlider.getMin()){
                integer = (int) rangeSlider.getMin();
            }
            currentLowerBound = integer;
            rangeSlider.setLowValue(currentLowerBound);
            System.out.println("Lower bound range set to: " + currentLowerBound);
        }catch (Exception e){
            brightRangeMin.setText("" + currentLowerBound);
            System.out.println("Failed lower bound range.");
        }
    }

    @FXML
    void onBrightManualHigh() {
        try {
            int integer = (int) Double.parseDouble(brightRangeMax.getText());
            if (integer > rangeSlider.getMax()){
                integer = (int) rangeSlider.getMax();
            }else if (integer < rangeSlider.getLowValue()){
                integer = (int) rangeSlider.getLowValue();
            }
            currentHigherBound = integer;
            rangeSlider.setHighValue(currentHigherBound);
            System.out.println("Higher bound range set to: " + currentHigherBound);
        }catch (Exception e){
            brightRangeMin.setText("" + currentHigherBound);
            System.out.println("Failed higher bound range.");
        }
    }

    @FXML
    void onBrightRangeManual(KeyEvent event) {
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
    void onDragSliderRangeMin() {
        setLight((int) brightSlider.getValue());
        System.out.println("Bright set to: " + currentbright);
    }

    @FXML
    void onDragSliderRangeMax() {
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
    void onFlipHorizontal() {
        this.rightImage = this.rightImage.flipHorizontal();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Flip Horizontal");
    }

    @FXML
    void onFlipVertical() {
        this.rightImage = this.rightImage.flipVertical();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Flip Vertical");
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

    @FXML
    void onTempSave(){
        this.leftImage = this.rightImage.clone();
        onRestaurar();
    }
}
