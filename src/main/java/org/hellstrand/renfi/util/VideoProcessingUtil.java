package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.Constants.DATE_TIMESTAMP_FORMAT;
import static org.hellstrand.renfi.constant.Constants.EXTENSION_AVI;
import static org.hellstrand.renfi.constant.Constants.EXTENSION_MOV;
import static org.hellstrand.renfi.constant.Constants.EXTENSION_MP4;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOADED_PREPARED;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RESOURCE_MISSING_FIELD;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.avi.AviMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.imaging.quicktime.QuickTimeMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.avi.AviDirectory;
import com.drew.metadata.mov.QuickTimeDirectory;
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

/**
 * @author (Mats Richard Hellstrand)
 * @version (4th of September, 2023)
 */
public final class VideoProcessingUtil extends FileProcessingUtil {
    public static void prepareHistoryByOrigin(File[] files, Map<String, String> history, String extension) {
        try {
            printMessage(MESSAGE_LOADED_PREPARED);
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATE_TIMESTAMP_FORMAT);
            Map<String, String> mapOfFailures = new LinkedHashMap<>();

            for (File file : files) {
                Metadata metadata;
                switch (extension) {
                    case EXTENSION_AVI:
                        metadata = AviMetadataReader.readMetadata(file);
                        break;
                    case EXTENSION_MP4:
                        metadata = Mp4MetadataReader.readMetadata(file);
                        break;
                    case EXTENSION_MOV:
                        metadata = QuickTimeMetadataReader.readMetadata(file);
                        break;
                    default:
                        metadata = null;
                        break;
                }

                if (metadata != null) {
                    for (Directory directory : metadata.getDirectories()) {
                        Date date;
                        switch (extension) {
                            case EXTENSION_AVI:
                                date = directory.getDate(AviDirectory.TAG_DATETIME_ORIGINAL);
                                break;
                            case EXTENSION_MP4:
                                date = directory.getDate(Mp4Directory.TAG_CREATION_TIME);
                                break;
                            case EXTENSION_MOV:
                                date = directory.getDate(QuickTimeDirectory.TAG_CREATION_TIME);
                                break;
                            default:
                                date = null;
                                break;
                        }

                        if (date != null) {
                            Instant instant = Instant.ofEpochMilli(date.getTime());
                            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                            String oldName = file.getName();
                            String newName = localDateTime.format(pattern).concat(extension);
                            history.put(oldName, newName);
                            break;
                        } else {
                            mapOfFailures.put(file.getName(), MESSAGE_RESOURCE_MISSING_FIELD.concat(file.getName()));
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
