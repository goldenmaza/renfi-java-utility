package org.hellstrand.renfi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author (Mats Richard Hellstrand)
 * @version (7th of March, 2021)
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
    public static final String FILE_PROCESSING = "-s";
    public static final String IMAGE_PROCESSING = "-i";
    public static final String VIDEO_PROCESSING = "-v";
    public static final List<String> BRANCH_FLAGS = new ArrayList<>() {{
        add(FILE_PROCESSING);
        add(ORIGIN_PROCESSING);
        add(LIST_PROCESSING);
    }};
    public static final List<String> COMMAND_FLAGS = new ArrayList<>() {{
        add(IMAGE_PROCESSING);
        add(VIDEO_PROCESSING);
    }};
    public static final List<String> ALLOWED_FLAGS = new ArrayList<>() {{
        addAll(BRANCH_FLAGS);
        addAll(COMMAND_FLAGS);
    }};

    // Filenames and supported extensions for manipulation...
    public static final String NAMES_SOURCE = "names.txt";
    public static final String EXTENSION_AVI = ".avi";
    public static final String EXTENSION_MP4 = ".mp4";
    public static final String EXTENSION_JPG = ".jpg";
    public static final Map<String, List<String>> PROCESSING_SUPPORT = new HashMap<>() {{
        put(VIDEO_PROCESSING, new ArrayList<>() {{
            add(EXTENSION_AVI);
            add(EXTENSION_MP4);
        }});
        put(IMAGE_PROCESSING, new ArrayList<>() {{
            add(EXTENSION_JPG);
        }});
    }};

    // Date, Timezone, Timestamp format etc...
    public static final String DATE_TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String DATE_TIMEZONE = "GMT";
    public static final String DATE_COUNTRY = "SE";
    public static final String DATE_LANGUAGE = "se";

    // Labels used by the application...
    public static final String LABEL_VIDEOS = "Videos";
    public static final String LABEL_IMAGES = "Images";
    public static final String LABEL_CREATED = "Created Date";
    public static final String LABEL_FILE = "From File";
    public static final String LABEL_FILENAMES = "Save Filenames";
    public static final String LABEL_NEVER_REACHED = "IT SHOULD NEVER BE REACHED!";

    // Message variables used for displaying states, or actions, in the application...
    public static final String MESSAGE_INVALID_USE = " === Invalid use of application ===";
    public static final String MESSAGE_DIRECTORY_UNAVAILABLE = " === No directory found with that path ===";
    public static final String MESSAGE_RESOURCES_UNAVAILABLE = " === No resources found with the desired command and predefined extensions ===";
    public static final String MESSAGE_PROCESSING_TASK = " === The task you have selected is: %s, %s, %s ===";
    public static final String MESSAGE_LOADING_DIRECTORY = " === Loading directory ===";
    public static final String MESSAGE_LOADING_FILES = " === Loading files ===";
    public static final String MESSAGE_SORTING_FILES = " === Sorting files ===";
    public static final String MESSAGE_LOADED_PREPARED = " === New names being loaded/prepared ===";
    public static final String MESSAGE_RESOURCE_MISSING_FIELD = " === The resource file was missing the datetime original field ===";
    public static final String MESSAGE_CONTINUE_RENAMING = " === Do you want to continue with the renaming process? (y/n) ===";
    public static final String MESSAGE_RENAMING_PROCESS = " === The process of renaming has begun ===";
    public static final String MESSAGE_CORRUPT_SOURCE = " === The file was corrupt: %s ===";
    public static final String MESSAGE_RENAMING_ALERT = "%s was renamed to %s%n";
    public static final String MESSAGE_RENAMING_FAILURE = " === The renaming process failed with a specific file ===";
    public static final String MESSAGE_FAILURE_SOURCES = " === Specific file: %s, Failed because: %s ===";
    public static final String MESSAGE_UNDO_CONTINUE = " === Do you want to undo the renaming process? (y/n) ===";
    public static final String MESSAGE_UNDO_RELOADING = " === Reloading undo files ===";
    public static final String MESSAGE_UNDO_RESTORING = " === The process of renaming back has begun ===";
    public static final String MESSAGE_UNDO_ALERT = "%s was renamed back to %s%n";
    public static final String MESSAGE_FAILED_UNDO_LOADING = " === The amount of reloaded undo files and original files do not match ===";
    public static final String MESSAGE_UNDO_ABORT = " === You chose not to undo the last renaming process, aborting the undo process ===";
    public static final String MESSAGE_FAILED_MISMATCH = " === The amount of generated names and amount of files do not match ===";
    public static final String MESSAGE_RENAMING_ABORT = " === You chose not to rename files, aborting the renaming process ===";

    private Constants() {}

    public static void displayHelpGuide() {
        final List <String> imageExtensions = PROCESSING_SUPPORT.get(IMAGE_PROCESSING);
        final List <String> videoExtensions = PROCESSING_SUPPORT.get(VIDEO_PROCESSING);

        final String HELP_GUIDE = "  === Help Guide ==="
            + "\n"
            + "\n  Operation legend:"
            + "\n"
            + "\n  java -jar Renfi.jar <BRANCH> <COMMAND> <INDEX> <PATH>"
            + "\n"
            + "\n  (BRANCH)\t\tdesired flow of the application, file handling: " + BRANCH_FLAGS.toString()
            + "\n  (COMMAND)\t\tfocus on either file type: " + COMMAND_FLAGS.toString()
            + "\n  (INDEX)\t\tselect file extension based on index: <INTEGER> (view 'Extension legend' below)"
            + "\n  (PATH)\t\tdirectory path to where the files are located: c:/directory/"
            + "\n"
            + "\n  Extension legend:"
            + "\n  (IMAGE)\t\tsupported extensions and their indexes: " + printExtensionWithIndex(imageExtensions)
            + "\n  (VIDEO)\t\tsupported extensions and their indexes: " + printExtensionWithIndex(videoExtensions)
            + "\n"
            + "\n  Flag legend:"
            + "\n  (" + FILE_PROCESSING + ")\t\t\tprepare a SOURCE FILE based on directory files"
            + "\n  (" + ORIGIN_PROCESSING + ")\t\t\tprepare history and execute conversion based on ORIGIN DATA"
            + "\n  (" + LIST_PROCESSING + ")\t\t\tprepare history and execute conversion based on SOURCE FILE"
            + "\n  (" + IMAGE_PROCESSING + ")\t\t\tprocess IMAGE files based on extension stated"
            + "\n  (" + VIDEO_PROCESSING + ")\t\t\tprocess VIDEO files based on extension stated"
            + "\n"
            + "\n  (<EMPTY> | help | -h | -help | --help)\t\tdisplay this help guide"
            + "\n";

        System.out.println(HELP_GUIDE);
    }

    private static String printExtensionWithIndex(List<String> strings) {
        StringBuilder sb = new StringBuilder();

        for (String s : strings) {
            sb.append(s.substring(1))
                .append(" (")
                .append(strings.indexOf(s))
                .append("), ");
        }

        return sb.toString();
    }
}
