package ru.muffinnorth.nef.utils;

import java.io.File;

public class SystemFile extends File {
    public SystemFile(String pathname) {
        super(pathname);
    }

    public SystemFile(File file) {
        super(file.getAbsoluteFile().toURI());
    }
}
