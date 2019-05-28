package com.filemover.Model;

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

    // TODO
    //  check if path to config.yml is ok when running program from jar
    //  use constructor in sneakyaml
    //  final pathToMainFolder? static initialization?


    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private final String CONFIG_FILE_NAME = "config.yml";
    private final String GITHUB_PROJECT_LINK = " link to github";

    private static Path pathToMainFolder;

    private Application app;

    public Config(Application app) {
        this.app = app;
    }

    public void setPathToMainFolder(String path) {
        log.debug("Setting path of main folder to: " + path);
        Config.pathToMainFolder = Paths.get(path);
    }
    public static Path getPathToMainFolder() {
        return Config.pathToMainFolder;
    }

    public void loadYAMLFile() {
        log.debug("Loading config");
        Yaml yaml = new Yaml();

        try(InputStream input = new FileInputStream(new File(CONFIG_FILE_NAME))) {

            log.debug("Loading data from yaml");
            Map<String, Object> data = yaml.load(input);

            setPathToMainFolder((String) data.get(PROP_MAIN_FOLDER));

            List<Object> directories = (List<Object>) data.get(PROP_DIRECTORIES);

            if (directories == null) {
                log.debug("List with directories is null. Propably bad format in config.yml");
                throw new NullPointerException("Couldn't initialize data. Propably format of config.txt is wrong. " +
                        "\nPlease try to configure it again. You can check example at " + GITHUB_PROJECT_LINK +
                        " \nor restore it to default by command XY [not yet]");
            }

            loadDirectories(directories);

        } catch (NullPointerException e) {
            log.error(e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("Couldn't find \"config.txt\" file. Remember it has to be in the same place as program executable file filemover.jar [ check this !!!] ");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("IO Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void loadDirectories(List<Object> directories) {
        for (Object obj :
                directories) {
            Map<String, Object> props = (Map<String, Object>) obj;
            String name = (String) props.get(PROP_NAME);
            String path = (String) props.get(PROP_PATH);
            List<String> extensions = (List<String>) props.get(PROP_EXTENSIONS);

            Directory dir = new Directory(name, path, extensions);
            app.addDirectory(dir);
        }
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
            Files.write(Paths.get(PATH_CONFIG_TXT), writer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
