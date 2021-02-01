package org.hellstrand.renfi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author (Mats Richard Hellstrand)
 * @version (1st of February, 2021)
 */
public final class Constants {
    // Application states and actions...
    public static final int SUCCESSFUL = 0;
    public static final int FAILURE = 1;
    public static final int BRANCH_INDEX = 0;
    public static final int COMMAND_INDEX = 1;
    public static final int EXTENSION_INDEX = 2;
    public static final int DIRECTORY_INDEX = 3;

    // Application flags for processing of source files...
    public static final String ORIGIN_PROCESSING = "-o";
    public static final String LIST_PROCESSING = "-l";
    public static final String FILE_PROCESSING = "-f";
    public static final String IMAGE_PROCESSING = "-i";
    public static final String VIDEO_PROCESSING = "-v";

    // Filenames and supported extensions for manipulation...
    public static final String NAMES_SOURCE = "names.txt";
    public static final String EXTENSION_AVI = ".avi";
    public static final String EXTENSION_MP4 = ".mp4";
    public static final String EXTENSION_JPG = ".jpg";
    public static final Map<String, List<String>> PROCESSING_SUPPORT = new HashMap<>() {{
        put(IMAGE_PROCESSING, new ArrayList<>(Arrays.asList(EXTENSION_JPG, ".png-NOT_SUPPORTED")));
        put(VIDEO_PROCESSING, new ArrayList<>(Arrays.asList(EXTENSION_AVI, EXTENSION_MP4)));
    }};

    // Date, Timezone, Timestamp format etc...
    public static final String DATE_TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String DATE_TIMEZONE = "GMT";
    public static final String DATE_COUNTRY = "SE";
    public static final String DATE_LANGUAGE = "se";

    // Message variables used for displaying states, or actions, in the application...
    public static final String MESSAGE_INVALID_USE = " === Invalid use of application ===";
    public static final String MESSAGE_DIRECTORY_UNAVAILABLE = " === No directory found with that path ===";
    public static final String MESSAGE_RESOURCES_UNAVAILABLE = " === No resources found with the desired command and predefined extensions ===";
    public static final String MESSAGE_PROCESSING_TASK = " === The task you have selected is: %s, %s, %s%n ===";
    public static final String MESSAGE_LOADING_DIRECTORY = " === Loading directory ===";
    public static final String MESSAGE_LOADING_FILES = " === Loading files ===";
    public static final String MESSAGE_SORTING_FILES = " === Sorting files ===";
    public static final String MESSAGE_LOADED_PREPARED = " === New names being loaded/prepared ===";
    public static final String MESSAGE_RESOURCE_MISSING_FIELD = " === The resource file was missing the datetime original field ===";
    public static final String MESSAGE_CONTINUE_RENAMING = " === Do you want to continue with the renaming process? (y/n) ===";
    public static final String MESSAGE_RENAMING_PROCESS = " === The process of renaming has begun ===";
    public static final String MESSAGE_CORRUPT_SOURCE = " === The file was corrupt: %s%n ===";
    public static final String MESSAGE_RENAMING_ALERT = "%s was renamed to %s%n";
    public static final String MESSAGE_RENAMING_FAILURE = " === The renaming process failed with a specific file ===";
    public static final String MESSAGE_FAILURE_SOURCES = " === Specific file: %s, Failed because: %s%n ===";
    public static final String MESSAGE_UNDO_CONTINUE = " === Do you want to undo the renaming process? (y/n) ===";
    public static final String MESSAGE_UNDO_RELOADING = " === Reloading undo files ===";
    public static final String MESSAGE_UNDO_RESTORING = " === The process of renaming back has begun ===";
    public static final String MESSAGE_UNDO_ALERT = "%s was renamed back to %s%n";
    public static final String MESSAGE_FAILED_UNDO_LOADING = " === The amount of reloaded undo files and original files do not match ===";
    public static final String MESSAGE_UNDO_ABORT = " === You chose not to undo the last renaming process, aborting the undo process ===";
    public static final String MESSAGE_FAILED_MISMATCH = " === The amount of generated names and amount of files do not match ===";
    public static final String MESSAGE_RENAMING_ABORT = " === You chose not to rename files, aborting the renaming process ===";

    private Constants() {}
}
