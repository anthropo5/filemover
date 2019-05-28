package com.filemover.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

    // TODO
    //  sprawdzać czy rozszerzenie jest dodane do jakiegokolwiek folderu

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private Config cfg;
    private List<Directory> directories;
    private List<FileInfo> filesInfo;
    private Map<String, Path> extensionsToFolderPath;
    private Map<String, Directory> nameToDir;

    public Application() {
        this.directories = new ArrayList<>();
        this.filesInfo = new ArrayList<>();
        this.extensionsToFolderPath = new HashMap<>();
        this.nameToDir = new HashMap<>();
        this.cfg = new Config(this);
    }

    private void init() {
//        cfg.read();
        cfg.loadYAMLFile();
//        cfg.load();
        createAllFolders();
        addExtensionsToMap();
        initNameToDirMap();
        loadFilesFromMainFolder();

//        System.out.println(directories);

    }

    public void run() {
        init();

        cfg.makeYAML();
    }




    public int countFilesToCopy() {
        return filesInfo.size();
    }

    public void moveFiles(boolean toSubFolders) {

        Path dst = Config.getPathToMainFolder();
        int i = 1;
        int size = countFilesToCopy();
        log.debug("Files to copy: " + size);

        StringBuilder sb = new StringBuilder();
        for (FileInfo file : filesInfo) {
            sb.setLength(0);
            sb.append(i++).append("/").append(size).append(" ");

            if (toSubFolders) {
                dst = extensionsToFolderPath.get(file.getExtension());
                if (dst == null) {
                    sb.append("Files has not defined extension: ").append(file.getExtension())
                                .append(" file name: ").append(file.getPath().getFileName());
                    log.warn(sb.toString());
                    continue;
                }
            }

            try {
                if (file.moveFile(dst)) {
                    sb.append("File: ").append(file.getPath().getFileName()).append("   moved to: ").append(dst.getFileName());
                    log.info(sb.toString());
                }
            } catch (IOException e) {
                log.error(sb.append(e.getMessage()).toString());
            }
        }
    }

    public void moveFilesToSubFolders() {

        if (loadFilesFromMainFolder() < 1) {
            log.info("There is no files to copy in main folder.");
            return;
        }

        moveFiles(true);
    }

    public void moveAllFilesToMainFolder() {

        if (loadFilesFromAllFolders() < 1) {
            log.info("There is nothing co copy - folders are empty");
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
//                log.debug(ext + " ext is related with " + dir.getPath().getFileName());
            }
        }
    }

    private void initNameToDirMap() {
        for (Directory dir :
                directories) {
            this.nameToDir.put(dir.getName(), dir);
        }
    }

    private int loadFilesFromAllFolders() {
        log.debug("FilesInfo is cleared ");
        this.filesInfo.clear();
        try (Stream<Path> walk = Files.walk(Config.getPathToMainFolder())) {

            filesInfo = walk
                    .filter(Files::isRegularFile)
                    .map(FileInfo::new)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Couldn't load files from all folders" + e.getMessage());
//            e.printStackTrace();
        }
        log.debug(filesInfo.size() + " files has been loaded from all folders ");
        return this.filesInfo.size();
    }

    private int loadFilesFromMainFolder() {
        Path dir = Config.getPathToMainFolder();
        log.debug("FilesInfo is cleared ");
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
            log.error("Couldn't load files from main folder " + e.getMessage());

        }
        log.debug(filesInfo.size() + " files has been loaded from main folder ");
        return this.filesInfo.size();
    }

//    public void makeAndSaveConfig() {
//        cfg.make();
//        cfg.save();
//    }



    public Directory getDirectoryByName(String name) {
        return nameToDir.get(name);
    }



    public boolean addDirectory(String name) {
        log.debug("Adding directory " + name);
        Directory toAdd = new Directory(name);
        if(!directories.contains(toAdd)) {
            this.directories.add(toAdd);
            this.nameToDir.put(name, toAdd);
            log.debug("     Directory added" );
            return true;
        }
        log.debug("     Directory already exists");
        return false;
    }

    public void addDirectory(Directory dir) {
        this.directories.add(dir);
    }

    public boolean deleteDirectory(Directory dir) {
//        Directory toDelete = new Directory(name);
        log.debug("Deleting directory: " + dir.getName());
        if (directories.contains(dir)) {
            Iterator<Directory> it = directories.iterator();
            while (it.hasNext()) {
                Directory toDelete = it.next();
                if (dir.equals(toDelete)) {
                    it.remove();
                    log.debug("     dir deleted");
                    return true;
                }
            }
        }
        log.debug("     not in a list");
        return false;
    }

    public List<Directory> getDirectories() {
        return this.directories;
    }



    public void sortFilesBy(SortingOption sortingOption) {
        if(sortingOption == SortingOption.NAME) {
//            Logger.log("Sorting by name ", Message.DEBUG);
            filesInfo.sort(Comparator.comparing( file -> file.getPath().getFileName().toString()));
        } else if (sortingOption == SortingOption.EXTENSION) {
//            Logger.log("Sorting by extension ", Message.DEBUG);
            filesInfo.sort(Comparator.comparing(FileInfo::getExtension));
        } else if (sortingOption == SortingOption.SIZE) {
//            Logger.log("Sorting by size ", Message.DEBUG);
            filesInfo.sort(Comparator.comparing(FileInfo::getSize));
        } else if (sortingOption == SortingOption.CREATION_TIME) {
//            Logger.log("Sorting by creation time ", Message.DEBUG);
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