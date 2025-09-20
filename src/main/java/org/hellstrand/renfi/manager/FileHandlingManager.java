package org.hellstrand.renfi.manager;

import static org.hellstrand.renfi.constant.Constants.COMPARE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CONVERT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CROP_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DETECT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_BRANCH_FAILURE;
import static org.hellstrand.renfi.constant.Constants.OUTPUT_SOURCE;
import static org.hellstrand.renfi.constant.Constants.SOURCE_PROCESSING;

import org.hellstrand.renfi.exception.InvalidBranchException;
import org.hellstrand.renfi.util.FileProcessingUtil;
import org.hellstrand.renfi.util.LoggingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author (Mats Richard Hellstrand)
 * @version (20th of September, 2025)
 */
public class FileHandlingManager {
    private static final Logger logger = LoggerFactory.getLogger(FileHandlingManager.class);

    public static void processBranch(
        LoggingUtil loggingUtil, String branch, File[] files, String path, String boundary, String leftXAxis, String leftYAxis, String fromExtension, String toExtension) {
        switch (branch) {
            case COMPARE_PROCESSING -> FileProcessingUtil.compareResources(loggingUtil, files, path, boundary);
            case CROP_PROCESSING -> FileProcessingUtil.cropResources(loggingUtil, files, path, leftXAxis, leftYAxis, toExtension);
            case CONVERT_PROCESSING -> FileProcessingUtil.convertResources(loggingUtil, files, path, fromExtension, toExtension);
            case DETECT_PROCESSING -> FileProcessingUtil.detectBlackBorders(loggingUtil, files, path);
            case SOURCE_PROCESSING -> FileProcessingUtil.writeSourceFile(files, FileProcessingUtil.createSourceFile(path.concat(OUTPUT_SOURCE)));
            default -> {
                logger.error(MESSAGE_INVALID_BRANCH_FAILURE);
                throw new InvalidBranchException(MESSAGE_INVALID_BRANCH_FAILURE);
            }
        }
    }
}
