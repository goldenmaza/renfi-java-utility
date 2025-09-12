package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.Constants.BRANCH_FLAGS;
import static org.hellstrand.renfi.constant.Constants.COMPARE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CONVERT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.CREATION_TIME_FLAG;
import static org.hellstrand.renfi.constant.Constants.CROP_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DATA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.DETECT_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.FILE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.JAVA_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LAST_ACCESS_TIME_FLAG;
import static org.hellstrand.renfi.constant.Constants.LAST_MODIFIED_TIME_FLAG;
import static org.hellstrand.renfi.constant.Constants.RESOURCE_FLAGS;
import static org.hellstrand.renfi.constant.Constants.FLOW_FLAGS;
import static org.hellstrand.renfi.constant.Constants.HELP_FLAGS;
import static org.hellstrand.renfi.constant.Constants.IMAGE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.LIST_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.PROCESSING_SUPPORT;
import static org.hellstrand.renfi.constant.Constants.SOURCE_PROCESSING;
import static org.hellstrand.renfi.constant.Constants.TYPE_FLAGS;
import static org.hellstrand.renfi.constant.Constants.VIDEO_PROCESSING;

import java.util.List;

/**
 * @author (Mats Richard Hellstrand)
 * @version (12th of September, 2025)
 */
public final class HelpGuideUtil {
    private HelpGuideUtil() {}

    public static void displayHelpGuide() {
        List<String> imageExtensions = PROCESSING_SUPPORT.get(IMAGE_PROCESSING);
        List<String> videoExtensions = PROCESSING_SUPPORT.get(VIDEO_PROCESSING);
        String helpFlags = HELP_FLAGS.toString()
            .replace("[", "(")
            .replace("]", ")");

        String guideDisplayed = """
            === Help Guide ===

            \tOperational overview:

            \tjava -jar Renfi.jar <FLOW> <BRANCH> <PATH> <RESOURCE_TYPE> <FROM_EXTENSION> <TO_EXTENSION> <X_AXIS> <Y_AXIS> <DATE_TYPE>

            \t(FLOW)\t\t\t\tThe desired flow of the application:\040""" + FLOW_FLAGS + """
            \n\t(BRANCH)\t\t\tThe desired branch of the application:\040""" + BRANCH_FLAGS + """
            \n\t(PATH)\t\t\t\tThe directory path to where the files are located, e.g. "c:/directory/" OR "c/directory/"
            \t(RESOURCE_TYPE)\t\tTo focus on either resource type:\040""" + RESOURCE_FLAGS + """
            \n\t(FROM_EXTENSION)\tThe desired file extension based on index (INTEGER). (view 'Extension legend' below)
            \t(TO_EXTENSION)\t\tThe desired file extension based on index (INTEGER). (view 'Extension legend' below)
            \t(X_AXIS)\t\t\tThe starting pixel on the X axis (INTEGER).
            \t(Y_AXIS)\t\t\tThe starting pixel on the Y axis (INTEGER).
            \t(DATE_TYPE)\t\t\tThe desired date type flag from file:\040""" + TYPE_FLAGS + """


            \n\tExtension legend:

            \t(IMAGE)\t\t\t\tThe supported extensions and their indexes:\040""" + printValues(imageExtensions) + """
            \n\t(VIDEO)\t\t\t\tThe supported extensions and their indexes *:\040""" + printValues(videoExtensions) + """
            \n\t\t\t\t\t\t* Not ALL video formats are supported by Drew Noakes's extractor, some might need Java +7 (nio) to fetch date...

            \tFlag legend:

            \t(""" + IMAGE_PROCESSING + """
            )\t\t\t\tTo process IMAGE files based on extension stated.
            \t(""" + VIDEO_PROCESSING + """
            )\t\t\t\tTo process VIDEO files based on extension stated.

            \t(""" + FILE_PROCESSING + """
            )\t\t\t\tThe File processing flow lets us manipulate the file itself or to analyze it. You can choose one of the following branches:
            \t(""" + COMPARE_PROCESSING + """
            )\t\t\t\tTo compare files, pixel by pixel, to determine if they are a duplicate or a close match.
            \t(""" + CROP_PROCESSING + """
            )\t\t\t\tTo crop images, based on X & Y coordinates, this will remove the borders on both sides of the image.
            \t(""" + CONVERT_PROCESSING + """
            )\t\t\t\tTo convert images from one extension to another.
            \t(""" + DETECT_PROCESSING + """
            )\t\t\t\tTo detect black borders and to sort them into folders based on detected height. Note: Currently, only top-down is supported!
            \t(""" + SOURCE_PROCESSING + """
            )\t\t\t\tTo prepare a SOURCE FILE based on directory files.

            \t(""" + DATA_PROCESSING + """
            )\t\t\t\tThe Data processing flow lets us create a source file or determine date and time. You can choose one of the following branches:
            \t(""" + JAVA_PROCESSING + """
            )\t\t\t\tTo use Java +7 (nio) to determine the date of creation.
            \t(""" + ORIGIN_PROCESSING + """
            )\t\t\t\tTo prepare history conversion based on ORIGIN DATA by using Drew Noakes's extractor.
            \t(""" + LIST_PROCESSING + """
            )\t\t\t\tTo prepare history conversion based on SOURCE FILE.

            \t(""" + CREATION_TIME_FLAG + """
            )\t\t\t\tTo use the Creation Time field for setting the date and time.
            \t(""" + LAST_MODIFIED_TIME_FLAG + """
            )\t\t\t\tTo use the Last Modified Time field for setting the date and time.
            \t(""" + LAST_ACCESS_TIME_FLAG + """
            )\t\t\t\tTo use the Last Access Time field for setting the date and time.

            \t""" + helpFlags + """
            \tTo display this help guide.""";

        System.out.println(guideDisplayed);
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
}
