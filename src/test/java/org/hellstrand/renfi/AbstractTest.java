package org.hellstrand.renfi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

/**
 * @author (Mats Richard Hellstrand)
 * @version (28th of September, 2025)
 */
public abstract class AbstractTest {
    protected void verifyRenameDirectoryStatus(File renameDirectory, int historySize) {
        File[] renameFiles = renameDirectory.listFiles();
        assert renameFiles != null;
        assertEquals(historySize, renameFiles.length);
    }

    protected void verifyProcessedDirectoryStatus(File processedDirectory, int historySize) {
        File[] processedFiles = processedDirectory.listFiles();
        assert processedFiles != null;
        assertEquals(historySize, processedFiles.length);
    }
}
