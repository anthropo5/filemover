package com.filemover;

import com.filemover.Model.Application;
import com.filemover.Model.Config;
import com.filemover.Model.Directory;
import com.filemover.View.CLI;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;




// cfg.load in Application::init has to be commented
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
        app.createAllFolders();
        app.loadFilesFromMainFolder();
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
        app.moveFilesToSubFolders();
        // one moved file is enough - but need to check expections
        // temp_folder/doc/file.doc
//        String expected = app.getDirectoryByName("doc").getPath().toString() + File.separator + "file.doc";
//        assertThat(getAllFilesInTempFolder(), hasItems(expected) );
        String expected = app.getDirectoryByName("doc").getPath().toString() + File.separator + "file.txt";
        String expected1 = app.getDirectoryByName("doc").getPath().toString() + File.separator + "file.doc";
        String expected2 = app.getDirectoryByName("video").getPath().toString() + File.separator + "file.mp4";
        String expected3 = app.getDirectoryByName("img").getPath().toString() + File.separator + "file.jpeg";
        String expected4 = app.getDirectoryByName("music").getPath().toString() + File.separator + "file.mp3";
        assertThat(getAllFilesInTempFolder(), hasItems(expected, expected1, expected2, expected3, expected4) );
    }

    @Test
    public void testIfFilesMovesToMainFolder() {
        List<String> expected = getAllFilesInTempFolder();
        app.moveFilesToSubFolders();
        app.moveAllFilesToMainFolder();
        assertThat(getAllFilesInTempFolder(), is(expected) );
    }











    // test api to "add" command


    @Test
    public void testIfFolderIsAddedWithoutExts() {
        app.addDirectory("folder_name");
        Directory dir = app.getDirectoryByName("folder_name");
        assertThat(dir, is(notNullValue()));
        assertThat(dir.getExtensions(), is(IsEmptyCollection.empty()));
    }

    @Test
    public void testIfFolderIsAddedWithExts() {
        List<String> args = new ArrayList<>(Arrays.asList("folder_name", "ext1", "ext2", "ext3"));
        app.addDirectoryWithExts(args);
        Directory dir = app.getDirectoryByName("folder_name");
        assertThat(dir, is(notNullValue()));
        assertThat(dir.getExtensions(), is(Arrays.asList("ext1", "ext2", "ext3")));
    }

    @Test
    public void testIfExtsAreAddedToFolder() {
        List<String> args = new ArrayList<>(Arrays.asList("folder_name", "ext1", "ext2", "ext3"));
        app.addDirectory("folder_name");
        app.addExtensionsToFolder(args);
        assertThat(app.getDirectoryByName("folder_name").getExtensions(), contains("ext1","ext2","ext3"));
    }











    // test api to "remove" command

    @Test
    public void testIfFolderIsRemoved() {
        app.removeDirectory("doc");
        assertThat(app.getDirectoryByName("doc"), is(nullValue()));
    }

    @Test
    public void testIfFoldersAreRemoved() {
        List<String> args = new ArrayList<>(Arrays.asList("doc", "img", "video"));
        app.removeDirectories(args);
        assertThat(app.getDirectoryByName("doc"), is(nullValue()));
        assertThat(app.getDirectoryByName("img"), is(nullValue()));
        assertThat(app.getDirectoryByName("video"), is(nullValue()));
    }

    @Test
    public void testIfExtsAreRemovedFromFolder() {
        List<String> args = new ArrayList<>(Arrays.asList("video", "mp4"));
        app.removeExtensionsFromDirectory(args);
        assertThat(app.getDirectoryByName("video").getExtensions(), is(IsEmptyCollection.empty()));
    }








    // test api to "config" command

    @Test
    public void testIfConfigIsLoading() {
        app.loadConfig();
        assertThat(app.getDirectories(), is(notNullValue()));
        assertThat(app.getDirectories(), is(not(IsEmptyCollection.empty())));
        assertThat(app.getDirectoryByName("doc"), is(notNullValue()));
        assertThat(app.getDirectoryByName("doc").getExtensions(), hasItems("txt", "doc", "pdf"));
    }

    @Test
    public void testIfConfigIsSaved() {
        assertThat(app.getDirectoryByName("iso"), is(notNullValue()));
        app.removeDirectory("iso");
        assertThat(app.getDirectoryByName("iso"), is(nullValue()));
        app.getCfg().setPathToMainFolder(parent);
        app.getCfg().makeYAML(parent + "/config_test.txt");
        app.getCfg().loadDataFromYAMLFile(parent + "/config_test.txt");
        app.getCfg().load(parent + "/config_test.txt");
//        app.loadConfig();
        assertThat(app.getDirectoryByName("iso"), is(nullValue()));
    }











    // test api to "show" command

    @Test
    public void testShowDirs() {
        app.showDirectories();
    }

    @Test
    public void testShowDirectory() {
        app.showDirectory("doc");
    }

    @Test
    public void testShowMainFolderPath() {
        app.showMainFolderPath();
    }

    @Test
    public void testShowFilesInMainFolder() {
        app.loadAndShowFilesInMainFolder();
    }

    @Test
    public void testShowFilesInSubFolders() {
        app.showFilesInAllFolders();
    }
}
