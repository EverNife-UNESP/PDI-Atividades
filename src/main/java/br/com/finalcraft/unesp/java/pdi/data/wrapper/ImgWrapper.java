package br.com.finalcraft.unesp.java.pdi.data.wrapper;

import br.com.finalcraft.unesp.java.pdi.data.ImgMatrix;

import java.util.function.Function;
import java.util.regex.Pattern;

public class ImgWrapper {

    final ImgMatrix red;
    final ImgMatrix green;
    final ImgMatrix blue;

    public ImgWrapper(ImgMatrix red) {
        this.red = red;
        this.green = null;
        this.blue = null;
    }

    public ImgWrapper(ImgMatrix red, ImgMatrix green, ImgMatrix blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public boolean isBlackAndWhite(){
        return this.green == null;
    }

    public ImgMatrix getRed() {
        return red;
    }

    public ImgMatrix getGreen() {
        return green;
    }

    public ImgMatrix getBlue() {
        return blue;
    }

    public ImgWrapper inverse(){
        ImgMatrix inverse_red = red.inverse();
        ImgMatrix inverse_green = isBlackAndWhite() ? null : green.inverse();
        ImgMatrix inverse_blue = isBlackAndWhite() ? null : blue.inverse();
        return new ImgWrapper(inverse_red, inverse_green, inverse_blue);
    }

    public ImgWrapper rotateRight(){
        ImgMatrix rotated_red = red.rotateRight();
        ImgMatrix rotated_green = isBlackAndWhite() ? null : green.rotateRight();
        ImgMatrix rotated_blue = isBlackAndWhite() ? null : blue.rotateRight();
        return new ImgWrapper(rotated_red, rotated_green, rotated_blue);
    }

    public ImgWrapper rotateLeft(){
        ImgMatrix rotated_red = red.rotateLeft();
        ImgMatrix rotated_green = isBlackAndWhite() ? null : green.rotateLeft();
        ImgMatrix rotated_blue = isBlackAndWhite() ? null : blue.rotateLeft();
        return new ImgWrapper(rotated_red, rotated_green, rotated_blue);
    }

    public ImgWrapper setBright(int bright){
        ImgMatrix brighted_red = red.setBright(bright);
        ImgMatrix brighted_green = isBlackAndWhite() ? null : green.setBright(bright);
        ImgMatrix brighted_blue = isBlackAndWhite() ? null : blue.setBright(bright);
        return new ImgWrapper(brighted_red, brighted_green, brighted_blue);
    }

    public ImgWrapper clone(){
        if (isBlackAndWhite()){
            return new ImgWrapper(this.getRed().clone());
        }else {
            return new ImgWrapper(this.getRed().clone(), this.getGreen().clone(), this.getBlue().clone());
        }
    }

    @Override
    public String toString() {
        return "ImgWrapper=\n" + red +  green +  blue ;
    }

    public static ImgWrapper fromString(String reference){
        String content[] = reference.split(Pattern.quote("ImgMatrix=")); //Size of 4
        //content[0] is trash
        ImgMatrix red = ImgMatrix.fromString(content[1]);
        ImgMatrix green = ImgMatrix.fromString(content[2]);
        ImgMatrix blue = ImgMatrix.fromString(content[3]);
        return new ImgWrapper(red, green, blue);
    }
}
