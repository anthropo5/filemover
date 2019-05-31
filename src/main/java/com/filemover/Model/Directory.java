package com.filemover.Model;

import com.filemover.Utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import static com.filemover.Model.Constants.*;

/*
    TODO
        - check create folder - testfolder2 is empty - why?
        - store extensions in Set? pros - zero duplicates; cons - order?
        - based on path to folder we can retrive file name and root path. currently path == root path
 */

public class Directory {
    private static final Logger log = LoggerFactory.getLogger(Directory.class);

    private String name;
    private Path path; // root folder
    private List<String> extensions;

    public Directory(String name, String path, List<String> extensions) {
        this.name = name;
        this.extensions = extensions;
        log.debug("Exts to str " + extensions.toString());
        if (path == null) {
            this.path = Config.getPathToMainFolder();
        } else {
            this.path = Paths.get(path);
        }
    }

    public Directory(String name) {
        this(name, null, new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public Path getParent() {
        return this.path;
    }

    public Path getPath() {
        return Paths.get(getParent().toString() + FileSystems.getDefault().getSeparator() + getName());
    }
    public void addExtension(String ext) {
        ext = StringUtils.pepare(ext);
        if (extensions.contains(ext)) {
            log.debug("Ext already added");
            return;
        }
        extensions.add(ext);
        log.debug("Adding ext: " + ext + " to: " + this.getName());
    }

    public void addExtensions(List<String> extensions) {
        for (String ext :
                extensions) {
            addExtension(ext);
        }
    }
    public void addExtensions(String extensions) {
        extensions = StringUtils.pepare(extensions);
//        extensions = StringUtils.removeSpecialChars(extensions);
        String[] exts = extensions.split("\\s");
        for (String ext :
                exts) {
            addExtension(ext.trim());
        }
    }

    public boolean removeExtension(String ext) {
        log.debug("Removing extension " + ext + " from: " + this.getName());
        if (!extensions.contains(ext)) {
            log.debug("     ext is not related with folder " + this.name);
            return false;
        }

        Iterator<String> iterator = extensions.iterator();
        while (iterator.hasNext()) {
            String toDelete = iterator.next();
            if (toDelete.equals(ext)) {
                iterator.remove();
                log.debug("     ext has been removed: " + ext);
                return true;
            }
        }
        return false;
    }

    public void removeExtensions(String extensions) {
        extensions = StringUtils.pepare(extensions);
        String[] exts = extensions.split(" ");
        for (String ext :
                exts) {
            removeExtension(ext.trim());
        }
    }

    public void removeExtensions(List<String> extensions) {
        for (String ext :
                extensions) {
            removeExtension(ext);
        }
    }


    public void createFolder() {
        try {
            // if path exists should be checked i constructor? if null creates record in yaml file?
            Path path = Paths.get(this.path.toString() + FileSystems.getDefault().getSeparator() + this.name);
            log.debug("Path to folder: " + path.toString());
            if (!Files.exists(this.path)) {
                throw new FileNotFoundException("Directory \"" + this.name + "\" do not exists. Check path in config.yml file");
            } else if (Files.isRegularFile(path)) {
                throw new NotDirectoryException(this.name + " already exists and is not a directory");
            } else if (Files.isDirectory(path)) {
                throw new FileAlreadyExistsException("Directory " + this.name +
                                                    " already exists. Files from main folder will be copied to it");
            }
            Files.createDirectory(path);
            log.info("Directory created: " + this.name);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            log.debug(e.getMessage());
        } catch (NotDirectoryException e) {
            log.debug(e.getMessage());
        } catch (FileAlreadyExistsException e) {
            log.debug(e.getMessage());
        } catch (IOException e) {
            log.error("IO Exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Directory directory = (Directory) o;
        return path.equals(directory.path) && name.equals(directory.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Folder name:        ").append(name);
        sb.append("\n       Path:  ");
        sb.append(this.path.toString());
        sb.append("\n       Extensions:  ");

        for (int j = 0; j < extensions.size(); j++) {
            sb.append(extensions.get(j) + " ");
            if ((j + 1) % 4 == 0) {
                sb.append("\n                    ");
            }
        }

        sb.append("\n");
        return sb.toString();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> dir = new LinkedHashMap<>();
        dir.put(PROP_NAME, this.name);
        if (!this.path.toString().equals(Config.getPathToMainFolder().toString())) {
            dir.put(PROP_PATH, this.path.toString());
        }
        dir.put(PROP_EXTENSIONS, this.extensions);
        return dir;
    }


}
