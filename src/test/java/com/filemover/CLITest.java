package com.filemover;

import com.filemover.Model.Application;
import com.filemover.Model.Config;
import com.filemover.View.CLI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CLITest {
    private Application app;
    private Config cfg;
    private CLI cli;
    private String parent;

    public final String NAME = "folder_name";
    public final String PATH = "/home/kusy/git/filemover/_testfolder2";
    public final List<String> EXTENSIONS = new ArrayList<>(Arrays.asList("ext1", "ext2", "ext3"));

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

//    @Rule
//    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() throws IOException {
        app = new Application();
        cfg = app.getCfg();
        cli = new CLI(app);

        parent = temporaryFolder.newFile("file.txt").getParent();
        cfg.setPathToMainFolder(parent);
        cfg.loadDirectories(cfg.getDirectoriesFromYAML());
        temporaryFolder.newFile("file.mp4");
        temporaryFolder.newFile("file.mp3");
        temporaryFolder.newFile("file.doc");
        temporaryFolder.newFile("file.jpeg");
        app.run();
        app.createAllFolders();
    }

    private List<String> getAllFilesInTempFolder() {
        try (Stream<Path> walk = Files.walk(Paths.get(parent))) {
            List<String> result = walk
//                    .filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

//            result.forEach(System.out::println);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showAllFilesInTempFolder() {
        for (String file :
                getAllFilesInTempFolder()) {
            System.out.println(file);
        }
    }
}
