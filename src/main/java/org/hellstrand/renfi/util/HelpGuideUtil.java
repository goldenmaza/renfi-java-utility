package org.hellstrand.renfi.util;

import java.util.List;

import static org.hellstrand.renfi.util.Constants.BRANCH_FLAGS;
import static org.hellstrand.renfi.util.Constants.COMMAND_FLAGS;
import static org.hellstrand.renfi.util.Constants.FILE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.HELP_FLAGS;
import static org.hellstrand.renfi.util.Constants.IMAGE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.LIST_PROCESSING;
import static org.hellstrand.renfi.util.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.util.Constants.PROCESSING_SUPPORT;
import static org.hellstrand.renfi.util.Constants.VIDEO_PROCESSING;

/**
 * @author (Mats Richard Hellstrand)
 * @version (17th of October, 2021)
 */
public class HelpGuideUtil {
    public static void displayHelpGuide() {
        StringBuilder sb = new StringBuilder();
        List<String> imageExtensions = PROCESSING_SUPPORT.get(IMAGE_PROCESSING);
        List <String> videoExtensions = PROCESSING_SUPPORT.get(VIDEO_PROCESSING);
        String helpFlags = HELP_FLAGS.toString()
            .replace("[", "(")
            .replace("]", ")");

        sb.append("  === Help Guide ===")
            .append("\n")
            .append("\n  Operation legend:")
            .append("\n")
            .append("\n  java -jar Renfi.jar <BRANCH> <COMMAND> <INDEX> <PATH>")
            .append("\n")
            .append("\n  (BRANCH)\t\tdesired flow of the application: ").append(BRANCH_FLAGS.toString())
            .append("\n  (COMMAND)\t\tfocus on either file type: ").append(COMMAND_FLAGS.toString())
            .append("\n  (INDEX)\t\tselect file extension based on index: <INTEGER> (view 'Extension legend' below)")
            .append("\n  (PATH)\t\tdirectory path to where the files are located, e.g. c:/directory/ OR c/directory/")
            .append("\n")
            .append("\n  Extension legend:")
            .append("\n  (IMAGE)\t\tsupported extensions and their indexes: ").append(printValues(imageExtensions))
            .append("\n  (VIDEO)\t\tsupported extensions and their indexes: ").append(printValues(videoExtensions))
            .append("\n")
            .append("\n  Flag legend:")
            .append("\n  (" + FILE_PROCESSING + ")\t\t\tprepare a SOURCE FILE based on directory files")
            .append("\n  (" + ORIGIN_PROCESSING + ")\t\t\tprepare history conversion based on ORIGIN DATA")
            .append("\n  (" + LIST_PROCESSING + ")\t\t\tprepare history conversion based on SOURCE FILE")
            .append("\n  (" + IMAGE_PROCESSING + ")\t\t\tprocess IMAGE files based on extension stated")
            .append("\n  (" + VIDEO_PROCESSING + ")\t\t\tprocess VIDEO files based on extension stated")
            .append("\n")
            .append("\n  ").append(helpFlags).append("\t\t\tdisplay this help guide");

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
