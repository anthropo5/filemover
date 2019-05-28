package com.filemover.Model;

import com.filemover.Logger.Logger;
import com.filemover.Logger.Message;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.Objects;

public class FileInfo {
    private Path path;
    private long size;
    private FileTime creationTime;

    public FileInfo(Path fileName) {
        this.path = fileName;
        try {
            BasicFileAttributes attr = Files.readAttributes(fileName, BasicFileAttributes.class);
            this.size = attr.size();
            this.creationTime = attr.creationTime();
        } catch (IOException e) {
            System.out.println("Couldnt read file attributes: " + e.getMessage());
            e.printStackTrace();
        }
    }




    public boolean moveFile(Path dstFolder) throws IOException {
//        try {
            Path newPath = copyFileTo(dstFolder);
            deleteFile();
            this.path = newPath;
//            System.out.println("File: " + this.path.getFileName() + " moved to: " + dstFolder.getFileName());
            return true;
//        } catch (IOException e) {
//            // File permission problems are caught here.
////            System.out.println("Cannot move file " + this.path.getFileName() + "   to: " + dstFolder.getFileName());
//            Logger.log("Cannot move file " + this.path.getFileName()
//                        + "   to: " + dstFolder.getFileName()
//                        + " msg: " + e.getMessage(), Message.ERROR);
//            return false;
//        }
    }

    public Path copyFileTo(Path dstFolder) throws IOException {
        // co jesli plik nie istnieje?
            Path dstPath = Paths.get(dstFolder.toString(), this.getPath().getFileName().toString());
            if(Files.exists(dstPath)) {
                throw new IOException("File  " + dstPath.getFileName() + "   already exists in " + dstFolder);
            }
            Files.copy(this.path, dstPath);
            return dstPath;
    }

    public void deleteFile() throws IOException {
        Files.delete(this.path);
        Logger.log(this.path.getFileName() + " has been deleted.", Message.DEBUG);
    }





    public Path getPath() {
        return this.path;
    }

    public String getExtension() {
        String ext = this.path.toString();
        ext = ext.substring(ext.lastIndexOf(".") + 1);
        return ext;
    }

    public long getSize() {
        return size;
    }

    public FileTime getCreationTime() {
        return creationTime;
    }




    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("File: ").append(this.path.getFileName());
        sb.append("\n size: ").append(this.size);
        sb.append("\n created: ").append(this.creationTime);
        return sb.append("\n").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return Objects.equals(path, fileInfo.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }


}
