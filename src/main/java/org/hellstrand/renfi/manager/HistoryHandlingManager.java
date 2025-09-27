package org.hellstrand.renfi.manager;

import static org.hellstrand.renfi.constant.Constants.LABEL_PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONTINUE_RENAMING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONVERSION_ENTRY_SET;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONVERSION_HISTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_EMPTY_CONVERSION_HISTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_MISMATCHING_CONVERSION_HISTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RENAMING_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RENAMING_PROCESS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_CONTINUE;

import org.hellstrand.renfi.exception.EmptyConversionHistoryException;
import org.hellstrand.renfi.exception.MismatchingConversionHistoryException;
import org.hellstrand.renfi.util.FileProcessingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

/**
 * @author (Mats Richard Hellstrand)
 * @version (27th of September, 2025)
 */
public final class HistoryHandlingManager {
    private static final Logger logger = LoggerFactory.getLogger(HistoryHandlingManager.class);

    public static void processHistory(File[] files, Map<String, String> history, String path, File directory) {
        // Display available conversion history...
        logger.info(MESSAGE_CONVERSION_HISTORY);
        if (!history.isEmpty()) {
            for (Map.Entry<String, String> entry : history.entrySet()) {
                logger.info(MESSAGE_CONVERSION_ENTRY_SET, entry.getKey(), entry.getValue());
            }

            // Begin the renaming process...
            logger.info(MESSAGE_CONTINUE_RENAMING);
            Scanner scanner = new Scanner(System.in);
            String key = scanner.nextLine();
            if (key.equals("y")) { // Should the renaming process be executed?
                logger.info(MESSAGE_RENAMING_PROCESS);
                if (files.length == history.size()) {
                    FileProcessingUtil.renamingProcess(files, history, path, LABEL_PROCESSED_DIRECTORY);

                    logger.info(MESSAGE_UNDO_CONTINUE);
                    key = scanner.nextLine();
                    if (key.equals("y")) { // Should the undo process be executed?
                        FileProcessingUtil.renamingUndoProcess(history, directory, path);
                    } else {
                        logger.info(MESSAGE_UNDO_ABORT);
                    }
                } else {
                    logger.error(MESSAGE_MISMATCHING_CONVERSION_HISTORY);
                    throw new MismatchingConversionHistoryException(MESSAGE_MISMATCHING_CONVERSION_HISTORY);
                }
            } else {
                logger.info(MESSAGE_RENAMING_ABORT);
            }
            scanner.close();
        } else {
            logger.error(MESSAGE_EMPTY_CONVERSION_HISTORY);
            throw new EmptyConversionHistoryException(MESSAGE_EMPTY_CONVERSION_HISTORY);
        }
    }
}
