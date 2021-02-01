package org.hellstrand.renfi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.hellstrand.renfi.Constants.MESSAGE_FAILURE_SOURCES;
import static org.hellstrand.renfi.Constants.MESSAGE_RENAMING_ALERT;
import static org.hellstrand.renfi.Constants.MESSAGE_RENAMING_FAILURE;
import static org.hellstrand.renfi.Constants.MESSAGE_SORTING_FILES;
import static org.hellstrand.renfi.Constants.MESSAGE_UNDO_ALERT;

/**
 * @author (Mats Richard Hellstrand)
 * @version (1st of February, 2021)
 */
public abstract class FileProcessingUtil {
    public static void prepareConversionHistory(File[] files, Map<String, String> history, String target, String extension) {
        try {
            System.out.println(MESSAGE_SORTING_FILES);
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            System.out.println(Arrays.toString(files));

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
        for (File f : files) {
            String previousName = f.getName();
            String newName = history.get(previousName);

            if (newName != null) {
                if (f.renameTo(new File(directory + newName))) {
                    System.out.printf(MESSAGE_RENAMING_ALERT, previousName, newName);
                } else {
                    System.out.println(MESSAGE_RENAMING_FAILURE);
                    System.out.printf(MESSAGE_FAILURE_SOURCES, previousName, newName);
                }
            } else {
                System.out.printf(MESSAGE_FAILURE_SOURCES, previousName, null);
            }
        }
    }

    public static void renamingUndoProcess(Map<String, String> history, File path, String directory) {
        System.out.println(Constants.MESSAGE_UNDO_RELOADING);
        File[] undo = path.listFiles((dir, name) -> history.values().stream().anyMatch(n -> n.equals(name)));

        if (undo != null && undo.length > 0) {
            System.out.println(Constants.MESSAGE_UNDO_RESTORING);
            for (File file : undo) {
                String undoName = file.getName();
                String previousName = history.entrySet().stream()
                    .filter(entry -> undoName.equals(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

                if (previousName != null && file.renameTo(new File(directory + previousName))) {
                    System.out.printf(MESSAGE_UNDO_ALERT, undoName, previousName);
                } else {
                    System.out.println(MESSAGE_RENAMING_FAILURE);
                    System.out.printf(MESSAGE_FAILURE_SOURCES, undoName, previousName);
                }
            }
        } else {
            System.out.println(Constants.MESSAGE_FAILED_UNDO_LOADING);
        }
    }
}
