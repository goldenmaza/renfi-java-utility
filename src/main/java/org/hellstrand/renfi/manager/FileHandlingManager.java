package org.hellstrand.renfi.manager;

import static org.hellstrand.renfi.constant.Constants.COMPARE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CONVERT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CROP_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DETECT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_EXECUTION_ABORT;
import static org.hellstrand.renfi.constant.Constants.OUTPUT_SOURCE;
import static org.hellstrand.renfi.constant.Constants.SOURCE_PROCESSING;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

import java.io.File;
import org.hellstrand.renfi.util.FileProcessingUtil;

/**
 * @author (Mats Richard Hellstrand)
 * @version (14th of September, 2025)
 */
public class FileHandlingManager {
    public static void processBranch(
        String branch, File[] files, String path, String boundary, String leftXAxis, String leftYAxis, String fromExtension, String toExtension) {
        String outputSource = path.concat(OUTPUT_SOURCE);

        switch (branch) {
            case COMPARE_PROCESSING -> FileProcessingUtil.compareResources(files, path, boundary, outputSource);
            case CROP_PROCESSING -> FileProcessingUtil.cropResources(files, path, outputSource, leftXAxis, leftYAxis, toExtension);
            case CONVERT_PROCESSING -> FileProcessingUtil.convertResources(files, path, outputSource, fromExtension, toExtension);
            case DETECT_PROCESSING -> FileProcessingUtil.detectBlackBorders(files, path, outputSource);
            case SOURCE_PROCESSING -> FileProcessingUtil.writeSourceFile(files, FileProcessingUtil.createSourceFile(outputSource));
            default -> printMessage(MESSAGE_EXECUTION_ABORT);
        }
    }
}
