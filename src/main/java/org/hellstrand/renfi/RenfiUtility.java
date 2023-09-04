package org.hellstrand.renfi;

import static org.hellstrand.renfi.constant.Constants.ALLOWED_FLAGS;
import static org.hellstrand.renfi.constant.Constants.BRANCH_INDEX;
import static org.hellstrand.renfi.constant.Constants.COMPARE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CONVERT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CREATION_TIME_FLAG;
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
import static org.hellstrand.renfi.constant.Constants.JAVA_PROCESSING;
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
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONTINUE_RENAMING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONVERSION_HISTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONVERSION_HISTORY_EMPTY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DESIRED_EXECUTION;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DIRECTORY_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_EXECUTION_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_FAILED_MISMATCH;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_USE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOADING_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOADING_FILES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_TASK;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RENAMING_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RENAMING_PROCESS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RESOURCES_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_SOURCE_CONTAINS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_SOURCE_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_CONTINUE;
import static org.hellstrand.renfi.constant.Constants.NAMES_SOURCE;
import static org.hellstrand.renfi.constant.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.OUTPUT_SOURCE;
import static org.hellstrand.renfi.constant.Constants.PROCESSING_SUPPORT;
import static org.hellstrand.renfi.constant.Constants.RESOURCE_TYPE_INDEX;
import static org.hellstrand.renfi.constant.Constants.SOURCE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.SUCCESSFUL;
import static org.hellstrand.renfi.constant.Constants.UPPER_LEFT_X_INDEX;
import static org.hellstrand.renfi.constant.Constants.UPPER_LEFT_Y_INDEX;
import static org.hellstrand.renfi.constant.Constants.VIDEO_PROCESSING;
import static org.hellstrand.renfi.util.HelpGuideUtil.displayHelpGuide;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import org.hellstrand.renfi.util.FileProcessingUtil;
import org.hellstrand.renfi.util.ImageProcessingUtil;
import org.hellstrand.renfi.util.NioProcessingUtil;
import org.hellstrand.renfi.util.VideoProcessingUtil;

/**
 * @author (Mats Richard Hellstrand)
 * @version (4th of September, 2023)
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
        int[] coordinates = new int[] {
            Integer.parseInt(args[UPPER_LEFT_X_INDEX]),
            Integer.parseInt(args[UPPER_LEFT_Y_INDEX])
        };
        if (!ALLOWED_FLAGS.contains(flow)
            || !ALLOWED_FLAGS.contains(branch)
            || !ALLOWED_FLAGS.contains(resourceType)
            || !PROCESSING_SUPPORT.containsKey(resourceType)) {
            printMessage(MESSAGE_INVALID_USE);
            System.exit(FAILURE);
        }

        int resourceFromIndex = -1, resourceToIndex = -1;
        if (fromIndex.length() == 1 && toIndex.length() == 1) {
            resourceFromIndex = Integer.parseInt(fromIndex);
            resourceToIndex = Integer.parseInt(toIndex);
            if (resourceFromIndex >= PROCESSING_SUPPORT.get(resourceType).size()
                || resourceFromIndex >= PROCESSING_SUPPORT.get(resourceType).size()) {
                printMessage(MESSAGE_INVALID_USE);
                System.exit(FAILURE);
            }
        } else if (PROCESSING_SUPPORT.get(resourceType).contains(fromIndex)
                   && PROCESSING_SUPPORT.get(resourceType).contains(toIndex)) {
            resourceFromIndex = PROCESSING_SUPPORT.get(resourceType).indexOf(fromIndex);
            resourceToIndex = PROCESSING_SUPPORT.get(resourceType).indexOf(toIndex);
        } else {
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
        String dateTypeFlag = args[DATE_TYPE_INDEX] != null ? args[DATE_TYPE_INDEX] : CREATION_TIME_FLAG;
        String fromExtension = PROCESSING_SUPPORT.get(resourceType).get(resourceFromIndex);
        String toExtension = PROCESSING_SUPPORT.get(resourceType).get(resourceToIndex);
        System.out.printf(MESSAGE_PROCESSING_TASK, flowTask, branchTask, resourceTask, path);
        System.out.printf(
            MESSAGE_PROCESSING_ATTRIBUTES,
            fromExtension.substring(1), toExtension.substring(1),
            dateTypeFlag, coordinates[0], coordinates[1]);
        System.out.println();

        printMessage(MESSAGE_DESIRED_EXECUTION);
        Scanner scanner = new Scanner(System.in);
        String key = scanner.nextLine();
        if (key.equals("y")) { // Should the overall task continue?
            try {
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
                    String outputSource = path.concat(OUTPUT_SOURCE);

                    switch (branch) {
                        case COMPARE_PROCESSING:
                            FileProcessingUtil.compareResources(files, path, outputSource);
                            break;
                        case CROP_PROCESSING:
                            FileProcessingUtil.cropResources(files, path, outputSource, coordinates, toExtension);
                            break;
                        case CONVERT_PROCESSING:
                            FileProcessingUtil.convertResources(files, path, outputSource, fromExtension, toExtension);
                            break;
                        case DETECT_PROCESSING:
                            FileProcessingUtil.detectBlackBorders(files, path, outputSource);
                            break;
                        default:
                            printMessage(MESSAGE_EXECUTION_ABORT);
                            break;
                    }
                } else if (flow.equals(DATA_PROCESSING)) {
                    String namesSource = path.concat(NAMES_SOURCE);

                    if (branch.equals(SOURCE_PROCESSING)) { // Prepare a source file based on directory files...
                        // Verify that the source file exist...
                        File sourceFile = new File(namesSource);
                        if (!sourceFile.isFile() && !sourceFile.exists()) {
                            printMessage(MESSAGE_SOURCE_UNAVAILABLE);
                            System.exit(FAILURE);
                        }

                        printMessage(MESSAGE_SOURCE_CONTAINS);
                        PrintWriter printWriter = new PrintWriter(namesSource);
                        for (File file : files) {
                            System.out.println(file.getName());
                            printWriter.println(file.getName());
                        }
                        printWriter.close();
                    } else { // Otherwise, prepare and process conversion...
                        Map<String, String> history = new LinkedHashMap<>();
                        if (branch.equals(JAVA_PROCESSING)) { // Prepare conversion history based on Java +7...
                            NioProcessingUtil.javaProcessing(files, history, fromExtension, dateTypeFlag);
                        } else { // Prepare conversion history based on Drew Noakes's extractor...
                            if (branch.equals(ORIGIN_PROCESSING)) { // Prepare conversion history based on origin data...
                                if (resourceType.equals(VIDEO_PROCESSING)) {
                                    VideoProcessingUtil.prepareHistoryByOrigin(files, history, fromExtension);
                                } else if (resourceType.equals(IMAGE_PROCESSING)) {
                                    ImageProcessingUtil.prepareHistoryByOrigin(files, history, fromExtension);
                                }
                            } else if (branch.equals(LIST_PROCESSING)) { // Prepare conversion history based on file input...
                                if (resourceType.equals(VIDEO_PROCESSING)) {
                                    VideoProcessingUtil.prepareHistoryByInput(files, history, namesSource, fromExtension);
                                } else if (resourceType.equals(IMAGE_PROCESSING)) {
                                    ImageProcessingUtil.prepareHistoryByInput(files, history, namesSource, fromExtension);
                                }
                            }
                        }

                        // Display available conversion history...
                        printMessage(MESSAGE_CONVERSION_HISTORY);
                        if (!history.isEmpty()) {
                            for (Map.Entry<String, String> entry : history.entrySet()) {
                                System.out.println("Entry: " + entry.getKey() + ": " + entry.getValue());
                            }

                            // Begin the renaming process...
                            printMessage(MESSAGE_CONTINUE_RENAMING);
                            scanner = new Scanner(System.in);
                            key = scanner.nextLine();
                            if (key.equals("y")) { // Should the renaming process be executed?
                                printMessage(MESSAGE_RENAMING_PROCESS);
                                if (history.size() > 0) {
                                    FileProcessingUtil.renamingProcess(files, history, path);

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
                        } else {
                            printMessage(MESSAGE_CONVERSION_HISTORY_EMPTY);
                        }
                        scanner.close();
                    }
                } else {
                    printMessage(MESSAGE_EXECUTION_ABORT);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.exit(FAILURE);
            }
        } else {
            printMessage(MESSAGE_EXECUTION_ABORT);
        }
        scanner.close();

        System.exit(SUCCESSFUL);
    }
}
