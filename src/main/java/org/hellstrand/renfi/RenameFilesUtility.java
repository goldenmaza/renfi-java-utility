package org.hellstrand.renfi;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RenameFilesUtility {
	private static final int DIR_INDEX = 0;
	private static final int COMMAND_INDEX = 1;
	private static final int EXTENSION_INDEX = 2;
	private static final String BABIES_COMMAND = "-b";
	private static final String SERIES_COMMAND = "-s";
	private static final Map<String, List<String>> COMMANDS = Map.ofEntries(
		new AbstractMap.SimpleEntry<String, List<String>>(BABIES_COMMAND, new ArrayList<>(List.of(".jpg"))),
		new AbstractMap.SimpleEntry<String, List<String>>(SERIES_COMMAND, new ArrayList<>(List.of(".mp4")))
	);

	public static void main(String[] args) {
		try {
			String command = args[COMMAND_INDEX];
			int extension = Integer.parseInt(args[EXTENSION_INDEX]);

			System.out.println(" === Loading directory ===");
			File dir = new File(args[DIR_INDEX]);

			System.out.println(" === Loading files ===");
			File[] files = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(COMMANDS.get(command).get(extension));
				}
			});
			for (File f : files) {
				System.out.println(f.getName());
			}

			System.out.println(" === Sorting files ===");
			// Arrays.sort(files, Comparator.comparingLong(File::lastModified));
			// for (File f : files) {
			// 	System.out.println(f.getName());
			// }

			Scanner s = null;
			List<String> names = new ArrayList<>();
			if (command.equals(BABIES_COMMAND)) {
			} else if (command.equals(SERIES_COMMAND)) {
				System.out.println(" === New names loaded ===");
				File name = new File(args[DIR_INDEX] + "names.txt");
				s = new Scanner(name);
				while (s.hasNextLine()) {
					names.add(s.nextLine() + extension);
				}
				s.close();
			} else {
				throw new Exception(" === Invalid command used! ===");
			}

			System.out.println(" === Do you want to continue with the renaming process? (y/n) ===");
			s = new Scanner(System.in);
			String key = s.nextLine();
			if (key.equals("y")) {
				System.out.println(" === Files renamed, step 2 ===");
				if (files.length == names.size()) {
					for (int i = 0; i < files.length; i++) {
						files[i].renameTo(new File(args[DIR_INDEX] + names.get(i)));
						System.out.println(files[i].getName() + " was renamed to " + names.get(i));
					}

					System.out.println(" === Do you want to undo the renaming process? (y/n) ===");
					key = s.nextLine();
					if (key.equals("y")) {
						System.out.println(" === Reloading undo files ===");
						File[] undo = dir.listFiles(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.toLowerCase().endsWith(COMMANDS.get(command).get(extension));
							}
						});

						if (undo.length == files.length) {
							Arrays.sort(undo, Comparator.comparingLong(File::lastModified));
							for (File f : undo) {
								System.out.println(f.getName());
							}

							System.out.println(" === Files are being restored, undo step ===");
							for (int i = 0; i < undo.length; i++) {
								undo[i].renameTo(new File(args[DIR_INDEX] + files[i].getName()));
								System.out.println("[" + undo[i].getName() + "] renamed to: [" + files[i].getName() + "]");
							}
						} else {
							System.out.println(" === Utility failed to process files... The amount of reloaded undo files and original files do not match... ===");
						}
					} else {			
						System.out.println(" === You chose not to undo the last renaming process, aborting the undo process... ===");
					}
				} else {			
					System.out.println(" === Utility failed to process files... The amount of generated names and amount of files do not match... ===");
				}
			} else {			
				System.out.println(" === You chose not to rename files, aborting the renaming process... ===");
			}
			s.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
