package org.hellstrand.renfi.manager;

import static org.hellstrand.renfi.constant.Constants.LABEL_PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONTINUE_RENAMING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONVERSION_HISTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONVERSION_HISTORY_EMPTY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_FAILED_MISMATCH;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RENAMING_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RENAMING_PROCESS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_CONTINUE;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import org.hellstrand.renfi.util.FileProcessingUtil;

/**
 * @author (Mats Richard Hellstrand)
 * @version (9th of September, 2025)
 */
public class HistoryHandlingManager {
    public static void processHistory(File[] files, Map<String, String> history, String path, File directory) {
        // Display available conversion history...
        printMessage(MESSAGE_CONVERSION_HISTORY);
        if (!history.isEmpty()) {
            for (Map.Entry<String, String> entry : history.entrySet()) {
                System.out.println("Entry: " + entry.getKey() + ": " + entry.getValue());
            }

            // Begin the renaming process...
            printMessage(MESSAGE_CONTINUE_RENAMING);
            Scanner scanner = new Scanner(System.in);
            String key = scanner.nextLine();
            if (key.equals("y")) { // Should the renaming process be executed?
                printMessage(MESSAGE_RENAMING_PROCESS);
                if (history.size() > 0) {
                    FileProcessingUtil.renamingProcess(files, history, path, LABEL_PROCESSED_DIRECTORY);

                    printMessage(MESSAGE_UNDO_CONTINUE);
                    key = scanner.nextLine();
                    if (key.equals("y")) { // Should the undo process be executed?
                        FileProcessingUtil.renamingUndoProcess(history, directory, path);
                    } else {
                        printMessage(MESSAGE_UNDO_ABORT);
                    }
                } else {
                    printMessage(MESSAGE_FAILED_MISMATCH);
                }
            } else {
                printMessage(MESSAGE_RENAMING_ABORT);
            }
            scanner.close();
        } else {
            printMessage(MESSAGE_CONVERSION_HISTORY_EMPTY);
        }
    }
}
