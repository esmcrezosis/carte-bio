/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.mcnp.client.dto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author HP
 */
public class FileUtils {

    public static byte[] toByteArrayAutoClosable(BufferedImage image, String type) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, type, out);
            return out.toByteArray();
        }
    }

    public static final byte[] get(String url, Map<String, String> headers) {
        try {
            URLConnection conn = new URL(url).openConnection();
            if (headers != null) {
                headers.entrySet().forEach((entry) -> {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                });
            }
            byte[] result;
            try (InputStream is = conn.getInputStream()) {
                result = IOUtils.toByteArray(is);
            }
            List<String> header = conn.getHeaderFields().get("Content-Disposition");
            if (header != null && header.size() > 0) {
                headers.put("Content-Disposition", header.get(0));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] fetchRemoteFile(String location) throws Exception {
        URL url = new URL(location);
        InputStream is = null;
        byte[] bytes = null;
        is = url.openStream(); //handle errors
        bytes = IOUtils.toByteArray(is);
        if (is != null) {
            is.close();
        }
        return bytes;
    }
}
