package org.hellstrand.renfi.manager;

import static org.hellstrand.renfi.constant.Constants.IMAGE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.JAVA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LIST_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.INPUT_SOURCE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_INVALID_BRANCH_FAILURE;
import static org.hellstrand.renfi.constant.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.VIDEO_PROCESSING;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

import java.io.File;
import java.util.Map;
import org.hellstrand.renfi.exception.InvalidBranchException;
import org.hellstrand.renfi.util.ImageProcessingUtil;
import org.hellstrand.renfi.util.NioProcessingUtil;
import org.hellstrand.renfi.util.VideoProcessingUtil;

/**
 * @author (Mats Richard Hellstrand)
 * @version (15th of September, 2025)
 */
public class DataHandlingManager {
    public static void processBranch(
        String branch, String resourceType, String path, File[] files, Map<String, String> history, String fromExtension, String dateTypeFlag) {
        String inputSourceName = path.concat(INPUT_SOURCE);

        switch (branch) {
            case JAVA_PROCESSING -> { // Prepare conversion history based on Java +7...
                NioProcessingUtil.prepareHistoryByNioProcessing(files, history, fromExtension, dateTypeFlag);
            }
            case ORIGIN_PROCESSING -> { // Prepare conversion history based on origin data with Drew Noakes's extractor...
                if (resourceType.equals(VIDEO_PROCESSING)) {
                    VideoProcessingUtil.prepareHistoryByOrigin(files, history, fromExtension);
                } else if (resourceType.equals(IMAGE_PROCESSING)) {
                    ImageProcessingUtil.prepareHistoryByOrigin(files, history, fromExtension);
                }
            }
            case LIST_PROCESSING -> { // Prepare conversion history based on file input...
                if (resourceType.equals(VIDEO_PROCESSING)) {
                    VideoProcessingUtil.prepareHistoryByInput(files, history, inputSourceName, fromExtension);
                } else if (resourceType.equals(IMAGE_PROCESSING)) {
                    ImageProcessingUtil.prepareHistoryByInput(files, history, inputSourceName, fromExtension);
                }
            }
            default -> {
                printMessage(MESSAGE_INVALID_BRANCH_FAILURE);
                throw new InvalidBranchException(MESSAGE_INVALID_BRANCH_FAILURE);
            }
        }
    }
}
