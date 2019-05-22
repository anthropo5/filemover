package com.filecopier.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // TODO
    //  config path - config has to be saved in folder with program. propably have to use relative path ./config.txt ?

    private static final Logger log = LoggerFactory.getLogger(Config.class);

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
        log.debug("Reading config form file");
        try (Stream<String> stream = Files.lines(Paths.get(CONFIG_FILE_NAME))) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            log.debug("Reading from config file failed");
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
        log.debug("Loaded main folder: " + path);
    }

    public void loadFoldersAndExtensions() {
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(":");
            String folderName = parts[0].trim();
            String[] extensions = parts[1].trim().split(" ");
            Directory dir = new Directory(folderName);
            directories.add(dir);
            log.debug("Loaded folder: " + folderName);
            for (int j = 0; j < extensions.length; j++) {
                dir.addExtension(extensions[j]);
                log.debug("    Loaded ext: " + extensions[j]);
            }
        }
    }

    // whole file or only line with specific folder?
    public String make() {
        log.debug("Making config");
        StringBuilder sb = new StringBuilder();

        sb.append("main folder: ").append(Config.getPathToMainFolder());
        log.debug("Making main folder " + Config.getPathToMainFolder());
        for (Directory dir :
                directories) {
            sb.append("\n").append(dir.getPath().getFileName()).append(": ");
            log.debug("Making folder: " + dir.getPath() );
            for (String ext :
                    dir.getExtensions()) {
                sb.append(ext).append(" ");
                log.debug("    Making ext: " + ext);
            }
        }
        return sb.toString();
    }
    
    public void save() {
        try {
            Path cfgPath = Paths.get(Config.getPathToMainFolder().toString(), "config.txt");
            log.debug("Saving config");
            Files.write(cfgPath, this.make().getBytes());
            log.debug("Config saved");
        } catch (IOException e) {
            log.error("Cannot save config file");
            e.printStackTrace();
        }
    }

    public void show() {
        for (String line :
                lines) {
            System.out.println(line);
        }
    }
}
