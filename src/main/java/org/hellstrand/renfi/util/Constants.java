package org.hellstrand.renfi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author (Mats Richard Hellstrand)
 * @version (10th of March, 2021)
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
    public static final String FILE_PROCESSING = "-s";
    public static final String ORIGIN_PROCESSING = "-o";
    public static final String LIST_PROCESSING = "-l";
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
    public static final String FULL_HELP_FLAG = "help";
    public static final String HYPHEN_HELP_FLAG = "-h";
    public static final String HYPHEN_FULL_HELP_FLAG = "-help";
    public static final List<String> HELP_FLAGS = new ArrayList<>() {{
        add(FULL_HELP_FLAG);
        add(HYPHEN_HELP_FLAG);
        add(HYPHEN_FULL_HELP_FLAG);
    }};

    // Filenames and supported extensions for manipulation...
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
    public static final String LABEL_VIDEOS = "Videos";
    public static final String LABEL_IMAGES = "Images";
    public static final String LABEL_CREATED = "Created Date";
    public static final String LABEL_FILE = "From File";
    public static final String LABEL_FILENAMES = "Save Filenames";
    public static final String LABEL_NEVER_REACHED = "IT SHOULD NEVER BE REACHED!";

    // Message variables used for displaying states, or actions, in the application...
    public static final String MESSAGE_INVALID_USE = "Invalid use of application";
    public static final String MESSAGE_DESIRED_EXECUTION = "Are you happy with the current task? (y/n)";
    public static final String MESSAGE_DIRECTORY_UNAVAILABLE = "No directory found with stated path";
    public static final String MESSAGE_RESOURCES_UNAVAILABLE = "No resources found with the desired command and predefined extensions";
    public static final String MESSAGE_SOURCE_UNAVAILABLE = "No source file found under the current path";
    public static final String MESSAGE_SOURCE_CONTAINS = "Source file contains";
    public static final String MESSAGE_PROCESSING_TASK = "The task you have selected is: %s, %s, %s";
    public static final String MESSAGE_LOADING_DIRECTORY = "Loading directory";
    public static final String MESSAGE_LOADING_FILES = "Available files";
    public static final String MESSAGE_SORTING_FILES = "Sorting files";
    public static final String MESSAGE_CONVERSION_HISTORY = "Conversion history";
    public static final String MESSAGE_LOADED_PREPARED = "New names being loaded/prepared";
    public static final String MESSAGE_RESOURCE_MISSING_FIELD = "The resource file was missing the datetime original field";
    public static final String MESSAGE_CONTINUE_RENAMING = "Do you want to continue with the renaming process? (y/n)";
    public static final String MESSAGE_RENAMING_PROCESS = "The process of renaming has begun";
    public static final String MESSAGE_CORRUPT_SOURCE = "The file was corrupt: %s";
    public static final String MESSAGE_RENAMING_ALERT = "%s was renamed to %s%n";
    public static final String MESSAGE_RENAMING_FAILURE = "The renaming process failed with a specific file";
    public static final String MESSAGE_FAILURE_SOURCES = "Specific file: %s, Failed because: %s";
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

    public static void displayHelpGuide() {
        StringBuilder sb = new StringBuilder();
        List <String> imageExtensions = PROCESSING_SUPPORT.get(IMAGE_PROCESSING);
        List <String> videoExtensions = PROCESSING_SUPPORT.get(VIDEO_PROCESSING);
        String helpFlags = HELP_FLAGS.toString()
            .replace("[", "(")
            .replace("]", ")");

        sb.append("  === Help Guide ===")
            .append("\n")
            .append("\n  Operation legend:")
            .append("\n")
            .append("\n  java -jar Renfi.jar <BRANCH> <COMMAND> <INDEX> <PATH>")
            .append("\n")
            .append("\n  (BRANCH)\t\tdesired flow of the application: ").append(BRANCH_FLAGS.toString())
            .append("\n  (COMMAND)\t\tfocus on either file type: ").append(COMMAND_FLAGS.toString())
            .append("\n  (INDEX)\t\tselect file extension based on index: <INTEGER> (view 'Extension legend' below)")
            .append("\n  (PATH)\t\tdirectory path to where the files are located: c:/directory/")
            .append("\n")
            .append("\n  Extension legend:")
            .append("\n  (IMAGE)\t\tsupported extensions and their indexes: ").append(printValues(imageExtensions))
            .append("\n  (VIDEO)\t\tsupported extensions and their indexes: ").append(printValues(videoExtensions))
            .append("\n")
            .append("\n  Flag legend:")
            .append("\n  (" + FILE_PROCESSING + ")\t\t\tprepare a SOURCE FILE based on directory files")
            .append("\n  (" + ORIGIN_PROCESSING + ")\t\t\tprepare history conversion based on ORIGIN DATA")
            .append("\n  (" + LIST_PROCESSING + ")\t\t\tprepare history conversion based on SOURCE FILE")
            .append("\n  (" + IMAGE_PROCESSING + ")\t\t\tprocess IMAGE files based on extension stated")
            .append("\n  (" + VIDEO_PROCESSING + ")\t\t\tprocess VIDEO files based on extension stated")
            .append("\n")
            .append("\n  ").append(helpFlags).append("\t\t\tdisplay this help guide");

        System.out.println(sb.toString());
    }

    private static String printValues(List<String> strings) {
        StringBuilder sb = new StringBuilder();

        for (String s : strings) {
            sb.append(s.substring(1))
                .append(" (")
                .append(strings.indexOf(s))
                .append("), ");
        }

        return sb.toString();
    }

    public static void printMessage(String message) {
        StringBuilder sb = new StringBuilder();

        sb.append("===== ##### ### ##### ####### ##### ### ##### =====\n\t").append(message);

        System.out.println(sb.toString());
    }
}
