package de.gaffa.tools.windows.recover.filehistory.util;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FileHistoryFileUtilsTest {

    private File testFileInRoot = FileUtils.toFile(getClass().getResource("/de/gaffa/tools/windows/recover/filehistory/test/examplebackup/IMG_3001 (2014_08_05 19_01_20 UTC).TXT"));
    private File rootFolder = testFileInRoot.getParentFile();
    private File testFileInFolder = FileUtils.toFile(getClass().getResource("/de/gaffa/tools/windows/recover/filehistory/test/examplebackup/939OAJJD/foo (2014_07_04 19_02_20 UTC).md"));
    private File testFileWithDoubleTimestampAndBracketInName = FileUtils.toFile(getClass().getResource("/de/gaffa/tools/windows/recover/filehistory/test/examplebackup/CIMG1394 (Large) (2012_12_02 13_22_28 UTC) (2014_06_17 13_57_37 UTC).JPG"));

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

        assertNotNull(testFileWithDoubleTimestampAndBracketInName);
        assertTrue(testFileWithDoubleTimestampAndBracketInName.exists());
        assertTrue(testFileWithDoubleTimestampAndBracketInName.isFile());
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

    @Test
    public void testMapFileWithDoubleTimestampAndBracketsInName() throws Exception {
        FileHistoryFileUtils.MappedFile mappedFile = FileHistoryFileUtils.mapFile(rootFolder, testFileWithDoubleTimestampAndBracketInName);
        assertNotNull(mappedFile);
        assertEquals("CIMG1394 (Large).JPG", mappedFile.name);
        assertEquals(1403013457000L, mappedFile.date.getTime());
    }

    @Test
    public void testClearTimeStamps() {

        assertEquals("CIMG1394 (Large)", FileHistoryFileUtils.clearTimeStamps("CIMG1394 (Large) (2012_12_02 13_22_28 UTC) (2014_06_17 13_57_37 UTC)"));
    }

    @Test
    public void testIsTimeStampPositive() {

        ArrayList<Character> timeStamp = new ArrayList<>();
        timeStamp.add('(');
        timeStamp.add('2');
        timeStamp.add('0');
        timeStamp.add('1');
        timeStamp.add('2');
        timeStamp.add('_');
        timeStamp.add('1');
        timeStamp.add('2');
        timeStamp.add('_');
        timeStamp.add('0');
        timeStamp.add('2');
        timeStamp.add(' ');
        timeStamp.add('1');
        timeStamp.add('3');
        timeStamp.add('_');
        timeStamp.add('2');
        timeStamp.add('2');
        timeStamp.add('_');
        timeStamp.add('2');
        timeStamp.add('8');
        timeStamp.add(' ');
        timeStamp.add('U');
        timeStamp.add('T');
        timeStamp.add('C');
        timeStamp.add(')');

        assertTrue(FileHistoryFileUtils.isTimeStamp(timeStamp));
    }

    @Test
    public void testIsTimeStampNegative() {

        ArrayList<Character> timeStamp = new ArrayList<>();
        timeStamp.add('(');
        timeStamp.add('L');
        timeStamp.add('a');
        timeStamp.add('r');
        timeStamp.add('g');
        timeStamp.add('e');
        timeStamp.add(')');

        assertFalse(FileHistoryFileUtils.isTimeStamp(timeStamp));
    }
}