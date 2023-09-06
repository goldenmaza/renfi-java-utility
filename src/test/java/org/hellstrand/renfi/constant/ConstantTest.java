package org.hellstrand.renfi.constant;

/**
 * @author (Mats Richard Hellstrand)
 * @version (6th of September, 2023)
 */
public final class ConstantTest {
    public static final String RESOURCES_PATH = "src/test/resources";
    public static final String RESOURCES_SELECTED_EXTENSION = ".png";
    public static final String EXISTING_PATH = RESOURCES_PATH.concat("/images/");
    public static final String INCORRECT_PATH = RESOURCES_PATH.concat("/vectors/");
    public static final String RESOURCES_OUTPUT_PATH = RESOURCES_PATH.concat("/output/");
    public static final String RESOURCES_INVALID_PATH = RESOURCES_PATH.concat("/out*put/");
    public static final String IMAGES_PNG_LARGE_RESOURCE_PATH = EXISTING_PATH.concat("/png/1000by1000/");
    public static final String OUTPUT_SOURCE_FILE_PATH = RESOURCES_OUTPUT_PATH.concat("output.txt");
    public static final String RESOURCES_NOTES_PATH = RESOURCES_PATH.concat("/notes/");
    public static final String INPUT_SOURCE_FILE_PATH = RESOURCES_NOTES_PATH.concat("input.txt");

    private ConstantTest() {}
}
