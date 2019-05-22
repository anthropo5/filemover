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
import java.util.Objects;

public class Directory {
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
        extensions.add(ext);
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
                Logger.log("Directory " + this.name + " already exists", Message.DEBUG);
                return;
            }
            Files.createDirectory(this.path);
            Logger.log("Directory created: " + this.name, Message.INFO);
        } catch (IOException e) {
            Logger.log("Cant create directory " + e.getMessage(), Message.ERROR);
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
