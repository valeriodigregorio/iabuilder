package com.swia.iabuilder.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogcatFileDump implements Thread.UncaughtExceptionHandler {

    private final File crashFile;
    private final Thread.UncaughtExceptionHandler defaultHandler;

    public LogcatFileDump(File file) {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        crashFile = file;
    }

    public void uncaughtException(Thread t, Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();
        printWriter.close();

        try {
            FileWriter fileWriter = new FileWriter(crashFile);
            fileWriter.append(stackTrace);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ignored) {
        }

        defaultHandler.uncaughtException(t, e);
    }
}
