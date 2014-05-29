package com.akdeniz.jdependency;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.objectweb.asm.ClassReader;

/**
 * 
 * @author akdeniz
 */

public class Parser {

	private String directory;
	private String outputFile;
	private List<Pattern> classIncludePatterns = new ArrayList<>();
	private List<Pattern> classExcludePatterns = new ArrayList<>();

	public Parser(String directory, String outputFile, List<String> classIncludePatterns, List<String> classExcludePatterns) {
		this.directory = directory;
		this.outputFile = outputFile;

		if (classIncludePatterns != null) {
			for (String string : classIncludePatterns) {
				this.classIncludePatterns.add(Pattern.compile(string));
			}
		}

		if (classExcludePatterns != null) {
			for (String string : classExcludePatterns) {
				this.classExcludePatterns.add(Pattern.compile(string));
			}
		}
	}

	public void parse() throws Exception {

		List<File> modules = findModules(directory);

		Map<String, Map<String, Set<String>>> jarGroups = new Hashtable<>();
		Map<String, Set<String>> packagesUsedInModule = new Hashtable<>();

		for (File module : modules) {
			DependencyVisitor visitor = new DependencyVisitor();
			try (ZipFile zipFile = new ZipFile(module)) {
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					if (entry.getName().endsWith(".class") && filterClassName(entry.getName())) {
						new ClassReader(zipFile.getInputStream(entry)).accept(visitor, 0);
					}
				}
			}
			Map<String, Set<String>> groups = visitor.getGroups();
			if (groups != null && groups.size() > 0) {
				jarGroups.put(module.getName(), groups);
				packagesUsedInModule.put(module.getName(), visitor.getPackages());
			}
		}

		Set<String> keySet = jarGroups.keySet();
		String[] moduleNames = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(moduleNames);

		@SuppressWarnings("unchecked")
		Set<String>[][] dependencies = new HashSet[moduleNames.length][moduleNames.length];

		System.out.println("Module count : " + moduleNames.length);
		for (int i = 0; i < moduleNames.length; i++) {
			Set<String> usedPackages = packagesUsedInModule.get(moduleNames[i]);
			for (int j = 0; j < moduleNames.length; j++) {
				Set<String> implementedPackages = jarGroups.get(moduleNames[j]).keySet();
				for (String pack : implementedPackages) {
					if (pack != null && usedPackages.contains(pack)) {
						if (dependencies[i][j] == null) {
							dependencies[i][j] = new HashSet<String>();
						}
						dependencies[i][j].add(pack);
					}
				}
			}
		}

		copyResourceFiles();

		HtmlBuilder.buildHtml(outputFile, moduleNames, dependencies);
	}

	private void copyResourceFiles() throws IOException {

		String parent = new File(outputFile).getAbsoluteFile().getParent();
		File jsDirectory = new File(parent + "/resources");
		if (!jsDirectory.exists()) {
			jsDirectory.mkdir();
		}

		try (InputStream inputStream = Parser.class.getClassLoader().getResourceAsStream("jdependency.js");
				OutputStream outputStream = new FileOutputStream(parent + "/resources/jdependency.js")) {
			copy(inputStream, outputStream);
		}

		try (InputStream inputStream = Parser.class.getClassLoader().getResourceAsStream("jquery.min.js");
				OutputStream outputStream = new FileOutputStream(parent + "/resources/jquery.min.js")) {
			copy(inputStream, outputStream);
		}

		try (InputStream inputStream = Parser.class.getClassLoader().getResourceAsStream("style.css");
				OutputStream outputStream = new FileOutputStream(parent + "/resources/style.css")) {
			copy(inputStream, outputStream);
		}

	}

	private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		byte[] buffer = new byte[4096];
		int readed;
		while ((readed = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readed);
		}

	}

	private boolean filterClassName(String className) {
		for (Pattern pattern : classIncludePatterns) {
			if (pattern.matcher(className).matches()) {
				return true;
			}
		}

		for (Pattern pattern : classExcludePatterns) {
			if (pattern.matcher(className).matches()) {
				return false;
			}
		}

		return true;
	}

	private List<File> findModules(String directory) {
		Queue<File> folders = new LinkedList<>();
		folders.add(new File(directory));

		ArrayList<File> modules = new ArrayList<File>();
		while (!folders.isEmpty()) {
			File file = folders.remove();
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					folders.add(f);
				}
			} else if (file.getName().endsWith(".jar")) {
				modules.add(file);
			}
		}
		return modules;
	}
}
