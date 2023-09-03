package org.hellstrand.renfi.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hellstrand.renfi.util.Constants.LABEL_PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.util.Constants.MESSAGE_CREATING_PROCESSED_DIRECTORY;
import static org.hellstrand.renfi.util.Constants.MESSAGE_FAILED_UNDO_LOADING;
import static org.hellstrand.renfi.util.Constants.MESSAGE_FAILURE_NEWNAME;
import static org.hellstrand.renfi.util.Constants.MESSAGE_FAILURE_SOURCES;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RENAMING_ALERT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RENAMING_FAILURE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_SORTING_FILES;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_ALERT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_RELOADING;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_RESTORING;
import static org.hellstrand.renfi.util.HelpGuideUtil.printMessage;

/**
 * @author (Mats Richard Hellstrand)
 * @version (17th of October, 2021)
 */
public abstract class FileProcessingUtil {
    public static void prepareHistoryByInput(File[] files, Map<String, String> history, String target, String extension) {
        try {
            printMessage(MESSAGE_SORTING_FILES);
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            for (File file : files) {
                System.out.println(file.getName());
            }

            File source = new File(target);
            Scanner scanner = new Scanner(source);
            List<String> names = new ArrayList<>();
            while (scanner.hasNextLine()) {
                names.add(scanner.nextLine() + extension);
            }
            scanner.close();

            for (int i = 0; i < files.length; i++) {
                String oldName = files[i].getName();
                String newName = names.get(i);
                history.put(oldName, newName);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void renamingProcess(Map<String, String> history, File[] files, String directory) {
        String fullDirectory = createProcessedDirectory(directory);

        for (File file : files) {
            String previousName = file.getName();
            String newName = history.get(previousName);

            if (Objects.nonNull(newName)) {
                if (file.renameTo(new File(fullDirectory + newName))) {
                    System.out.printf(MESSAGE_RENAMING_ALERT, previousName, newName);
                } else {
                    printMessage(MESSAGE_RENAMING_FAILURE);
                    System.out.printf(MESSAGE_FAILURE_SOURCES, previousName, newName);
                }
            } else {
                System.out.printf(MESSAGE_FAILURE_SOURCES, previousName, MESSAGE_FAILURE_NEWNAME);
            }
        }
    }

    private static String createProcessedDirectory(String directory) {
        String fullDirectory = directory + LABEL_PROCESSED_DIRECTORY;

        File processed = new File(fullDirectory);
        if (!processed.exists()) {
            printMessage(MESSAGE_CREATING_PROCESSED_DIRECTORY);
            processed.mkdir();
        }

        return fullDirectory;
    }

    public static void renamingUndoProcess(Map<String, String> history, File path, String directory) {
        printMessage(MESSAGE_UNDO_RELOADING);
        File[] undo = path.listFiles((dir, name) -> history.values().stream().anyMatch(n -> n.equals(name)));
        if (Objects.requireNonNull(undo).length > 0) {
            for (File file : undo) {
                System.out.println(file.getName());
            }

            printMessage(MESSAGE_UNDO_RESTORING);
            for (File file : undo) {
                String undoName = file.getName();
                String previousName = history.entrySet().stream()
                    .filter(entry -> undoName.equals(entry.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

                if (Objects.nonNull(previousName) && file.renameTo(new File(directory + previousName))) {
                    System.out.printf(MESSAGE_UNDO_ALERT, undoName, previousName);
                } else {
                    printMessage(MESSAGE_RENAMING_FAILURE);
                    System.out.printf(MESSAGE_FAILURE_SOURCES, undoName, previousName);
                }
            }
        } else {
            printMessage(MESSAGE_FAILED_UNDO_LOADING);
        }
    }

    public static void compareResources(File[] files, String directory, String logging) {
        Set<String> processHistory = Collections.synchronizedSet(new HashSet<>());
        Set<String> duplicates = Collections.synchronizedSet(new HashSet<>());
        Set<String> matching = Collections.synchronizedSet(new HashSet<>());

        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(logging), true);

            printMessage("Comparison starts...");
            long comparisonProcessStart = System.currentTimeMillis();

            Arrays.stream(files).parallel().forEach(originalFile -> {
                long threadProcessStart = System.currentTimeMillis();
                String originalName = originalFile.getName();
                int index = 0;

                for (File comparedFile : files) {
                    String comparedName = comparedFile.getName();
                    index++;

                    if (!duplicates.contains(originalName) || !matching.contains(originalName)) {
                        BufferedImage originalImage = null, comparedImage = null;
                        try {
                            originalImage = ImageIO.read(originalFile);
                            comparedImage = ImageIO.read(comparedFile);
                        } catch (IOException e) {
                            System.err.println("Error: Can't read images");
                        }

                        int width1 = originalImage.getWidth();
                        int width2 = comparedImage.getWidth();
                        int height1 = originalImage.getHeight();
                        int height2 = comparedImage.getHeight();

                        if (width1 == width2 && height1 == height2) {
                            long difference = 0;

                            for (int y = 0; y < height1; y++) {
                                for (int x = 0; x < width1; x++) {
                                    int rgbA = originalImage.getRGB(x, y);
                                    int redA = (rgbA >> 16) & 0xff;
                                    int greenA = (rgbA >> 8) & 0xff;
                                    int blueA = (rgbA) & 0xff;
                                    int rgbB = comparedImage.getRGB(x, y);
                                    int redB = (rgbB >> 16) & 0xff;
                                    int greenB = (rgbB >> 8) & 0xff;
                                    int blueB = (rgbB) & 0xff;

                                    difference += Math.abs(redA - redB);
                                    difference += Math.abs(greenA - greenB);
                                    difference += Math.abs(blueA - blueB);
                                }
                            }

                            double total_pixels = width1 * height1 * 3;
                            double avg_different_pixels = difference / total_pixels;
                            double percentage = (avg_different_pixels / 255) * 100;

                            if (!duplicates.contains(originalName) || !matching.contains(originalName)) {
                                if (!originalName.equals(comparedName)) {
                                    if (Double.compare(percentage, 0.0) == 0) {
                                        printMessage("Comparison results (DUPLICATES)...");
                                        System.out.println("OriginalFile: " + originalName);
                                        System.out.println("ComparedFile: " + comparedName);
                                        System.out.println("Difference Percentage: " + percentage);
                                        printWriter.println("Comparison results (DUPLICATES)...");
                                        printWriter.println("OriginalFile: " + originalName);
                                        printWriter.println("ComparedFile: " + comparedName);
                                        printWriter.println("Difference Percentage: " + percentage);
                                        duplicates.add(originalName);
                                        duplicates.add(comparedName);
                                    } else if (Double.compare(percentage, 10.0) < 0) {
                                        printMessage("Comparison results (MATCHING)...");
                                        System.out.println("OriginalFile: " + originalName);
                                        System.out.println("ComparedFile: " + comparedName);
                                        System.out.println("Difference Percentage: " + percentage);
                                        printWriter.println("Comparison results (MATCHING)...");
                                        printWriter.println("OriginalFile: " + originalName);
                                        printWriter.println("ComparedFile: " + comparedName);
                                        printWriter.println("Difference Percentage: " + percentage);
                                        matching.add(originalName);
                                        matching.add(comparedName);
                                    }
                                }
                            }
                        }
                    }
                }
                processHistory.add(originalName);
                long threadProcessEnd = System.currentTimeMillis();

                float processingStatus = ((Float.intBitsToFloat(processHistory.size()) / Float.intBitsToFloat(files.length)) * 100.0f);
                printMessage("Processing Thread results...");
                System.out.println("=== === === === ===");
                System.out.println("Processing status (%): " + String.format("%.02f", processingStatus));
                System.out.println("=== === === === ===");
                System.out.println("Thread name: " + Thread.currentThread().getName());
                System.out.println("Thread runtime: " + TimeUnit.MILLISECONDS.toSeconds(threadProcessEnd - threadProcessStart));
                System.out.println("Threads available: " + Thread.activeCount());
                System.out.println("=== === === === ===");
                System.out.println("Parent: " + originalName);
                System.out.println("Child: " + index + "/" + files.length);
                System.out.println("History: " + processHistory.size());
                System.out.println("Duplicates: " + duplicates.size());
                System.out.println("Matching: " + matching.size());
            });

            long comparisonProcessEnd = System.currentTimeMillis();
            long comparisonProcessHours = TimeUnit.MILLISECONDS.toHours(comparisonProcessEnd - comparisonProcessStart);
            printMessage("Processing Batch results...");
            System.out.println("Elapsed time: " + comparisonProcessHours);
            System.out.println("Duplicates: " + duplicates.size());
            System.out.println("Matching: " + matching.size());
            printWriter.println("Processing Batch results...");
            printWriter.println("Elapsed time: " + comparisonProcessHours);
            printWriter.println("Duplicates: " + duplicates.size());
            printWriter.println("Matching: " + matching.size());

            if (!duplicates.isEmpty()) {
                String path = directory + "duplicates/";
                new File(path).mkdirs();
                for (String duplicate : duplicates) {
                    for (File file : files) {
                        if (file.getName().equals(duplicate) && file.renameTo(new File(path + duplicate))) {
                            System.out.println("Moved file (duplicates): " + duplicate);
                            printWriter.println("Moved file (duplicates): " + duplicate);
                        }
                    }
                }
            }
            if (!matching.isEmpty()) {
                String path = directory + "matching/";
                new File(path).mkdirs();
                for (String match : matching) {
                    for (File file : files) {
                        if (file.getName().equals(match) && file.renameTo(new File(path + match))) {
                            System.out.println("Moved file (matching): " + match);
                            printWriter.println("Moved file (matching): " + match);
                        }
                    }
                }
            }

            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void cropResources(File[] files, String directory, String logging, int[] coordinates, String toExtension) {
        Set<String> processHistory = Collections.synchronizedSet(new HashSet<>());

        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(logging), true);
            String path = directory + "done/";
            new File(path).mkdirs();

            printMessage("Cropping starts...");
            long croppingProcessStart = System.currentTimeMillis();

            int x = coordinates[0], y = coordinates[1];
            Arrays.stream(files).parallel().forEach(originalFile -> {
                long threadProcessStart = System.currentTimeMillis();
                String originalName = originalFile.getName();

                try {
                    BufferedImage bufferedImage = ImageIO.read(originalFile);
                    BufferedImage croppedImage = bufferedImage.getSubimage(x, y, (bufferedImage.getWidth() - (x + x)), (bufferedImage.getHeight() - (y + y)));
                    File toCroppedFile = new File(path + originalName);
                    ImageIO.write(croppedImage, toExtension.substring(1), toCroppedFile);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                processHistory.add(originalName);
                long threadProcessEnd = System.currentTimeMillis();

                float processingStatus = ((Float.intBitsToFloat(processHistory.size()) / Float.intBitsToFloat(files.length)) * 100.0f);
                printMessage("Processing Thread results...");
                System.out.println("=== === === === ===");
                System.out.println("Processing status (%): " + String.format("%.02f", processingStatus));
                System.out.println("=== === === === ===");
                System.out.println("Thread name: " + Thread.currentThread().getName());
                System.out.println("Thread runtime: " + TimeUnit.MILLISECONDS.toSeconds(threadProcessEnd - threadProcessStart));
                System.out.println("Threads available: " + Thread.activeCount());
                System.out.println("=== === === === ===");
                System.out.println("Current: " + originalName);
                System.out.println("Processed: " + processHistory.size());
            });

            long croppingProcessEnd = System.currentTimeMillis();
            long croppingProcessHours = TimeUnit.MILLISECONDS.toHours(croppingProcessEnd - croppingProcessStart);
            printMessage("Processing Batch results...");
            System.out.println("Elapsed time: " + croppingProcessHours);
            printWriter.println("Processing Batch results...");
            printWriter.println("Elapsed time: " + croppingProcessHours);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void convertResources(File[] files, String directory, String target, String fromExtension, String toExtension) {
        Set<String> processHistory = Collections.synchronizedSet(new HashSet<>());

        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(target), true);
            String path = directory + "done/";
            new File(path).mkdirs();

            printMessage("Converting starts...");
            long convertingProcessStart = System.currentTimeMillis();

            Arrays.stream(files).parallel().forEach(originalFile -> {
                long threadProcessStart = System.currentTimeMillis();
                String originalName = originalFile.getName();

                try {
                    BufferedImage bufferedImage = ImageIO.read(originalFile);
                    File toConvertedFile = new File(path + originalName.replace(fromExtension, toExtension));
                    ImageIO.write(bufferedImage, toExtension.substring(1), toConvertedFile);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                processHistory.add(originalName);
                long threadProcessEnd = System.currentTimeMillis();

                float processingStatus = ((Float.intBitsToFloat(processHistory.size()) / Float.intBitsToFloat(files.length)) * 100.0f);
                printMessage("Processing Thread results...");
                System.out.println("=== === === === ===");
                System.out.println("Processing status (%): " + String.format("%.02f", processingStatus));
                System.out.println("=== === === === ===");
                System.out.println("Thread name: " + Thread.currentThread().getName());
                System.out.println("Thread runtime: " + TimeUnit.MILLISECONDS.toSeconds(threadProcessEnd - threadProcessStart));
                System.out.println("Threads available: " + Thread.activeCount());
                System.out.println("=== === === === ===");
                System.out.println("Current: " + originalName);
                System.out.println("Processed: " + processHistory.size());
            });

            long convertingProcessEnd = System.currentTimeMillis();
            long convertingProcessHours = TimeUnit.MILLISECONDS.toHours(convertingProcessEnd - convertingProcessStart);
            printMessage("Processing Batch results...");
            System.out.println("Elapsed time: " + convertingProcessHours);
            printWriter.println("Processing Batch results...");
            printWriter.println("Elapsed time: " + convertingProcessHours);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void detectBlackBorders(File[] files, String directory, String logging) {
        Set<String> processHistory = Collections.synchronizedSet(new HashSet<>());
        Map<String, List<String>> borderMapping = Collections.synchronizedMap(new HashMap<>());

        try {
            PrintWriter printWriter = new PrintWriter(logging);

            printMessage("Detection starts...");
            long detectionProcessStart = System.currentTimeMillis();

            Arrays.stream(files).parallel().forEach(originalFile -> {
                long threadProcessStart = System.currentTimeMillis();
                String originalName = originalFile.getName();

                try {
                    BufferedImage originalImage = ImageIO.read(originalFile);
                    int height = originalImage.getHeight();
                    int width = originalImage.getWidth();

                    imageCheck:
                    for (int y = 0; y < height; y++) {
                        String key = String.valueOf(y);
                        for (int x = 0; x < width; x++) {
                            if ((originalImage.getRGB(x, y) & 0x00FFFFFF) != 0) {
                                if (x == width - 1) {
                                    List<String> values = borderMapping.get(key);
                                    if (values == null) {
                                        values = new ArrayList<>();
                                    }
                                    values.add(originalName);
                                    borderMapping.put(key, values);

                                    String oldKey = String.valueOf(y - 1);
                                    List<String> mappedValues = borderMapping.get(oldKey);
                                    if (Objects.nonNull(mappedValues) && mappedValues.contains(originalName)) {
                                        borderMapping.get(oldKey).remove(originalName);
                                    }
                                }
                            } else {
                                break imageCheck;
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error: Can't read image");
                }
                processHistory.add(originalName);
                long threadProcessEnd = System.currentTimeMillis();

                float processingStatus = ((Float.intBitsToFloat(processHistory.size()) / Float.intBitsToFloat(files.length)) * 100.0f);
                printMessage("Processing Thread results...");
                System.out.println("=== === === === ===");
                System.out.println("Processing status (%): " + String.format("%.02f", processingStatus));
                System.out.println("=== === === === ===");
                System.out.println("Thread name: " + Thread.currentThread().getName());
                System.out.println("Thread runtime: " + TimeUnit.MILLISECONDS.toSeconds(threadProcessEnd - threadProcessStart));
                System.out.println("Threads available: " + Thread.activeCount());
                System.out.println("=== === === === ===");
            });

            long detectionProcessEnd = System.currentTimeMillis();
            long detectionProcessHours = TimeUnit.MILLISECONDS.toHours(detectionProcessEnd - detectionProcessStart);
            printMessage("Processing Batch results...");
            System.out.println("Elapsed time: " + detectionProcessHours);
            printWriter.println("Processing Batch results...");
            printWriter.println("Elapsed time: " + detectionProcessHours);

            printWriter.println("Moving resources begins...");
            for (Map.Entry<String, List<String>> entry : borderMapping.entrySet()) {
                String path = directory + entry.getKey() + "/";
                new File(path).mkdirs();
                for (String originalName : entry.getValue()) {
                    for (File originalFile : files) {
                        if (originalName.equals(originalFile.getName())) {
                            if (originalFile.renameTo(new File(path + originalName))) {
                                System.out.println("Moved file: " + originalName);
                                printWriter.println("Moved file: " + originalName);
                            }
                        }
                    }
                }
            }

            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
