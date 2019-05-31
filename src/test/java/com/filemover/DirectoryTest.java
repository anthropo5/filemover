package com.filemover;

import com.filemover.Model.Application;
import com.filemover.Model.Config;
import com.filemover.Model.Directory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DirectoryTest {
    private Application app;
    private Config cfg;

    public final String NAME = "folder_name";
    public final String PATH = "/home/kusy/git/filemover/_testfolder2";
    public final List<String> EXTENSIONS = new ArrayList<>(Arrays.asList("ext1", "ext2", "ext3"));

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        app = new Application();
        cfg = new Config(app);
        cfg.load();
    }

    @Test
    public void directoryConstructor_AllParameters() {
        Directory dir = new Directory(NAME, PATH, EXTENSIONS);

        assertEquals("Wrong dir name", NAME, dir.getName());
        assertEquals("Wrong path", PATH, dir.getParent().toString());
        assertThat(EXTENSIONS, hasItems("ext1", "ext2", "ext3"));
    }

    @Test
    public void directoryConstructor_WithoutPath() {
        Directory dir = new Directory(NAME, null, EXTENSIONS);

        assertEquals("Wrong dir name", NAME, dir.getName());
        String msg = "Wrong path - should be to main folder specified in config file";
        assertEquals(msg, Config.getPathToMainFolder().toString(), dir.getParent().toString());
        assertThat(EXTENSIONS, hasItems("ext1", "ext2", "ext3"));
    }

    @Test
    public void test_addOneExtension() {
        Directory dir = new Directory(NAME, PATH, EXTENSIONS);
        List<String> expected = new ArrayList<>(Arrays.asList("ext1", "ext2", "ext3", "ext4"));

        dir.addExtension(" '     ext.-4/    /?  ");
        assertThat("'ext4' not added", dir.getExtensions(), is(equalTo(expected)));

        dir.addExtension("ext4");
        assertThat("'ext4' added again - duplicate", dir.getExtensions(), is(equalTo(expected)));
    }


    @Test
    public void test_addManyExtensions() {
        Directory dir;
        List<String> expected;

        dir = new Directory(NAME, PATH, EXTENSIONS);
        expected = new ArrayList<>(Arrays.asList("ext1", "ext2", "ext3", "ext4", "ext5", "ext6"));
        dir.addExtensions("ext4 ext5    ext6");
        assertThat(dir.getExtensions(), is(equalTo(expected)));

        dir = new Directory(NAME, PATH, EXTENSIONS);
        expected = new ArrayList<>(Arrays.asList("ext1", "ext2", "ext3", "ext4", "ext5", "ext6"));
        dir.addExtensions("         ext4 .  , .-=+ext5 `~!@  ext6        ");
        assertThat(dir.getExtensions(), is(equalTo(expected)));
    }

    @Test
    public void test_removeOneExtension() {
        Directory dir = new Directory(NAME, PATH, EXTENSIONS);
        List<String> expected = new ArrayList<>(Arrays.asList("ext1", "ext3"));

        dir.removeExtension("ext2");
        assertThat(dir.getExtensions(), is(equalTo(expected)));
        assertFalse(dir.removeExtension("ext2"));
        assertTrue(dir.removeExtension("ext1"));
    }

    @Test
    public void test_removeManyExtensions() {
        List<String> expected = new ArrayList<>(Arrays.asList("ext1"));
        Directory dir;

        dir = new Directory(NAME, PATH, EXTENSIONS);
        dir.removeExtensions("ext2 ext3");
        assertThat(dir.getExtensions(), is(equalTo(expected)));

        dir = new Directory(NAME, PATH, EXTENSIONS);
        dir.removeExtensions("  ./ ext2      ;[  ext3 ext5  .;. ;  ? ");
        assertThat(dir.getExtensions(), is(equalTo(expected)));
    }

    @Test
    public void test_createFolder() throws IOException {

        String parent = temporaryFolder.newFile("file.txt").getParent();
        temporaryFolder.newFile("file.mp4");
        temporaryFolder.newFile("file.mp3");
        temporaryFolder.newFile("file.doc");
        temporaryFolder.newFile("file.jpeg");

        Directory dir = new Directory(NAME, parent, EXTENSIONS);
        dir.createFolder();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(parent))) {
            for (Path fileName : stream) {
                if (Files.isDirectory(fileName)) {
                    if(fileName.getParent().equals(dir.getParent())) {
                        return;
                    }
                }
            }
            fail("directory not found - not created");
        } catch (IOException | DirectoryIteratorException e) {
            e.printStackTrace();
        }

//        thrown.expect(IOException.class);
//        thrown.expectMessage("file already exists");
    }
}
