package de.gaffa.tools.windows.recover.filehistory.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHistoryFileReader {

    public static List<FileHistoryFileUtils.MappedFile> read(File sourceFolder) {

        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            throw new IllegalArgumentException("source folder needs to exist and be a directory");
        }

        List<FileHistoryFileUtils.MappedFile> files = read(sourceFolder, sourceFolder);

        Map<String, List<FileHistoryFileUtils.MappedFile>> mappedByName = mapByName(files);

        return singlefy(mappedByName);
    }

    static List<FileHistoryFileUtils.MappedFile> singlefy(Map<String, List<FileHistoryFileUtils.MappedFile>> mappedByName) {

        List<FileHistoryFileUtils.MappedFile> singlefied = new ArrayList<>();

        for (String name : mappedByName.keySet()) {

            List<FileHistoryFileUtils.MappedFile> mappedFiles = mappedByName.get(name);
            FileHistoryFileUtils.MappedFile selected = null;
            for (FileHistoryFileUtils.MappedFile file : mappedFiles) {
                if (selected == null || file.date == null || selected.date == null || file.date.after(selected.date)) {
                    selected = file;
                }
            }
            singlefied.add(selected);
        }

        return singlefied;
    }

    static Map<String, List<FileHistoryFileUtils.MappedFile>> mapByName(List<FileHistoryFileUtils.MappedFile> files) {

        Map<String, List<FileHistoryFileUtils.MappedFile>> mappedFiles = new HashMap<>();
        for (FileHistoryFileUtils.MappedFile file : files) {
            if (!mappedFiles.containsKey(file.name)) {
                mappedFiles.put(file.name, new ArrayList<>());
            }
            List<FileHistoryFileUtils.MappedFile> mapEntry = mappedFiles.get(file.name);
            mapEntry.add(file);
        }
        return mappedFiles;
    }

    static List<FileHistoryFileUtils.MappedFile> read(File sourceFolder, File scanFolder) {

        List<FileHistoryFileUtils.MappedFile> mappedFiles = new ArrayList<>();

        for (File file : scanFolder.listFiles()) {
            if (file.isDirectory()) {
                mappedFiles.addAll(read(sourceFolder, file));
            } else {
                mappedFiles.add(FileHistoryFileUtils.mapFile(sourceFolder, file));
            }
        }

        return mappedFiles;
    }
}