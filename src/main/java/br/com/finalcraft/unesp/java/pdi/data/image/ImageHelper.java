package br.com.finalcraft.unesp.java.pdi.data.image;

import br.com.finalcraft.unesp.java.pdi.data.ImgMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;

public class ImageHelper {

    public static BufferedImage readImage(File file){
        try {
            return ImageIO.read(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ImgMatrix convertToPGM(BufferedImage original){
        try {
            ImgMatrix imgMatrix = new ImgMatrix(original.getWidth(),original.getHeight());
            for (int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {
                    int color = original.getRGB(x, y);
                    // extract each color component
                    int red   = (color >>> 16) & 0xFF;
                    int green = (color >>>  8) & 0xFF;
                    int blue  = (color >>>  0) & 0xFF;
                    // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                    int luminance = (int) (red * 0.2126f + green * 0.7152f + blue * 0.0722f);
                    imgMatrix.setPixel(x,y,luminance);
                }
            }
            return imgMatrix;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage convertToBufferedImage(ImgMatrix imgMatrix){
        try {
            BufferedImage bufferedImage = new BufferedImage(imgMatrix.getWidth(),imgMatrix.getHeight(), 1);
            for (int y = 0; y < imgMatrix.getHeight(); y++) {
                for (int x = 0; x < imgMatrix.getWidth(); x++) {
                    // extract each color component
                    int luminance = imgMatrix.getLuminance(x,y);
                    // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                    bufferedImage.setRGB(x,y,new Color(luminance, luminance, luminance,0).getRGB());
                }
            }
            return bufferedImage;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
