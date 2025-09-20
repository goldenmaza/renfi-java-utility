package org.hellstrand.renfi.util;

import static java.lang.String.format;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        if (!logs.containsKey(key)) {
            logs.put(key, new ArrayList<>());
        }
        logs.get(key).add(value);
    }

    public void write() {
        for (Map.Entry<String, List<String>> entry : logs.entrySet()) {
            for (String entryRow : entry.getValue()) {
                printWriter.println(entryRow);
            }
        }
        printWriter.close();
    }
}
