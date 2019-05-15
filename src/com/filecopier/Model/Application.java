package com.filecopier.Model;

import com.filecopier.Logger.Logger;
import com.filecopier.Logger.Message;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*

Application moves (copy -> delete) files from defined folder to sub-folders.
Files are related to sub-folders with extension.
Relation is defined in config.txt

 todo
    how to refresh ui ? sth like progress bar
    optional move to default folder
    main directory class - allows user to add few folders to manage

*/


public class Application {
    private Config cfg;
    private List<Directory> directories;
    private List<FileInfo> filesInfo;
    private HashMap<String, Path> extensionsToFolderPath;

    public Application() {
        this.directories = new ArrayList<>();
        this.filesInfo = new ArrayList<>();
        this.cfg = new Config(this.directories);
        this.extensionsToFolderPath = new HashMap<>();
    }




    private void init() {
        cfg.read();
        cfg.load();
        createAllFolders();
        addExtensionsToMap();
        loadFilesFromMainFolder();
    }

    public void run() {
        init();
    }




    public int countFilesToCopy() {
        return filesInfo.size();
    }

    public void moveFiles(boolean toSubFolders) {

        Path dst = Config.getPathToMainFolder();
        int i = 1;
        int size = countFilesToCopy();
        Logger.log("Files to copy: " + size, Message.INFO);

        StringBuilder sb = new StringBuilder();
        for (FileInfo file : filesInfo) {
            sb.setLength(0);
            sb.append(i++).append("/").append(size).append(" ");

            if (toSubFolders) {
                dst = extensionsToFolderPath.get(file.getExtension());
                if (dst == null) {
                    sb.append("Files has not defined extension: ").append(file.getExtension())
                                .append(" file name: ").append(file.getPath().getFileName());
                    Logger.log(sb.toString(), Message.WARNING);
                    continue;
                }
            }

            try {
                if (file.moveFile(dst)) {
                    sb.append("File: ").append(file.getPath().getFileName()).append("   moved to: ").append(dst.getFileName());
                    Logger.log(sb.toString(), Message.INFO);
                }
            } catch (IOException e) {
                Logger.log(sb.append(e.getMessage()).toString(), Message.ERROR);
            }
        }
    }

    public void moveFilesToSubFolders() {

        if (loadFilesFromMainFolder() < 1) {
            Logger.log("There is no files to copy in main folder.", Message.INFO);
            return;
        }

        moveFiles(true);
    }

    public void moveAllFilesToMainFolder() {

        if (loadFilesFromAllFolders() < 1) {
            Logger.log("There is nothing co copy - folders are empty", Message.INFO);
            return;
        }

        moveFiles(false);
    }





    private void createAllFolders() {
        for (Directory directory :
                this.directories) {
            directory.createFolder();
        }
    }

    private void addExtensionsToMap() {
        for (Directory dir :
                directories) {
            for (String ext :
                    dir.getExtensions()) {
                extensionsToFolderPath.put(ext, dir.getPath());
                Logger.log(ext + " ext is related with " + dir.getPath().getFileName(), Message.DEBUG);
            }
        }
    }

    private int loadFilesFromAllFolders() {
        Logger.log("FilesInfo is cleared ", Message.DEBUG);
        this.filesInfo.clear();
        try (Stream<Path> walk = Files.walk(Config.getPathToMainFolder())) {

            filesInfo = walk
                    .filter(Files::isRegularFile)
                    .map(FileInfo::new)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            Logger.log("Couldn't load files from all folders" + e.getMessage(), Message.ERROR);
//            e.printStackTrace();
        }
        Logger.log(filesInfo.size() + " files has been loaded from all folders ", Message.DEBUG);
        return this.filesInfo.size();
    }

    private int loadFilesFromMainFolder() {
        Path dir = Config.getPathToMainFolder();
        Logger.log("FilesInfo is cleared ", Message.DEBUG);
        this.filesInfo.clear();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path fileName : stream) {
                if (Files.isDirectory(fileName)) {
                    continue;
                }

                FileInfo fileInfo = new FileInfo(fileName);

                if (this.filesInfo.contains(fileInfo)) {
                    continue;
                }
                this.filesInfo.add(fileInfo);
            }
        } catch (IOException | DirectoryIteratorException e) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            Logger.log("Couldn't load files from main folder " + e.getMessage(), Message.ERROR);

        }
        Logger.log(filesInfo.size() + " files has been loaded from main folder ", Message.DEBUG);
        return this.filesInfo.size();
    }



    public void sortFilesBy(SortingOption sortingOption) {
        if(sortingOption == SortingOption.NAME) {
            Logger.log("Sorting by name ", Message.DEBUG);
            filesInfo.sort(Comparator.comparing( file -> file.getPath().getFileName().toString()));
        } else if (sortingOption == SortingOption.EXTENSION) {
            Logger.log("Sorting by extension ", Message.DEBUG);
            filesInfo.sort(Comparator.comparing(FileInfo::getExtension));
        } else if (sortingOption == SortingOption.SIZE) {
            Logger.log("Sorting by size ", Message.DEBUG);
            filesInfo.sort(Comparator.comparing(FileInfo::getSize));
        } else if (sortingOption == SortingOption.CREATION_TIME) {
            Logger.log("Sorting by creation time ", Message.DEBUG);
            filesInfo.sort(Comparator.comparing(FileInfo::getCreationTime));
        }
    }


    public void showFiles() {
        System.out.println("There is " + filesInfo.size() + " files in main folder\n");
        for (FileInfo file :
                filesInfo) {
            System.out.println(file);
        }
    }

    public void showDirectories() {
        for (Directory dir :
                directories) {
            System.out.println(dir);
        }
    }

    public void loadAndShowFilesInMainFolder() {
        loadFilesFromMainFolder();
        showFiles();
    }

    public void showExtensionsToPath() {
        for (String ext : extensionsToFolderPath.keySet()) {
            System.out.println("ext: " + ext + " folder: " + extensionsToFolderPath.get(ext));
        }
    }
}
