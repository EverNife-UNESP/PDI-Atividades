package br.com.finalcraft.unesp.java.pdi.data;

import br.com.finalcraft.unesp.java.pdi.data.image.ImageHelper;

import java.io.*;
import java.util.Arrays;
import java.util.function.Function;

public class FileHelper {

    public static enum ExtensionTypes {
        PGM(".pgm",
                imgMatrix -> {
                    StringBuilder allPixelsInOrder = new StringBuilder();
                    for (int pixel : imgMatrix.getAllPixelsInOrder()) {
                        allPixelsInOrder.append(pixel + "\n");
                    }
                    return "P2" +
                            "\n" + imgMatrix.getWidth() + " " + imgMatrix.getHeight() +
                            "\n255\n" +
                            "\n" + allPixelsInOrder.toString();
                },
                string -> {
                    String[] allLines = string.split("\n");
                    System.out.println("Line2" + allLines[2]);
                    int width = Integer.parseInt(allLines[2].split(" ")[0]);
                    int height = Integer.parseInt(allLines[2].split(" ")[1]);
                    ImgMatrix imgMatrix = new ImgMatrix(width,height);
                    int index = 4;
                    for (int x = 0; x < height; x++) {
                        for (int y = 0; y < width; y++) {
                            imgMatrix.matrix[x][y] = Integer.valueOf(allLines[index]);
                            index++;
                        }
                    }
                    return imgMatrix;
                }),

        IMG_MATRIX(".imm",
                imgMatrix -> {
                    return imgMatrix.toString();
                },
                string -> {
                    return ImgMatrix.fromString(string);
                }
        );

        ExtensionTypes(String extension, Function<ImgMatrix, String> exporter, Function<String, ImgMatrix> importer) {
            this.extension = extension;
            this.exporter = exporter;
            this.importer = importer;
        }

        final String extension;
        final Function<ImgMatrix, String> exporter;
        final Function<String, ImgMatrix> importer;

        public String getExtension() {
            return extension;
        }

        public Function<ImgMatrix, String> getExporter() {
            return exporter;
        }

        public Function<String, ImgMatrix> getImporter() {
            return importer;
        }

        public static ExtensionTypes getExtesionTypeOf(String fileName){
            for (ExtensionTypes extensionn : values()) {
                if (fileName.toLowerCase().endsWith(extensionn.getExtension().toLowerCase())){
                    return extensionn;
                }
            }
            return null;
        }
    }

    public static boolean export(File file, ImgMatrix imgMatrix, ExtensionTypes extension) {
        try {
            if (!file.getName().endsWith(extension.getExtension())){
                file = new File(file.getParentFile(), file.getName() + extension.getExtension());
            }
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
            outputWriter.write(extension.getExporter().apply(imgMatrix));
            outputWriter.flush();
            outputWriter.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static ImgMatrix readAndCreateMatrix(File file) {
        try {
            ExtensionTypes extension = ExtensionTypes.getExtesionTypeOf(file.getName());
            if (extension != null){
                System.out.println("Found extension!");
                BufferedReader outputWriter = new BufferedReader(new FileReader(file));
                String result = outputWriter.lines().reduce((s, s2) -> s2 = s + (extension == ExtensionTypes.PGM ? "\n" : "") + s2).get();
                return extension.getImporter().apply(result);
            }
            return ImageHelper.converToImgMatrix(ImageHelper.readImage(file));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
