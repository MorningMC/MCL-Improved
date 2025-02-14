package morningmc.foliage.util.logging;

import morningmc.foliage.launcher.LauncherInfo;
import morningmc.foliage.util.CurrentDate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger {
    private final String className;
    private final File outputFile;
    private final File errorFile;
    private Level level;

    public Logger(String className) {
        this.className = className;

        CurrentDate date = new CurrentDate();
        String filename = date.filenameFormat();

        outputFile = new File(LauncherInfo.FileSystem.getLogDir(), filename + ".log");
        errorFile = new File(LauncherInfo.FileSystem.getLogDir(), filename + ".error.log");
    }

    public void log(Level level, String message) {
        String logString = generateLogMessage(level, message);

        write(message);
        if (level.isError()) {
            writeError(message);
        }
    }

    public void trace(String message) {
        log(Level.TRACE, message);
    }

    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void warn(String message) {
        log(Level.WARN, message);
    }

    public void error(String message) {
        log(Level.ERROR, message);
    }

    public void fatal(String message) {
        log(Level.FATAL, message);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    private String generateLogMessage(Level level, String message) {
        String logString = "";

        // date
        CurrentDate date = new CurrentDate();
        logString += date.standardFormat();

        // level
        logString += " [" + level + "] ";

        // class name
        logString += className + " - ";

        // message
        logString += message;

        return logString;
    }

    private void write(String message) {
        System.out.println(message);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(message.getBytes());
            outputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeError(String message) {
        System.err.println(message);

        try {
            FileOutputStream outputStream = new FileOutputStream(errorFile);
            outputStream.write(message.getBytes());
            outputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
