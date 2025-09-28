package org.hellstrand.renfi.constant;

/**
 * @author (Mats Richard Hellstrand)
 * @version (28th of September, 2025)
 */
public final class ConstantTest {
    // Default canonical path and extensions...
    public static final String RESOURCES_DIRECTORY_PATH = "src/test/resources/";
    public static final String RESOURCES_PNG_EXTENSION = ".png";

    // Valid filenames...
    public static final String SOURCE_FILE_OUTPUT_TXT = "output.txt";
    public static final String SOURCE_FILE_INPUT_TXT = "input.txt";
    public static final String SOURCE_FILE_FIRST_RENAME_FILE_TXT = "firstRenameFile.txt";
    public static final String SOURCE_FILE_FIRST_CHANGED_FILE_TXT = "firstChangedFile.txt";
    public static final String SOURCE_FILE_SECOND_RENAME_FILE_TXT = "secondRenameFile.txt";
    public static final String SOURCE_FILE_SECOND_CHANGED_FILE_TXT = "secondChangedFile.txt";
    public static final String SOURCE_FILE_THIRD_RENAME_FILE_TXT = "thirdRenameFile.txt";
    public static final String SOURCE_FILE_THIRD_CHANGED_FILE_TXT = "thirdChangedFile.txt";

    // Invalid filenames...
    public static final String SOURCE_FILE_INVALID_INPUT_TXT = "in*ut.txt";

    // Valid directory names...
    public static final String IMAGES_DIRECTORY = "images/";
    public static final String OUTPUT_DIRECTORY = "output/";
    public static final String NOTES_DIRECTORY = "notes/";
    public static final String RENAME_DIRECTORY = "rename/";
    public static final String PROCESSED_DIRECTORY = "processed/";
    public static final String PNG_LARGE_DIRECTORY = "png/1000by1000/";

    // Invalid directory names...
    public static final String INVALID_VECTORS_DIRECTORY = "vectors/";
    public static final String INVALID_OUTPUT_DIRECTORY = "out*put/";

    // Valid canonical paths (directories only)...
    public static final String RESOURCES_IMAGES_PATH = RESOURCES_DIRECTORY_PATH.concat(IMAGES_DIRECTORY);
    public static final String RESOURCES_OUTPUT_PATH = RESOURCES_DIRECTORY_PATH.concat(OUTPUT_DIRECTORY);
    public static final String RESOURCES_NOTES_PATH = RESOURCES_DIRECTORY_PATH.concat(NOTES_DIRECTORY);
    public static final String RESOURCES_RENAME_PATH = RESOURCES_DIRECTORY_PATH.concat(RENAME_DIRECTORY);
    public static final String RESOURCES_RENAME_PROCESSED_PATH = RESOURCES_RENAME_PATH.concat(PROCESSED_DIRECTORY);
    public static final String RESOURCES_IMAGES_PNG_LARGE_PATH = RESOURCES_IMAGES_PATH.concat(PNG_LARGE_DIRECTORY);

    // Valid canonical paths (directories and files)...
    public static final String RESOURCES_OUTPUT_SOURCE_FILE_PATH = RESOURCES_OUTPUT_PATH.concat(SOURCE_FILE_OUTPUT_TXT);
    public static final String RESOURCES_NOTES_SOURCE_FILE_PATH = RESOURCES_NOTES_PATH.concat(SOURCE_FILE_INPUT_TXT);

    // Invalid canonical paths (directories only)...
    public static final String INVALID_RESOURCES_PATH_VECTORS = RESOURCES_DIRECTORY_PATH.concat(INVALID_VECTORS_DIRECTORY);
    public static final String INVALID_RESOURCES_OUTPUT_PATH = RESOURCES_DIRECTORY_PATH.concat(INVALID_OUTPUT_DIRECTORY);

    // Invalid canonical paths (directories and files)...
    public static final String INVALID_INPUT_SOURCE_FILE_PATH = RESOURCES_NOTES_PATH.concat(SOURCE_FILE_INVALID_INPUT_TXT);

    // Logging variables...
    public static final String TEST_LOGGING_DIRECTORY_OUTPUT = "Directory was deleted: {}";
    public static final String TEST_LOGGING_FILE_OUTPUT = "File was deleted: {}";

    private ConstantTest() {}
}
