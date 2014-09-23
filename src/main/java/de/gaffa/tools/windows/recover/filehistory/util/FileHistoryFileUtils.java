package de.gaffa.tools.windows.recover.filehistory.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        String fileNameWithOutExtension = fileName.substring(0, fileName.indexOf(extension) - 1);

        // eg 9
        int timeStampIndex = fileNameWithOutExtension.indexOf("(");

        // eg IMG_3001
        String fileNameWithOutTimeStamp = fileNameWithOutExtension.substring(0, timeStampIndex - 1);

        // eg IMG_3001.TXT
        String name = fileNameWithOutTimeStamp + "." + extension;
        mappedFile.name = relativePath.length() > 1 ? relativePath + File.separator + name : name;

        // eg 2014_08_05 19_01_20 UTC
        String timeStamp = fileNameWithOutExtension.substring(timeStampIndex + 1, fileNameWithOutExtension.length() - 1);

        // parse date
        try {
            mappedFile.date = dateFormat.parse(timeStamp);
        } catch (ParseException e) {
            throw new RuntimeException("could not parse timeStamp: " + timeStamp);
        }

        // set file
        mappedFile.file = file;

        return mappedFile;
    }

    public static class MappedFile {

        public File file;
        public String name;
        public Date date;
    }
}
