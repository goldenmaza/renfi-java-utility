package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.ConstantTest.EXISTING_PATH_IMAGES;
import static org.hellstrand.renfi.constant.ConstantTest.FIRST_CHANGED_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.FIRST_RENAME_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.IMAGES_PNG_LARGE_RESOURCE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.INVALID_INPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.INVALID_PATH_VECTORS;
import static org.hellstrand.renfi.constant.ConstantTest.INPUT_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.INPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.LABEL_PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.constant.ConstantTest.OUTPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_INVALID_OUTPUT_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_OUTPUT_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_PROCESSED_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_RENAME_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_SELECTED_EXTENSION;
import static org.hellstrand.renfi.constant.ConstantTest.SECOND_CHANGED_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.SECOND_RENAME_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.THIRD_CHANGED_SOURCE_FILE;
import static org.hellstrand.renfi.constant.ConstantTest.THIRD_RENAME_SOURCE_FILE;
import static org.hellstrand.renfi.util.FileProcessingUtil.createSourceFile;
import static org.hellstrand.renfi.util.FileProcessingUtil.createTargetDirectory;
import static org.hellstrand.renfi.util.FileProcessingUtil.prepareHistoryByInput;
import static org.hellstrand.renfi.util.FileProcessingUtil.renamingProcess;
import static org.hellstrand.renfi.util.FileProcessingUtil.renamingUndoProcess;
import static org.hellstrand.renfi.util.FileProcessingUtil.validateTarget;
import static org.hellstrand.renfi.util.FileProcessingUtil.writeSourceFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hellstrand.renfi.AbstractTest;
import org.hellstrand.renfi.exception.DirectoryUnavailableException;
import org.hellstrand.renfi.exception.SourceUnavailableException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author (Mats Richard Hellstrand)
 * @version (28th of September, 2025)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileProcessingUtilTest extends AbstractTest {
    @AfterAll
    public static void clearAll() {
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
    @DisplayName("We are validating that a directory cannot be found with an invalid path...")
    void validateTargetTest_InvalidPathDirectoryMissing() {
        // Assert & Execute
        assertFalse(validateTarget(INVALID_PATH_VECTORS));
    }

    @Test
    @Order(2)
    @DisplayName("We are validating that a directory can be found with an existing path...")
    void validateTargetTest_ExistingPathDirectoryFound() {
        // Assert & Execute
        assertTrue(validateTarget(EXISTING_PATH_IMAGES));
    }

    @Test
    @Order(3)
    @DisplayName("We are validating that a file cannot be found under an existing path...")
    void validateTargetTest_ExistingPathFileMissing() {
        // Assert & Execute
        assertFalse(validateTarget(EXISTING_PATH_IMAGES.concat(INPUT_SOURCE_FILE)));
    }

    @Test
    @Order(4)
    @DisplayName("We are validating that a file can be found under an existing path...")
    void validateTargetTest_ExistingPathFileFound() {
        // Assert & Execute
        assertTrue(validateTarget(INPUT_SOURCE_FILE_PATH));
    }

    @Test
    @Order(5)
    @DisplayName("We are validating that a directory cannot be created with an invalid path...")
    void createTargetDirectoryTest_CreateDirectoryWithInvalidPathThrowsException() {
        // Prepare
        File directory = new File(RESOURCES_INVALID_OUTPUT_PATH);
        String absolutePathToDirectory = directory.getAbsolutePath();

        // Assert & Execute
        assertThrows(DirectoryUnavailableException.class, () -> createTargetDirectory(absolutePathToDirectory));
    }

    @Test
    @Order(6)
    @DisplayName("We are validating that a directory can be created with an existing path...")
    void createTargetDirectoryTest_CreateDirectoryWithExistingPathPassesValidation() {
        // Prepare
        File directory = new File(RESOURCES_OUTPUT_PATH);
        String absolutePathToDirectory = directory.getAbsolutePath();

        // Assert & Execute
        assertTrue(createTargetDirectory(absolutePathToDirectory));
    }

    @Test
    @Order(7)
    @DisplayName("We are validating that a source file cannot be created with an invalid name under an existing path...")
    void createSourceFileTest_CreateSourceFileWithExistingPathPassesValidation() {
        // Prepare
        File file = new File(INVALID_INPUT_SOURCE_FILE_PATH);
        String absolutePathToOutput = file.getAbsolutePath();

        // Assert & Execute
        assertThrows(SourceUnavailableException.class, () -> createSourceFile(absolutePathToOutput));
    }

    @Test
    @Order(8)
    @DisplayName("We are validating that a source file can be created under an existing path...")
    void createSourceFileTest_CreateSourceFileWithExistingPathPassesValidation2() {
        // Prepare
        File file = new File(OUTPUT_SOURCE_FILE_PATH);
        String absolutePathToOutput = file.getAbsolutePath();

        // Execute
        createSourceFile(absolutePathToOutput);

        // Assert
        assertTrue(validateTarget(absolutePathToOutput));
    }

    @Test
    @Order(9)
    @DisplayName("We are validating that a source file can be populated with existing file names...")
    void writeSourceFileTest_PopulateSourceFileWithFilenames() {
        // Prepare
        File directory = new File(IMAGES_PNG_LARGE_RESOURCE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(RESOURCES_SELECTED_EXTENSION));
        File file = new File(OUTPUT_SOURCE_FILE_PATH);
        assertDoesNotThrow(() -> {
            try {
                // Assert
                Stream<String> numberOfLines = Files.lines(file.toPath());
                assertEquals(numberOfLines.count(), 0);

                // Execute
                assert files != null;
                writeSourceFile(files, file);

                // Assert
                numberOfLines = Files.lines(file.toPath());
                assertEquals(numberOfLines.count(), files.length);
            } catch (IOException ignored) {}
        });
    }

    @Test
    @Order(10)
    @DisplayName("We are validating that the history hashmap gets populated by a source file...")
    void prepareHistoryByInput_HistoryPopulatedByInputSourceFile() {
        // Prepare
        Map<String, String> history = new HashMap<>();
        File directory = new File(IMAGES_PNG_LARGE_RESOURCE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(RESOURCES_SELECTED_EXTENSION));

        // Execute
        assert files != null;
        prepareHistoryByInput(files, history, INPUT_SOURCE_FILE_PATH, RESOURCES_SELECTED_EXTENSION);

        // Assert
        assertEquals(history.size(), files.length);
    }

    @Test
    @Order(11)
    @DisplayName("We are validating that multiple files can be renamed...")
    void renamingProcess_MultipleFilesAreRenamed() {
        // Prepare
        Map<String, String> history = Map.of(
            FIRST_RENAME_SOURCE_FILE, FIRST_CHANGED_SOURCE_FILE,
            SECOND_RENAME_SOURCE_FILE, SECOND_CHANGED_SOURCE_FILE,
            THIRD_RENAME_SOURCE_FILE, THIRD_CHANGED_SOURCE_FILE);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        File[] renameFiles = renameDirectory.listFiles();
        File processedDirectory = new File(RESOURCES_PROCESSED_PATH);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, history.size());

        // Execute
        renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, LABEL_PROCESSED_DIRECTORY);

        // Assert
        verifyProcessedDirectoryStatus(processedDirectory, history.size());
    }

    @Test
    @Order(13)
    @DisplayName("We are validating that the renaming process can fail, but remaining files are processed as expected...")
    void renamingProcess_MultipleFilesAreRenamedButOneFails() {
        // Prepare
        Map<String, String> history = Map.of(
            FIRST_RENAME_SOURCE_FILE, FIRST_CHANGED_SOURCE_FILE,
            INPUT_SOURCE_FILE, SECOND_CHANGED_SOURCE_FILE,
            THIRD_RENAME_SOURCE_FILE, THIRD_CHANGED_SOURCE_FILE);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        File[] renameFiles = renameDirectory.listFiles();
        File processedDirectory = new File(RESOURCES_PROCESSED_PATH);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, history.size());

        // Execute
        renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, LABEL_PROCESSED_DIRECTORY);

        // Assert
        verifyProcessedDirectoryStatus(processedDirectory, history.size() - 1);
    }

    @Test
    @Order(15)
    @DisplayName("We are validating that without a history the renaming process will fail, with just a message...")
    void renamingProcess_NoExecutionFailureUponEmptyHistory() {
        // Prepare
        Map<String, String> history = new HashMap<>();
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        File[] renameFiles = renameDirectory.listFiles();

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, 3);

        // Execute
        renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, LABEL_PROCESSED_DIRECTORY);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, 4);
    }

    @Test
    @Order(12)
    @DisplayName("We are validating that multiple files that were renamed can be restored...")
    void renamingUndoProcessTest_MultipleFilesAreRestored() {
        // Prepare
        Map<String, String> history = Map.of(
            FIRST_RENAME_SOURCE_FILE, FIRST_CHANGED_SOURCE_FILE,
            SECOND_RENAME_SOURCE_FILE, SECOND_CHANGED_SOURCE_FILE,
            THIRD_RENAME_SOURCE_FILE, THIRD_CHANGED_SOURCE_FILE);
        File processedDirectory = new File(RESOURCES_PROCESSED_PATH);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);

        // Assert
        verifyProcessedDirectoryStatus(processedDirectory, history.size());

        // Execute
        renamingUndoProcess(history, processedDirectory, RESOURCES_RENAME_PATH);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, history.size() + 1);
    }

    @Test
    @Order(14)
    @DisplayName("We are validating that specific files that were renamed can be restored...")
    void renamingUndoProcessTest_MultipleFilesAreRestoredSpecifically() {
        // Prepare
        Map<String, String> history = Map.of(
            FIRST_RENAME_SOURCE_FILE, FIRST_CHANGED_SOURCE_FILE,
            INPUT_SOURCE_FILE, SECOND_CHANGED_SOURCE_FILE,
            THIRD_RENAME_SOURCE_FILE, THIRD_CHANGED_SOURCE_FILE);
        File processedDirectory = new File(RESOURCES_PROCESSED_PATH);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);

        // Assert
        verifyProcessedDirectoryStatus(processedDirectory, history.size() - 1);

        // Execute
        renamingUndoProcess(history, processedDirectory, RESOURCES_RENAME_PATH);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, history.size() + 1);
    }
}
