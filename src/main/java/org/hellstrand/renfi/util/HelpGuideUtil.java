package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.util.Constants.BRANCH_FLAGS;
import static org.hellstrand.renfi.util.Constants.COMPARE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.CONVERT_PROCESSING;
import static org.hellstrand.renfi.util.Constants.CROP_PROCESSING;
import static org.hellstrand.renfi.util.Constants.DATA_PROCESSING;
import static org.hellstrand.renfi.util.Constants.DETECT_PROCESSING;
import static org.hellstrand.renfi.util.Constants.FILE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.RESOURCE_FLAGS;
import static org.hellstrand.renfi.util.Constants.FLOW_FLAGS;
import static org.hellstrand.renfi.util.Constants.HELP_FLAGS;
import static org.hellstrand.renfi.util.Constants.IMAGE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.LIST_PROCESSING;
import static org.hellstrand.renfi.util.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.util.Constants.PROCESSING_SUPPORT;
import static org.hellstrand.renfi.util.Constants.SOURCE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.VIDEO_PROCESSING;

import java.util.List;

/**
 * @author (Mats Richard Hellstrand)
 * @version (4th of September, 2023)
 */
public class HelpGuideUtil {
    public static void displayHelpGuide() {
        StringBuilder sb = new StringBuilder();
        List<String> imageExtensions = PROCESSING_SUPPORT.get(IMAGE_PROCESSING);
        List<String> videoExtensions = PROCESSING_SUPPORT.get(VIDEO_PROCESSING);
        String helpFlags = HELP_FLAGS.toString()
            .replace("[", "(")
            .replace("]", ")");

        sb.append("  === Help Guide ===")
            .append("\n")
            .append("\n  Operation legend:")
            .append("\n")
            .append("\n  java -jar Renfi.jar <FLOW> <BRANCH> <PATH> <RESOURCE_TYPE> <FROM_EXTENSION> <TO_EXTENSION> <X_AXIS> <Y_AXIS> <DATE_TYPE>")
            .append("\n")
            .append("\n  (FLOW)\t\t\t\tThe desired flow of the application: ").append(FLOW_FLAGS)
            .append("\n  (BRANCH)\t\t\t\tThe desired branch of the application: ").append(BRANCH_FLAGS)
            .append("\n  (PATH)\t\t\t\tThe directory path to where the files are located, e.g. \"c:/directory/\" OR \"c/directory/\"")
            .append("\n  (RESOURCE_TYPE)\t\tTo focus on either resource type: ").append(RESOURCE_FLAGS)
            .append("\n  (FROM_EXTENSION)\t\tThe desired file extension based on index (INTEGER). (view 'Extension legend' below)")
            .append("\n  (TO_EXTENSION)\t\tThe desired file extension based on index (INTEGER). (view 'Extension legend' below)")
            .append("\n  (X_AXIS)\t\t\t\tThe starting pixel on the X axis (INTEGER).")
            .append("\n  (Y_AXIS)\t\t\t\tThe starting pixel on the Y axis (INTEGER).")
            .append("\n  (DATE_TYPE)\t\t\tThe desired date type from file.")
            .append("\n")
            .append("\n  Extension legend:")
            .append("\n  (IMAGE)\t\t\t\tThe supported extensions and their indexes: ").append(printValues(imageExtensions))
            .append("\n  (VIDEO)\t\t\t\tThe supported extensions and their indexes: ").append(printValues(videoExtensions))
            .append("\n")
            .append("\n  Flag legend:")
            .append("\n  (" + IMAGE_PROCESSING + ")\t\t\t\t\tTo process IMAGE files based on extension stated.")
            .append("\n  (" + VIDEO_PROCESSING + ")\t\t\t\t\tTo process VIDEO files based on extension stated.")
            .append("\n")
            .append("\n  (" + FILE_PROCESSING + ")\t\t\t\t\tThe File processing flow lets us manipulate the file itself or to analyze it. You can choose one of the following branches:")
            .append("\n  (" + COMPARE_PROCESSING + ")\t\t\t\t\tTo compare files, pixel by pixel, to determine if they are a duplicate or a close match.")
            .append("\n  (" + CROP_PROCESSING + ")\t\t\t\tTo crop images, based on X & Y coordinates, this will remove the borders on both sides of the image.")
            .append("\n  (" + CONVERT_PROCESSING + ")\t\t\t\tTo convert images from one extension to another.")
            .append("\n  (" + DETECT_PROCESSING + ")\t\t\t\tTo detect black borders and to sort them into folders based on detected height. Note: Currently, only top-down is supported!")
            .append("\n")
            .append("\n  (" + DATA_PROCESSING + ")\t\t\t\t\tThe Data processing flow lets us create a source file or determine date and time. You can choose one of the following branches:")
            .append("\n  (" + SOURCE_PROCESSING + ")\t\t\t\t\tTo prepare a SOURCE FILE based on directory files.")
            .append("\n  (" + ORIGIN_PROCESSING + ")\t\t\t\t\tTo prepare history conversion based on ORIGIN DATA.")
            .append("\n  (" + LIST_PROCESSING + ")\t\t\t\t\tTo prepare history conversion based on SOURCE FILE.")
            .append("\n")
            .append("\n  ")
            .append(helpFlags).append("\t\tTo display this help guide.");

        System.out.println(sb);
    }

    private static String printValues(List<String> strings) {
        StringBuilder sb = new StringBuilder();

        for (String s : strings) {
            sb.append(s.substring(1))
                .append(" (")
                .append(strings.indexOf(s))
                .append("), ");
        }

        return sb.toString();
    }

    public static void printMessage(String message) {
        StringBuilder sb = new StringBuilder();

        sb.append("===== ##### ### ##### ####### ##### ### ##### =====\n\t").append(message);

        System.out.println(sb);
    }

    private HelpGuideUtil() {}
}
