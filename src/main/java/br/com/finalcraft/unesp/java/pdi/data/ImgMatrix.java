package br.com.finalcraft.unesp.java.pdi.data;

import br.com.finalcraft.unesp.java.pdi.data.image.ImageHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
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

    public int getLuminance(int widthCoord, int heightCoord){
        return this.matrix[heightCoord][widthCoord];
    }

    private int checkBounds(int value){
        return value <= 0 ? 0 : value > LIMIAR ? LIMIAR : value;
    }

    public ImgMatrix(int[][] matrix) {
        this.matrix = matrix;
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
        Function<Integer,Integer> inverse = integer -> Math.abs(integer / LIMIAR);
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[0].length; y++) {
                result.matrix[x][y] = inverse.apply(this.matrix[x][y]);
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
        return "ImgMatrix{" +
                "matrix=\n" + stringBuilder.toString() +
                '}';
    }

    public static ImgMatrix fromString(String reference){
        reference = reference.toString().replaceAll("[^0-9," + Pattern.quote("]") + "]","");

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


    public static void main(String[] args) {
        try {
            ImgMatrix original = new ImgMatrix(10,3,128);
            System.out.println("Original One");
            System.out.println(String.valueOf(original));

            ImgMatrix theOneToSubtract = new ImgMatrix(4,4, 20);

            System.out.println("TheOneToSubtract");
            System.out.println(String.valueOf(theOneToSubtract));

            ImgMatrix result = original.subtract(theOneToSubtract);

            System.out.println("The Result");
            System.out.println(String.valueOf(result));

            System.out.println(FileHelper.readAndCreateMatrix(new File("teste.txt")));

            BufferedImage originalImage = ImageHelper.readImage(new File("tests/Lena 256x256.png"));

            ImgMatrix imgMatrix = ImageHelper.convertToPGM(originalImage);
            FileHelper.write(new File("tests/lena256x256.txt"), imgMatrix);

            showImage(originalImage);

            ImgMatrix inverse = imgMatrix;

            BufferedImage inverseBufferedImage = ImageHelper.convertToBufferedImage(inverse);

            showImage(inverseBufferedImage);
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    private static int counter = 0;
    public static void showImage(BufferedImage image){
        final int num = ++counter;
        new Thread(){
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.getContentPane().setLayout(new FlowLayout());
                frame.getContentPane().add(new JLabel(new ImageIcon(image)));
                frame.pack();
                frame.setTitle("Image " + num);
                frame.setVisible(true);
            }
        }.start();
    }


}
