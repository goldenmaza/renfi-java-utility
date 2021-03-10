package org.hellstrand.renfi.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static org.hellstrand.renfi.util.Constants.DATE_COUNTRY;
import static org.hellstrand.renfi.util.Constants.DATE_LANGUAGE;
import static org.hellstrand.renfi.util.Constants.DATE_TIMESTAMP_FORMAT;
import static org.hellstrand.renfi.util.Constants.DATE_TIMEZONE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_LOADED_PREPARED;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RESOURCE_MISSING_FIELD;
import static org.hellstrand.renfi.util.Constants.printMessage;

/**
 * @author (Mats Richard Hellstrand)
 * @version (10th of March, 2021)
 */
public final class ImageProcessingUtil extends FileProcessingUtil {
    public static void prepareHistoryByOrigin(File[] files, Map<String, String> history, String extension) {
        try {
            printMessage(MESSAGE_LOADED_PREPARED);
            Locale locale = new Locale(DATE_LANGUAGE, DATE_COUNTRY);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIMESTAMP_FORMAT, locale);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(DATE_TIMEZONE));

            for (File file : files) {
                Metadata metadata = ImageMetadataReader.readMetadata(file);
                for (Directory directory : metadata.getDirectories()) {
                    Date originalDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    if (originalDate != null) {
                        String oldName = file.getName();
                        String newName = simpleDateFormat.format(originalDate) + extension;
                        history.put(oldName, newName);
                    } else {
                        printMessage(MESSAGE_RESOURCE_MISSING_FIELD);
                    }
                }
            }
        } catch (ImageProcessingException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
