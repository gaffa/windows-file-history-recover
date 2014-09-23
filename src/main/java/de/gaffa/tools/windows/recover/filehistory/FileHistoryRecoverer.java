package de.gaffa.tools.windows.recover.filehistory;

import de.gaffa.tools.windows.recover.filehistory.util.FileHistoryFileReader;
import de.gaffa.tools.windows.recover.filehistory.util.FileHistoryFileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FileHistoryRecoverer {

    public static void main(String[] args) {

        if (args.length != 2) {
            printUsage();
            throw new IllegalArgumentException("wrong arguments");
        }

        File sourceFolder = new File(args[0]);
        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            printUsage();
            throw new IllegalArgumentException("source folder does not exist or is not a directory");
        }

        File targetFolder = new File(args[1]);
        if (!targetFolder.exists()) {
            System.out.println("target folder does not exist. creating...");
            boolean createdDirectory = targetFolder.mkdirs();
            if (!createdDirectory) {
                printUsage();
                throw new RuntimeException("target folder does not exist and cannot be created");
            }
        }
        if (!targetFolder.isDirectory()) {
            printUsage();
            throw new IllegalArgumentException("target folder param must be a directory");
        }

        // read files
        List<FileHistoryFileUtils.MappedFile> mappedFiles = FileHistoryFileReader.read(sourceFolder);

        // write files
        for (FileHistoryFileUtils.MappedFile mappedFile : mappedFiles) {
            File targetFile = new File(targetFolder + File.separator + mappedFile.name);
            try {
                boolean newFileCreated = targetFile.createNewFile();
                if (!newFileCreated) {
                    throw new RuntimeException("zakrament!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (FileInputStream source = new FileInputStream(mappedFile.file); FileOutputStream target = new FileOutputStream(targetFile)) {
                IOUtils.copy(source, target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void printUsage() {
        System.out.println("usage: java -jar recover {source} {target}");
    }
}
