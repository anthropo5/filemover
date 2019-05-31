package com.filemover.View;

import com.filemover.Model.Application;
import com.filemover.Model.Config;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLI {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static final String OPTION_MOVE_FILES_TO_SUB_FOLDERS = "f";

    public static final String OPTION_SHOW_DIRS = "d";
    public static final String OPTION_SHOW_DIR = "dp";
    public static final String OPTION_SHOW_MAIN = "m";
    public static final String OPTION_SHOW_MAIN_PATH = "mp";
    public static final String OPTION_SHOW_SUB_FOLDERS = "s";
    public static final String OPTION_HELP = "h";
    public static final String OPTION_MOVE_FILES_TO_MAIN_FOLDER = "m";
    public static final String OPTION_ADD_FOLDER = "f";
    public static final String OPTION_ADD_EXTENSIONS = "e";
    public static final String OPTION_REMOVE_FOLDER = "f";
    public static final String OPTION_REMOVE_EXTENSIONS = "e";
    public static final String OPTION_CONFIG_LOAD = "l";

    private Application app;
    private Scanner reader;
    private CommandLineParser parser;
    private CommandLine cmd;
    private Options options;
    private HelpFormatter formatter;
    private StringWriter out = new StringWriter();
    private PrintWriter pw = new PrintWriter(out);

    private final int LEFT_PAD = 1;
    private final int DESC_PAD = 3;
    private final int WIDTH = 80;


    // diffrent name, because it is removing folder only from program memory and the you have to save config
    private final String CMD_REMOVE = "remove";
    private final String CMD_ADD = "add";
    private final String CMD_HELP = "help";
    private final String CMD_CONFIG = "config";
    private final String CMD_SHOW = "show";
    private final String CMD_MOVE = "move";
    private final List<String> CMD_QUIT = new ArrayList<>(Arrays.asList("quit", "q", "exit"));

    private final String HEADER_CONFIG = "Config is loaded form file when program is started.\n" +
            "If you made change in config file while program is running, you have to load config again.\n" +
            "Changes in config made in application are not saved automatically.\n" +
            "You have to save them or run 'quit' with proper flag: 'quit --save'";
    private final String HEADER_QUIT = "Type 'quit', 'q', 'exit' to exit program.";





    /*
    TODO
        how getArgs works. test on executeAdd
        extract constants
        use lambda expressions to create execute commands  ??? not sure if possible
     */

    public CLI(Application app) {
        this.app = app;
        this.reader = new Scanner(System.in);
        this.parser = new DefaultParser();
        this.options = new Options();
        this.formatter = new HelpFormatter();
    }

    public void executeMove(String[] args) throws ParseException {
        options = createOptionsMove();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_MOVE_FILES_TO_SUB_FOLDERS)) {
            app.moveFilesToSubFolders();
        } else if (cmd.hasOption(OPTION_MOVE_FILES_TO_MAIN_FOLDER)) {
            app.moveAllFilesToMainFolder();
        } else if (cmd.hasOption(OPTION_HELP)) {
            formatter.printHelp(CMD_MOVE, createOptionsMove());
        } else {
            printInvalidCommand(CMD_CONFIG);
        }
    }

    public void executeAdd(String[] args) throws ParseException {
        options = createOptionsAdd();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_ADD_FOLDER)) {
            // add folders
//            String[] val = cmd.getOptionValues(OPTION_ADD_FOLDER);
//            List<String> values = new ArrayList<>(Arrays.asList(val));
//            app.addDirectoryWithExts(values);
//

            List<String> vals = cmd.getArgList();
//            cmd.getOptionValues()
            String[] values = cmd.getOptionValues(OPTION_ADD_FOLDER);
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
            System.out.println("----");
            for (String v :
                    vals) {
                System.out.println(v);
            }
        } else if (cmd.hasOption(OPTION_ADD_EXTENSIONS)) {
            // add exts
            String[] val = cmd.getOptionValues(OPTION_ADD_EXTENSIONS);
            List<String> values = new ArrayList<>(Arrays.asList(val));
            app.addExtensionsToFolder(values);
        } else if (cmd.hasOption(OPTION_HELP)) {
            formatter.printHelp(CMD_ADD, options);
        } else {
            printInvalidCommand(CMD_ADD);
        }
    }

//    TODO add remove all
    public void executeRemove(String[] args) throws ParseException {
        options = createOptionsRemove();
        cmd = parser.parse(options, args);
//        cmd.ge
        if (cmd.hasOption(OPTION_REMOVE_FOLDER)) {
            String[] val = cmd.getOptionValues(OPTION_REMOVE_FOLDER);
            List<String> values = new ArrayList<>(Arrays.asList(val));
            app.removeDirectories(values);
        } else if (cmd.hasOption(OPTION_REMOVE_EXTENSIONS)) {
            // remove exts
            String[] val = cmd.getOptionValues(OPTION_REMOVE_EXTENSIONS);
            List<String> values = new ArrayList<>(Arrays.asList(val));
        } else if (cmd.hasOption(OPTION_HELP)) {
            formatter.printHelp(CMD_REMOVE, options);
        } else {
            formatter.printHelp(CMD_REMOVE, options);
        }
    }

    // get arg list on beginning, pass first as folder name -> delete first -> pass exts
    public void executeConfig(String[] args) throws ParseException {

        options = createOptionsConfig();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_CONFIG_LOAD)) {
            // load cfg
            List<String> vals = cmd.getArgList();
            String[] values = cmd.getOptionValues(OPTION_CONFIG_LOAD);
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
            System.out.println("----");
            for (String v :
                    vals) {
                System.out.println(v);
            }
        } else if (cmd.hasOption("s")) {
            // save cfg
            String[] values = cmd.getOptionValues("s");
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        } else if (cmd.hasOption("h")) {
            formatter.printHelp("remove", options);
        } else {
            formatter.printHelp("remove", options);
        }
    }

    public void executeShow(String[] args) throws ParseException {
        options = createOptionsShow();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_SHOW_DIRS)) {
            // load cfg
            String[] values = cmd.getOptionValues(OPTION_SHOW_DIRS);
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        } else if (cmd.hasOption(OPTION_SHOW_DIR)) {
            // save cfg
            String[] values = cmd.getOptionValues(OPTION_SHOW_DIR);
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        } else if (cmd.hasOption(OPTION_SHOW_MAIN)) {
            // save cfg
            String[] values = cmd.getOptionValues(OPTION_SHOW_MAIN);
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        } else if (cmd.hasOption(OPTION_SHOW_MAIN_PATH)) {
            // save cfg
            String[] values = cmd.getOptionValues(OPTION_SHOW_MAIN_PATH);
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        } else if (cmd.hasOption(OPTION_SHOW_SUB_FOLDERS)) {
            // save cfg
            String[] values = cmd.getOptionValues(OPTION_SHOW_SUB_FOLDERS);
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        } else if (cmd.hasOption(OPTION_HELP)) {
            formatter.printHelp("show", options);
        } else {
            formatter.printHelp("show", options);
        }
    }

    public void executeQuit(String[] args) throws ParseException {
        options = createOptionsQuit();
        cmd = parser.parse(options, args);
        if (cmd.hasOption("s")) {
            // save cfg
            String[] values = cmd.getOptionValues("s");
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        }
    }

//  public void executeAdd(String[] args) throws ParseException {
//  }

    public void executeHelp() {
        System.out.println(createHelpAll());
    }

    public boolean execute(String input) {
        String[] args = input.split("\\s");
        String command = args[0].trim();
        try {
            if (command.equals(CMD_MOVE)) {
                executeMove(args);
            } else if (command.equals(CMD_ADD)) {
                executeAdd(args);
            } else if (command.equals(CMD_REMOVE)) {
                executeRemove(args);
            } else if (command.equals(CMD_CONFIG)) {
                executeConfig(args);
                // f m h
            } else if (command.equals(CMD_SHOW)) {
                executeShow(args);
//                } else if (command.equals(CMD_REMOVE)) {
//                } else if (command.equals(CMD_REMOVE)) {
//                } else if (command.equals(CMD_REMOVE)) {
//                } else if (command.equals(CMD_REMOVE)) {

            } else if (command.equals(CMD_HELP)) {
//            System.out.println(createHelpAll());
                executeHelp();
            } else if (CMD_QUIT.contains(command)) {
                executeQuit(args);
//                exit = true;
//                System.out.println("quit");
                return true;
            } else {
                System.out.println("Invalid input. Type 'help' to show all commands ");
            }
//            return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void runCLI() {
        log.debug("CLI started");
            execute("add qwe xzc -f folder sd xcz we");

//            System.out.println("-------------------------------------------------");
//            execute("help");


//        System.out.println("Type 'help' to show all commands");
//        boolean exit = false;
//        while (!exit) {
//            System.out.print("> ");
////            String fakeInput = "help";
////            reader = new Scanner(fakeInput);
//            String input = reader.nextLine();
////            String[] args = reader.nextLine().split("\\s");
////            exit = true;
//            exit = execute(input);
//        }
    }

    public void printHelpConfig() {
        printHelp(CMD_CONFIG, HEADER_CONFIG, createOptionsConfig(), "");
    }


    public void printHelp(String command, String header, Options options, String footer) {
        formatter.printHelp(command, header, options, footer);
    }

    public void printHelpMove() {
        formatter.printHelp(CMD_MOVE, HEADER_CONFIG, createOptionsMove(), "");
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

    public Options createOptionsShow() {


        Options options = new Options();

        options.addOption(Option.builder(OPTION_SHOW_DIRS)
                .longOpt("dirs")
                .desc("show directories")
                .hasArg(false)
                .build());

        options.addOption(Option.builder(OPTION_SHOW_DIR)
                .longOpt("dir")
                .desc("show specified directory")
                .hasArg(true)
                .argName("folder1")
                .build());

        options.addOption(Option.builder(OPTION_SHOW_MAIN)
                .longOpt("main")
                .desc("show files in main-folder")
                .hasArg(false)
                .build());

        options.addOption(Option.builder(OPTION_SHOW_MAIN_PATH)
                .longOpt("main-path")
                .desc("show main folder path")
                .hasArg(false)
                .build());

        options.addOption(Option.builder(OPTION_SHOW_SUB_FOLDERS)
                .longOpt("sub")
                .desc("show files in sub-folders")
                .hasArg(false)
                .build());

        options.addOption(Option.builder(OPTION_HELP)
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


    public String createHelpMove() {
        out.getBuffer().setLength(0);

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, CMD_MOVE, ""
                , createOptionsMove(), LEFT_PAD, DESC_PAD, "");

        pw.flush();
        out.flush();

        return out.toString();
    }

    public String createHelpAdd() {
        out.getBuffer().setLength(0);

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, CMD_ADD, ""
                , createOptionsAdd(), LEFT_PAD, DESC_PAD, "");


        pw.flush();
        out.flush();

        return out.toString();
    }

    public String createHelpRemove() {
        out.getBuffer().setLength(0);

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, CMD_REMOVE, ""
                , createOptionsRemove(), LEFT_PAD, DESC_PAD, "");

        pw.flush();
        out.flush();

        return out.toString();
    }

    public String createHelpShow() {
        out.getBuffer().setLength(0);

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, CMD_SHOW, ""
                , createOptionsShow(), LEFT_PAD, DESC_PAD, "");

        pw.flush();
        out.flush();

        return out.toString();
    }

    public String createHelpConfig() {
        out.getBuffer().setLength(0);

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, CMD_CONFIG, HEADER_CONFIG
                , createOptionsConfig(), LEFT_PAD, DESC_PAD, "");

        pw.flush();
        out.flush();

        return out.toString();
    }

    public String createHelpQuit() {
        out.getBuffer().setLength(0);

        formatter.printWrapped(pw, WIDTH, "");
        formatter.printHelp(pw, WIDTH, CMD_QUIT.get(0), HEADER_QUIT
                , createOptionsQuit(), LEFT_PAD, DESC_PAD, "");

        pw.flush();
        out.flush();

        return out.toString();
    }

    public String createHelpAll() {
        StringBuilder sb = new StringBuilder();

        sb.append(createHelpMove());
        sb.append(createHelpAdd());
        sb.append(createHelpRemove());
        sb.append(createHelpShow());
        sb.append(createHelpConfig());
        sb.append(createHelpQuit());

        return sb.toString();
    }

    public void printInvalidCommand(String command) {
        System.out.println("Invalid command. Type '" + command + " -h' or '" + command + " --help'");
    }

//    public void run() {
//        app.run();
//
//        printMainFolder();
//
//        boolean exit = false;
//        while (!exit) {
//            showMenu();
//
//            int number;
//            try {
//                System.out.print("> ");
//                number = Integer.parseInt(reader.nextLine());
//            } catch (Exception e) {
//                System.out.println("!!! You have to type number related to option.");
//                continue;
//            }
//
//            switch (number) {
//                case 1:
//                    app.showDirectories();
//                    break;
//                case 2:
//                    app.loadAndShowFilesInMainFolder();
//                    break;
//                case 3:
//                    app.moveFilesToSubFolders();
//                    System.out.println("\nErrors and warnings occured while processing");
////                    Logger.showErrors();
////                    Logger.showWarnings();
////                    Logger.clear();
//                    break;
//                case 4:
//                    app.moveAllFilesToMainFolder();
//                    System.out.println("\nErrors and warnings occured while processing");
////                    Logger.showErrors();
////                    Logger.showWarnings();
////                    Logger.clear();
//                    break;
//                case 5:
//                    printMainFolder();
//                    break;
//                case 6:
//                    app.sortFilesBy(SortingOption.NAME);
//                    app.showFiles();
//                    break;
//                case 7:
//                    app.sortFilesBy(SortingOption.SIZE);
//                    app.showFiles();
//                    break;
//                case 8:
//                    app.sortFilesBy(SortingOption.EXTENSION);
//                    app.showFiles();
//                    break;
//                case 9:
//                    app.sortFilesBy(SortingOption.CREATION_TIME);
//                    app.showFiles();
//                    break;
//                case 10: {
//                    System.out.println("Type a folder name: ");
//                    String name = reader.nextLine();
//                    app.addDirectory(name); // name validation
//                    break;
//                }
//                case 11: {
//                    System.out.println("Choose a folder - type a name");
//                    String line = reader.nextLine();
//                    Directory dir = app.getDirectoryByName(line);
//                    if (dir == null) {
//                        System.out.println("Directory doesnt exists");
//                    } else {
//                        System.out.println("Type extensions separated by space: ");
//                        line = reader.nextLine();
//                        String[] exts = line.trim().split(" ");
//                        for (int i = 0; i < exts.length; i++) {
//                            dir.addExtension(exts[i]);
//                        }
//                    }
//                    break;
//                }
//                case 12: {
//                    System.out.println("Type folder to delete: ");
//                    String line = reader.nextLine();
//                    Directory dir = app.getDirectoryByName(line);
//                    if (dir != null) {
//                        app.removeDirectory(dir);
//                    } else {
//                        System.out.println("Directory doesnt exists");
//                    }
//                    break;
//                }
//                case 13: {
//                    System.out.println("Type folder to change");
//                    String line = reader.nextLine();
//                    Directory dir = app.getDirectoryByName(line);
//                    if (dir == null) {
//                        System.out.println("Directory doesnt exists");
//                        break;
//                    }
//                    System.out.println("Type exts to delete");
//                    line = reader.nextLine();
//                    String[] exts = line.trim().split(" ");
//                    for (int i = 0; i < exts.length; i++) {
//                        dir.removeExtension(exts[i]);
//                    }
//                    break;
//                }
//                case 99:
//                    exit = true;
//                    break;
////                case 90:
////                    exit = true;
////                    app.makeAndSaveConfig();
////                    break;
//
//                default:
//                    System.out.println("Choose appropiate option");
//            }
//        }
//        reader.close();
//    }
//    private void showMenu() {
//        int option = 1;
//        StringBuilder sb = new StringBuilder();
//        sb.append("\n=== Menu:\n");
//        sb.append(option++).append(". Show dirs \n");
//        sb.append(option++).append(". Show files in main folder \n");
//        sb.append(option++).append(". Copy from main folder to sub-folders \n");
//        sb.append(option++).append(". Copy from folders to main folder \n");
//        sb.append(option++).append(". Show main folder \n");
//        sb.append("\n");
//        sb.append(option++).append(". Sort files by name \n");
//        sb.append(option++).append(". Sort files by size \n");
//        sb.append(option++).append(". Sort files by extension \n");
//        sb.append(option++).append(". Sort files by creation time \n");
//        sb.append("\n");
//        sb.append(option++).append(". Add folder \n");
//        sb.append(option++).append(". Add extensions to folder \n");
//        sb.append(option++).append(". Delete directory \n");
//        sb.append(option++).append(". Remove extensions from specified directory \n");
//        sb.append("99. Exit\n");
////        sb.append(option++).append(". Exit and save config\n");
//
//        System.out.println(sb.toString());
//    }


    private void printMainFolder() {
        System.out.println("\nMain folder path: " + Config.getPathToMainFolder());
        System.out.println("Main folder name: " + Config.getPathToMainFolder().getFileName());
    }
}
