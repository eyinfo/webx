package com.eyinfo.webx.utils;

import com.eyinfo.foundation.utils.GlobalUtils;
import com.eyinfo.webx.CustomMultipartFile;
import jakarta.servlet.ServletContext;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileUtils {

    public static File getDir(ServletContext servletContext) {
        String files = servletContext.getRealPath("files");
        File dir = new File(files);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    //创建新文件
    public static File createNewFile(ServletContext servletContext, String extension) {
        File dir = getDir(servletContext);
        String fileName = String.format("%s.%s", GlobalUtils.getNewGuid(), extension);
        return new File(dir.getAbsolutePath() + File.separator + fileName);
    }

    public static MultipartFile getMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CustomMultipartFile(item);
    }

    public static MultipartFile getMultipartFile(byte[] bytes, String fileName) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , fileName);
        InputStream sbs = new ByteArrayInputStream(bytes);
        try (OutputStream os = item.getOutputStream()) {
            IOUtils.copy(sbs, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CustomMultipartFile(item);
    }

    /**
     * BufferedImage转File
     *
     * @param servletContext ServletContext
     * @param bufferedImage  BufferedImage
     * @param format         图片格式
     * @return
     */
    public static File toFile(ServletContext servletContext, BufferedImage bufferedImage, String format) {
        File serverFile = FileUtils.createNewFile(servletContext, format);
        try {
            boolean status = ImageIO.write(bufferedImage, format, serverFile);
            if (status) {
                return serverFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File toFile(ServletContext servletContext, InputStream ins, String format) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = new BufferedInputStream(ins);
        try {
            File serverFile = createNewFile(servletContext, format);
            bos = new BufferedOutputStream(new FileOutputStream(serverFile));
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = bis.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return serverFile;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //保存byte[]为File
    public static File toFile(ServletContext servletContext, byte[] data, String format) {
        FileOutputStream fos = null;
        File serverFile = createNewFile(servletContext, format);
        try {
            try {
                fos = new FileOutputStream(serverFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.write(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return serverFile;
    }

    public static File toFile(ServletContext servletContext, MultipartFile file, String format) {
        File serverFile = FileUtils.createNewFile(servletContext, format);
        try {
            file.transferTo(serverFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverFile;
    }
}
