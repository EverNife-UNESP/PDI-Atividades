package br.com.finalcraft.unesp.java.pdi.data;

import java.io.*;
import java.util.stream.Collectors;

public class FileHelper {

    public static boolean write(File file, ImgMatrix imgMatrix) {
        try {
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
            outputWriter.write(imgMatrix.toString());
            outputWriter.flush();
            outputWriter.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static String read(File file) {
        try {
            BufferedReader outputWriter = new BufferedReader(new FileReader(file));
            String result = outputWriter.lines().reduce((s, s2) -> s2 = s + s2).get();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ImgMatrix readAndCreateMatrix(File file) {
        try {
            BufferedReader outputWriter = new BufferedReader(new FileReader(file));
            String result = outputWriter.lines().reduce((s, s2) -> s2 = s + s2).get();
            ImgMatrix imgMatrix = ImgMatrix.fromString(result);
            return imgMatrix;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
