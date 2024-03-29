package org.hellstrand.renfi;

import static org.hellstrand.renfi.constant.Constants.ALLOWED_FLAGS;
import static org.hellstrand.renfi.constant.Constants.BRANCH_INDEX;
import static org.hellstrand.renfi.constant.Constants.COMPARE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CONVERT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CROP_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DATA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DATE_TYPE_INDEX;
import static org.hellstrand.renfi.constant.Constants.DETECT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_ATTRIBUTES;
import static org.hellstrand.renfi.constant.Constants.PATH_INDEX;
import static org.hellstrand.renfi.constant.Constants.EXTENSION_FROM_INDEX;
import static org.hellstrand.renfi.constant.Constants.EXTENSION_TO_INDEX;
import static org.hellstrand.renfi.constant.Constants.FAILURE;
import static org.hellstrand.renfi.constant.Constants.FILE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.FLOW_INDEX;
import static org.hellstrand.renfi.constant.Constants.HELP_FLAGS;
import static org.hellstrand.renfi.constant.Constants.IMAGE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LABEL_COMPARE;
import static org.hellstrand.renfi.constant.Constants.LABEL_CONVERT;
import static org.hellstrand.renfi.constant.Constants.LABEL_CREATED;
import static org.hellstrand.renfi.constant.Constants.LABEL_CROP;
import static org.hellstrand.renfi.constant.Constants.LABEL_DATA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LABEL_DETECT;
import static org.hellstrand.renfi.constant.Constants.LABEL_FILE;
import static org.hellstrand.renfi.constant.Constants.LABEL_FILENAMES;
import static org.hellstrand.renfi.constant.Constants.LABEL_FILE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LABEL_IMAGES;
import static org.hellstrand.renfi.constant.Constants.LABEL_UNKNOWN_EXECUTION;
import static org.hellstrand.renfi.constant.Constants.LABEL_VIDEOS;
import static org.hellstrand.renfi.constant.Constants.LIST_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DESIRED_EXECUTION;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DIRECTORY_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_EXECUTION_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_USE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOADING_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOADING_FILES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_TASK;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RESOURCES_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.PROCESSING_SUPPORT;
import static org.hellstrand.renfi.constant.Constants.RESOURCE_TYPE_INDEX;
import static org.hellstrand.renfi.constant.Constants.SOURCE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.SUCCESSFUL;
import static org.hellstrand.renfi.constant.Constants.UPPER_LEFT_X_INDEX;
import static org.hellstrand.renfi.constant.Constants.UPPER_LEFT_Y_INDEX;
import static org.hellstrand.renfi.util.FileProcessingUtil.validateTarget;
import static org.hellstrand.renfi.util.HelpGuideUtil.displayHelpGuide;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import org.hellstrand.renfi.manager.DataHandlingManager;
import org.hellstrand.renfi.manager.FileHandlingManager;
import org.hellstrand.renfi.manager.HistoryHandlingManager;

/**
 * @author (Mats Richard Hellstrand)
 * @version (6th of September, 2023)
 */
public final class RenfiUtility {
    public static void main(String[] args) {
        if (args.length < 9 || HELP_FLAGS.contains(args[0])) {
            displayHelpGuide();
            System.exit(SUCCESSFUL);
        }

        // "Prepare" the flow of the application...
        String flow = args[FLOW_INDEX];
        String branch = args[BRANCH_INDEX];
        String path = args[PATH_INDEX];
        String resourceType = args[RESOURCE_TYPE_INDEX];
        String fromIndex = args[EXTENSION_FROM_INDEX];
        String toIndex = args[EXTENSION_TO_INDEX];
        String leftXAxis = args[UPPER_LEFT_X_INDEX];
        String leftYAxis = args[UPPER_LEFT_Y_INDEX];
        String dateType = args[DATE_TYPE_INDEX];
        if (!ALLOWED_FLAGS.contains(flow)
            || !ALLOWED_FLAGS.contains(branch)
            || !ALLOWED_FLAGS.contains(resourceType)
            || !PROCESSING_SUPPORT.containsKey(resourceType)) {
            printMessage(MESSAGE_INVALID_USE);
            System.exit(FAILURE);
        }

        if (!validateTarget(path)) {
            System.out.printf(MESSAGE_DIRECTORY_UNAVAILABLE, path);
            System.exit(FAILURE);
        }

        List<String> selectedExtensions = PROCESSING_SUPPORT.get(resourceType);
        int extensionFromIndex = Integer.parseInt(fromIndex), extensionToIndex = Integer.parseInt(toIndex);
        if (extensionFromIndex < 0 && extensionToIndex >= selectedExtensions.size()) {
            printMessage(MESSAGE_INVALID_USE);
            System.exit(FAILURE);
        }

        String flowTask = flow.equals(FILE_PROCESSING) ? LABEL_FILE_PROCESSING :
            flow.equals(DATA_PROCESSING) ? LABEL_DATA_PROCESSING :
                LABEL_UNKNOWN_EXECUTION;
        String branchTask =
            branch.equals(COMPARE_PROCESSING) ? LABEL_COMPARE :
                branch.equals(CROP_PROCESSING) ? LABEL_CROP :
                    branch.equals(CONVERT_PROCESSING) ? LABEL_CONVERT :
                        branch.equals(DETECT_PROCESSING) ? LABEL_DETECT :
                            branch.equals(ORIGIN_PROCESSING) ? LABEL_CREATED :
                                branch.equals(LIST_PROCESSING) ? LABEL_FILE :
                                    branch.equals(SOURCE_PROCESSING) ? LABEL_FILENAMES :
                                        LABEL_UNKNOWN_EXECUTION;
        String resourceTask = resourceType.equals(IMAGE_PROCESSING) ? LABEL_IMAGES : LABEL_VIDEOS;
        String fromExtension = selectedExtensions.get(extensionFromIndex);
        String toExtension = selectedExtensions.get(extensionToIndex);
        System.out.printf(MESSAGE_PROCESSING_TASK, flowTask, branchTask, resourceTask, path);
        System.out.printf(
            MESSAGE_PROCESSING_ATTRIBUTES,
            fromExtension.substring(1), toExtension.substring(1), dateType, leftXAxis, leftYAxis);

        printMessage(MESSAGE_DESIRED_EXECUTION);
        Scanner scanner = new Scanner(System.in);
        String key = scanner.nextLine();
        scanner.close();
        if (key.equals("y")) { // Should the overall task continue?
            // Verify that the target directory exist...
            printMessage(MESSAGE_LOADING_DIRECTORY);
            File directory = new File(path);
            if (!directory.exists() && !directory.isDirectory()) {
                System.out.printf(MESSAGE_DIRECTORY_UNAVAILABLE, path);
                System.exit(FAILURE);
            } else {
                System.out.println(path);
            }

            // Load the files into memory under the target directory...
            printMessage(MESSAGE_LOADING_FILES);
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(fromExtension));
            if (Objects.nonNull(files) && files.length > 0) {
                for (File file : files) {
                    System.out.println(file.getName());
                }
            } else {
                printMessage(MESSAGE_RESOURCES_UNAVAILABLE);
                System.exit(FAILURE);
            }

            if (flow.equals(FILE_PROCESSING)) { // If we want to modify a file or analyze it...
                FileHandlingManager.processBranch(branch, files, path, leftXAxis, leftYAxis, fromExtension, toExtension);
            } else if (flow.equals(DATA_PROCESSING)) { // If we want to prepare conversion history and execute renaming...
                Map<String, String> history = new LinkedHashMap<>();
                DataHandlingManager.processBranch(branch, resourceType, path, files, history, fromExtension, dateType);
                HistoryHandlingManager.processHistory(files, history, path, directory);
            }
        } else {
            printMessage(MESSAGE_EXECUTION_ABORT);
        }

        System.exit(SUCCESSFUL);
    }
}
