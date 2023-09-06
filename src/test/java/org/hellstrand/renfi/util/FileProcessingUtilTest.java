package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_NOTES_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_OUTPUT_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_INVALID_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.OUTPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.IMAGES_PNG_LARGE_RESOURCE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.INPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_SELECTED_EXTENSION;
import static org.hellstrand.renfi.constant.ConstantTest.EXISTING_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.INCORRECT_PATH;
import static org.hellstrand.renfi.util.FileProcessingUtil.createTargetDirectory;
import static org.hellstrand.renfi.util.FileProcessingUtil.validateTarget;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileProcessingUtilTest {
    @AfterAll
    static void clear() {
        if (new File(OUTPUT_SOURCE_FILE_PATH).delete()) {
            System.out.println("File was deleted: " + OUTPUT_SOURCE_FILE_PATH);
        }
        if (new File(RESOURCES_OUTPUT_PATH).delete()) {
            System.out.println("Path was deleted: " + RESOURCES_OUTPUT_PATH);
        }
    }

    @Test
    @Order(1)
    @DisplayName("We are validating that a directory cannot be found with an incorrect path...")
    void validateTargetTest_IncorrectPath() {
        assertFalse(validateTarget(INCORRECT_PATH));
    }

    @Test
    @Order(2)
    @DisplayName("We are validating that a directory can be found with an existing path...")
    void validateTargetTest_ExistingPath() {
        assertTrue(validateTarget(EXISTING_PATH));
    }

    @Test
    @Order(3)
    @DisplayName("We are validating that a directory cannot be created with an invalid path...")
    void createTargetDirectoryTest_CreateInvalidPath() {
        File directory = new File(RESOURCES_INVALID_PATH);
        String absolutePathToDirectory = directory.getAbsolutePath();
        assertFalse(createTargetDirectory(absolutePathToDirectory));
    }

    @Test
    @Order(4)
    @DisplayName("We are validating that a directory can be created with an existing path...")
    void createTargetDirectoryTest_CreatedCorrectlyPath() {
        File directory = new File(RESOURCES_OUTPUT_PATH);
        String absolutePathToDirectory = directory.getAbsolutePath();
        assertTrue(createTargetDirectory(absolutePathToDirectory));
    }

    @Test
    @Order(5)
    @DisplayName("We are validating that a source file can be created...")
    void createSourceFileTest_SourceFileCreated() {
        File file = new File(OUTPUT_SOURCE_FILE_PATH);
        String absolutePathToOutput = file.getAbsolutePath();
        FileProcessingUtil.createSourceFile(absolutePathToOutput);
        assertTrue(validateTarget(absolutePathToOutput));
    }

    @Test
    @Order(6)
    @DisplayName("We are validating that a source file can be populated with existing file names...")
    void writeSourceFileTest_SourceFileNotEmpty() {
        File directory = new File(IMAGES_PNG_LARGE_RESOURCE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(RESOURCES_SELECTED_EXTENSION));
        File file = new File(OUTPUT_SOURCE_FILE_PATH);
        assert files != null;
        FileProcessingUtil.writeSourceFile(files, file);
        assertTrue(validateTarget(OUTPUT_SOURCE_FILE_PATH));
        assertNotEquals(file.length(), 0);
    }

    @Test
    @Order(7)
    @DisplayName("We are validating that the history hashmap gets populated by a source file...")
    void prepareHistoryByInput_HistoryPopulatedByInputSourceFile() {
        Map<String, String> history = new HashMap<>();
        File directory = new File(IMAGES_PNG_LARGE_RESOURCE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(RESOURCES_SELECTED_EXTENSION));
        FileProcessingUtil.prepareHistoryByInput(files, history, INPUT_SOURCE_FILE_PATH, RESOURCES_SELECTED_EXTENSION);
        assertEquals(history.size(), directory.list().length);
    }
}
