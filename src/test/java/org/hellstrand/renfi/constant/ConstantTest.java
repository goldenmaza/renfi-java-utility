package org.hellstrand.renfi.constant;

/**
 * @author (Mats Richard Hellstrand)
 * @version (9th of September, 2025)
 */
public final class ConstantTest {
    public static final String RESOURCES_PATH = "src/test/resources";
    public static final String RESOURCES_SELECTED_EXTENSION = ".png";
    public static final String EXISTING_PATH = RESOURCES_PATH.concat("/images/");
    public static final String INCORRECT_PATH = RESOURCES_PATH.concat("/vectors/");
    public static final String RESOURCES_OUTPUT_PATH = RESOURCES_PATH.concat("/output/");
    public static final String RESOURCES_INVALID_PATH = RESOURCES_PATH.concat("/out*put/");
    public static final String IMAGES_PNG_LARGE_RESOURCE_PATH = EXISTING_PATH.concat("/png/1000by1000/");
    public static final String OUTPUT_SOURCE_FILE = "output.txt";
    public static final String OUTPUT_SOURCE_FILE_PATH = RESOURCES_OUTPUT_PATH.concat(OUTPUT_SOURCE_FILE);
    public static final String LABEL_NOTES_DIRECTORY = "/notes/";
    public static final String RESOURCES_NOTES_PATH = RESOURCES_PATH.concat(LABEL_NOTES_DIRECTORY);
    public static final String INPUT_SOURCE_FILE = "input.txt";
    public static final String INPUT_SOURCE_FILE_PATH = RESOURCES_NOTES_PATH.concat(INPUT_SOURCE_FILE);
    public static final String LABEL_RENAME_DIRECTORY = "/rename/";
    public static final String RESOURCES_RENAME_PATH = RESOURCES_PATH.concat(LABEL_RENAME_DIRECTORY);
    public static final String LABEL_PROCESSED_DIRECTORY = "processed/";
    public static final String RESOURCES_PROCESSED_PATH = RESOURCES_RENAME_PATH.concat(LABEL_PROCESSED_DIRECTORY);
    public static final String FIRST_RENAME_SOURCE_FILE = "firstRenameFile.txt";
    public static final String FIRST_CHANGED_SOURCE_FILE = "firstChangedFile.txt";
    public static final String SECOND_RENAME_SOURCE_FILE = "secondRenameFile.txt";
    public static final String SECOND_CHANGED_SOURCE_FILE = "secondChangedFile.txt";
    public static final String THIRD_RENAME_SOURCE_FILE = "thirdRenameFile.txt";
    public static final String THIRD_CHANGED_SOURCE_FILE = "thirdChangedFile.txt";

    private ConstantTest() {}
}
