package com.rookitebyte;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class Files {

    private Files() {
    }

    public static InputStream getAsStream(String path) throws FileNotFoundException {
        fileNullCheck(path);


        var resourceAsStream = getResourceAsStream(path);
        if (resourceAsStream != null) {
            return resourceAsStream;
        }

        return getFileAsStream(new File(path));
    }

    private static void fileNullCheck(String path) throws FileNotFoundException {
        if (path == null || path.isBlank()) {
            throw new FileNotFoundException("Could not find path with null path");
        }
    }

    private static InputStream getFileAsStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    private static InputStream getResourceAsStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
