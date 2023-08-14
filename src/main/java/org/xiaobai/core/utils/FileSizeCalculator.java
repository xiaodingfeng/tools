package org.xiaobai.core.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileSizeCalculator {
    public static String getFileSize(MultipartFile file) {
        long fileSize = file.getSize();

        return formatSize(fileSize);
    }

    public static String getFileSize(File file) {
        long fileSize = file.length();

        return formatSize(fileSize);
    }


    private static String formatSize(long fileSize) {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            double sizeKB = fileSize / 1024.0;
            return String.format("%.2f KB", sizeKB);
        } else {
            double sizeMB = fileSize / (1024.0 * 1024.0);
            return String.format("%.2f MB", sizeMB);
        }
    }
}
