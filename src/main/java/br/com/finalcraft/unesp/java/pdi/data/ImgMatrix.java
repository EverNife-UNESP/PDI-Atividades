package br.com.finalcraft.unesp.java.pdi.data;

import br.com.finalcraft.unesp.java.pdi.data.image.ImageHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ImgMatrix {

    public int LIMIAR = 255;
    public final int matrix[][];
    public int getWidth() {
        return this.matrix[0].length;
    }
    public int getHeight() {
        return this.matrix.length;
    }

    public int[] getAllPixelsInOrder(){
        int[] allPixels = new int[this.getHeight() * this.getWidth()];
        int index = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                allPixels[index] = matrix[i][j];
                index++;
            }
        }
        return allPixels;
    }

    private int checkBounds(int value){
        return value <= 0 ? 0 : value > LIMIAR ? LIMIAR : value;
    }

    public ImgMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getPixel(int widthCoord, int heightCoord){
        return this.matrix[heightCoord][widthCoord];
    }

    public void setPixel(int widthCoord, int heightCoord, int value){
        this.matrix[heightCoord][widthCoord] = checkBounds(value);
    }

    public ImgMatrix(int imageWidth, int imageHeight) {
        matrix = new int[imageHeight][imageWidth];
    }

    public ImgMatrix(int xCoordSize, int yCoordSize, int defValue) {
        matrix = new int[yCoordSize][xCoordSize];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = defValue;
            }
        }
    }

    public ImgMatrix inverse(){
        Function<Integer,Integer> inverse = integer -> Math.abs(LIMIAR - integer);
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[0].length; y++) {
                result.matrix[x][y] = inverse.apply(this.matrix[x][y]);
            }
        }
        return result;
    }

    public ImgMatrix setBright(int bright){
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[0].length; y++) {
                result.matrix[x][y] = checkBounds(this.matrix[x][y] + bright);
            }
        }
        return result;
    }

    public ImgMatrix subtract(ImgMatrix other){
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                if (x >= other.matrix.length || y >= other.matrix[x].length){
                    continue;
                }
                result.matrix[x][y] = checkBounds(this.matrix[x][y] - other.matrix[x][y]);
            }
        }
        return result;
    }

    public ImgMatrix add(ImgMatrix other){
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                if (x >= other.matrix.length || y >= other.matrix[x].length){
                    continue;
                }
                result.matrix[x][y] = this.matrix[x][y] + other.matrix[x][y];
                if (result.matrix[x][y] < 0) result.matrix[x][y] = 0;
                if (result.matrix[x][y] > 100) result.matrix[x][y] = 100;
            }
        }
        return result;
    }

    public ImgMatrix rotateRight(){
        ImgMatrix result = new ImgMatrix(this.matrix.length, this.matrix[0].length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                result.matrix[y][this.matrix.length - 1 - x] = this.matrix[x][y];
            }
        }
        return result;
    }

    public ImgMatrix rotateLeft(){
        ImgMatrix result = new ImgMatrix(this.matrix.length, this.matrix[0].length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                result.matrix[this.matrix.length - 1 - y][x] = this.matrix[x][y];
            }
        }
        return result;
    }

    public ImgMatrix clone(){
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                result.matrix[x][y] = this.matrix[x][y];
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            stringBuilder.append("[");
            for (int j = 0; j < matrix[i].length; j++) {
                stringBuilder.append(matrix[i][j]);
                if (j+1 < matrix[i].length){
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append("]\n");
        }
        return "ImgMatrix=\n" + stringBuilder.toString();
    }

    public static ImgMatrix fromString(String reference){
        reference = reference.toString().replaceAll("[^0-9," + Pattern.quote("]") + "]","");
        System.out.println(reference);
        String lines[] = reference.split(Pattern.quote("]"));
        int[][] matrix = new int[lines.length][lines[0].split(Pattern.quote(",")).length];
        for (int x = 0; x < lines.length; x++) {
            String numbers[] = lines[x].split(Pattern.quote(","));
            for (int y = 0; y < numbers.length; y++) {
                matrix[x][y] = Integer.parseInt(numbers[y]);
            }
        }
        return new ImgMatrix(matrix);
    }
}
