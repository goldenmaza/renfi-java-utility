package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.util.Constants.DATE_TIMESTAMP_FORMAT;
import static org.hellstrand.renfi.util.Constants.EXTENSION_AVI;
import static org.hellstrand.renfi.util.Constants.EXTENSION_MP4;
import static org.hellstrand.renfi.util.Constants.MESSAGE_CORRUPT_SOURCE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_LOADED_PREPARED;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RESOURCE_MISSING_FIELD;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.avi.AviMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.avi.AviDirectory;
import com.drew.metadata.mp4.Mp4Directory;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author (Mats Richard Hellstrand)
 * @version (3rd of September, 2023)
 */
public final class VideoProcessingUtil extends FileProcessingUtil {
    public static void prepareHistoryByOrigin(File[] files, Map<String, String> history, String extension) {
        try {
            printMessage(MESSAGE_LOADED_PREPARED);
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_TIMESTAMP_FORMAT);
            Map<String, String> mapOfFailures = new LinkedHashMap<>();

            Metadata metadata = null;
            for (File file : files) {
                if (extension.equals(EXTENSION_AVI)) {
                    metadata = AviMetadataReader.readMetadata(file);
                } else if (extension.equals(EXTENSION_MP4)) {
                    metadata = Mp4MetadataReader.readMetadata(file);
                }

                if (metadata != null) {
                    for (Directory directory : metadata.getDirectories()) {
                        Date date = null;
                        LocalDateTime localDateTime;
                        if (extension.equals(EXTENSION_AVI)) {
                            date = directory.getDate(AviDirectory.TAG_DATETIME_ORIGINAL);
                        } else if (extension.equals(EXTENSION_MP4)) {
                            date = directory.getDate(Mp4Directory.TAG_CREATION_TIME);
                        }

                        if (date != null) {
                            Instant instant = Instant.ofEpochMilli(date.getTime());
                            localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                            LocalDateTime originalDate = localDateTime.plusHours(1);//WTF, WHY IS THIS NECESSARY?!
                            if (Objects.nonNull(originalDate)) {
                                String oldName = file.getName();
                                String newName = originalDate.format(pattern) + extension;
                                history.put(oldName, newName);
                                break;
                            } else {
                                mapOfFailures.put(file.getName(), MESSAGE_RESOURCE_MISSING_FIELD + file.getName());
                            }
                        } else {
                            System.out.printf(MESSAGE_CORRUPT_SOURCE, file.getName());
                        }
                    }
                }
            }

            for (Map.Entry<String, String> entry : mapOfFailures.entrySet()) {
                System.out.println(entry.getValue());
            }
        } catch (ImageProcessingException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
