package br.com.finalcraft.unesp.java.pdi.data.image;

import br.com.finalcraft.unesp.java.pdi.data.ImgMatrix;
import br.com.finalcraft.unesp.java.pdi.data.wrapper.ImgWrapper;

import java.util.Arrays;
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
                int width = -1;
                int height = -1;
                for (int i = 0; i < allLines.length; i++) {
                    if (allLines[i].equalsIgnoreCase("255")){
                        width = Integer.parseInt(allLines[i -1].split(" ")[0]);
                        height = Integer.parseInt(allLines[i -1].split(" ")[1]);
                        allLines = Arrays.copyOfRange(allLines, i+1, allLines.length);
                        break;
                    }
                }
                System.out.println(String.format("width[%s]", width));
                System.out.println(String.format("height[%s]", height));
                String[] allNumbers = String.join(" ", allLines).trim().split(" ");

                ImgMatrix imgMatrix = new ImgMatrix(width,height);
                int index = 0;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        imgMatrix.matrix[x][y] = Integer.valueOf(allNumbers[index]);
                        index++;
                    }
                }
                return new ImgWrapper(imgMatrix);
            }),

    PPM(".ppm",
            imgWrapper -> {
                StringBuilder allPixelsInOrder = new StringBuilder();

                int[] allpixels_red = imgWrapper.getRed().getAllPixelsInOrder();
                int[] allpixels_green = imgWrapper.getGreen().getAllPixelsInOrder();
                int[] allpixels_blue = imgWrapper.getBlue().getAllPixelsInOrder();

                for (int i = 0; i < allpixels_red.length; i++) {
                    int r = allpixels_red[i];
                    int g = allpixels_green[i];
                    int b = allpixels_blue[i];
                    allPixelsInOrder.append(r + " " + g + " " + b + "\n");
                }

                return "P3" +
                        "\n" + imgWrapper.getWidth() + " " + imgWrapper.getHeight() +
                        "\n255\n" +
                        "\n" + allPixelsInOrder.toString();
            },
            string -> {
                String[] allLines = string.split("\n");
                int width = -1;
                int height = -1;
                for (int i = 0; i < allLines.length; i++) {
                    if (allLines[i].equalsIgnoreCase("255")){
                        width = Integer.parseInt(allLines[i -1].split(" ")[0]);
                        height = Integer.parseInt(allLines[i -1].split(" ")[1]);
                        allLines = Arrays.copyOfRange(allLines, i+1, allLines.length);
                        break;
                    }
                }
                System.out.println(String.format("width[%s]", width));
                System.out.println(String.format("height[%s]", height));

                String[] allNumbers = String.join(" ", allLines).trim().split(" ");

                System.out.println(Arrays.toString(allNumbers));

                ImgMatrix imgmatrix_red = new ImgMatrix(width,height);
                ImgMatrix imgmatrix_green = new ImgMatrix(width,height);
                ImgMatrix imgmatrix_blue = new ImgMatrix(width,height);
                int index = 0;
                for (int x = 0; x < height; x++) {
                    for (int y = 0; y < width; y++) {
                        imgmatrix_red.matrix[x][y] = Integer.valueOf(allNumbers[index++]);
                        imgmatrix_green.matrix[x][y] = Integer.valueOf(allNumbers[index++]);
                        imgmatrix_blue.matrix[x][y] = Integer.valueOf(allNumbers[index++]);
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
