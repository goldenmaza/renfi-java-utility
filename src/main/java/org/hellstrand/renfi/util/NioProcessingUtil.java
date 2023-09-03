package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.util.Constants.DATE_TIMESTAMP_FORMAT;
import static org.hellstrand.renfi.util.Constants.LAST_ACCESS_TIME_FLAG;
import static org.hellstrand.renfi.util.Constants.LAST_MODIFIED_TIME_FLAG;
import static org.hellstrand.renfi.util.Constants.MESSAGE_CORRUPT_SOURCE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_LOADED_PREPARED;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RESOURCE_MISSING_FIELD;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author (Mats Richard Hellstrand)
 * @version (3rd of September, 2023)
 */
public class NioProcessingUtil {
    public static void javaProcessing(File[] fileFiles, Map<String, String> history, String extension, String dateTypeFlag) {
        printMessage(MESSAGE_LOADED_PREPARED);
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_TIMESTAMP_FORMAT);
        Map<String, String> mapOfFailures = new LinkedHashMap<>();

        Path[] pathFiles = new Path[fileFiles.length];
        for (int i = 0; i < fileFiles.length; i++) {
            pathFiles[i] = fileFiles[i].toPath();
        }

        try {
            for (Path file : pathFiles) {
                BasicFileAttributes basicFileAttributes = Files.readAttributes(file, BasicFileAttributes.class);

                Instant instant;
                if (basicFileAttributes != null) {
                    if (dateTypeFlag.equals(LAST_ACCESS_TIME_FLAG)) {
                        instant = basicFileAttributes.lastAccessTime().toInstant();
                    } else if (dateTypeFlag.equals(LAST_MODIFIED_TIME_FLAG)) {
                        instant = basicFileAttributes.lastModifiedTime().toInstant();
                    } else { // CREATION_TIME_FLAG
                        instant = basicFileAttributes.creationTime().toInstant();
                    }

                    if (instant != null) {
                        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        if (Objects.nonNull(localDateTime)) {
                            String oldName = file.getFileName().toString();
                            String newName = localDateTime.format(pattern) + extension;
                            history.put(oldName, newName);
                        } else {
                            mapOfFailures.put(file.getFileName().toString(), MESSAGE_RESOURCE_MISSING_FIELD + file.getFileName().toString());
                        }
                    } else {
                        System.out.printf(MESSAGE_CORRUPT_SOURCE, file.getFileName().toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> entry : mapOfFailures.entrySet()) {
            System.out.println(entry.getValue());
        }
    }
}
