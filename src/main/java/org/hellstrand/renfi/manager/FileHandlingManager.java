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
 * @version (6th of September, 2023)
 */
public class FileHandlingManager {
    public static void processBranch(
        String branch, File[] files, String path, String leftXAxis, String leftYAxis, String fromExtension, String toExtension) {
        String outputSource = path.concat(OUTPUT_SOURCE);

        switch (branch) {
            case COMPARE_PROCESSING:
                FileProcessingUtil.compareResources(files, path, outputSource);
                break;
            case CROP_PROCESSING:
                FileProcessingUtil.cropResources(files, path, outputSource, leftXAxis, leftYAxis, toExtension);
                break;
            case CONVERT_PROCESSING:
                FileProcessingUtil.convertResources(files, path, outputSource, fromExtension, toExtension);
                break;
            case DETECT_PROCESSING:
                FileProcessingUtil.detectBlackBorders(files, path, outputSource);
                break;
            case SOURCE_PROCESSING:
                File sourceFile = FileProcessingUtil.createSourceFile(outputSource);
                FileProcessingUtil.writeSourceFile(files, sourceFile);
                break;
            default:
                printMessage(MESSAGE_EXECUTION_ABORT);
                break;
        }
    }
}
