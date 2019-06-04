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
    public static final String OPTION_SHOW_ALL_FILES = "s";
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


    public void runCLI() {
        System.out.println("Type 'help' to show all commands");
        boolean exit = false;
        while (!exit) {
            System.out.print("> ");
            String input = reader.nextLine();
            exit = execute(input);
        }
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
            } else if (command.equals(CMD_SHOW)) {
                executeShow(args);
            } else if (command.equals(CMD_HELP)) {
                executeHelp();
            } else if (CMD_QUIT.contains(command)) {
                executeQuit(args);
                return true;
            } else {
                System.out.println("Invalid input. Type 'help' to show all commands ");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void executeMove(String[] args) throws ParseException {
        options = createOptionsMove();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_MOVE_FILES_TO_SUB_FOLDERS)) {
            app.moveFilesToSubFolders();
        } else if (cmd.hasOption(OPTION_MOVE_FILES_TO_MAIN_FOLDER)) {
            app.moveAllFilesToMainFolder();
        } else if (cmd.hasOption(OPTION_HELP)) {
            System.out.println(createHelpMove());
        } else {
            printInvalidCommand(CMD_CONFIG);
        }
    }

    public void executeAdd(String[] args) throws ParseException {
        options = createOptionsAdd();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_ADD_FOLDER)) {
            String[] val = cmd.getOptionValues(OPTION_ADD_FOLDER);
            List<String> values = new ArrayList<>(Arrays.asList(val));
            app.addDirectoryWithExts(values);
        } else if (cmd.hasOption(OPTION_ADD_EXTENSIONS)) {
            String[] val = cmd.getOptionValues(OPTION_ADD_EXTENSIONS);
            List<String> values = new ArrayList<>(Arrays.asList(val));
            app.addExtensionsToFolder(values);
        } else if (cmd.hasOption(OPTION_HELP)) {
            System.out.println(createHelpAdd());
        } else {
            printInvalidCommand(CMD_ADD);
        }
    }

    //    TODO add remove all
    public void executeRemove(String[] args) throws ParseException {
        options = createOptionsRemove();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_REMOVE_FOLDER)) {
            String[] val = cmd.getOptionValues(OPTION_REMOVE_FOLDER);
            List<String> values = new ArrayList<>(Arrays.asList(val));
            app.removeDirectories(values);
        } else if (cmd.hasOption(OPTION_REMOVE_EXTENSIONS)) {
            String[] val = cmd.getOptionValues(OPTION_REMOVE_EXTENSIONS);
            List<String> values = new ArrayList<>(Arrays.asList(val));
            app.removeExtensionsFromDirectory(values);
        } else if (cmd.hasOption(OPTION_HELP)) {
            System.out.println(createHelpRemove());
        } else {
            printInvalidCommand(CMD_REMOVE);
        }
    }

    public void executeConfig(String[] args) throws ParseException {
        options = createOptionsConfig();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_CONFIG_LOAD)) {
            app.loadConfig();
        } else if (cmd.hasOption("s")) {
            app.saveConfig();
        } else if (cmd.hasOption("h")) {
            System.out.println(createHelpConfig());
        } else {
            printInvalidCommand(CMD_CONFIG);
        }
    }

    public void executeShow(String[] args) throws ParseException {
        options = createOptionsShow();
        cmd = parser.parse(options, args);
        if (cmd.hasOption(OPTION_SHOW_DIRS)) {
            app.showDirectories();
        } else if (cmd.hasOption(OPTION_SHOW_DIR)) {
            String value = cmd.getOptionValue(OPTION_SHOW_DIR);
            app.showDirectory(value);
        } else if (cmd.hasOption(OPTION_SHOW_MAIN)) {
            app.loadAndShowFilesInMainFolder();
        } else if (cmd.hasOption(OPTION_SHOW_MAIN_PATH)) {
            app.showMainFolderPath();
        } else if (cmd.hasOption(OPTION_SHOW_ALL_FILES)) {
            app.showFilesInAllFolders();
        } else if (cmd.hasOption(OPTION_HELP)) {
            System.out.println(createHelpShow());
        } else {
            printInvalidCommand(CMD_SHOW);
        }
    }

    public void executeQuit(String[] args) throws ParseException {
        options = createOptionsQuit();
        cmd = parser.parse(options, args);
        if (cmd.hasOption("s")) {
            app.saveConfig();
        } else if (cmd.hasOption("h")) {
            System.out.println(createHelpQuit());
        } else {
//            printInvalidCommand(CMD_QUIT.get(0));
        }
    }

    public void executeHelp() {
        System.out.println(createHelpAll());
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

        options.addOption(Option.builder(OPTION_SHOW_ALL_FILES)
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
}
