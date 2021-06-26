package org.hellstrand.renfi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static org.hellstrand.renfi.util.Constants.MESSAGE_FAILED_UNDO_LOADING;
import static org.hellstrand.renfi.util.Constants.MESSAGE_FAILURE_SOURCES;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RENAMING_ALERT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RENAMING_FAILURE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_SORTING_FILES;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_ALERT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_RELOADING;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_RESTORING;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

/**
 * @author (Mats Richard Hellstrand)
 * @version (26th of June, 2021)
 */
public abstract class FileProcessingUtil {
    public static void prepareHistoryByInput(File[] files, Map<String, String> history, String target, String extension) {
        try {
            printMessage(MESSAGE_SORTING_FILES);
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            for (File file : files) {
                System.out.println(file.getName());
            }

            File source = new File(target);
            Scanner scanner = new Scanner(source);
            List<String> names = new ArrayList<>();
            while (scanner.hasNextLine()) {
                names.add(scanner.nextLine() + extension);
            }
            scanner.close();

            for (int i = 0; i < files.length; i++) {
                String oldName = files[i].getName();
                String newName = names.get(i);
                history.put(oldName, newName);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void renamingProcess(Map<String, String> history, File[] files, String directory) {
        for (File file : files) {
            String previousName = file.getName();
            String newName = history.get(previousName);

            if (Objects.nonNull(newName)) {
                if (file.renameTo(new File(directory + newName))) {
                    System.out.printf(MESSAGE_RENAMING_ALERT, previousName, newName);
                } else {
                    printMessage(MESSAGE_RENAMING_FAILURE);
                    System.out.printf(MESSAGE_FAILURE_SOURCES, previousName, newName);
                }
            } else {
                System.out.printf(MESSAGE_FAILURE_SOURCES, previousName, null);
            }
        }
    }

    public static void renamingUndoProcess(Map<String, String> history, File path, String directory) {
        printMessage(MESSAGE_UNDO_RELOADING);
        File[] undo = path.listFiles((dir, name) -> history.values().stream().anyMatch(n -> n.equals(name)));
        if (Objects.requireNonNull(undo).length > 0) {
            for (File file : undo) {
                System.out.println(file.getName());
            }

            printMessage(MESSAGE_UNDO_RESTORING);
            for (File file : undo) {
                String undoName = file.getName();
                String previousName = history.entrySet().stream()
                    .filter(entry -> undoName.equals(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

                if (Objects.nonNull(previousName) && file.renameTo(new File(directory + previousName))) {
                    System.out.printf(MESSAGE_UNDO_ALERT, undoName, previousName);
                } else {
                    printMessage(MESSAGE_RENAMING_FAILURE);
                    System.out.printf(MESSAGE_FAILURE_SOURCES, undoName, previousName);
                }
            }
        } else {
            printMessage(MESSAGE_FAILED_UNDO_LOADING);
        }
    }
}
