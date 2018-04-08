package com.programtom.generatestart;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GenerateStart {

	public static void main(String[] args) throws IOException {

		if (args == null || args.length == 0) {
			System.out.println("Type the file name of the to-be-executed jar");
			return;
		}
		String path = GenerateStart.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");

		// search for jars
		List<String> jars = new ArrayList<String>();
		File root = new File(System.getProperty("user.dir"));
		find(jars, root, root);

		// exclude current jar
		String name = new File(decodedPath).getName();
		jars.remove(name);

		// exclude target (java -jar jar);
		String string = args[0];
		jars.remove(string);

		// write java -jar -class path at file

		for (String string2 : jars) {
			System.out.println(string2);
		}

		StringBuffer sb = new StringBuffer();
		sb.append("java -jar ").append(string);

		if (jars.size() > 0) {
			sb.append(" -cp ");
			for (String string2 : jars) {
				sb.append("\"").append(string2).append("\":");
			}
			sb.deleteCharAt(sb.length()-1);
		}

		Files.write(Paths.get(new File("start.sh").toURI()), sb.toString().getBytes());
		Files.write(Paths.get(new File("start.bat").toURI()), sb.toString().getBytes());
	}

	private static void find(List<String> jars, File root, File currentDir) {
		File[] list = currentDir.listFiles();

		if (list != null) {
			for (File file : list) {
				if (file.isDirectory()) {
					find(jars, root, file);
				} else {
					if (file.getName().toLowerCase().endsWith(".jar")) {
						jars.add(file.getAbsolutePath().replace(root.getAbsolutePath() + File.separator, ""));
					}
				}
			}
		}
	}
}
