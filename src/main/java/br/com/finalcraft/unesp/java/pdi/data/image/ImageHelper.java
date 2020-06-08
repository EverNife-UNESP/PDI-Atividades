package br.com.finalcraft.unesp.java.pdi.data.image;

import br.com.finalcraft.unesp.java.pdi.data.ImgMatrix;
import br.com.finalcraft.unesp.java.pdi.data.wrapper.ImgWrapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class ImageHelper {

    public static BufferedImage readRegularImage(File file){
        try {
            return ImageIO.read(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ImgMatrix converToImgMatrix(BufferedImage original){
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

    public static ImgWrapper converToImgWrapper(BufferedImage original){
        try {
            ImgMatrix imgMatrixRed = new ImgMatrix(original.getWidth(),original.getHeight());
            ImgMatrix imgMatrixGreen = new ImgMatrix(original.getWidth(),original.getHeight());
            ImgMatrix imgMatrixBlue = new ImgMatrix(original.getWidth(),original.getHeight());
            for (int y = 0; y < original.getHeight(); y++) {
                for (int x = 0; x < original.getWidth(); x++) {
                    int color = original.getRGB(x, y);
                    // extract each color component
                    int red   = (color >>> 16) & 0xFF;
                    int green = (color >>>  8) & 0xFF;
                    int blue  = (color >>>  0) & 0xFF;
                    // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                    imgMatrixRed.setPixel(x,y,red);
                    imgMatrixGreen.setPixel(x,y,green);
                    imgMatrixBlue.setPixel(x,y,blue);
                }
            }
            return new ImgWrapper(imgMatrixRed, imgMatrixGreen, imgMatrixBlue);
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
                    int luminance = imgMatrix.getPixel(x,y);
                    // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                    bufferedImage.setRGB(x,y,new Color(luminance, luminance, luminance,0).getRGB());
                }
            }
            return bufferedImage;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage convertToBufferedImage(ImgWrapper imgMatrix){
        if (imgMatrix.isBlackAndWhite()){
            return convertToBufferedImage(imgMatrix.getRed());
        }
        try {
            BufferedImage bufferedImage = new BufferedImage(imgMatrix.getRed().getWidth(),imgMatrix.getRed().getHeight(), 1);
            for (int y = 0; y < imgMatrix.getRed().getHeight(); y++) {
                for (int x = 0; x < imgMatrix.getRed().getWidth(); x++) {
                    // extract each color component
                    int luminance_red = imgMatrix.getRed().getPixel(x,y);
                    int luminance_green = imgMatrix.getGreen().getPixel(x,y);
                    int luminance_blue = imgMatrix.getBlue().getPixel(x,y);
                    // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
                    bufferedImage.setRGB(x,y,new Color(luminance_red, luminance_green, luminance_blue,0).getRGB());
                }
            }
            return bufferedImage;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    private static int counter = 0;
    public static void showImage(final ImgMatrix imgMatrix){
        final int num = ++counter;
        new Thread(){
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.getContentPane().setLayout(new FlowLayout());
                frame.getContentPane().add(new JLabel(new ImageIcon(ImageHelper.convertToBufferedImage(imgMatrix))));
                frame.pack();
                frame.setTitle("Image " + num);
                frame.setVisible(true);
                if (num == 1){
                    try {
                        while (true){
                            System.out.println("Por favor, insira um valor de luminosidade!");
                            Scanner input = new Scanner(System.in);
                            Integer newLight;
                            try {
                                newLight = Integer.parseInt(input.nextLine());
                            }catch (Exception e){
                                System.out.println("Valor invalido!!!");
                                continue;
                            }
                            frame.getContentPane().removeAll();
                            ImgMatrix modified = imgMatrix.setBright(newLight);
                            frame.getContentPane().add(new JLabel(new ImageIcon(ImageHelper.convertToBufferedImage(modified))));
                            frame.pack();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }



}
