package com.eyinfo.webx.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.eyinfo.foundation.utils.ObjectJudge;
import com.eyinfo.foundation.utils.TextUtils;
import com.madgag.gif.fmsware.GifDecoder;
import jakarta.servlet.ServletContext;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtils {

    //从url获取图片流
    public static InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //从流中获取图片格式
    public static String getImageFormat(InputStream inputStream) {
        try {
            if (inputStream == null) {
                return "";
            }
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            if (metadata == null) {
                return "";
            }
            String format = "";
            Iterable<Directory> directories = metadata.getDirectories();
            for (Directory directory : directories) {
                Collection<Tag> tags = directory.getTags();
                if (ObjectJudge.isNullOrEmpty(tags)) {
                    continue;
                }
                for (Tag tag : tags) {
                    String tagName = tag.getTagName();
                    String description = tag.getDescription();
                    if (TextUtils.equals(tagName, "Expected File Name Extension")) {
                        format = description;
                        break;
                    }
                }
            }
            return format.trim().toLowerCase();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    //从url中获取图片格式
    public static String getImageFormat(String url) {
        InputStream imageStream = getImageStream(url);
        return getImageFormat(imageStream);
    }

    //根据url获取BufferedImage
    public static BufferedImage fromImage(ServletContext servletContext, String url) {
        BufferedImage imageRead = null;
        InputStream istream = null;
        try {
            String format = getImageFormat(url);
            istream = getImageStream(url);
            if (TextUtils.equals(format, "gif")) {
                //这块是通过GifDecoder来处理gif图片
                GifDecoder decoder = new GifDecoder();
                if (istream == null) {
                    return null;
                }
                //通过GifDecoder去读取gif图片
                decoder.read(istream);
                //获取图片
                imageRead = decoder.getImage();
            } else if (TextUtils.equals(format, "png") ||
                    TextUtils.equals(format, "bmp") ||
                    TextUtils.equals(format, "jpg") ||
                    TextUtils.equals(format, "wbmp") ||
                    TextUtils.equals(format, "jpeg")) {
                if (istream == null) {
                    return null;
                }
                imageRead = ImageIO.read(istream);
            } else {
                istream = getImageStream(url);
                if (istream == null) {
                    return null;
                }
                File target = FileUtils.toFile(servletContext, istream, "png");
                if (target == null) {
                    return null;
                }
                imageRead = ImageIO.read(target);
                target.deleteOnExit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (istream != null) {
                    istream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageRead;
    }

    /**
     * 图片压缩
     *
     * @param servletContext ServletContext
     * @param file           待压缩的图片
     * @param scaleWidth     宽度比例,0~1值越小压缩出来的图片宽度越小
     * @param scaleHeight    高度比例,0~1值越小压缩出来的图片高度越小
     * @param quality        图片质量，0~1越接近于1质量越好，越接近于0质量越差
     * @param format         输出格式
     */
    public static File compression(ServletContext servletContext, File file, double scaleWidth, double scaleHeight, double quality, String format) throws IOException {
        File dir = FileUtils.getDir(servletContext);
        File outFile = FileUtils.createNewFile(servletContext, format);
        Thumbnails.of(file).scale(scaleWidth, scaleHeight).outputQuality(quality).outputFormat(format).toFile(outFile);
        return outFile;
    }

    /**
     * 图片压缩
     *
     * @param servletContext ServletContext
     * @param stream         待压缩的图片流
     * @param scaleWidth     宽度比例,0~1值越小压缩出来的图片宽度越小
     * @param scaleHeight    高度比例,0~1值越小压缩出来的图片高度越小
     * @param quality        图片质量，0~1越接近于1质量越好，越接近于0质量越差
     * @param format         输出格式
     */
    public static File compression(ServletContext servletContext, InputStream stream, double scaleWidth, double scaleHeight, double quality, String format) throws IOException {
        File dir = FileUtils.getDir(servletContext);
        File outFile = FileUtils.createNewFile(servletContext, format);
        Thumbnails.of(stream).scale(scaleWidth, scaleHeight).outputQuality(quality).outputFormat(format).toFile(outFile);
        return outFile;
    }

    private static boolean colorInRange(int color) {
        int color_range = 210;
        int red = (color & 0xff0000) >> 16;// 获取color(RGB)中R位
        int green = (color & 0x00ff00) >> 8;// 获取color(RGB)中G位
        int blue = (color & 0x0000ff);// 获取color(RGB)中B位
        // 通过RGB三分量来判断当前颜色是否在指定的颜色区间内
        return red >= color_range && green >= color_range && blue >= color_range;
    }

    private static boolean noBackground(byte[] imageBytes) throws Exception {
        try (ByteArrayInputStream basis = new ByteArrayInputStream(imageBytes)) {
            return noBackground(basis);
        }
    }

    private static boolean noBackground(InputStream is) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(is);
        return noBackground(bufferedImage);
    }

    private static boolean noBackground(BufferedImage bufferedImage) {
        if (bufferedImage == null)
            return false;
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int rgb = bufferedImage.getRGB(w, h);
                if (rgb >> 24 == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    //转换透明png
    public static byte[] convertTransparentPng(BufferedImage image) {
        if (image == null)
            return null;
        ByteArrayOutputStream baos = null;
        byte[] bytes;
        if (noBackground(image)) {
            /*背景透明时,直接返回原图*/
            baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = baos.toByteArray();
            return bytes;
        }
        try {
            baos = new ByteArrayOutputStream();
            // 高度和宽度
            int height = image.getHeight();
            int width = image.getWidth();
            // 初始化背景透明和内容透明的图片
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
            // 绘制Image的图片
            g2D.drawImage(imageIcon.getImage(), 0, 0, null);
            int alpha; // 图片透明度
            // 外层遍历是Y轴的像素
            for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
                // 内层遍历是X轴的像素
                for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    // 对当前颜色判断是否在指定区间内
                    if (colorInRange(rgb)) {
                        alpha = 0;
                    } else {
                        // 设置为不透明
                        alpha = 255;
                    }
                    //最前两位为透明度
                    rgb = (alpha << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(x, y, rgb);
                }
            }
            // 绘制设置了RGB的新图片,这一步感觉不用也可以,只是透明地方的深浅有变化而已，就像蒙了两层的感觉
            g2D.drawImage(bufferedImage, 0, 0, null);
            // 生成图片为PNG
            try {
                ImageIO.write(bufferedImage, "png", baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = baos.toByteArray();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    /**
     * 从html中获取图片集合
     *
     * @param htmlStr html字符串
     * @return 图片集合
     */
    public static List<String> getImagesFromHtml(String htmlStr) {
        List<String> pics = new ArrayList<>();
        String img;
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                String withoutDomain = getUrlWithoutDomain(m.group(1));
                if (!TextUtils.isEmpty(withoutDomain)) {
                    pics.add(withoutDomain);
                }
            }
        }
        return pics;
    }

    private static String getUrlWithoutDomain(String url) {
        try {
            URL murl = new URL(url);
            String path = murl.getPath();
            if (path.startsWith("/") && path.length() > 2) {
                path = path.substring(1);
            } else {
                path = "";
            }
            return path;
        } catch (Exception e) {
            return "";
        }
    }
}
