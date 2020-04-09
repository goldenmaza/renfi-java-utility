package org.hellstrand.renfi;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class RenameFilesUtility {
	public static void main(String[] args) {
		try {
			System.out.println(" === Loading directory ===");
			File dir = new File(args[1]);
			List<String> names = new ArrayList<>();

			System.out.println(" === Loading files ===");
			File[] files = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".mp4");
				}
			});
			for (File f : files) {
				System.out.println(f.getName());
			}

			System.out.println(" === Sorting files ===");
			Arrays.sort(files, Comparator.comparingLong(File::lastModified));
			for (File f : files) {
				System.out.println(f.getName());
			}

			System.out.println(" === New names loaded ===");
			File name = new File(args[1] + "names.txt");
			Scanner s = new Scanner(name);
			while (s.hasNextLine()) {
				names.add(s.nextLine() + ".mp4");
			}
			s.close();

			System.out.println(" === Do you want to continue with the renaming process? (y/n) ===");
			s = new Scanner(System.in);
			String key = s.nextLine();
			if (key.equals("y")) {
				System.out.println(" === Files renamed, step 2 ===");
				if (files.length == names.size()) {
					for (int i = 0; i < files.length; i++) {
						files[i].renameTo(new File(args[1] + names.get(i)));
						System.out.println("[" + files[i].getName() + "] renamed to: [" + names.get(i) + "]");
					}

					System.out.println(" === Do you want to undo the renaming process? (y/n) ===");
					key = s.nextLine();
					if (key.equals("y")) {
						System.out.println(" === Reloading undo files ===");
						File[] undo = dir.listFiles(new FilenameFilter() {
							public boolean accept(File dir, String name) {
								return name.toLowerCase().endsWith(".mp4");
							}
						});
						Arrays.sort(undo, Comparator.comparingLong(File::lastModified));
						for (File f : undo) {
							System.out.println(f.getName());
						}
						System.out.println(" === Files renamed, undo step ===");
						for (int i = 0; i < undo.length; i++) {
							undo[i].renameTo(new File(args[1] + files[i].getName()));
							System.out.println("[" + undo[i].getName() + "] renamed to: [" + files[i].getName() + "]");
						}
					} else {			
						System.out.println("Files are not restored...");
					}
				} else {			
					System.out.println("Utility failed to process files...");
				}
			} else {			
				System.out.println("Files are not renamed...");
			}
			s.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
