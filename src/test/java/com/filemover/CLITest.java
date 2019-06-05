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
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class CLITest {
    private Application app;
    private Config cfg;
    private CLI cli;
    private String parent;

    // TODO create folder in tempFolder
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
        app.createAllFolders();
        app.loadFilesFromMainFolder();
    }

    @Test
    public void testIfFilesAreMovedToSubFolders() {
        cli.execute("move -f");
        String expected = app.getDirectoryByName("doc").getPath().toString() + File.separator + "file.txt";
        String expected1 = app.getDirectoryByName("doc").getPath().toString() + File.separator + "file.doc";
        String expected2 = app.getDirectoryByName("video").getPath().toString() + File.separator + "file.mp4";
        String expected3 = app.getDirectoryByName("img").getPath().toString() + File.separator + "file.jpeg";
        String expected4 = app.getDirectoryByName("music").getPath().toString() + File.separator + "file.mp3";
        assertThat(getAllFilesInTempFolder(), hasItems(expected, expected1, expected2, expected3, expected4));
    }

    @Test
    public void testIfFilesAreMovedToMainFolder() {
        List<String> expected = getAllFilesInTempFolder();
        cli.execute("move -m");
        assertThat(getAllFilesInTempFolder(), is(expected));
    }








    // test "add" command

    @Test
    public void testIfFolderIsAddedWithoutExts() {
        cli.execute("add -f folder_name");
        Directory dir = app.getDirectoryByName("folder_name");
        assertThat(dir, is(notNullValue()));
        assertThat(dir.getExtensions(), is(IsEmptyCollection.empty()));
    }

    @Test
    public void testIfFolderIsAddedWithExts() {
        cli.execute("add -f folder_name ext1 ext2 ext3");
        Directory dir = app.getDirectoryByName("folder_name");
        assertThat(dir, is(notNullValue()));
        assertThat(dir.getExtensions(), is(Arrays.asList("ext1", "ext2", "ext3")));
    }

    @Test
    public void testIfExtsAreAddedToFolder() {
        cli.execute("add -f folder_name ext1 ext2 ext3");
        assertThat(app.getDirectoryByName("folder_name").getExtensions(), contains("ext1","ext2","ext3"));
    }




    // test "remove" command

    @Test
    public void testIfFolderIsRemoved() {
        cli.execute("remove -f doc");
        assertThat(app.getDirectoryByName("doc"), is(nullValue()));
    }

    @Test
    public void testIfFoldersAreRemoved() {
        cli.execute("remove -f doc img video");
        assertThat(app.getDirectoryByName("doc"), is(nullValue()));
        assertThat(app.getDirectoryByName("img"), is(nullValue()));
        assertThat(app.getDirectoryByName("video"), is(nullValue()));
    }

    @Test
    public void testIfExtsAreRemovedFromFolder() {
        cli.execute("remove -e video mp4");
        assertThat(app.getDirectoryByName("video").getExtensions(), is(IsEmptyCollection.empty()));
    }







    // helpers

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
