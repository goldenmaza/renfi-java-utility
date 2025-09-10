package org.hellstrand.renfi.constant;

import static org.hellstrand.renfi.constant.Constants.COMPARE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CONVERT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CROP_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DATA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DETECT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.FILE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.IMAGE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.JAVA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LABEL_COMPARE;
import static org.hellstrand.renfi.constant.Constants.LABEL_CONVERT;
import static org.hellstrand.renfi.constant.Constants.LABEL_CREATED;
import static org.hellstrand.renfi.constant.Constants.LABEL_CROP;
import static org.hellstrand.renfi.constant.Constants.LABEL_DATA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LABEL_DETECT;
import static org.hellstrand.renfi.constant.Constants.LABEL_FILE;
import static org.hellstrand.renfi.constant.Constants.LABEL_JAVA;
import static org.hellstrand.renfi.constant.Constants.LABEL_SOURCE;
import static org.hellstrand.renfi.constant.Constants.LABEL_FILE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LABEL_IMAGES;
import static org.hellstrand.renfi.constant.Constants.LABEL_UNKNOWN_EXECUTION;
import static org.hellstrand.renfi.constant.Constants.LABEL_VIDEOS;
import static org.hellstrand.renfi.constant.Constants.LIST_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.SOURCE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.VIDEO_PROCESSING;

import java.util.Objects;

/**
 * @author (Mats Richard Hellstrand)
 * @version (10th of September, 2025)
 */
public final class ConstantExtractionUtil {
    private ConstantExtractionUtil() {}

    public static String extractFlowTask(String flowTask) {
        if (Objects.isNull(flowTask)) {
            return null;
        }

        return switch (flowTask) {
            case FILE_PROCESSING -> LABEL_FILE_PROCESSING;
            case DATA_PROCESSING -> LABEL_DATA_PROCESSING;
            default -> LABEL_UNKNOWN_EXECUTION;
        };
    }

    public static String extractBranchTask(String branchTask) {
        if (Objects.isNull(branchTask)) {
            return null;
        }

        return switch (branchTask) {
            case COMPARE_PROCESSING -> LABEL_COMPARE;
            case CROP_PROCESSING -> LABEL_CROP;
            case CONVERT_PROCESSING -> LABEL_CONVERT;
            case DETECT_PROCESSING -> LABEL_DETECT;
            case SOURCE_PROCESSING -> LABEL_SOURCE;
            case JAVA_PROCESSING -> LABEL_JAVA;
            case ORIGIN_PROCESSING -> LABEL_CREATED;
            case LIST_PROCESSING -> LABEL_FILE;
            default -> LABEL_UNKNOWN_EXECUTION;
        };
    }

    public static String extractResourceTask(String resourceTask) {
        if (Objects.isNull(resourceTask)) {
            return null;
        }

        return switch (resourceTask) {
            case IMAGE_PROCESSING -> LABEL_IMAGES;
            case VIDEO_PROCESSING -> LABEL_VIDEOS;
            default -> LABEL_UNKNOWN_EXECUTION;
        };
    }
}
