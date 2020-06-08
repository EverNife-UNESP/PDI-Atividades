package br.com.finalcraft.unesp.java.pdi.data.image;

import br.com.finalcraft.unesp.java.pdi.data.ImgMatrix;
import br.com.finalcraft.unesp.java.pdi.data.wrapper.ImgWrapper;

import java.util.function.Function;

public enum ExtensionTypes {
    PGM(".pgm",
            imgWrapper -> {
                StringBuilder allPixelsInOrder = new StringBuilder();
                ImgMatrix imgMatrix = imgWrapper.getRed();
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
                return new ImgWrapper(imgMatrix);
            }),

    PPM(".ppm",
            imgWrapper -> {
                StringBuilder allPixelsInOrder = new StringBuilder();
                ImgMatrix imgMatrix = imgWrapper.getRed();

                int[] allpixels_red = imgWrapper.getRed().getAllPixelsInOrder();
                int[] allpixels_green = imgWrapper.getGreen().getAllPixelsInOrder();
                int[] allpixels_blue = imgWrapper.getBlue().getAllPixelsInOrder();

                for (int i : allpixels_red) {
                    allPixelsInOrder.append(allpixels_red[i] + " " + allpixels_green[i] + " " + allpixels_blue[i] + "\n");
                }
                return "P3" +
                        "\n" + imgMatrix.getWidth() + " " + imgMatrix.getHeight() +
                        "\n255\n" +
                        "\n" + allPixelsInOrder.toString();
            },
            string -> {
                String[] allLines = string.split("\n");
                int width = Integer.parseInt(allLines[2].split(" ")[0]);
                int height = Integer.parseInt(allLines[2].split(" ")[1]);
                ImgMatrix imgmatrix_red = new ImgMatrix(width,height);
                ImgMatrix imgmatrix_green = new ImgMatrix(width,height);
                ImgMatrix imgmatrix_blue = new ImgMatrix(width,height);
                int index = 4;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        imgmatrix_red.matrix[x][y] = Integer.valueOf(allLines[index++]);
                        imgmatrix_green.matrix[x][y] = Integer.valueOf(allLines[index++]);
                        imgmatrix_blue.matrix[x][y] = Integer.valueOf(allLines[index++]);
                    }
                }
                return new ImgWrapper(imgmatrix_red, imgmatrix_green, imgmatrix_blue);
            }),

    FC_IMAGE(".fcimage",
            imgWrapper -> {
                return imgWrapper.toString();
            },
            string -> {
                return ImgWrapper.fromString(string);
            }
    );

    final String extension;
    final Function<ImgWrapper, String> exporter;
    final Function<String, ImgWrapper> importer;

    ExtensionTypes(String extension, Function<ImgWrapper, String> exporter, Function<String, ImgWrapper> importer) {
        this.extension = extension;
        this.exporter = exporter;
        this.importer = importer;
    }

    public String getExtension() {
        return extension;
    }

    public Function<ImgWrapper, String> getExporter() {
        return exporter;
    }

    public Function<String, ImgWrapper> getImporter() {
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

    public static String[] allExtensions(){
        String[] allExtensions = new String[values().length];
        int index = 0;
        for (ExtensionTypes extensionn : values()) {
            allExtensions[index] = extensionn.getExtension().toLowerCase();
            index++;
        }
        return allExtensions;
    }
}
