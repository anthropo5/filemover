package com.filemover.Model;

import com.filemover.Utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.filemover.Model.Constants.*;

public class Config {
    /*
     TODO
      - check if path to config.yml is ok when running program from jar
      - check if path exists in setPathToMainFolder
      - use constructor in sneakyaml
      - final pathToMainFolder? static initialization?
      - Directory::toMap - is hack - i have to convert array list to one long string with all exts. exts should be
        saved and loaded as array list. currently they are saved as string and loaded from string. but thanks to this
        config.txt is easier to maintain from user perspective who edit this file.
      - loadYAML(String), save(String) - a lot of duplicated code - refactor this methods
      - makeYAML() - change path to ./config.txt
    */

    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static Path pathToMainFolder;
    private Application app;

    public Config(Application app) {
        this.app = app;
    }

    public static Path getPathToMainFolder() {
        return Config.pathToMainFolder;
    }

    public void setPathToMainFolder(String path) {
        try {
            // check if path exists
            log.debug("Setting path of main folder to: " + path);
            Config.pathToMainFolder = Paths.get(path);
        } catch (NullPointerException e) {
            log.debug("Path to main folder is null");
            e.printStackTrace();
        }
    }

    public void loadDirectories(List<Object> directories) {
//        List<Object> directories = (List<Object>) data.get(PROP_DIRECTORIES);

        if (directories == null) {
            log.debug("List with directories is null. Propably bad format in config.yml");
            throw new NullPointerException("Couldn't initialize data. Propably format of config.txt is wrong. " +
                    "\nPlease try to configure it again. You can check example at " + GITHUB_PROJECT_LINK +
                    " \nor restore it to default by command XY [not yet]");
        }

        log.debug("Loading directories...");
        for (Object obj :
                directories) {
            Map<String, Object> props = (Map<String, Object>) obj;
            String name = (String) props.get(PROP_NAME);
            String path = (String) props.get(PROP_PATH);
            String extensions = (String) props.get(PROP_EXTENSIONS);

            log.debug(extensions);
//
            String[] tmp = StringUtils.pepare(extensions).split("\\s+");
            List<String> exts = new ArrayList<>();
            for (String ext :
                    tmp) {
                exts.add(ext);
            }
            Directory dir = new Directory(name, path, exts);
            app.addDirectory(dir);
        }
    }

    public void load() {
        Map<String, Object> data = loadDataFromYAMLFile();

        setPathToMainFolder((String) data.get(PROP_MAIN_FOLDER));

        List<Object> directories = (List<Object>) data.get(PROP_DIRECTORIES);
        loadDirectories(directories);

    }

    public void load(String path) {
        Map<String, Object> data = loadDataFromYAMLFile(path);

        setPathToMainFolder((String) data.get(PROP_MAIN_FOLDER));

        List<Object> directories = (List<Object>) data.get(PROP_DIRECTORIES);
        loadDirectories(directories);

    }

    public List<Object> getDirectoriesFromYAML() {
        Map<String, Object> data = loadDataFromYAMLFile();
        return (List<Object>) data.get(PROP_DIRECTORIES);
    }

//    public void

    public Map<String, Object> loadDataFromYAMLFile() {
        log.debug("Loading config");
        Yaml yaml = new Yaml();

        try (InputStream input = new FileInputStream(new File(CONFIG_FILE_NAME))) {

            log.debug("Loading data from yaml");
            return yaml.load(input);

//            setPathToMainFolder((String) data.get(PROP_MAIN_FOLDER));
//            loadDirectories(directories);

        } catch (FileNotFoundException e) {
            log.error("Couldn't find \"config.txt\" file. Remember it has to be in the same place as program executable file filemover.jar [ check this !!!] ");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> loadDataFromYAMLFile(String pathToConfig) {
        log.debug("Loading config");
        Yaml yaml = new Yaml();

        try (InputStream input = new FileInputStream(new File(pathToConfig))) {

            log.debug("Loading data from yaml");
            return yaml.load(input);

//            setPathToMainFolder((String) data.get(PROP_MAIN_FOLDER));
//            loadDirectories(directories);

        } catch (FileNotFoundException e) {
            log.error("Couldn't find \"config.txt\" file. Remember it has to be in the same place as program executable file filemover.jar [ check this !!!] ");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void makeYAML() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(PROP_MAIN_FOLDER, Config.getPathToMainFolder().toString());

        List<Map<String, Object>> dirsToFile = new ArrayList<>();

        for (Directory dir :
                app.getDirectories()) {
            dirsToFile.add(dir.toMap());
        }

        data.put(PROP_DIRECTORIES, dirsToFile);
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
        System.out.println(writer.toString());
        try {
            // TODO change path to ./config.txt
            Files.write(Paths.get(PATH_CONFIG_TXT), writer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void makeYAML(String pathToSave) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(PROP_MAIN_FOLDER, Config.getPathToMainFolder().toString());

        List<Map<String, Object>> dirsToFile = new ArrayList<>();

        for (Directory dir :
                app.getDirectories()) {
            dirsToFile.add(dir.toMap());
        }

        data.put(PROP_DIRECTORIES, dirsToFile);
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
        System.out.println(writer.toString());
        try {
            // TODO change path to ./config.txt
            Files.write(Paths.get(pathToSave), writer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void restoreDefault() {
        //doc, img, video, comments;
    }
}
