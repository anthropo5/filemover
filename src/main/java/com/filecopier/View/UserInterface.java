package com.filecopier.View;

import com.filecopier.Logger.Logger;
import com.filecopier.Model.Application;
import com.filecopier.Model.Config;
import com.filecopier.Model.Directory;
import com.filecopier.Model.SortingOption;

import java.util.Scanner;

public class UserInterface {
    private Application app;
    private Scanner reader;

    public UserInterface(Application app) {
        this.app = app;
        this.reader = new Scanner(System.in);
    }

    public void run() {
        app.run();

        showMainFolder();

        boolean exit = false;
        while (!exit) {
            showMenu();

            int number;
            try {
                System.out.print("> ");
                number = Integer.parseInt(reader.nextLine());
            } catch (Exception e) {
                System.out.println("!!! You have to type number related to option.");
                continue;
            }

            switch (number) {
                case 1:
                    app.showDirectories();
                    break;
                case 2:
                    app.loadAndShowFilesInMainFolder();
                    break;
                case 3:
                    app.moveFilesToSubFolders();
                    System.out.println("\nErrors and warnings occured while processing");
//                    Logger.showErrors();
//                    Logger.showWarnings();
//                    Logger.clear();
                    break;
                case 4:
                    app.moveAllFilesToMainFolder();
                    System.out.println("\nErrors and warnings occured while processing");
//                    Logger.showErrors();
//                    Logger.showWarnings();
//                    Logger.clear();
                    break;
                case 5:
                    showMainFolder();
                    break;
                case 6:
                    app.sortFilesBy(SortingOption.NAME);
                    app.showFiles();
                    break;
                case 7:
                    app.sortFilesBy(SortingOption.SIZE);
                    app.showFiles();
                    break;
                case 8:
                    app.sortFilesBy(SortingOption.EXTENSION);
                    app.showFiles();
                    break;
                case 9:
                    app.sortFilesBy(SortingOption.CREATION_TIME);
                    app.showFiles();
                    break;
                case 10:
                {
                    System.out.println("Type a folder name: ");
                    String name = reader.nextLine();
                    app.addDirectory(name); // name validation
                    break;
                }
                case 11:
                {
                    System.out.println("Choose a folder - type a name");
                    String line = reader.nextLine();
                    Directory dir = app.getDirectoryByName(line);
                    if (dir == null) {
                        System.out.println("Directory doesnt exists");
                    } else {
                        System.out.println("Type extensions separated by space: ");
                        line = reader.nextLine();
                        String[] exts = line.trim().split(" ");
                        for (int i = 0; i < exts.length; i++) {
                            dir.addExtension(exts[i]);
                        }
                    }
                    break;
                }
                case 12: {
                    System.out.println("Type folder to delete: ");
                    String line = reader.nextLine();
                    Directory dir = app.getDirectoryByName(line);
                    if (dir != null) {
                        app.deleteDirectory(dir);
                    } else {
                        System.out.println("Directory doesnt exists");
                    }
                    break;
                }
                case 13: {
                    System.out.println("Type folder to change");
                    String line = reader.nextLine();
                    Directory dir = app.getDirectoryByName(line);
                    if (dir == null) {
                        System.out.println("Directory doesnt exists");
                        break;
                    }
                    System.out.println("Type exts to delete");
                    line = reader.nextLine();
                    String[] exts = line.trim().split(" ");
                    for (int i = 0; i < exts.length; i++) {
                        dir.removeExtension(exts[i]);
                    }
                    break;
                }
                case 99:
                    exit = true;
                    break;
//                case 90:
//                    exit = true;
//                    app.makeAndSaveConfig();
//                    break;

                default:
                    System.out.println("Choose appropiate option");
            }
        }
        reader.close();
    }

    private void showMainFolder() {
        System.out.println("\nMain folder path: " + Config.getPathToMainFolder());
        System.out.println("Main folder name: " + Config.getPathToMainFolder().getFileName());
    }

    private void showMenu() {
        int option = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Menu:\n");
        sb.append(option++).append(". Show dirs \n");
        sb.append(option++).append(". Show files in main folder \n");
        sb.append(option++).append(". Copy from main folder to sub-folders \n");
        sb.append(option++).append(". Copy from folders to main folder \n");
        sb.append(option++).append(". Show main folder \n");
        sb.append("\n");
        sb.append(option++).append(". Sort files by name \n");
        sb.append(option++).append(". Sort files by size \n");
        sb.append(option++).append(". Sort files by extension \n");
        sb.append(option++).append(". Sort files by creation time \n");
        sb.append("\n");
        sb.append(option++).append(". Add folder \n");
        sb.append(option++).append(". Add extensions to folder \n");
        sb.append(option++).append(". Delete directory \n");
        sb.append(option++).append(". Remove extensions from specified directory \n");
        sb.append("99. Exit\n");
//        sb.append(option++).append(". Exit and save config\n");

        System.out.println(sb.toString());
    }
}
