package com.filemover.View;

import com.filemover.Model.Application;
import com.filemover.Model.Config;
import com.filemover.Model.Directory;
import com.filemover.Model.SortingOption;
import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private Application app;
    private Scanner reader;
    private CommandLineParser parser;
    private CommandLine cmd;
    private Options options;
    private HelpFormatter formatter;


    // diffrent name, because it is removing folder only from program memory and the you have to save config
    private final String CMD_REMOVE = "remove";
    private final String CMD_ADD = "add";
    private final String CMD_HELP = "help";
    private final String CMD_CONFIG = "config";
    private final String CMD_SHOW = "show";
    private final String CMD_MOVE = "move";
    private final List<String> CMD_QUIT = new ArrayList<>(Arrays.asList("quit", "q", "exit"));

    // move -fs --main - form sub folders - turn of checking apache cli tutorial
    // move // to sub by default
    // move -h

    public UserInterface(Application app) {
        this.app = app;
        this.reader = new Scanner(System.in);
        this.parser = new DefaultParser();
        this.options = new Options();
        this.formatter = new HelpFormatter();
    }

    public Options createOptionsRemove() {
        Options options = new Options();

        options.addOption(Option.builder("f")
                .longOpt("folder")
                .desc("remove directory")
                .hasArgs()
                .argName("folder1> <folder2> ... <folderN")
                .build());

        options.addOption(Option.builder("e")
                .longOpt("exts")
                .desc("remove extensions")
                .hasArgs()
                .argName("folder1> <ext1> ... <extN")
                .build());


        options.addOption(Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("show help")
                .build());

        return options;
    }

    public Options createOptionsAdd() {
        Options options = new Options();

        options.addOption(Option.builder("f")
                .longOpt("folder")
                .desc("add directory")
                .hasArgs()
                .argName("folder> <ext1> ... <extN")
                .build());

        options.addOption(Option.builder("e")
                .longOpt("exts")
                .desc("add extensions")
                .hasArgs()
                .argName("folder> <ext2> ... <extN")
                .build());


        options.addOption(Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("show help")
                .build());

        return options;
    }

    public Options createOptionsConfig() {
        Options options = new Options();

        options.addOption(Option.builder("l")
                .longOpt("load")
                .desc("load config again")
                .hasArg(false)
                .build());

        options.addOption(Option.builder("s")
                .longOpt("save")
                .desc("save config")
                .build());

        options.addOption(Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("show help")
                .build());

        return options;
    }
    public Options createOptionsMove() {
        Options options = new Options();

        options.addOption(Option.builder("f")
                .longOpt("files")
                .desc("move files from main-folder to sub-folders")
                .hasArg(false)
                .build());

        options.addOption(Option.builder("m")
                .longOpt("main")
                .desc("move files form sub-folder to main-folder")
                .build());

        options.addOption(Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("show help")
                .build());

        return options;
    }
    public Options createOptionsShow() {
        Options options = new Options();

        options.addOption(Option.builder("d")
                .longOpt("dirs")
                .desc("show directories")
                .hasArg(false)
                .build());

        options.addOption(Option.builder("dp")
                .longOpt("dir")
                .desc("show specified directory")
                .hasArg(true)
                .argName("folder1")
                .build());

        options.addOption(Option.builder("m")
                .longOpt("main")
                .desc("show files in main-folder")
                .hasArg(false)
                .build());

        options.addOption(Option.builder("mp")
                .longOpt("main-path")
                .desc("show main folder path")
                .hasArg(false)
                .build());

        options.addOption(Option.builder("s")
                .longOpt("sub")
                .desc("show files in sub-folders")
                .hasArg(false)
                .build());

        options.addOption(Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("show help")
                .build());

        return options;
    }
    public Options createOptionsQuit() {
        Options options = new Options();

        options.addOption(Option.builder("s")
                .longOpt("save")
                .desc("save config and quit")
                .hasArg(false)
                .build());

        options.addOption(Option.builder("h")
                .longOpt("help")
                .hasArg(false)
                .desc("show help")
                .build());

        return options;
    }

    public String createHelpMenu() {
        final int LEFT_PAD = 1;
        final int DESC_PAD = 3;
        final int WIDTH = 80;

        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, "move", ""
                , createOptionsMove(), LEFT_PAD, DESC_PAD, "");

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, "add", ""
                , createOptionsAdd(), LEFT_PAD, DESC_PAD, "");

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, "remove", ""
                , createOptionsRemove(), LEFT_PAD, DESC_PAD, "");

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, "show", ""
                , createOptionsShow(), LEFT_PAD, DESC_PAD, "");

        formatter.printWrapped(pw, WIDTH, "");
        final String HEADER_CONFIG = "Config is loaded form file when program is started.\n" +
                "If you made change in config file while program is running, you have to load config again.\n" +
                "Changes in config made in application are not saved automatically.\n" +
                "You have to save them or run 'quit' with proper flag: 'quit --save'";
        formatter.printHelp(pw, WIDTH, "config", HEADER_CONFIG
                , createOptionsQuit(), LEFT_PAD, DESC_PAD, "");

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, "quit", "Type 'quit' to exit program. \nAliases: 'q', 'exit'"
                , createOptionsQuit(), LEFT_PAD, DESC_PAD, "");

        formatter.printWrapped(pw, WIDTH, "\nType 'help' to show all commands");
        pw.flush();

        return out.toString();
    }

    public void runCLI() {
        System.out.println("Type 'help' to show all commands");
        HelpFormatter formatter = new HelpFormatter();
        boolean exit = false;
        while (!exit) {
            System.out.print("> ");
            String input = "help";
            reader = new Scanner(input);
            String[] args = reader.nextLine().split("\\s");
            exit = true;
            try {
                String command = args[0];

                if (command.equals(CMD_ADD)) {
                    options = createOptionsAdd();
                    cmd = parser.parse(options, args);
                    if (cmd.hasOption("f")) {
                        // add folders
                        String[] values = cmd.getOptionValues("f");
                        for (int i = 0; i < values.length; i++) {
                            System.out.println(values[i]);
                        }
                    } else if (cmd.hasOption("e")) {
                        // add exts
                        String[] values = cmd.getOptionValues("f");
                        for (int i = 0; i < values.length; i++) {
                            System.out.println(values[i]);
                        }
                    } else if (cmd.hasOption("h")) {
                        formatter.printHelp("add", options);
                    } else {
                        System.out.println("Invalid command. Type 'add --help'");
                    }
                    continue;
                } else if (command.equals(CMD_REMOVE)) {
                    options = createOptionsRemove();
                    cmd = parser.parse(options, args);
                    if (cmd.hasOption("f")) {
                        // remove folders
                        String[] values = cmd.getOptionValues("f");
                        for (int i = 0; i < values.length; i++) {
                            System.out.println(values[i]);
                        }
                    } else if (cmd.hasOption("e")) {
                        // remove exts
                        String[] values = cmd.getOptionValues("f");
                        for (int i = 0; i < values.length; i++) {
                            System.out.println(values[i]);
                        }
                    } else if (cmd.hasOption("h")) {
                        formatter.printHelp("remove", options);
                    } else {
                        formatter.printHelp("remove", options);
                    }
                    continue;
                } else if (command.equals(CMD_CONFIG)) {

                } else if (command.equals(CMD_MOVE)) {
                    // f m h
                } else if (command.equals(CMD_SHOW)) {
//                } else if (command.equals(CMD_REMOVE)) {
//                } else if (command.equals(CMD_REMOVE)) {
//                } else if (command.equals(CMD_REMOVE)) {
//                } else if (command.equals(CMD_REMOVE)) {

                } else if (command.equals(CMD_HELP)) {
                    System.out.println(createHelpMenu());
                } else if (CMD_QUIT.contains(command)) {
                    exit = true;
                    System.out.println("quit");
                } else {
                    System.out.println("Invalid input. Type 'help' to show all commands ");
                }

/*
                    switch (command) {
                    case CMD_REMOVE: {
                        options = createOptionsRemove();
                        cmd = parser.parse(options, args);
                        if (cmd.hasOption("f")) {
                            // remove folders
                            String[] values = cmd.getOptionValues("f");
                            for (int i = 0; i < values.length; i++) {
                                System.out.println(values[i]);
                            }
                        } else if (cmd.hasOption("e")) {
                            // remove exts
                            String[] values = cmd.getOptionValues("f");
                            for (int i = 0; i < values.length; i++) {
                                System.out.println(values[i]);
                            }
                        } else if (cmd.hasOption("h")) {
                            formatter.printHelp("remove", options);
                        } else {
                            formatter.printHelp("remove", options);
                        }
                        break;
                    }
                    case CMD_ADD: {
                        options = createOptionsAdd();
                        cmd = parser.parse(options, args);
                        if (cmd.hasOption("f")) {
                            // remove folders
                            String[] values = cmd.getOptionValues("f");
                            for (int i = 0; i < values.length; i++) {
                                System.out.println(values[i]);
                            }
                        } else if (cmd.hasOption("e")) {
                            // remove exts
                            String[] values = cmd.getOptionValues("f");
                            for (int i = 0; i < values.length; i++) {
                                System.out.println(values[i]);
                            }
                        } else if (cmd.hasOption("h")) {
                            formatter.printHelp("add", options);
                        } else {
                            formatter.printHelp("add", options);
                        }
                        break;
                    }
                    case CMD_HELP: {
//                        System.out.println("Allowed commands: ");
//                        options = createOptionsRemove();
//                        formatter.printHelp("remove", options);
//                        System.out.println("example: remove -f images videos");
//                        options = createOptionsAdd();
//                        formatter.printHelp("add", options);

                        System.out.println(createHelpMenu());

//                        System.out.println("Allowed commands: "
//                                            + "\n   remove - removing folders/exts"
//                                            + "\n   add    - adding folders/exts"
//                                            + "\n   move   - moves files to/from main/sub folder"
//                                            + "\nType 'command --help' to get information about usage"
//                                            );
                        break;


                    }
                    case "quit":
                    case "q":
                        exit = true;
                        System.out.println("quit");
                        break;

                    default:
                        System.out.println("Invalid command. Type 'help' to see proper syntax");
                }*/
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


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
                case 10: {
                    System.out.println("Type a folder name: ");
                    String name = reader.nextLine();
                    app.addDirectory(name); // name validation
                    break;
                }
                case 11: {
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
