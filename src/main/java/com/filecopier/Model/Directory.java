package com.filecopier.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Directory {
    private static final Logger log = LoggerFactory.getLogger(Directory.class);

    private String name;
    private Path path;
    private List<String> extensions;


    public Directory(String name) {
        this.name = name;
        this.path = Paths.get(Config.getPathToMainFolder() + FileSystems.getDefault().getSeparator() + this.name);
        this.extensions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public Path getPath() {
        return this.path;
    }

    public void addExtension(String ext) {
        if (extensions.contains(ext)) {
            log.debug("Ext already added");
            return;
        }
        extensions.add(ext);
        log.debug("Adding ext: " + ext + " to: " + this.getName());
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Folder name:        ").append(name);
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

    public void createFolder() {
        try {
            if(Files.isDirectory(this.path)) {
                log.debug("Directory " + this.name + " already exists");
                return;
            }
            Files.createDirectory(this.path);
            log.info("Directory created: " + this.name);
        } catch (IOException e) {
            log.error("Cant create directory " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Directory directory = (Directory) o;
        return path.equals(directory.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
