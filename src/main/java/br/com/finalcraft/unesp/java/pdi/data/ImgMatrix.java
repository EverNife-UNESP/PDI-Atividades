package br.com.finalcraft.unesp.java.pdi.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public boolean hasPixel(int widthCoord, int heightCoord){
        return this.matrix.length > 0 && //just to prevent OutOfBound.
                widthCoord >= 0 && heightCoord >= 0 && heightCoord < this.matrix.length && widthCoord < this.matrix[0].length;
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

    public ImgMatrix setBright(int bright, int minBound, int maxBound){
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[0].length; y++) {
                int currentValue = this.matrix[x][y];
                if (currentValue >= minBound && currentValue <= maxBound){
                    result.matrix[x][y] = checkBounds(currentValue + bright);
                }else {
                    result.matrix[x][y] = currentValue;
                }
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
                result.matrix[this.matrix[x].length - 1 - y][x] = this.matrix[x][y];
            }
        }
        return result;
    }

    public ImgMatrix flipHorizontal(){
        ImgMatrix result = new ImgMatrix(this.matrix[0].length,this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                result.matrix[x][this.matrix[x].length - 1 - y] = this.matrix[x][y];
            }
        }
        return result;
    }

    public ImgMatrix flipVertical(){
        ImgMatrix result = new ImgMatrix(this.matrix[0].length,this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                result.matrix[this.matrix.length - 1 - x][y] = this.matrix[x][y];
            }
        }
        return result;
    }

    public ImgMatrix equalizarBrilho(){
        HashMap<Integer, Integer> mapOfPixelsCount = new HashMap<>(); //Pixels Count, VALOR NK
        for (int i : getAllPixelsInOrder()) {
            Integer currentAmount = mapOfPixelsCount.getOrDefault(i, 0);
            currentAmount++;
            mapOfPixelsCount.put(i, currentAmount);
        }
        HashMap<Integer, Double> mapOfPixelsWeight = new HashMap<>(); //Pixels Weight, VALOR Pr(Rk)=nk/MN
        int totalPixels = this.getHeight() * this.getWidth();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : mapOfPixelsCount.entrySet()) {
            Integer pixelNumber = integerIntegerEntry.getKey();
            Double weight = ( (double)integerIntegerEntry.getValue() /  (double)totalPixels);
            mapOfPixelsWeight.put(pixelNumber, weight);
        }
        HashMap<Integer, Integer> mapOfPixelsExit = new HashMap<>(); //Pixels to Pixel after Equalization
        double previousWeight = 0;
        for (int i = 0; i <= LIMIAR; i++) {
            previousWeight = previousWeight + mapOfPixelsWeight.getOrDefault(i,0D);
            int newValue = (int) (LIMIAR * previousWeight); // LIMIAR * SOMATORIA(Pr(Rk))
            mapOfPixelsExit.put(i, newValue);
        }
        ImgMatrix result = new ImgMatrix(this.matrix[0].length, this.matrix.length);
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {
                result.matrix[x][y] = checkBounds(mapOfPixelsExit.get(this.matrix[x][y]));
            }
        }
        return result;
    }

    public ImgMatrix filtragemEspacialMedia(double coeficiente, double[][] pesos){
        ImgMatrix result = this.cloneEmpty();

        int dislocador = -((pesos.length - 1) / 2);

        //Usando método aonde é ignorado os valores fora da borda
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {

                double valorFinal = 0;
                //Varredura de acordo com o tamanho de pesos
                for (int i = 0; i < pesos.length; i++) {
                    for (int j = 0; j < pesos[i].length; j++) {
                        int targetX = x + i + dislocador;
                        int targetY = y + j + dislocador;

                        if (hasPixel(targetY,targetX)){ //widthCoord, int heightCoord
                            valorFinal = valorFinal + (this.matrix[targetX][targetY] * pesos[i][j]);
                        }
                    }
                }

                result.matrix[x][y] = checkBounds((int) (valorFinal * coeficiente));
            }
        }

        return result;
    }

    public ImgMatrix filtragemEspacialMediana(double[][] pesos){
        ImgMatrix result = this.cloneEmpty();

        int dislocador = -((pesos.length - 1) / 2);

        //Usando método aonde é ignorado os valores fora da borda
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {

                List<Integer> valores = new ArrayList<>();
                //Varredura de acordo com o tamanho de pesos, embora pesos sejam ignorados na mediana
                for (int i = 0; i < pesos.length; i++) {
                    for (int j = 0; j < pesos[i].length; j++) {
                        int targetX = x + i + dislocador;
                        int targetY = y + j + dislocador;
                        if (hasPixel(targetY,targetX)){ //widthCoord, int heightCoord
                            valores.add(this.matrix[targetX][targetY]);
                        }
                    }
                }
                valores.sort(Integer::compareTo);

                int valorFinal = valores.get( (valores.size() - 1) / 2);
                result.matrix[x][y] = checkBounds(valorFinal);
            }
        }

        return result;
    }

    public ImgMatrix filtragemEspacialLaplaciana(double[][] pesos){
        ImgMatrix result = this.cloneEmpty();

        int dislocador = -((pesos.length - 1) / 2);

        //Usando método aonde é ignorado os valores fora da borda
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[x].length; y++) {

                double valorASomar = 0;
                //Varredura de acordo com o tamanho de pesos
                for (int i = 0; i < pesos.length; i++) {
                    for (int j = 0; j < pesos[i].length; j++) {
                        int targetX = x + i + dislocador;
                        int targetY = y + j + dislocador;

                        if (hasPixel(targetY,targetX)){ //widthCoord, int heightCoord
                            valorASomar = valorASomar + (this.matrix[targetX][targetY] * pesos[i][j]);
                        }
                    }
                }

                result.matrix[x][y] = checkBounds((int) (valorASomar + this.matrix[x][y]));
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

    public ImgMatrix cloneEmpty(){
        return new ImgMatrix(this.matrix[0].length, this.matrix.length);
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
