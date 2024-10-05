package com.eyinfo.webx;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomMultipartFile implements MultipartFile {
    private final FileItem fileItem;

    public CustomMultipartFile(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    @Override
    public String getName() {
        return fileItem.getFieldName();
    }

    @Override
    public String getOriginalFilename() {
        return fileItem.getName();
    }

    @Override
    public String getContentType() {
        return fileItem.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return fileItem.getSize() == 0;
    }

    @Override
    public long getSize() {
        return fileItem.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileItem.get();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return fileItem.getInputStream();
    }

    @Override
    public void transferTo(File dest) {
        try {
            fileItem.write(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
