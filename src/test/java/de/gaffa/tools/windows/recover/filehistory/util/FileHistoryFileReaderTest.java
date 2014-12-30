package de.gaffa.tools.windows.recover.filehistory.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileHistoryFileReaderTest {

    @Test
    public void testSinglefy() throws Exception {

        HashMap<String, List<FileHistoryFileUtils.MappedFile>> mapped = new HashMap<>();

        List<FileHistoryFileUtils.MappedFile> mappedFiles1 = new ArrayList<>();

        FileHistoryFileUtils.MappedFile file1 = new FileHistoryFileUtils.MappedFile();
        file1.name = "file1";
        file1.date = new Date();
        mappedFiles1.add(file1);

        FileHistoryFileUtils.MappedFile file2 = new FileHistoryFileUtils.MappedFile();
        file2.name = "file2";
        file2.date = new Date(file1.date.getTime() + 10000);
        mappedFiles1.add(file2);

        mapped.put("mappedFiles1", mappedFiles1);


        List<FileHistoryFileUtils.MappedFile> mappedFiles2 = new ArrayList<>();

        FileHistoryFileUtils.MappedFile file3 = new FileHistoryFileUtils.MappedFile();
        file3.name = "file3";
        file3.date = new Date();
        mappedFiles2.add(file3);

        mapped.put("mappedFiles2", mappedFiles2);


        List<FileHistoryFileUtils.MappedFile> singlefied = FileHistoryFileReader.singlefy(mapped);

        assertEquals(2, singlefied.size());
        assertEquals(file2, singlefied.get(0));
        assertEquals(file3, singlefied.get(1));
    }

    @Test
    public void testMapByName() throws Exception {

        ArrayList<FileHistoryFileUtils.MappedFile> files = new ArrayList<>();

        FileHistoryFileUtils.MappedFile file1 = new FileHistoryFileUtils.MappedFile();
        file1.name = "fileA";
        files.add(file1);

        FileHistoryFileUtils.MappedFile file2 = new FileHistoryFileUtils.MappedFile();
        file2.name = "fileA";
        files.add(file2);

        FileHistoryFileUtils.MappedFile file3 = new FileHistoryFileUtils.MappedFile();
        file3.name = "fileB";
        files.add(file3);

        Map<String, List<FileHistoryFileUtils.MappedFile>> mappedByName = FileHistoryFileReader.mapByName(files);

        assertEquals(2, mappedByName.size());

        assertTrue(mappedByName.containsKey("fileA"));
        List<FileHistoryFileUtils.MappedFile> fileAGroup = mappedByName.get("fileA");
        assertEquals(2, fileAGroup.size());
        assertEquals(file1, fileAGroup.get(0));
        assertEquals(file2, fileAGroup.get(1));

        List<FileHistoryFileUtils.MappedFile> fileBGroup = mappedByName.get("fileB");
        assertEquals(file3, fileBGroup.get(0));
    }
}