package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.Constants.LABEL_DUPLICATES_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.LABEL_MATCHING_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.LABEL_PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_COMPARE_RESULTS_DIFFERENCE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_COMPARE_RESULTS_DUPLICATES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_COMPARE_RESULTS_MATCHING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_COMPARE_RESULTS_VS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_COMPARE_STARTS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CONVERTING_STARTS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CREATING_PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_CROPPING_STARTS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DETECTION_STARTS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_DIRECTORY_CREATION_FAILURE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_FAILED_UNDO_LOADING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_IMAGEIO_FAILURE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_KEY_PAIR_FAILURE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_BATCH_RESULTS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_DUPLICATES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_ELAPSED_TIME;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_FILE_MOVED;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_HISTORY;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_MATCHING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_MOVING_RESOURCES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_ORIGINAL_NAME;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_STATUS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_THREADS_AVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_THREAD_NAME;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_THREAD_RESULTS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_THREAD_RUNTIME;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_UNABLE_MOVING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_RENAMING_ALERT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_SORTING_FILES;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_SOURCE_AVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_SOURCE_CONTAINS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_SOURCE_UNAVAILABLE;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_ALERT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_RELOADING;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_UNDO_RESTORING;
import static org.hellstrand.renfi.util.LoggingUtil.formatMessage;

import org.hellstrand.renfi.exception.DirectoryUnavailableException;
import org.hellstrand.renfi.exception.MismatchingConversionHistoryException;
import org.hellstrand.renfi.exception.SourceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

/**
 * @author (Mats Richard Hellstrand)
 * @version (27th of September, 2025)
 */
public abstract class FileProcessingUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessingUtil.class);

    public static boolean validateTarget(String target) {
        return new File(target).exists();
    }

    public static boolean createTargetDirectory(String directory) {
        File targetDirectory = new File(directory);
        if (targetDirectory.mkdir()) {
            logger.info(MESSAGE_CREATING_PROCESSED_DIRECTORY);
            return true;
        } else {
            logger.error(MESSAGE_DIRECTORY_CREATION_FAILURE, directory);
            throw new DirectoryUnavailableException(formatMessage(MESSAGE_DIRECTORY_CREATION_FAILURE, directory));
        }
    }

    public static File createSourceFile(String outputSource) {
        File sourceFile = new File(outputSource);
        try {
            if (sourceFile.createNewFile()) {
                logger.info(MESSAGE_SOURCE_AVAILABLE);
            }
        } catch (IOException e) {
            logger.error(MESSAGE_SOURCE_UNAVAILABLE);
            throw new SourceUnavailableException(MESSAGE_SOURCE_UNAVAILABLE);
        }
        return sourceFile;
    }

    public static void writeSourceFile(File[] files, File sourceFile) {
        logger.info(MESSAGE_SOURCE_CONTAINS);
        try {
            PrintWriter printWriter = new PrintWriter(sourceFile);
            for (File file : files) {
                logger.info(file.getName());
                printWriter.println(file.getName());
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            logger.error(MESSAGE_SOURCE_UNAVAILABLE);
            throw new SourceUnavailableException(MESSAGE_SOURCE_UNAVAILABLE);
        }
    }

    public static void moveResources(Set<String> resources, String path, File[] files, LoggingUtil loggingUtil, String parentThreadID) {
        if (!resources.isEmpty()) {
            if (createTargetDirectory(path)) {
                logger.info(MESSAGE_PROCESSING_MOVING_RESOURCES);
                for (String resource : resources) {
                    for (File file : files) {
                        if (resource.equals(file.getName()) && file.renameTo(new File(path.concat(resource)))) {
                            loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_FILE_MOVED, resource));
                        } else {
                            logger.warn(MESSAGE_PROCESSING_UNABLE_MOVING, resource);
                            loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_UNABLE_MOVING, resource));
                        }
                    }
                }
            }
        }
    }

    public static void prepareHistoryByInput(
        File[] files, Map<String, String> history, String inputSourceName, String fromExtension) {
        try {
            logger.info(MESSAGE_SORTING_FILES);
            for (File file : files) {
                logger.info(file.getName());
            }

            File inputSourceFile = new File(inputSourceName);
            Scanner scanner = new Scanner(inputSourceFile);
            List<String> names = new ArrayList<>();
            while (scanner.hasNextLine()) {
                names.add(scanner.nextLine().concat(fromExtension));
            }
            scanner.close();

            for (int i = 0; i < files.length; i++) {
                String oldName = files[i].getName();
                String newName = names.get(i);
                history.put(oldName, newName);
            }
        } catch (FileNotFoundException e) {
            logger.error(MESSAGE_SOURCE_UNAVAILABLE);
            throw new SourceUnavailableException(MESSAGE_SOURCE_UNAVAILABLE);
        }
    }

    public static void renamingProcess(File[] files, Map<String, String> history, String path, String pathExtension) {
        String directory = path.concat(pathExtension);
        if (createTargetDirectory(directory)) {
            for (File file : files) {
                String previousName = file.getName();
                String newName = history.get(previousName);

                if (Objects.nonNull(newName) && file.renameTo(new File(directory.concat(newName)))) {
                    logger.info(MESSAGE_RENAMING_ALERT, previousName, newName);
                } else {
                    logger.warn(MESSAGE_KEY_PAIR_FAILURE, previousName);
                }
            }
        }
    }

    public static void renamingUndoProcess(Map<String, String> history, File directory, String path) {
        logger.info(MESSAGE_UNDO_RELOADING);
        File[] undoList = directory.listFiles((dir, name) -> history.values().stream().anyMatch(n -> n.equals(name)));
        if (Objects.requireNonNull(undoList).length == history.size()) {
            for (File file : undoList) {
                logger.info(file.getName());
            }

            logger.info(MESSAGE_UNDO_RESTORING);
            for (File file : undoList) {
                String undoName = file.getName();
                String previousName = history.entrySet().stream()
                    .filter(entry -> undoName.equals(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

                if (Objects.nonNull(previousName) && file.renameTo(new File(path.concat(previousName)))) {
                    logger.info(MESSAGE_UNDO_ALERT, undoName, previousName);
                } else {
                    logger.warn(MESSAGE_KEY_PAIR_FAILURE, undoName);
                }
            }
        } else {
            logger.info(MESSAGE_FAILED_UNDO_LOADING);
            throw new MismatchingConversionHistoryException(MESSAGE_FAILED_UNDO_LOADING);
        }
    }

    public static void compareResources(LoggingUtil loggingUtil, File[] files, String path, String boundary) {
        Set<String> processHistory = Collections.synchronizedSet(new HashSet<>());
        Set<String> duplicates = Collections.synchronizedSet(new HashSet<>());
        Set<String> matching = Collections.synchronizedSet(new HashSet<>());
        double boundaryLimit = Double.parseDouble(boundary) / 100;

        logger.info(MESSAGE_COMPARE_STARTS);
        String parentThreadID = "P".concat(String.valueOf(Thread.currentThread().getId()));
        long comparisonProcessStart = System.currentTimeMillis();

        Arrays.stream(files).parallel().forEach(originalFile -> {
            String childThreadID = "C".concat(String.valueOf(Thread.currentThread().getId()));
            long threadProcessStart = System.currentTimeMillis();
            String originalName = originalFile.getName();

            Arrays.stream(files).sequential()
                    .filter(f -> !f.getName().equals(originalName))
                    .forEach(comparedFile -> {
                String grandchildThreadID = "GC".concat(String.valueOf(Thread.currentThread().getId()));
                String comparedName = comparedFile.getName();

                try {
                    BufferedImage originalImage = ImageIO.read(originalFile);
                    int originalWidth = originalImage.getWidth();
                    int originalHeight = originalImage.getHeight();
                    BufferedImage comparedImage = ImageIO.read(comparedFile);
                    int comparedWidth = comparedImage.getWidth();
                    int comparedHeight = comparedImage.getHeight();
                    int layeredPixels = 0, rgbMultiplier = 3, maxColourIntensity = 255;
                    double percentageDifference = 0, colourDifference = 0;

                    if (originalWidth == comparedWidth && originalHeight == comparedHeight) {
                        for (int y = 0, rowMultiplier = 1; y < originalHeight && percentageDifference <= boundaryLimit; y++, rowMultiplier++) {
                            for (int x = 0; x < originalWidth; x++) {
                                int rgbOriginal = originalImage.getRGB(x, y);
                                int redOriginal = (rgbOriginal >> 16) & 0xff;
                                int greenOriginal = (rgbOriginal >> 8) & 0xff;
                                int blueOriginal = (rgbOriginal) & 0xff;
                                int rgbCompared = comparedImage.getRGB(x, y);
                                int redCompared = (rgbCompared >> 16) & 0xff;
                                int greenCompared = (rgbCompared >> 8) & 0xff;
                                int blueCompared = (rgbCompared) & 0xff;

                                colourDifference += Math.abs(redOriginal - redCompared);
                                colourDifference += Math.abs(greenOriginal - greenCompared);
                                colourDifference += Math.abs(blueOriginal - blueCompared);
                            }
                            layeredPixels = originalWidth * rgbMultiplier * rowMultiplier;
                            percentageDifference = colourDifference / layeredPixels / maxColourIntensity;
                        }

                        if (Double.compare(percentageDifference, 0.0) == 0) {
                            loggingUtil.log(grandchildThreadID, MESSAGE_COMPARE_RESULTS_DUPLICATES);
                            loggingUtil.log(grandchildThreadID, formatMessage(MESSAGE_COMPARE_RESULTS_VS, originalName, comparedName));
                            loggingUtil.log(grandchildThreadID, formatMessage(MESSAGE_COMPARE_RESULTS_DIFFERENCE, percentageDifference));
                            duplicates.add(originalName);
                            duplicates.add(comparedName);
                        } else if (Double.compare(percentageDifference, boundaryLimit) < 0) {
                            loggingUtil.log(grandchildThreadID, MESSAGE_COMPARE_RESULTS_MATCHING);
                            loggingUtil.log(grandchildThreadID, formatMessage(MESSAGE_COMPARE_RESULTS_VS, originalName, comparedName));
                            loggingUtil.log(grandchildThreadID, formatMessage(MESSAGE_COMPARE_RESULTS_DIFFERENCE, percentageDifference));
                            matching.add(originalName);
                            matching.add(comparedName);
                        }
                    }
                } catch (IOException e) {
                    logger.error(MESSAGE_IMAGEIO_FAILURE);
                    throw new SourceUnavailableException(MESSAGE_IMAGEIO_FAILURE, e);
                }
            });
            processHistory.add(originalName);
            long threadProcessEnd = System.currentTimeMillis();

            float processingStatus = ((Float.intBitsToFloat(processHistory.size()) / Float.intBitsToFloat(files.length)) * 100.0f);
            loggingUtil.log(childThreadID, MESSAGE_PROCESSING_THREAD_RESULTS);
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_STATUS, String.format("%.02f", processingStatus)));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREAD_NAME, Thread.currentThread().getName()));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREAD_RUNTIME, TimeUnit.MILLISECONDS.toSeconds(threadProcessEnd - threadProcessStart)));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREADS_AVAILABLE, Thread.activeCount()));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_ORIGINAL_NAME, originalName));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_DUPLICATES, duplicates.size()));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_MATCHING, matching.size()));
        });

        long comparisonProcessEnd = System.currentTimeMillis();
        long comparisonProcessHours = TimeUnit.MILLISECONDS.toHours(comparisonProcessEnd - comparisonProcessStart);
        loggingUtil.log(parentThreadID, MESSAGE_PROCESSING_BATCH_RESULTS);
        loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_ELAPSED_TIME, comparisonProcessHours));
        loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_HISTORY, processHistory.size()));
        loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_DUPLICATES, duplicates.size()));
        loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_MATCHING, matching.size()));

        moveResources(duplicates, path.concat(LABEL_DUPLICATES_DIRECTORY), files, loggingUtil, parentThreadID);
        moveResources(matching, path.concat(LABEL_MATCHING_DIRECTORY), files, loggingUtil, parentThreadID);
    }

    public static void cropResources(LoggingUtil loggingUtil, File[] files, String path, String leftXAxis, String leftYAxis, String toExtension) {
        Set<String> processHistory = Collections.synchronizedSet(new HashSet<>());

        String directory = path.concat(LABEL_PROCESSED_DIRECTORY);
        if (createTargetDirectory(directory)) {
            logger.info(MESSAGE_CROPPING_STARTS);
            String parentThreadID = "P".concat(String.valueOf(Thread.currentThread().getId()));
            int coordinateX = Integer.parseInt(leftXAxis), coordinateY = Integer.parseInt(leftYAxis),
                equalBorderWidthX = coordinateX + coordinateX, equalBorderHeightY = coordinateY + coordinateY;
            long croppingProcessStart = System.currentTimeMillis();

            Arrays.stream(files).parallel().forEach(originalFile -> {
                String childThreadID = "C".concat(String.valueOf(Thread.currentThread().getId()));
                long threadProcessStart = System.currentTimeMillis();
                String originalName = originalFile.getName();

                try {
                    BufferedImage bufferedImage = ImageIO.read(originalFile);
                    int imageWidth = bufferedImage.getWidth();
                    int imageHeight = bufferedImage.getHeight();
                    BufferedImage croppedImage = bufferedImage.getSubimage(
                        coordinateX, coordinateY, imageWidth - equalBorderWidthX, imageHeight - equalBorderHeightY);
                    File toCroppedFile = new File(directory.concat(originalName));
                    ImageIO.write(croppedImage, toExtension.substring(1), toCroppedFile);
                } catch (IOException e) {
                    logger.error(MESSAGE_IMAGEIO_FAILURE);
                    throw new SourceUnavailableException(MESSAGE_IMAGEIO_FAILURE, e);
                }
                processHistory.add(originalName);
                long threadProcessEnd = System.currentTimeMillis();

                float processingStatus = ((Float.intBitsToFloat(processHistory.size()) / Float.intBitsToFloat(files.length)) * 100.0f);
                loggingUtil.log(childThreadID, MESSAGE_PROCESSING_THREAD_RESULTS);
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_STATUS, String.format("%.02f", processingStatus)));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREAD_NAME, Thread.currentThread().getName()));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREAD_RUNTIME, TimeUnit.MILLISECONDS.toSeconds(threadProcessEnd - threadProcessStart)));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREADS_AVAILABLE, Thread.activeCount()));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_ORIGINAL_NAME, originalName));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_HISTORY, processHistory.size()));
            });

            long croppingProcessEnd = System.currentTimeMillis();
            long croppingProcessHours = TimeUnit.MILLISECONDS.toHours(croppingProcessEnd - croppingProcessStart);
            loggingUtil.log(parentThreadID, MESSAGE_PROCESSING_BATCH_RESULTS);
            loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_ELAPSED_TIME, croppingProcessHours));
            loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_HISTORY, processHistory.size()));
        }
    }

    public static void convertResources(LoggingUtil loggingUtil, File[] files, String path, String fromExtension, String toExtension) {
        Set<String> processHistory = Collections.synchronizedSet(new HashSet<>());

        String directory = path.concat(LABEL_PROCESSED_DIRECTORY);
        if (createTargetDirectory(directory)) {
            logger.info(MESSAGE_CONVERTING_STARTS);
            String parentThreadID = "P".concat(String.valueOf(Thread.currentThread().getId()));
            long convertingProcessStart = System.currentTimeMillis();

            Arrays.stream(files).parallel().forEach(originalFile -> {
                String childThreadID = "C".concat(String.valueOf(Thread.currentThread().getId()));
                long threadProcessStart = System.currentTimeMillis();
                String originalName = originalFile.getName();

                try {
                    BufferedImage bufferedImage = ImageIO.read(originalFile);
                    File toConvertedFile = new File(directory.concat(originalName.replace(fromExtension, toExtension)));
                    ImageIO.write(bufferedImage, toExtension.substring(1), toConvertedFile);
                } catch (IOException e) {
                    logger.error(MESSAGE_IMAGEIO_FAILURE);
                    throw new SourceUnavailableException(MESSAGE_IMAGEIO_FAILURE, e);
                }
                processHistory.add(originalName);
                long threadProcessEnd = System.currentTimeMillis();

                float processingStatus = ((Float.intBitsToFloat(processHistory.size()) / Float.intBitsToFloat(files.length)) * 100.0f);
                loggingUtil.log(childThreadID, MESSAGE_PROCESSING_THREAD_RESULTS);
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_STATUS, String.format("%.02f", processingStatus)));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREAD_NAME, Thread.currentThread().getName()));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREAD_RUNTIME, TimeUnit.MILLISECONDS.toSeconds(threadProcessEnd - threadProcessStart)));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREADS_AVAILABLE, Thread.activeCount()));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_ORIGINAL_NAME, originalName));
                loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_HISTORY, processHistory.size()));
            });

            long convertingProcessEnd = System.currentTimeMillis();
            long convertingProcessHours = TimeUnit.MILLISECONDS.toHours(convertingProcessEnd - convertingProcessStart);
            loggingUtil.log(parentThreadID, MESSAGE_PROCESSING_BATCH_RESULTS);
            loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_ELAPSED_TIME, convertingProcessHours));
            loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_HISTORY, processHistory.size()));
        }
    }

    public static void detectBlackBorders(LoggingUtil loggingUtil, File[] files, String path) {
        Set<String> processHistory = Collections.synchronizedSet(new HashSet<>());
        Map<String, Set<String>> borderMapping = Collections.synchronizedMap(new HashMap<>());

        logger.info(MESSAGE_DETECTION_STARTS);
        String parentThreadID = "P".concat(String.valueOf(Thread.currentThread().getId()));
        long detectionProcessStart = System.currentTimeMillis();

        Arrays.stream(files).parallel().forEach(originalFile -> {
            String childThreadID = "C".concat(String.valueOf(Thread.currentThread().getId()));
            long threadProcessStart = System.currentTimeMillis();
            String originalName = originalFile.getName();

            try {
                BufferedImage originalImage = ImageIO.read(originalFile);
                int imageHeight = originalImage.getHeight();
                int imageWidthFinalPixel = originalImage.getWidth() - 1;

                for (int y = 0; y < imageHeight; y++) {
                    if ((originalImage.getRGB(imageWidthFinalPixel, y) & 0x00FFFFFF) != 0) {
                        String newlyDetectedKey = String.valueOf(y);
                        Set<String> values = borderMapping.get(newlyDetectedKey);
                        if (values == null) {
                            values = new HashSet<>();
                        }
                        values.add(originalName);
                        borderMapping.put(newlyDetectedKey, values);

                        String previouslyDetectedKey = String.valueOf(y - 1);
                        Set<String> mappedValues = borderMapping.get(previouslyDetectedKey);
                        if (mappedValues.contains(originalName)) {
                            borderMapping.get(previouslyDetectedKey).remove(originalName);
                        }
                    }
                }
            } catch (IOException e) {
                logger.error(MESSAGE_IMAGEIO_FAILURE);
                throw new SourceUnavailableException(MESSAGE_IMAGEIO_FAILURE, e);
            }
            processHistory.add(originalName);
            long threadProcessEnd = System.currentTimeMillis();

            float processingStatus = ((Float.intBitsToFloat(processHistory.size()) / Float.intBitsToFloat(files.length)) * 100.0f);
            loggingUtil.log(childThreadID, MESSAGE_PROCESSING_THREAD_RESULTS);
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_STATUS, String.format("%.02f", processingStatus)));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREAD_NAME, Thread.currentThread().getName()));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREAD_RUNTIME, TimeUnit.MILLISECONDS.toSeconds(threadProcessEnd - threadProcessStart)));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_THREADS_AVAILABLE, Thread.activeCount()));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_ORIGINAL_NAME, originalName));
            loggingUtil.log(childThreadID, formatMessage(MESSAGE_PROCESSING_HISTORY, processHistory.size()));
        });

        long detectionProcessEnd = System.currentTimeMillis();
        long detectionProcessHours = TimeUnit.MILLISECONDS.toHours(detectionProcessEnd - detectionProcessStart);
        loggingUtil.log(parentThreadID, MESSAGE_PROCESSING_BATCH_RESULTS);
        loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_ELAPSED_TIME, detectionProcessHours));
        loggingUtil.log(parentThreadID, formatMessage(MESSAGE_PROCESSING_HISTORY, processHistory.size()));

        for (Map.Entry<String, Set<String>> entry : borderMapping.entrySet()) {
            String target = entry.getKey();
            String directory = path.concat(target);
            moveResources(entry.getValue(), directory, files, loggingUtil, parentThreadID);
        }
    }
}
