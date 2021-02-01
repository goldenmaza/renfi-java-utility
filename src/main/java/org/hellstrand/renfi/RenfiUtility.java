package org.hellstrand.renfi;

import org.hellstrand.renfi.util.FileProcessingUtil;
import org.hellstrand.renfi.util.ImageProcessingUtil;
import org.hellstrand.renfi.util.VideoProcessingUtil;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.hellstrand.renfi.util.Constants.BRANCH_INDEX;
import static org.hellstrand.renfi.util.Constants.COMMAND_INDEX;
import static org.hellstrand.renfi.util.Constants.DIRECTORY_INDEX;
import static org.hellstrand.renfi.util.Constants.EXTENSION_INDEX;
import static org.hellstrand.renfi.util.Constants.FAILURE;
import static org.hellstrand.renfi.util.Constants.FILE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.IMAGE_PROCESSING;
import static org.hellstrand.renfi.util.Constants.LABEL_CREATED;
import static org.hellstrand.renfi.util.Constants.LABEL_FILE;
import static org.hellstrand.renfi.util.Constants.LABEL_FILENAMES;
import static org.hellstrand.renfi.util.Constants.LABEL_IMAGES;
import static org.hellstrand.renfi.util.Constants.LABEL_NEVER_REACHED;
import static org.hellstrand.renfi.util.Constants.LABEL_VIDEOS;
import static org.hellstrand.renfi.util.Constants.LIST_PROCESSING;
import static org.hellstrand.renfi.util.Constants.MESSAGE_CONTINUE_RENAMING;
import static org.hellstrand.renfi.util.Constants.MESSAGE_DIRECTORY_UNAVAILABLE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_FAILED_MISMATCH;
import static org.hellstrand.renfi.util.Constants.MESSAGE_INVALID_USE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_LOADING_DIRECTORY;
import static org.hellstrand.renfi.util.Constants.MESSAGE_LOADING_FILES;
import static org.hellstrand.renfi.util.Constants.MESSAGE_PROCESSING_TASK;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RENAMING_ABORT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RENAMING_PROCESS;
import static org.hellstrand.renfi.util.Constants.MESSAGE_RESOURCES_UNAVAILABLE;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_ABORT;
import static org.hellstrand.renfi.util.Constants.MESSAGE_UNDO_CONTINUE;
import static org.hellstrand.renfi.util.Constants.NAMES_SOURCE;
import static org.hellstrand.renfi.util.Constants.ORIGIN_PROCESSING;
import static org.hellstrand.renfi.util.Constants.PROCESSING_SUPPORT;
import static org.hellstrand.renfi.util.Constants.SUCCESSFUL;
import static org.hellstrand.renfi.util.Constants.VIDEO_PROCESSING;

/**
 * @author (Mats Richard Hellstrand)
 * @version (1st of February, 2021)
 *
 * Example command: java -jar Renfi.jar -l -v 1 /c/directory/
 */
public final class RenfiUtility {
	public static void main(String[] args) {
		if (args.length == 0) { // TODO: Refactor to include flags...
			System.out.println(MESSAGE_INVALID_USE);
			System.exit(FAILURE);
		}

		try {
			// "Determine" the flow of the application...
			String branch = args[BRANCH_INDEX];
			String command = args[COMMAND_INDEX];
			int index = Integer.parseInt(args[EXTENSION_INDEX]);
			String extension = PROCESSING_SUPPORT.get(command).get(index);
			String branchTask =
				branch.equals(ORIGIN_PROCESSING) ? LABEL_CREATED :
					branch.equals(LIST_PROCESSING) ? LABEL_FILE :
						branch.equals(FILE_PROCESSING) ? LABEL_FILENAMES :
								LABEL_NEVER_REACHED;
			String commandTask = command.equals(IMAGE_PROCESSING) ? LABEL_IMAGES : LABEL_VIDEOS;
			System.out.printf(MESSAGE_PROCESSING_TASK, branchTask, commandTask, extension);

			// Verify that the target directory exist...
			System.out.println(MESSAGE_LOADING_DIRECTORY);
			String directory = args[DIRECTORY_INDEX];
			File path = new File(directory);
			if (!path.exists() && !path.isDirectory()) {
				System.out.println(MESSAGE_DIRECTORY_UNAVAILABLE);
				System.exit(FAILURE);
			}

			// Load the files into memory under the target directory...
			System.out.println(MESSAGE_LOADING_FILES);
			File[] files = path.listFiles((dir, name) -> name.toLowerCase().endsWith(extension));
			if (files != null) {
				System.out.println(Arrays.toString(files));
			} else {
				System.out.println(MESSAGE_RESOURCES_UNAVAILABLE);
				System.exit(FAILURE);
			}

			String target = directory + NAMES_SOURCE;
			if (branch.equals(FILE_PROCESSING)) { // Prepare a source file based on directory files...
				PrintWriter printWriter = new PrintWriter(target);
				for (File file : files) {
					printWriter.println(file.getName());
				}
				printWriter.close();
			} else { // Otherwise, prepare and process conversion...
				Map<String, String> history = new HashMap<>();
				if (branch.equals(ORIGIN_PROCESSING)) { // Prepare conversion history based on origin data...
					if (command.equals(VIDEO_PROCESSING)) {
						VideoProcessingUtil.prepareHistoryProcess(files, history, extension);
					} else if (command.equals(IMAGE_PROCESSING)) {
						ImageProcessingUtil.prepareHistoryProcess(files, history, extension);
					} else { // TODO: Refactor to confirm in the beginning of application...
						System.out.println(MESSAGE_INVALID_USE);
						System.exit(FAILURE);
					}
				} else if (branch.equals(LIST_PROCESSING)) { // Prepare conversion history based on file input...
					if (command.equals(VIDEO_PROCESSING)) {
						VideoProcessingUtil.prepareHistoryProcess(files, history, target, extension);
					} else if (command.equals(IMAGE_PROCESSING)) {
						ImageProcessingUtil.prepareHistoryProcess(files, history, target, extension);
					} else { // TODO: Refactor to confirm in the beginning of application...
						System.out.println(MESSAGE_INVALID_USE);
						System.exit(FAILURE);
					}
				} else { // TODO: Refactor to confirm in the beginning of application...
					System.out.println(MESSAGE_INVALID_USE);
					System.exit(FAILURE);
				}

				// Display available conversion history...
				for (Map.Entry<String, String> entry : history.entrySet()) {
					System.out.println("Entry: " + entry.getKey() + ": " + entry.getValue());
				}

				// Begin the renaming process...
				System.out.println(MESSAGE_CONTINUE_RENAMING);
				Scanner scanner = new Scanner(System.in);
				String key = scanner.nextLine();
				if (key.equals("y")) { // Should the renaming process be executed?
					System.out.println(MESSAGE_RENAMING_PROCESS);
					if (history.size() > 0) {
						FileProcessingUtil.renamingProcess(history, files, directory);

						System.out.println(MESSAGE_UNDO_CONTINUE);
						key = scanner.nextLine();
						if (key.equals("y")) { // Should the undo process be executed?
							FileProcessingUtil.renamingUndoProcess(history, path, directory);
						} else {
							System.out.println(MESSAGE_UNDO_ABORT);
						}
					} else {
						System.out.println(MESSAGE_FAILED_MISMATCH);
					}
				} else {
					System.out.println(MESSAGE_RENAMING_ABORT);
				}
				scanner.close();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(FAILURE);
		}
		System.exit(SUCCESSFUL);
	}
}
