package br.com.finalcraft.unesp.java.pdi.data.wrapper;

import br.com.finalcraft.unesp.java.pdi.colorutil.ColorUtil;
import br.com.finalcraft.unesp.java.pdi.data.ImgMatrix;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ImgWrapper {

    final ImgMatrix red;
    final ImgMatrix green;
    final ImgMatrix blue;

    public ImgWrapper(ImgMatrix monoColor) {
        this.red = monoColor;
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

    public int getWidth(){
        return this.red.getWidth();
    }

    public int getHeight(){
        return this.red.getHeight();
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

    public ImgWrapper flipHorizontal(){
        ImgMatrix brighted_red = red.flipHorizontal();
        ImgMatrix brighted_green = isBlackAndWhite() ? null : green.flipHorizontal();
        ImgMatrix brighted_blue = isBlackAndWhite() ? null : blue.flipHorizontal();
        return new ImgWrapper(brighted_red, brighted_green, brighted_blue);
    }

    public ImgWrapper flipVertical(){
        ImgMatrix brighted_red = red.flipVertical();
        ImgMatrix brighted_green = isBlackAndWhite() ? null : green.flipVertical();
        ImgMatrix brighted_blue = isBlackAndWhite() ? null : blue.flipVertical();
        return new ImgWrapper(brighted_red, brighted_green, brighted_blue);
    }

    public ImgWrapper equalizarBrilho(){
        ImgMatrix equalized_red = red.equalizarBrilho();
        ImgMatrix equalized_green = isBlackAndWhite() ? null : green.equalizarBrilho();
        ImgMatrix equalized_blue = isBlackAndWhite() ? null : blue.equalizarBrilho();
        return new ImgWrapper(equalized_red, equalized_green, equalized_blue);
    }

    public ImgWrapper filtragemEspacialMedia(double coeficiente, double[][] pesos){
        ImgMatrix equalized_red = red.filtragemEspacialMedia(coeficiente, pesos);
        ImgMatrix equalized_green = isBlackAndWhite() ? null : green.filtragemEspacialMedia(coeficiente, pesos);
        ImgMatrix equalized_blue = isBlackAndWhite() ? null : blue.filtragemEspacialMedia(coeficiente, pesos);
        return new ImgWrapper(equalized_red, equalized_green, equalized_blue);
    }

    public ImgWrapper filtragemEspacialMediana(double[][] pesos){
        ImgMatrix equalized_red = red.filtragemEspacialMediana(pesos);
        ImgMatrix equalized_green = isBlackAndWhite() ? null : green.filtragemEspacialMediana(pesos);
        ImgMatrix equalized_blue = isBlackAndWhite() ? null : blue.filtragemEspacialMediana(pesos);
        return new ImgWrapper(equalized_red, equalized_green, equalized_blue);
    }

    public ImgWrapper filtragemEspacialLaplaciana(double[][] pesos){
        ImgMatrix equalized_red = red.filtragemEspacialLaplaciana(pesos);
        ImgMatrix equalized_green = isBlackAndWhite() ? null : green.filtragemEspacialLaplaciana(pesos);
        ImgMatrix equalized_blue = isBlackAndWhite() ? null : blue.filtragemEspacialLaplaciana(pesos);
        return new ImgWrapper(equalized_red, equalized_green, equalized_blue);
    }

    public ImgWrapper setBright(int bright){
        ImgMatrix brighted_red = red.setBright(bright);
        ImgMatrix brighted_green = isBlackAndWhite() ? null : green.setBright(bright);
        ImgMatrix brighted_blue = isBlackAndWhite() ? null : blue.setBright(bright);
        return new ImgWrapper(brighted_red, brighted_green, brighted_blue);
    }

    public ImgWrapper setBright(int bright, int minBound, int maxBound){
        ImgMatrix brighted_red = red.setBright(bright, minBound, maxBound);
        ImgMatrix brighted_green = isBlackAndWhite() ? null : green.setBright(bright, minBound, maxBound);
        ImgMatrix brighted_blue = isBlackAndWhite() ? null : blue.setBright(bright, minBound, maxBound);
        return new ImgWrapper(brighted_red, brighted_green, brighted_blue);
    }

    public ImgWrapper clone(){
        if (isBlackAndWhite()){
            return new ImgWrapper(this.getRed().clone());
        }else {
            return new ImgWrapper(this.getRed().clone(), this.getGreen().clone(), this.getBlue().clone());
        }
    }

    public ImgWrapper cloneEmpty(){
        if (isBlackAndWhite()){
            return new ImgWrapper(this.getRed().cloneEmpty());
        }else {
            return new ImgWrapper(this.getRed().cloneEmpty(), this.getGreen().cloneEmpty(), this.getBlue().cloneEmpty());
        }
    }

    public ImgWrapper colorify(){
        ImgWrapper result = new ImgWrapper(this.red.cloneEmpty(), this.red.cloneEmpty(), this.red.cloneEmpty());
        for (int x = 0; x < this.red.matrix.length; x++) {
            for (int y = 0; y < this.red.matrix[x].length; y++) {
                double B_W = this.red.matrix[x][y]  / 255D;
                double[] R_G_B = ColorUtil.spectralColor(B_W);
                result.red.matrix[x][y] = (int) (R_G_B[0] * 255);
                result.green.matrix[x][y] = (int) (R_G_B[1] * 255);
                result.blue.matrix[x][y] = (int) (R_G_B[2] * 255);
            }
        }
        return result;
    }

    public ImgWrapper extractRed(){
        return new ImgWrapper(this.red.clone());
    }

    public ImgWrapper extractGreen(){
        return new ImgWrapper(this.green.clone());
    }

    public ImgWrapper extractBlue(){
        return new ImgWrapper(this.blue.clone());
    }

    public ImgWrapper extractCyan(){
        return new ImgWrapper(this.red.inverse());
    }

    public ImgWrapper extractMagenta(){
        return new ImgWrapper(this.green.inverse());
    }

    public ImgWrapper extractYellow(){
        return new ImgWrapper(this.blue.inverse());
    }

    public ImgWrapper extractHue(){
        ImgMatrix result = red.cloneEmpty();
        for (int x = 0; x < this.red.matrix.length; x++) {
            for (int y = 0; y < this.red.matrix[x].length; y++) {
                double R = this.red.matrix[x][y];
                double G = !this.isBlackAndWhite() ? this.green.matrix[x][y] : R;
                double B = !this.isBlackAndWhite() ? this.blue.matrix[x][y] : R;

                if (R + G + B == 0){
                    continue;
                }

                double r = R / (R+G+B);
                double g = G / (R+G+B);
                double b = B / (R+G+B);

                double H;

                double cima = (0.5*( (r - g) + (r - b) ));
                double baixo = Math.pow(Math.pow(r-g, 2) + ((r-b) * (g-b)), 0.5);
                if (baixo == 0) baixo = 0.0000000001;

                H = Math.acos(cima / baixo); //Retorno é um valor entre 0 e 1
                if (b > g){
                    H = 2D - H; // valor entre 1 e 2
                }
                H = H / 2D; //Como o intervalo está enrte [0-2], vamos reduzir para [0-1]

                result.setPixel(y,x, (int) (H * 255));
            }
        }
        return new ImgWrapper(result);
    }

    public ImgWrapper extractSaturation(){
        ImgMatrix result = red.cloneEmpty();
        for (int x = 0; x < this.red.matrix.length; x++) {
            for (int y = 0; y < this.red.matrix[x].length; y++) {
                double R = this.red.matrix[x][y];
                double G = !this.isBlackAndWhite() ? this.green.matrix[x][y] : R;
                double B = !this.isBlackAndWhite() ? this.blue.matrix[x][y] : R;

                if (R + G + B == 0){
                    continue;
                }

                double r = R / (R+G+B);
                double g = G / (R+G+B);
                double b = B / (R+G+B);

                double min = Math.min(r, Math.min(g, b));
                double S = 1 - (3*min);
                result.setPixel(y,x, (int) (S * 255));
            }
        }
        return new ImgWrapper(result);
    }

    public ImgWrapper extractIntensity(){
        ImgMatrix result = red.cloneEmpty();
        for (int x = 0; x < this.red.matrix.length; x++) {
            for (int y = 0; y < this.red.matrix[x].length; y++) {
                double R = this.red.matrix[x][y];
                double G = !this.isBlackAndWhite() ? this.green.matrix[x][y] : R;
                double B = !this.isBlackAndWhite() ? this.blue.matrix[x][y] : R;

                if (R + G + B == 0){
                    continue;
                }

                double r = R / (R+G+B);
                double g = G / (R+G+B);
                double b = B / (R+G+B);

                double I = (R + G + B) / 3D;

                result.setPixel(y,x, (int) I );
            }
        }
        return new ImgWrapper(result);
    }

    public ImgWrapper onConvertFromRGBToHSI(){
        ImgWrapper h = this.extractHue();
        ImgWrapper s = this.extractSaturation();
        ImgWrapper i = this.extractIntensity();
        return new ImgWrapper(h.red, s.red, i.red);
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
