package org.hellstrand.renfi;

import static org.hellstrand.renfi.constant.Constants.ALLOWED_FLAGS;
import static org.hellstrand.renfi.constant.Constants.BOUNDARY_INDEX;
import static org.hellstrand.renfi.constant.Constants.BRANCH_INDEX;
import static org.hellstrand.renfi.constant.Constants.DATA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DATE_TYPE_INDEX;
import static org.hellstrand.renfi.constant.Constants.EXTENSION_FROM_INDEX;
import static org.hellstrand.renfi.constant.Constants.EXTENSION_TO_INDEX;
import static org.hellstrand.renfi.constant.Constants.FILE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.FLOW_INDEX;
import static org.hellstrand.renfi.constant.Constants.HELP_FLAGS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DESIRED_EXECUTION;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DIRECTORY_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DISPLAY_HELP_GUIDE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_EXECUTION_ABORT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_BOUNDARY_INDEX;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_BRANCH_INDEX;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_EXTENSION_RANGES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_FLOW_INDEX;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_RESOURCE_TYPE_INDEX;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOADING_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOADING_FILES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_LOGGING_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_ATTRIBUTES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_TASK;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RESOURCES_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.OUTPUT_SOURCE;
import static org.hellstrand.renfi.constant.Constants.PATH_INDEX;
import static org.hellstrand.renfi.constant.Constants.PROCESSING_SUPPORT;
import static org.hellstrand.renfi.constant.Constants.RESOURCE_TYPE_INDEX;
import static org.hellstrand.renfi.constant.Constants.SUCCESSFUL;
import static org.hellstrand.renfi.constant.Constants.UPPER_LEFT_X_INDEX;
import static org.hellstrand.renfi.constant.Constants.UPPER_LEFT_Y_INDEX;
import static org.hellstrand.renfi.util.FileProcessingUtil.validateTarget;
import static org.hellstrand.renfi.util.HelpGuideUtil.displayHelpGuide;
import static org.hellstrand.renfi.util.LoggingUtil.formatMessage;

import org.hellstrand.renfi.constant.ConstantExtractionUtil;
import org.hellstrand.renfi.exception.DirectoryUnavailableException;
import org.hellstrand.renfi.exception.DisplayHelpGuideException;
import org.hellstrand.renfi.exception.InvalidUseException;
import org.hellstrand.renfi.exception.ResourcesUnavailableException;
import org.hellstrand.renfi.exception.SourceUnavailableException;
import org.hellstrand.renfi.manager.DataHandlingManager;
import org.hellstrand.renfi.manager.FileHandlingManager;
import org.hellstrand.renfi.manager.HistoryHandlingManager;
import org.hellstrand.renfi.util.LoggingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author (Mats Richard Hellstrand)
 * @version (20th of September, 2025)
 */
public final class RenfiUtility {
    private static final Logger logger = LoggerFactory.getLogger(RenfiUtility.class);

    public static void main(String[] args) {
        if (args.length < 9 || HELP_FLAGS.contains(args[0])) {
            displayHelpGuide();
            throw new DisplayHelpGuideException(MESSAGE_DISPLAY_HELP_GUIDE);
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
        String boundary = args[BOUNDARY_INDEX];

        if (!ALLOWED_FLAGS.contains(flow)) {
            logger.error(MESSAGE_INVALID_FLOW_INDEX, flow);
            throw new InvalidUseException(formatMessage(MESSAGE_INVALID_FLOW_INDEX, flow));
        }

        if (!ALLOWED_FLAGS.contains(branch)) {
            logger.error(MESSAGE_INVALID_BRANCH_INDEX, branch);
            throw new InvalidUseException(formatMessage(MESSAGE_INVALID_BRANCH_INDEX, branch));
        }

        if (!ALLOWED_FLAGS.contains(resourceType) || !PROCESSING_SUPPORT.containsKey(resourceType)) {
            logger.error(MESSAGE_INVALID_RESOURCE_TYPE_INDEX, resourceType);
            throw new InvalidUseException(formatMessage(MESSAGE_INVALID_RESOURCE_TYPE_INDEX, resourceType));
        }

        if (!validateTarget(path)) {
            logger.error(MESSAGE_DIRECTORY_UNAVAILABLE, path);
            throw new DirectoryUnavailableException(formatMessage(MESSAGE_DIRECTORY_UNAVAILABLE, path));
        }

        List<String> selectedExtensions = PROCESSING_SUPPORT.get(resourceType);
        int extensionFromIndex = Integer.parseInt(fromIndex), extensionToIndex = Integer.parseInt(toIndex);
        if (extensionFromIndex < 0 || extensionToIndex >= selectedExtensions.size()) {
            logger.error(MESSAGE_INVALID_EXTENSION_RANGES, fromIndex, toIndex);
            throw new InvalidUseException(formatMessage(MESSAGE_INVALID_EXTENSION_RANGES, fromIndex, toIndex));
        }

        if (Integer.parseInt(boundary) < 1 || Integer.parseInt(boundary) > 100) {
            logger.error(MESSAGE_INVALID_BOUNDARY_INDEX, boundary);
            throw new InvalidUseException(formatMessage(MESSAGE_INVALID_BOUNDARY_INDEX, boundary));
        }

        String flowTask = ConstantExtractionUtil.extractFlowTask(flow);
        String branchTask = ConstantExtractionUtil.extractBranchTask(branch);
        String resourceTask = ConstantExtractionUtil.extractResourceTask(resourceType);
        String fromExtension = selectedExtensions.get(extensionFromIndex);
        String toExtension = selectedExtensions.get(extensionToIndex);

        logger.info(MESSAGE_PROCESSING_TASK, flowTask, branchTask, resourceTask, boundary, path);
        logger.info(MESSAGE_PROCESSING_ATTRIBUTES, fromExtension.substring(1), toExtension.substring(1), dateType, leftXAxis, leftYAxis);
        logger.info(MESSAGE_DESIRED_EXECUTION);
        Scanner scanner = new Scanner(System.in);
        String key = scanner.nextLine();
        scanner.close();
        if (key.equals("y")) { // Should the overall task continue?
            // Verify that the target directory exist...
            logger.info(MESSAGE_LOADING_DIRECTORY);
            File directory = new File(path);
            if (!directory.exists() && !directory.isDirectory()) {
                logger.error(MESSAGE_DIRECTORY_UNAVAILABLE, path);
                throw new DirectoryUnavailableException(formatMessage(MESSAGE_DIRECTORY_UNAVAILABLE, path));
            }

            // Load the files into memory under the target directory...
            logger.info(MESSAGE_LOADING_FILES);
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(fromExtension));
            if (Objects.nonNull(files) && files.length > 0) {
                for (File file : files) {
                    logger.info(file.getName());
                }
            } else {
                logger.warn(MESSAGE_RESOURCES_UNAVAILABLE);
                throw new ResourcesUnavailableException(MESSAGE_RESOURCES_UNAVAILABLE);
            }

            try {
                LoggingUtil loggingUtil = new LoggingUtil(path.concat(OUTPUT_SOURCE));
                if (flow.equals(FILE_PROCESSING)) { // If we want to modify a file or analyze it...
                    FileHandlingManager.processBranch(loggingUtil, branch, files, path, boundary, leftXAxis, leftYAxis, fromExtension, toExtension);
                } else if (flow.equals(DATA_PROCESSING)) { // If we want to prepare conversion history and execute renaming...
                    Map<String, String> history = new LinkedHashMap<>();
                    DataHandlingManager.processBranch(branch, resourceType, path, files, history, fromExtension, dateType);
                    HistoryHandlingManager.processHistory(files, history, path, directory);
                }
                loggingUtil.write();
            } catch (FileNotFoundException e) {
                logger.error(MESSAGE_LOGGING_UNAVAILABLE);
                throw new SourceUnavailableException(MESSAGE_LOGGING_UNAVAILABLE);
            }
        } else {
            logger.info(MESSAGE_EXECUTION_ABORT);
        }

        System.exit(SUCCESSFUL);
    }
}
