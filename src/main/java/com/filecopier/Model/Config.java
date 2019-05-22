package com.filecopier.Model;

import com.filecopier.Logger.Logger;
import com.filecopier.Logger.Message;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {
    private final String CONFIG_FILE_NAME = "config.txt";

    // how to store this as final? static initialization?
    // everything would be static?
    private static Path pathToMainFolder;

    private List<Directory> directories;
//    private List<FileInfo> filesInfo;

    private List<String> lines;

    public Config(List<Directory> directories) {
        this.directories = directories;
//        this.filesInfo = filesInfo;
        this.lines = new ArrayList<>();
    }

    public static Path getPathToMainFolder() {
        return Config.pathToMainFolder;
    }

    public void read() {
        Logger.log("Reading config form file", Message.DEBUG);
        try (Stream<String> stream = Files.lines(Paths.get(CONFIG_FILE_NAME))) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            Logger.log("Reading from config file failed " + e.getMessage(), Message.ERROR);
//            e.printStackTrace();
        }
    }

    public void load() {
        loadPathToMainFolder();
        loadFoldersAndExtensions();
        lines = new ArrayList<>(); // releases resources?
    }

    public void loadPathToMainFolder() {
        String path = lines.get(0);
        path = path.substring(path.indexOf(":") + 1).trim() + FileSystems.getDefault().getSeparator();
        Config.pathToMainFolder = Paths.get(path);
        Logger.log("Loaded main folder: " + path, Message.DEBUG);
    }

    public void loadFoldersAndExtensions() {
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(":");
            String folderName = parts[0].trim();
            String[] extensions = parts[1].trim().split(" ");
            Directory dir = new Directory(folderName);
            directories.add(dir);
            Logger.log("Loaded folder: " + folderName, Message.DEBUG);
            for (int j = 0; j < extensions.length; j++) {
                dir.addExtension(extensions[j]);
                Logger.log("    Loaded ext: " + extensions[j], Message.DEBUG);
            }
        }
    }

    // whole file or only line with specific folder?
    public void save() {

    }

    public void show() {
        for (String line :
                lines) {
            System.out.println(line);
        }
    }
}
