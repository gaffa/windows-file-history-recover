package de.gaffa.tools.windows.recover.filehistory.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileHistoryFileUtils {

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd kk_mm_ss z");

    public static MappedFile mapFile(File baseDir, File file) {

        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("source file must exist (and be a file)");
        }

        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new IllegalArgumentException("folder that file shall be relative to must exist (and be a directory)");
        }

        String filePath = file.getParentFile().getPath();
        String relativeToPath = baseDir.getPath();

        if (!filePath.contains(relativeToPath)) {
            throw new IllegalArgumentException("file is not relative to path");
        }

        // extract relative Path
        String relativePath = filePath.equals(relativeToPath) ? "" : filePath.substring(relativeToPath.length() + 1);

        MappedFile mappedFile = new MappedFile();

        // eg "IMG_3001 (2014_08_05 19_01_20 UTC).TXT"
        String fileName = file.getName();

        // eg "TXT"
        String extension = FilenameUtils.getExtension(fileName);

        // eg "IMG_3001 (2014_08_05 19_01_20 UTC)"
        String fileNameWithOutExtension;
        try {
            fileNameWithOutExtension = extension.length() > 0 ? fileName.substring(0, fileName.indexOf("." + extension)) : fileName;
        } catch (StringIndexOutOfBoundsException e) {
            throw new RuntimeException(fileName + " is invalid");
        }

        // eg 9
        int timeStampIndex = fileNameWithOutExtension.lastIndexOf("(");

        // eg IMG_3001
        String fileNameWithOutTimeStamps = clearTimeStamps(fileNameWithOutExtension);

        // eg IMG_3001.TXT
        String name = fileNameWithOutTimeStamps + "." + extension;
        mappedFile.name = relativePath.length() > 1 ? relativePath + File.separator + name : name;

        // eg 2014_08_05 19_01_20 UTC
        String timeStamp = fileNameWithOutExtension.substring(timeStampIndex + 1, fileNameWithOutExtension.length() - 1);

        // parse date
        try {
            mappedFile.date = dateFormat.parse(timeStamp);
        } catch (ParseException e) {
            System.out.println("warning! could not parse timeStamp: " + timeStamp + " for file: " + fileName);
        }

        // set file
        mappedFile.file = file;

        return mappedFile;
    }

    static String clearTimeStamps(String fileNameWithOutExtension) {

        boolean open = false;
        int openIndex = 0;
        ArrayList<Character> timestamp = null;
        ArrayList<Character> cleanString = new ArrayList<>();
        for (Character c : fileNameWithOutExtension.toCharArray()) {
            if (c == '(') {
                open = true;
                timestamp = new ArrayList<>();
            }
            if (open) {
                timestamp.add(c);
            } else {
                cleanString.add(c);
            }
            if (c == ')') {
                if (!isTimeStamp(timestamp)) {
                    cleanString.addAll(timestamp);
                }
                open = false;
                timestamp = null;
            }
        }
        return toString(cleanString).trim();
    }

    static boolean isTimeStamp(ArrayList<Character> timestamp) {

        ArrayList<Character> copy = new ArrayList<>(timestamp);
        copy.remove(0);
        copy.remove(copy.size() - 1);
        String timeStampString = toString(copy);
        try {
            dateFormat.parse(timeStampString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    static String toString(ArrayList<Character> characters) {
        StringBuilder builder = new StringBuilder(characters.size());
        for (Character ch : characters) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public static class MappedFile {

        public File file;
        public String name;
        public Date date;
    }
}
