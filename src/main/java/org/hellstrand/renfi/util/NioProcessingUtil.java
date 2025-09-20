package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.Constants.DATE_TIMESTAMP_FORMAT;
import static org.hellstrand.renfi.constant.Constants.LAST_ACCESS_TIME_FLAG;
import static org.hellstrand.renfi.constant.Constants.LAST_MODIFIED_TIME_FLAG;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOADED_PREPARED;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_NIO_FAILURE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RESOURCE_MISSING_FIELD;

import org.hellstrand.renfi.exception.SourceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * @author (Mats Richard Hellstrand)
 * @version (20th of September, 2025)
 */
public class NioProcessingUtil {
    private static final Logger logger = LoggerFactory.getLogger(NioProcessingUtil.class);

    public static void prepareHistoryByNioProcessing(File[] files, Map<String, String> history, String fromExtension, String dateTypeFlag) {
        logger.info(MESSAGE_LOADED_PREPARED);
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_TIMESTAMP_FORMAT);
        Map<String, String> mapOfFailures = new LinkedHashMap<>();

        Path[] pathFiles = new Path[files.length];
        for (int i = 0; i < files.length; i++) {
            pathFiles[i] = files[i].toPath();
        }

        try {
            for (Path pathFile : pathFiles) {
                BasicFileAttributes basicFileAttributes = Files.readAttributes(pathFile, BasicFileAttributes.class);
                String oldName = pathFile.getFileName().toString();

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
                        String newName = localDateTime.format(pattern).concat(fromExtension);
                        history.put(oldName, newName);
                    } else {
                        mapOfFailures.put(oldName, MESSAGE_RESOURCE_MISSING_FIELD.concat(oldName));
                    }
                }
            }
        } catch (IOException e) {
            logger.error(MESSAGE_NIO_FAILURE);
            throw new SourceUnavailableException(MESSAGE_NIO_FAILURE, e);
        }

        for (Map.Entry<String, String> entry : mapOfFailures.entrySet()) {
            logger.info(entry.getValue());
        }
    }
}
