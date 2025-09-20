package org.hellstrand.renfi.util;

import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_LOGGING_BORDERS;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_LOGGING_INPUT;
import static org.hellstrand.renfi.constant.Constants.MESSAGE_PROCESSING_LOGGING_OUTPUT;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author (Mats Richard Hellstrand)
 * @version (20th of September, 2025)
 */
public class LoggingUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);
    private static final Map<String, List<String>> logs = Collections.synchronizedMap(new HashMap<>());
    private static PrintWriter printWriter;

    public LoggingUtil(String outputResource) throws FileNotFoundException {
        printWriter = new PrintWriter(new FileOutputStream(outputResource), true);
    }

    public static String formatMessage(String messageTemplate, Object... messageParameters) {
        return format(messageTemplate.replace("{}", "%s"), messageParameters);
    }

    public void log(String key, String value) {
        logger.info(MESSAGE_PROCESSING_LOGGING_INPUT, key, value);
        if (!logs.containsKey(key)) {
            logs.put(key, new ArrayList<>());
        }
        logs.get(key).add(value);
    }

    public void write() {
        logger.info(MESSAGE_PROCESSING_LOGGING_OUTPUT);
        for (Map.Entry<String, List<String>> entry : logs.entrySet()) {
            printWriter.println(formatMessage(MESSAGE_PROCESSING_LOGGING_BORDERS, entry.getKey()));
            for (String entryRow : entry.getValue()) {
                printWriter.println(entryRow);
            }
        }
        printWriter.close();
    }
}
