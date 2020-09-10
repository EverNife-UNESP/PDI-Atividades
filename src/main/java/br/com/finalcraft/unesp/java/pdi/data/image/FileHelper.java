package br.com.finalcraft.unesp.java.pdi.data.image;

import br.com.finalcraft.unesp.java.pdi.data.wrapper.ImgWrapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileHelper {

    public static boolean export(File file, ImgWrapper imgWrapper, ExtensionTypes extension) {
        try {
            if (!file.getName().endsWith(extension.getExtension())){
                file = new File(file.getParentFile(), file.getName() + extension.getExtension());
            }
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
            outputWriter.write(extension.getExporter().apply(imgWrapper));
            outputWriter.flush();
            outputWriter.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void export(File file, ImgWrapper imgWrapper){
        try {
            ExtensionTypes knownExtension = ExtensionTypes.getExtesionTypeOf(file.getName());
            if (knownExtension != null){
                export(file, imgWrapper, knownExtension);
                return;
            }
            BufferedImage bufferedImage = ImageHelper.convertToBufferedImage(imgWrapper);
            ImageIO.write(bufferedImage, "png", file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ImgWrapper readAndCreateImageWrapper(File file) {
        try {
            ExtensionTypes extension = ExtensionTypes.getExtesionTypeOf(file.getName());
            if (extension != null){
                System.out.println("Found custom known extension!");
                BufferedReader outputWriter = new BufferedReader(new FileReader(file));
                String[] allLines = outputWriter.lines().toArray(String[]::new);
                return extension.getImporter().apply(String.join("\n", allLines));
            }
            System.out.println("Fallback on JavaCommonImage System!");
            BufferedImage bufferedImage = ImageHelper.readRegularImage(file);
            return ImageHelper.converToImgWrapper(bufferedImage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String[] readFileAndGetLines(File file){
        try {
            BufferedReader outputWriter = new BufferedReader(new FileReader(file));
            return outputWriter.lines().toArray(String[]::new);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
