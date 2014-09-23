package de.gaffa.tools.windows.recover.filehistory.util;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class FileHistoryFileUtilsTest {

    private File testFileInRoot = FileUtils.toFile(getClass().getResource("/de/gaffa/tools/windows/recover/filehistory/test/examplebackup/IMG_3001 (2014_08_05 19_01_20 UTC).TXT"));
    private File rootFolder = testFileInRoot.getParentFile();
    private File testFileInFolder = FileUtils.toFile(getClass().getResource("/de/gaffa/tools/windows/recover/filehistory/test/examplebackup/939OAJJD/foo (2014_07_04 19_02_20 UTC).md"));

    @Before
    public void checkFiles() {

        assertNotNull(testFileInRoot);
        assertTrue(testFileInRoot.exists());
        assertTrue(testFileInRoot.isFile());

        assertNotNull(rootFolder);
        assertTrue(rootFolder.exists());
        assertTrue(rootFolder.isDirectory());

        assertNotNull(testFileInFolder);
        assertTrue(testFileInFolder.exists());
        assertTrue(testFileInFolder.isFile());
    }

    @Test
    public void testMapFileInRoot() throws Exception {
        FileHistoryFileUtils.MappedFile mappedFile = FileHistoryFileUtils.mapFile(rootFolder, testFileInRoot);
        assertNotNull(mappedFile);
        assertEquals("IMG_3001.TXT", mappedFile.name);
        assertEquals(1407265280000L, mappedFile.date.getTime());
    }

    @Test
    public void testMapFileInFolder() throws Exception {
        FileHistoryFileUtils.MappedFile mappedFile = FileHistoryFileUtils.mapFile(rootFolder, testFileInFolder);
        assertNotNull(mappedFile);
        assertEquals("939OAJJD" + File.separator + "foo.md", mappedFile.name);
        assertEquals(1404500540000L, mappedFile.date.getTime());
    }
}