package com.filemover;

import com.filemover.Model.Application;
import com.filemover.Model.Config;
import com.filemover.Model.Directory;
import com.filemover.View.CLI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ApplicationTest {
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
        // cfg.load in Application::init has to be commented
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
//        app.run();
//        app.createAllFolders();
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

    @Test
    public void testIfAllFoldersCreated() {
        List<Directory> dirs = app.getDirectories();
        List<String> expected = new ArrayList<>();

        for (Directory dir:
             dirs) {
            expected.add(dir.getPath().toString());
        }

        app.createAllFolders();
        assertThat("Folder defined in config.yml was not created",
                getAllFilesInTempFolder(), hasItems(
                                                        expected.get(0)
                                                       ,expected.get(1)
                                                       ,expected.get(2)
                                                       ,expected.get(3)
                                                       ,expected.get(4)
                                                       ,expected.get(5)
                                                       ,expected.get(6)
                                                       ,expected.get(7)
                                                        ));
    }

    @Test
    public void testIfFoldersAreNotCreatedTwice() {
        app.createAllFolders();

        List<String> expected = getAllFilesInTempFolder();

        app.createAllFolders();

        List<String> actual = getAllFilesInTempFolder();

        assertThat("folders created twice", actual, is(expected));
    }

    @Test
    public void testIfFilesMovesToSubFolders() {
        app.createAllFolders();
        app.moveFilesToSubFolders();

        // one moved file is enough - but need to check expections
        // temp_folder/doc/file.doc
        String expected = app.getDirectoryByName("doc").getPath().toString() + File.separator + "file.doc";
        assertThat(getAllFilesInTempFolder(), hasItems(expected) );
    }

    @Test
    public void testIfFilesMovesToMainFolder() {
        app.createAllFolders();

        List<String> expected = getAllFilesInTempFolder();

        app.moveFilesToSubFolders();
        app.moveAllFilesToMainFolder();

        assertThat(getAllFilesInTempFolder(), is(expected) );
    }

}
