package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.ConstantTest.INVALID_INPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.INVALID_RESOURCES_OUTPUT_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.INVALID_RESOURCES_PATH_VECTORS;
import static org.hellstrand.renfi.constant.ConstantTest.PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_IMAGES_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_IMAGES_PNG_LARGE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_NOTES_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_OUTPUT_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_OUTPUT_SOURCE_FILE_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_PNG_EXTENSION;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_RENAME_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.RESOURCES_RENAME_PROCESSED_PATH;
import static org.hellstrand.renfi.constant.ConstantTest.SOURCE_FILE_FIRST_CHANGED_FILE_TXT;
import static org.hellstrand.renfi.constant.ConstantTest.SOURCE_FILE_FIRST_RENAME_FILE_TXT;
import static org.hellstrand.renfi.constant.ConstantTest.SOURCE_FILE_INPUT_TXT;
import static org.hellstrand.renfi.constant.ConstantTest.SOURCE_FILE_SECOND_CHANGED_FILE_TXT;
import static org.hellstrand.renfi.constant.ConstantTest.SOURCE_FILE_SECOND_RENAME_FILE_TXT;
import static org.hellstrand.renfi.constant.ConstantTest.SOURCE_FILE_THIRD_CHANGED_FILE_TXT;
import static org.hellstrand.renfi.constant.ConstantTest.SOURCE_FILE_THIRD_RENAME_FILE_TXT;
import static org.hellstrand.renfi.constant.ConstantTest.TEST_LOGGING_DIRECTORY_OUTPUT;
import static org.hellstrand.renfi.constant.ConstantTest.TEST_LOGGING_FILE_OUTPUT;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(FileProcessingUtil.class);

    @AfterAll
    static void clearAll() {
        File fileOutput = new File(RESOURCES_OUTPUT_SOURCE_FILE_PATH);
        if (fileOutput.exists() && fileOutput.delete()) {
            logger.info(TEST_LOGGING_FILE_OUTPUT, RESOURCES_OUTPUT_SOURCE_FILE_PATH);
        }

        File directoryOutput = new File(RESOURCES_OUTPUT_PATH);
        if (directoryOutput.exists() && directoryOutput.delete()) {
            logger.info(TEST_LOGGING_DIRECTORY_OUTPUT, RESOURCES_OUTPUT_PATH);
        }
    }

    @AfterEach
    void clearEach() {
        File directoryProcessed = new File(RESOURCES_RENAME_PROCESSED_PATH);
        if (directoryProcessed.exists() && directoryProcessed.delete()) {
            logger.info(TEST_LOGGING_DIRECTORY_OUTPUT, RESOURCES_RENAME_PROCESSED_PATH);
        }
    }

    @Test
    @Order(1)
    @DisplayName("We are validating that a directory cannot be found with an invalid path...")
    void validateTargetTest_InvalidPathDirectoryMissing() {
        // Assert & Execute
        assertFalse(validateTarget(INVALID_RESOURCES_PATH_VECTORS));
    }

    @Test
    @Order(2)
    @DisplayName("We are validating that a directory can be found with an existing path...")
    void validateTargetTest_ExistingPathDirectoryFound() {
        // Assert & Execute
        assertTrue(validateTarget(RESOURCES_IMAGES_PATH));
    }

    @Test
    @Order(3)
    @DisplayName("We are validating that a file cannot be found under an existing path...")
    void validateTargetTest_ExistingPathFileMissing() {
        // Assert & Execute
        assertFalse(validateTarget(RESOURCES_IMAGES_PATH.concat(SOURCE_FILE_INPUT_TXT)));
    }

    @Test
    @Order(4)
    @DisplayName("We are validating that a file can be found under an existing path...")
    void validateTargetTest_ExistingPathFileFound() {
        // Assert & Execute
        assertTrue(validateTarget(RESOURCES_NOTES_SOURCE_FILE_PATH));
    }

    @Test
    @Order(5)
    @DisplayName("We are validating that a directory cannot be created with an invalid path...")
    void createTargetDirectoryTest_CreateDirectoryWithInvalidPathThrowsException() {
        // Prepare
        File directory = new File(INVALID_RESOURCES_OUTPUT_PATH);
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
        File file = new File(RESOURCES_OUTPUT_SOURCE_FILE_PATH);
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
        File directory = new File(RESOURCES_IMAGES_PNG_LARGE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(RESOURCES_PNG_EXTENSION));
        File file = new File(RESOURCES_OUTPUT_SOURCE_FILE_PATH);
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
        File directory = new File(RESOURCES_IMAGES_PNG_LARGE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(RESOURCES_PNG_EXTENSION));

        // Execute
        assert files != null;
        prepareHistoryByInput(files, history, RESOURCES_NOTES_SOURCE_FILE_PATH, RESOURCES_PNG_EXTENSION);

        // Assert
        assertEquals(history.size(), files.length);
    }

    @Test
    @Order(11)
    @DisplayName("We are validating that multiple files can be renamed...")
    void renamingProcess_MultipleFilesAreRenamed() {
        // Prepare
        Map<String, String> history = Map.of(
            SOURCE_FILE_FIRST_RENAME_FILE_TXT, SOURCE_FILE_FIRST_CHANGED_FILE_TXT,
            SOURCE_FILE_SECOND_RENAME_FILE_TXT, SOURCE_FILE_SECOND_CHANGED_FILE_TXT,
            SOURCE_FILE_THIRD_RENAME_FILE_TXT, SOURCE_FILE_THIRD_CHANGED_FILE_TXT);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        File[] renameFiles = renameDirectory.listFiles();
        File processedDirectory = new File(RESOURCES_RENAME_PROCESSED_PATH);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, history.size());

        // Execute
        renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, PROCESSED_DIRECTORY);

        // Assert
        verifyProcessedDirectoryStatus(processedDirectory, history.size());
    }

    @Test
    @Order(13)
    @DisplayName("We are validating that the renaming process can fail, but remaining files are processed as expected...")
    void renamingProcess_MultipleFilesAreRenamedButOneFails() {
        // Prepare
        Map<String, String> history = Map.of(
            SOURCE_FILE_FIRST_RENAME_FILE_TXT, SOURCE_FILE_FIRST_CHANGED_FILE_TXT,
            SOURCE_FILE_INPUT_TXT, SOURCE_FILE_SECOND_CHANGED_FILE_TXT,
            SOURCE_FILE_THIRD_RENAME_FILE_TXT, SOURCE_FILE_THIRD_CHANGED_FILE_TXT);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);
        File[] renameFiles = renameDirectory.listFiles();
        File processedDirectory = new File(RESOURCES_RENAME_PROCESSED_PATH);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, history.size());

        // Execute
        renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, PROCESSED_DIRECTORY);

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
        renamingProcess(renameFiles, history, RESOURCES_RENAME_PATH, PROCESSED_DIRECTORY);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, 4);
    }

    @Test
    @Order(12)
    @DisplayName("We are validating that multiple files that were renamed can be restored...")
    void renamingUndoProcessTest_MultipleFilesAreRestored() {
        // Prepare
        Map<String, String> history = Map.of(
            SOURCE_FILE_FIRST_RENAME_FILE_TXT, SOURCE_FILE_FIRST_CHANGED_FILE_TXT,
            SOURCE_FILE_SECOND_RENAME_FILE_TXT, SOURCE_FILE_SECOND_CHANGED_FILE_TXT,
            SOURCE_FILE_THIRD_RENAME_FILE_TXT, SOURCE_FILE_THIRD_CHANGED_FILE_TXT);
        File processedDirectory = new File(RESOURCES_RENAME_PROCESSED_PATH);
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
            SOURCE_FILE_FIRST_RENAME_FILE_TXT, SOURCE_FILE_FIRST_CHANGED_FILE_TXT,
            SOURCE_FILE_INPUT_TXT, SOURCE_FILE_SECOND_CHANGED_FILE_TXT,
            SOURCE_FILE_THIRD_RENAME_FILE_TXT, SOURCE_FILE_THIRD_CHANGED_FILE_TXT);
        File processedDirectory = new File(RESOURCES_RENAME_PROCESSED_PATH);
        File renameDirectory = new File(RESOURCES_RENAME_PATH);

        // Assert
        verifyProcessedDirectoryStatus(processedDirectory, history.size() - 1);

        // Execute
        renamingUndoProcess(history, processedDirectory, RESOURCES_RENAME_PATH);

        // Assert
        verifyRenameDirectoryStatus(renameDirectory, history.size() + 1);
    }
}
