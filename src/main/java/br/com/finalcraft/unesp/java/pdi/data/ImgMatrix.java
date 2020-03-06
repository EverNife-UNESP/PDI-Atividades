package br.com.finalcraft.unesp.java.pdi.data;

import java.io.File;
import java.util.regex.Pattern;

public class ImgMatrix {

    public final int matrix[][];

    public ImgMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public ImgMatrix(int xCoordSize, int yCoordSize) {
        matrix = new int[yCoordSize][xCoordSize];
    }

    public ImgMatrix(int xCoordSize, int yCoordSize, int defValue) {
        matrix = new int[yCoordSize][xCoordSize];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = defValue;
            }
        }
    }

    public ImgMatrix subtract(ImgMatrix other){
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                if (x >= other.matrix.length || y >= other.matrix[x].length){
                    continue;
                }
                result.matrix[x][y] = this.matrix[x][y] - other.matrix[x][y];
                if (result.matrix[x][y] < 0) result.matrix[x][y] = 0;
                if (result.matrix[x][y] > 100) result.matrix[x][y] = 100;
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
            ImgMatrix original = new ImgMatrix(10,3);

            System.out.println("Original One");
            System.out.println(String.valueOf(original));

            ImgMatrix theOneToSubtract = new ImgMatrix(4,4, -200);

            System.out.println("TheOneToSubtract");
            System.out.println(String.valueOf(theOneToSubtract));

            ImgMatrix result = original.subtract(theOneToSubtract);

            System.out.println("The Result");
            System.out.println(String.valueOf(result));

            System.out.println(FileHelper.readAndCreateMatrix(new File("teste.txt")));
        }catch (Throwable t){
            t.printStackTrace();
        }
    }


}
