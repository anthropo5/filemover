package com.filemover.Logger;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static List<String> info = new ArrayList<>();;
    private static List<String> warnings = new ArrayList<>();;
    private static List<String> errors = new ArrayList<>();;
    private static List<String> debug = new ArrayList<>();;

    public Logger() {
    }

    public static void log(String message, Message messageType) {

        if(messageType == Message.INFO) {
            info.add(message);
            System.out.println(message);
        } else if (messageType == Message.WARNING) {
            message = "! " + message;
            warnings.add(message);
            System.out.println(message);
        } else if (messageType == Message.ERROR) {
            message = "!!! " + message;
            errors.add(message);
            System.out.println(message);
        } else if (messageType == Message.DEBUG) {
            message = "? " + message;
            debug.add(message);
//            System.out.println(message);
        } else {
            System.out.println("STH GO WRONG WITH LOGGER");
        }
    }

    public static void clear() {
        info.clear();
        warnings.clear();
        errors.clear();
        debug.clear();
    }

    public static List<String> getInfo() {
        return info;
    }

    public static List<String> getWarnings() {
        return warnings;
    }

    public static List<String> getErrors() {
        return errors;
    }

    public static List<String> getDebug() {
        return debug;
    }

    public static void showInfo() {
        System.out.println("INFO");
        show(info);
    }
    public static void showWarnings() {
        System.out.println("WARNINGS");
        show(warnings);
    }
    public static void showDebug() {
        System.out.println("DEBUG");
        show(debug);
    }
    public static void showErrors() {
        System.out.println("ERRORS");
        show(errors);
    }

    public static void showAll() {
        showInfo();
        showDebug();
        showWarnings();
        showErrors();
    }


    private static void show(List<String> list) {
        for (String line :
                list) {
            System.out.println(line);
        }
    }

}
