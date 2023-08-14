package org.xiaobai.core.utils;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImagePixelExtractor {
    public static int[] getImagePixel(InputStream inputStream) throws IOException {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            int width = image.getWidth();
            int height = image.getHeight();

            return new int[]{width, height};
        } catch (Exception e) {
            log.error("Invalid or unsupported image format.");
            return new int[]{};
        }
    }

    public static int[] getImagePixel(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        return new int[]{width, height};
    }
}
