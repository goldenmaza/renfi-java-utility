package org.hellstrand.renfi;

import org.hellstrand.renfi.util.FileProcessingUtil;
import org.hellstrand.renfi.util.ImageProcessingUtil;
import org.hellstrand.renfi.util.NioProcessingUtil;
import org.hellstrand.renfi.util.VideoProcessingUtil;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static org.hellstrand.renfi.util.Constants.ALLOWED_FLAGS;
import static org.hellstrand.renfi.util.Constants.BRANCH_INDEX;
import static org.hellstrand.renfi.util.Constants.COMMAND_INDEX;
import static org.hellstrand.renfi.util.Constants.CREATION_TIME_FLAG;
import static org.hellstrand.renfi.util.Constants.DATA_PROCESSING;
import static org.hellstrand.renfi.util.Constants.DATE_TYPE_INDEX;
import static org.hellstrand.renfi.util.Constants.DIRECTORY_INDEX;
import static org.hellstrand.renfi.util.Constants.EXTENSION_INDEX;
import static org.hellstrand.renfi.util.Constants.FAILURE;
import static org.hellstrand.renfi.util.Constants.FILE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.SOURCE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.FLOW_INDEX;
import static org.hellstrand.renfi.util.Constants.HELP_FLAGS;
import static org.hellstrand.renfi.util.Constants.IMAGE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.JAVA_PROCESSING;
import static org.hellstrand.renfi.util.Constants.LABEL_CREATED;
import static org.hellstrand.renfi.util.Constants.LABEL_DATA_PROCESSING;
import static org.hellstrand.renfi.util.Constants.LABEL_FILE;
import static org.hellstrand.renfi.util.Constants.LABEL_FILENAMES;
import static org.hellstrand.renfi.util.Constants.LABEL_FILE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.LABEL_IMAGES;
import static org.hellstrand.renfi.util.Constants.LABEL_UNKNOWN_EXECUTION;
import static org.hellstrand.renfi.util.Constants.LABEL_VIDEOS;
import static org.hellstrand.renfi.util.Constants.LIST_PROCESSING;
import static org.hellstrand.renfi.util.Constants.MESSAGE_CONTINUE_RENAMING;
import static org.hellstrand.renfi.util.Constants.MESSAGE_CONVERSION_HISTORY;
import static org.hellstrand.renfi.util.Constants.MESSAGE_DESIRED_EXECUTION;
import static org.hellstrand.renfi.util.Constants.MESSAGE_DIRECTORY_UNAVAILABLE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_EXECUTION_ABORT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_FAILED_MISMATCH;
import static org.hellstrand.renfi.util.Constants.MESSAGE_INVALID_USE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_LOADING_DIRECTORY;
import static org.hellstrand.renfi.util.Constants.MESSAGE_LOADING_FILES;
import static org.hellstrand.renfi.util.Constants.MESSAGE_PROCESSING_TASK;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RENAMING_ABORT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RENAMING_PROCESS;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RESOURCES_UNAVAILABLE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_SOURCE_CONTAINS;
import static org.hellstrand.renfi.util.Constants.MESSAGE_SOURCE_UNAVAILABLE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_ABORT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_CONTINUE;
import static org.hellstrand.renfi.util.Constants.NAMES_SOURCE;
import static org.hellstrand.renfi.util.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.util.Constants.PROCESSING_SUPPORT;
import static org.hellstrand.renfi.util.Constants.SUCCESSFUL;
import static org.hellstrand.renfi.util.Constants.VIDEO_PROCESSING;
import static org.hellstrand.renfi.util.HelpGuideUtil.displayHelpGuide;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

/**
 * @author (Mats Richard Hellstrand)
 * @version (3rd of September, 2023)
 */
public final class RenfiUtility {
    public static void main(String[] args) {
        if (args.length == 0
            || args.length == 1 && HELP_FLAGS.contains(args[0])) {
            displayHelpGuide();
            System.exit(SUCCESSFUL);
        } else if (args.length < 4) {
            printMessage(MESSAGE_INVALID_USE);
            System.exit(FAILURE);
        }

        // "Prepare" the flow of the application...
        String flow = args[FLOW_INDEX];
        String branch = args[BRANCH_INDEX];
        String command = args[COMMAND_INDEX];
        String index = args[EXTENSION_INDEX];
        if (!ALLOWED_FLAGS.contains(flow)
            || !ALLOWED_FLAGS.contains(branch)
            || !ALLOWED_FLAGS.contains(command)
            || !PROCESSING_SUPPORT.containsKey(command)) {
            printMessage(MESSAGE_INVALID_USE);
            System.exit(FAILURE);
        }

        int commandIndex = -1;
        if (index.length() == 1) {
            commandIndex = Integer.parseInt(index);
            if (commandIndex >= PROCESSING_SUPPORT.get(command).size()) {
                printMessage(MESSAGE_INVALID_USE);
                System.exit(FAILURE);
            }
        } else if (PROCESSING_SUPPORT.get(command).contains(index)) {
            commandIndex = PROCESSING_SUPPORT.get(command).indexOf(index);
        } else {
            printMessage(MESSAGE_INVALID_USE);
            System.exit(FAILURE);
        }

        String extension = PROCESSING_SUPPORT.get(command).get(commandIndex);
        String flowType = flow.equals(FILE_PROCESSING) ? LABEL_FILE_PROCESSING :
            flow.equals(DATA_PROCESSING) ? LABEL_DATA_PROCESSING :
                LABEL_UNKNOWN_EXECUTION;
        String branchTask =
            branch.equals(ORIGIN_PROCESSING) ? LABEL_CREATED :
                branch.equals(LIST_PROCESSING) ? LABEL_FILE :
                    branch.equals(SOURCE_PROCESSING) ? LABEL_FILENAMES :
                        LABEL_UNKNOWN_EXECUTION;
        String commandTask = command.equals(IMAGE_PROCESSING) ? LABEL_IMAGES : LABEL_VIDEOS;
        String dateTypeFlag = args[DATE_TYPE_INDEX] != null ? args[DATE_TYPE_INDEX] : CREATION_TIME_FLAG;
        System.out.printf(MESSAGE_PROCESSING_TASK, flowType, branchTask, commandTask, extension.substring(1));
        System.out.println();

        printMessage(MESSAGE_DESIRED_EXECUTION);
        Scanner scanner = new Scanner(System.in);
        String key = scanner.nextLine();
        if (key.equals("y")) { // Should the overall task continue?
            try {
                // Verify that the target directory exist...
                printMessage(MESSAGE_LOADING_DIRECTORY);
                String directory = args[DIRECTORY_INDEX];
                File path = new File(directory);
                if (!path.exists() && !path.isDirectory()) {
                    printMessage(MESSAGE_DIRECTORY_UNAVAILABLE);
                    System.exit(FAILURE);
                } else {
                    System.out.println(path);
                }

                // Load the files into memory under the target directory...
                printMessage(MESSAGE_LOADING_FILES);
                File[] files = path.listFiles((dir, name) -> name.toLowerCase().endsWith(extension));
                if (Objects.nonNull(files) && files.length > 0) {
                    for (File file : files) {
                        System.out.println(file.getName());
                    }
                } else {
                    printMessage(MESSAGE_RESOURCES_UNAVAILABLE);
                    System.exit(FAILURE);
                }

                String target = directory + NAMES_SOURCE;
                if (flow.equals(FILE_PROCESSING)) {
                    if (false) {
                        // TODO: Implement COMPARE
                    } else if (false) {
                        // TODO: Implement CROP
                    } else if (false) {
                        // TODO: Implement CONVERT
                    } else if (false) {
                        // TODO: Implement DETECT
                    } else {
                        printMessage(MESSAGE_EXECUTION_ABORT);
                    }
                } else if (flow.equals(DATA_PROCESSING)) {
                    if (branch.equals(SOURCE_PROCESSING)) { // Prepare a source file based on directory files...
                        // Verify that the source file exist...
                        File source = new File(target);
                        if (!source.isFile() && !source.exists()) {
                            printMessage(MESSAGE_SOURCE_UNAVAILABLE);
                            System.exit(FAILURE);
                        }

                        printMessage(MESSAGE_SOURCE_CONTAINS);
                        PrintWriter printWriter = new PrintWriter(target);
                        for (File file : files) {
                            System.out.println(file.getName());
                            printWriter.println(file.getName());
                        }
                        printWriter.close();
                    } else { // Otherwise, prepare and process conversion...
                        Map<String, String> history = new LinkedHashMap<>();
                        if (branch.equals(JAVA_PROCESSING)) { // Prepare conversion history based on Java +7...
                            NioProcessingUtil.javaProcessing(files, history, extension, dateTypeFlag);
                        } else { // Prepare conversion history based on Drew Noakes's extractor...
                            if (branch.equals(ORIGIN_PROCESSING)) { // Prepare conversion history based on origin data...
                                if (command.equals(VIDEO_PROCESSING)) {
                                    VideoProcessingUtil.prepareHistoryByOrigin(files, history, extension);
                                } else if (command.equals(IMAGE_PROCESSING)) {
                                    ImageProcessingUtil.prepareHistoryByOrigin(files, history, extension);
                                }
                            } else if (branch.equals(LIST_PROCESSING)) { // Prepare conversion history based on file input...
                                if (command.equals(VIDEO_PROCESSING)) {
                                    VideoProcessingUtil.prepareHistoryByInput(files, history, target, extension);
                                } else if (command.equals(IMAGE_PROCESSING)) {
                                    ImageProcessingUtil.prepareHistoryByInput(files, history, target, extension);
                                }
                            }
                        }

                        // Display available conversion history...
                        printMessage(MESSAGE_CONVERSION_HISTORY);
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
                                FileProcessingUtil.renamingProcess(history, files, directory);

                                printMessage(MESSAGE_UNDO_CONTINUE);
                                key = scanner.nextLine();
                                if (key.equals("y")) { // Should the undo process be executed?
                                    FileProcessingUtil.renamingUndoProcess(history, path, directory);
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
