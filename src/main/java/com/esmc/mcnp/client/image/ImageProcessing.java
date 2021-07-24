/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.image;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author HP
 */
public class ImageProcessing {

    private String name;

    public ImageProcessing() {
    }

    public ImageProcessing(String name) {
        this.name = name;
    }

    public BufferedImage loadImage(String name) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(name));
        } catch (IOException ex) {
            Logger.getLogger(ImageProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return img;
    }

    public javafx.scene.image.Image loadFxImage(String name) {
        String urlStr = getClass()
                .getClassLoader()
                .getResource(name)
                .toExternalForm();
        return new javafx.scene.image.Image(urlStr, true);
    }

    public static void addImageWatermark(File watermark, String type, File source, File destination) throws IOException {
        BufferedImage image = ImageIO.read(source);
        BufferedImage overlay = resize(ImageIO.read(watermark), 150, 150);
        // determine image type and handle correct transparency
        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
        // initializes necessary graphic properties
        Graphics2D w = (Graphics2D) watermarked.getGraphics();
        w.drawImage(image, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        w.setComposite(alphaChannel);
        // calculates the coordinate where the String is painted
        int centerX = image.getWidth() / 2;
        int centerY = image.getHeight() / 2;
        // add text watermark to the image
        w.drawImage(overlay, centerX, centerY, null);
        ImageIO.write(watermarked, type, destination);
        w.dispose();
    }

    public BufferedImage transform(BufferedImage image, String name) {
        BufferedImage IMAGE_FRAME = loadImage(name);
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage modified = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = modified.createGraphics();
        g2.drawImage(image, null, 0, 0);
        g2.drawImage(IMAGE_FRAME, null, 0, 0);
        g2.dispose();
        modified.flush();
        return modified;
    }

    public BufferedImage writeThePicture(File file, BufferedImage photo, BufferedImage qrcode) {
        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        try {
            graphics.setBackground(java.awt.Color.WHITE);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            graphics.drawRect(0, 0, 400, 400);
            graphics.drawImage(photo, 0, 0, null);
            graphics.drawImage(qrcode, 0, 0, null);
            ImageIO.write(image, "png", file);
            if (file.exists()) {
                System.out.println(file.getAbsolutePath());
                return image;
            } else {
                System.out.println("No, there is no motherfucker image here !!");
                return null;
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageProcessing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public Dimension getPreferredSize(BufferedImage img) {
        if (img == null) {
            return new Dimension(100, 100);
        } else {
            return new Dimension(img.getWidth(null), img.getHeight(null));
        }
    }

    public String getName() {
        return name;
    }

    public void setPath(String name) {
        this.name = name;
    }

}
