package org.hellstrand.renfi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author (Mats Richard Hellstrand)
 * @version (3rd of September, 2023)
 */
public final class Constants {
    // Application states and actions...
    public static final int SUCCESSFUL = 0;
    public static final int FAILURE = 1;
    public static final int FLOW_INDEX = 0;
    public static final int BRANCH_INDEX = 1;
    public static final int COMMAND_INDEX = 2;
    public static final int EXTENSION_INDEX = 3;
    public static final int DIRECTORY_INDEX = 4;
    public static final int DATE_TYPE_INDEX = 5;

    // Application flags for handling files...
    public static final String FILE_PROCESSING = "-f";
    public static final String COMPARE_PROCESSING = "-vs";
    public static final String DATA_PROCESSING = "-d";
    public static final String JAVA_PROCESSING = "-j";
    public static final String SOURCE_PROCESSING = "-s";
    public static final String ORIGIN_PROCESSING = "-o";
    public static final String LIST_PROCESSING = "-l";
    public static final String IMAGE_PROCESSING = "-i";
    public static final String VIDEO_PROCESSING = "-v";
    public static final String CREATION_TIME_FLAG = "-ct";
    public static final String LAST_MODIFIED_TIME_FLAG = "-lmt";
    public static final String LAST_ACCESS_TIME_FLAG = "-lat";
    public static final List<String> FLOW_FLAGS = new ArrayList<>() {{
        add(FILE_PROCESSING);
        add(DATA_PROCESSING);
    }};
    public static final List<String> BRANCH_FLAGS = new ArrayList<>() {{
        add(COMPARE_PROCESSING);
        add(JAVA_PROCESSING);
        add(SOURCE_PROCESSING);
        add(ORIGIN_PROCESSING);
        add(LIST_PROCESSING);
    }};
    public static final List<String> COMMAND_FLAGS = new ArrayList<>() {{
        add(IMAGE_PROCESSING);
        add(VIDEO_PROCESSING);
    }};
    public static final List<String> TYPE_FLAGS = new ArrayList<>() {{
        add(CREATION_TIME_FLAG);
        add(LAST_MODIFIED_TIME_FLAG);
        add(LAST_ACCESS_TIME_FLAG);
    }};
    public static final List<String> ALLOWED_FLAGS = new ArrayList<>() {{
        addAll(FLOW_FLAGS);
        addAll(BRANCH_FLAGS);
        addAll(COMMAND_FLAGS);
        addAll(TYPE_FLAGS);
    }};
    public static final String FULL_HELP_FLAG = "help";
    public static final String HYPHEN_HELP_FLAG = "-h";
    public static final String HYPHEN_FULL_HELP_FLAG = "-help";
    public static final List<String> HELP_FLAGS = new ArrayList<>() {{
        add(FULL_HELP_FLAG);
        add(HYPHEN_HELP_FLAG);
        add(HYPHEN_FULL_HELP_FLAG);
    }};

    // Filenames and supported extensions for manipulation...
    public static final String OUTPUT_SOURCE = "output.txt";
    public static final String NAMES_SOURCE = "names.txt";
    public static final String EXTENSION_AVI = ".avi";
    public static final String EXTENSION_MP4 = ".mp4";
    public static final String EXTENSION_JPG = ".jpg";
    public static final Map<String, List<String>> PROCESSING_SUPPORT = new HashMap<>() {{
        put(IMAGE_PROCESSING, new ArrayList<>() {{
            add(EXTENSION_JPG);
        }});
        put(VIDEO_PROCESSING, new ArrayList<>() {{
            add(EXTENSION_AVI);
            add(EXTENSION_MP4);
        }});
    }};

    // Date, Timezone, Timestamp format etc...
    public static final String DATE_TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String DATE_TIMEZONE = "GMT";
    public static final String DATE_COUNTRY = "SE";
    public static final String DATE_LANGUAGE = "se";

    // Labels used by the application...
    public static final String LABEL_FILE_PROCESSING = "File handling";
    public static final String LABEL_COMPARE = "Compare resources";
    public static final String LABEL_DATA_PROCESSING = "Data handling";
    public static final String LABEL_VIDEOS = "Videos";
    public static final String LABEL_IMAGES = "Images";
    public static final String LABEL_CREATED = "Created Date";
    public static final String LABEL_FILE = "From File";
    public static final String LABEL_FILENAMES = "Save Filenames";
    public static final String LABEL_UNKNOWN_EXECUTION = "UNKNOWN";
    public static final String LABEL_PROCESSED_DIRECTORY = "processed\\";

    // Message variables used for displaying states, or actions, in the application...
    public static final String MESSAGE_INVALID_USE = "Invalid use of application";
    public static final String MESSAGE_DESIRED_EXECUTION = "Are you happy with the current task? (y/n)";
    public static final String MESSAGE_DIRECTORY_UNAVAILABLE = "No directory found with the stated path";
    public static final String MESSAGE_RESOURCES_UNAVAILABLE = "No resources found with the desired command and predefined extensions";
    public static final String MESSAGE_SOURCE_UNAVAILABLE = "No source file found under the current path";
    public static final String MESSAGE_SOURCE_CONTAINS = "Source file contains";
    public static final String MESSAGE_PROCESSING_TASK = "The task you have selected is: %s, %s, %s, %s%n";
    public static final String MESSAGE_LOADING_DIRECTORY = "Loading directory";
    public static final String MESSAGE_LOADING_FILES = "Available files";
    public static final String MESSAGE_SORTING_FILES = "Sorting files";
    public static final String MESSAGE_CONVERSION_HISTORY = "Conversion history";
    public static final String MESSAGE_LOADED_PREPARED = "New names being loaded/prepared";
    public static final String MESSAGE_RESOURCE_MISSING_FIELD = "The resource file was missing the datetime original field: ";
    public static final String MESSAGE_CONTINUE_RENAMING = "Do you want to continue with the renaming process? (y/n)";
    public static final String MESSAGE_RENAMING_PROCESS = "The process of renaming has begun";
    public static final String MESSAGE_CREATING_PROCESSED_DIRECTORY = "Creating the 'processed' directory";
    public static final String MESSAGE_CORRUPT_SOURCE = "The file was corrupt: %s%n";
    public static final String MESSAGE_RENAMING_ALERT = "%s was renamed to %s%n";
    public static final String MESSAGE_RENAMING_FAILURE = "The renaming process failed with a specific file";
    public static final String MESSAGE_FAILURE_SOURCES = "Specific file: %s, Failed because: %s%n";
    public static final String MESSAGE_FAILURE_NEWNAME = "newName.equals(NULL)";
    public static final String MESSAGE_UNDO_CONTINUE = "Do you want to undo the renaming process? (y/n)";
    public static final String MESSAGE_UNDO_RELOADING = "Reloading undo files";
    public static final String MESSAGE_UNDO_RESTORING = "The process of renaming back has begun";
    public static final String MESSAGE_UNDO_ALERT = "%s was renamed back to %s%n";
    public static final String MESSAGE_FAILED_UNDO_LOADING = "The amount of reloaded undo files and original files do not match";
    public static final String MESSAGE_UNDO_ABORT = "You chose not to undo the last renaming process, ignoring the undo process";
    public static final String MESSAGE_FAILED_MISMATCH = "The amount of generated names and amount of files do not match";
    public static final String MESSAGE_RENAMING_ABORT = "You chose not to rename files, aborting the renaming process";
    public static final String MESSAGE_EXECUTION_ABORT = "You chose not to continue with the execution, aborting the process";

    private Constants() {}
}
