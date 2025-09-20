package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.ConstantTest.EXISTING_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.FIRST_CHANGED_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.FIRST_RENAME_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.IMAGES_PNG_LARGE_RESOURCE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.INCORRECT_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.INPUT_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.INPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.LABEL_PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.constant.ConstantTest.OUTPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_INVALID_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_OUTPUT_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_PROCESSED_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_RENAME_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_SELECTED_EXTENSION;
import static org.hellstrand.renfi.constant.ConstantTest.SECOND_CHANGED_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.SECOND_RENAME_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.THIRD_CHANGED_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.THIRD_RENAME_SOURCE_FILE;
import static org.hellstrand.renfi.util.FileProcessingUtil.createTargetDirectory;
import static org.hellstrand.renfi.util.FileProcessingUtil.validateTarget;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileProcessingUtilTest {
    @AfterAll
    static void clearAll() {
        File fileOutput = new File(OUTPUT_SOURCE_FILE_PATH);
        if (fileOutput.exists() && fileOutput.delete()) {
            System.out.println("File was deleted: " + OUTPUT_SOURCE_FILE_PATH);
        }

        File directoryOutput = new File(RESOURCES_OUTPUT_PATH);
        if (directoryOutput.exists() && directoryOutput.delete()) {
            System.out.println("Path was deleted: " + RESOURCES_OUTPUT_PATH);
        }
    }

    @AfterEach
    void clearEach() {
        File directoryProcessed = new File(RESOURCES_PROCESSED_PATH);
        if (directoryProcessed.exists() && directoryProcessed.delete()) {
            System.out.println("Path was deleted: " + RESOURCES_PROCESSED_PATH);
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
    @Order(7)
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
    @Order(8)
    @DisplayName("We are validating that the history hashmap gets populated by a source file...")
    void prepareHistoryByInput_HistoryPopulatedByInputSourceFile() {
        Map<String, String> history = new HashMap<>();
        File directory = new File(IMAGES_PNG_LARGE_RESOURCE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(RESOURCES_SELECTED_EXTENSION));
        assert files != null;
        FileProcessingUtil.prepareHistoryByInput(files, history, INPUT_SOURCE_FILE_PATH, RESOURCES_SELECTED_EXTENSION);
        assertEquals(history.size(), Objects.requireNonNull(directory.list()).length);
    }

    @Test
    @Order(10)
    @DisplayName("We are validating that multiple files can be renamed...")
    void renamingProcess_MultipleFilesAreRenamed() {
        Map<String, String> history = Map.of(
            FIRST_RENAME_SOURCE_FILE, FIRST_CHANGED_SOURCE_FILE,
            SECOND_RENAME_SOURCE_FILE, SECOND_CHANGED_SOURCE_FILE,
            THIRD_RENAME_SOURCE_FILE, THIRD_CHANGED_SOURCE_FILE);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        File[] renameFiles = renameDirectory.listFiles();
        assertEquals(history.size(), Objects.requireNonNull(renameDirectory.list()).length);
        FileProcessingUtil.renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, LABEL_PROCESSED_DIRECTORY);
        File processedDirectory = new File(RESOURCES_PROCESSED_PATH);
        assertEquals(history.size(), Objects.requireNonNull(processedDirectory.list()).length);
    }

    @Test
    @Order(12)
    @DisplayName("We are validating that the renaming process can fail, but remaining files are processed as expected...")
    void renamingProcess_MultipleFilesAreRenamedButOneFails() {
        Map<String, String> history = Map.of(
            FIRST_RENAME_SOURCE_FILE, FIRST_CHANGED_SOURCE_FILE,
            INPUT_SOURCE_FILE, SECOND_CHANGED_SOURCE_FILE,
            THIRD_RENAME_SOURCE_FILE, THIRD_CHANGED_SOURCE_FILE);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        File[] renameFiles = renameDirectory.listFiles();
        assertEquals(history.size(), Objects.requireNonNull(renameDirectory.list()).length);
        FileProcessingUtil.renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, LABEL_PROCESSED_DIRECTORY);
        File processedDirectory = new File(RESOURCES_PROCESSED_PATH);
        assertEquals(history.size() - 1, Objects.requireNonNull(processedDirectory.list()).length);
    }

    @Test
    @Order(14)
    @DisplayName("We are validating that without a history the renaming process will fail, with just a message...")
    void renamingProcess_NoExecutionFailureUponEmptyHistory() {
        Map<String, String> history = new HashMap<>();
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        File[] renameFiles = renameDirectory.listFiles();
        FileProcessingUtil.renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, LABEL_PROCESSED_DIRECTORY);
    }

    @Test
    @Order(11)
    @DisplayName("We are validating that multiple files that were renamed can be restored...")
    void renamingUndoProcessTest_MultipleFilesAreRestored() {
        Map<String, String> history = Map.of(
            FIRST_RENAME_SOURCE_FILE, FIRST_CHANGED_SOURCE_FILE,
            SECOND_RENAME_SOURCE_FILE, SECOND_CHANGED_SOURCE_FILE,
            THIRD_RENAME_SOURCE_FILE, THIRD_CHANGED_SOURCE_FILE);
        File processedDirectory = new File(RESOURCES_PROCESSED_PATH);
        assertEquals(history.size(), Objects.requireNonNull(processedDirectory.list()).length);
        FileProcessingUtil.renamingUndoProcess(history, processedDirectory, RESOURCES_RENAME_PATH);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        assertEquals(history.size() + 1, Objects.requireNonNull(renameDirectory.list()).length);
    }

    @Test
    @Order(13)
    @DisplayName("We are validating that multiple files that were renamed can be restored...")
    void renamingUndoProcessTest_MultipleFilesAreRestoredSpecifically() {
        Map<String, String> history = Map.of(
            FIRST_RENAME_SOURCE_FILE, FIRST_CHANGED_SOURCE_FILE,
            INPUT_SOURCE_FILE, SECOND_CHANGED_SOURCE_FILE,
            THIRD_RENAME_SOURCE_FILE, THIRD_CHANGED_SOURCE_FILE);
        File processedDirectory = new File(RESOURCES_PROCESSED_PATH);
        assertEquals(history.size() - 1, Objects.requireNonNull(processedDirectory.list()).length);
        FileProcessingUtil.renamingUndoProcess(history, processedDirectory, RESOURCES_RENAME_PATH);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        assertEquals(history.size() + 1, Objects.requireNonNull(renameDirectory.list()).length);
    }
}
