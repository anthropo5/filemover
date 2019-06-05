package com.filemover.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Constants {
    private Constants() {}

    public static final String PROP_MAIN_FOLDER = "main folder";
    public static final String PROP_DIRECTORIES = "directories";
    public static final String PROP_NAME = "name";
    public static final String PROP_PATH = "path";
    public static final String PROP_EXTENSIONS = "extensions";

    public static final String PATH_CONFIG_YML = "/home/kusy/git/filemover/config.yml";
    public static final String PATH_CONFIG_TXT = "/home/kusy/git/filemover/config.txt";

    public static final String CONFIG_FILE_NAME = "config.yml";
    public static final String GITHUB_PROJECT_LINK = " link to github";

    public static final class CLI {
        private CLI() {} // prevent instantiation

        public static final int LEFT_PAD = 1;
        public static final int DESC_PAD = 3;
        public static final int WIDTH = 80;

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
        public static final String OPTION_SORT_BY_NAME = "n";
        public static final String OPTION_SORT_BY_SIZE = "s";
        public static final String OPTION_SORT_BY_EXTENSION = "e";
        public static final String OPTION_SORT_BY_CREATION_TIME = "d";

        public static final String CMD_REMOVE = "remove";
        public static final String CMD_ADD = "add";
        public static final String CMD_HELP = "help";
        public static final String CMD_CONFIG = "config";
        public static final String CMD_SHOW = "show";
        public static final String CMD_MOVE = "move";
        public static final List<String> CMD_QUIT = new ArrayList<>(Arrays.asList("quit", "q", "exit"));
        public static final String CMD_SORT = "sort";


        public static final String HEADER_MOVE = "";
        public static final String HEADER_ADD = "Command doesn't add folder in file system. " +
                "It allows you to modify config. You have to type 'config --save' and 'config --load' " +
                "to take advantage of the changes";
        public static final String HEADER_REMOVE = "Command doesn't remove folder in file system." +
                "It allows you to modify config. You have to type 'config --save' and 'config --load' " +
                "to take advantage of the changes";
        public static final String HEADER_SHOW = "";
        public static final String HEADER_SORT = "Files by default are sorted by name. " +
                "Files will be moved in order they are sorted. Type 'show --help' to check how to show files.";
        public static final String HEADER_CONFIG = "Config is loaded form file when program is started. \n" +
                "If you made change in config file while program is running, you have to load config again. \n" +
                "Changes in config made in application are not saved automatically. \n" +
                "You have to save them or run 'quit' with proper flag: 'quit --save'";
        public static final String HEADER_QUIT = "Type 'quit', 'q', 'exit' to exit program.";
    }
}
