package me.momocow.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtils {
	public static File getJarFile (Class<?> clazz) throws URISyntaxException {
		return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
	}
	
	public static List<String> listFiles (File jar, String resourceDir) throws IOException {
		List<String> files = new ArrayList<>();
		try (JarFile jarfile = new JarFile(jar)) {
			Enumeration<JarEntry> entries = jarfile.entries();
			while (entries.hasMoreElements()) {
				String entryName = entries.nextElement().getName();
				if (entryName.startsWith(resourceDir) && !entryName.endsWith("/")) {
					files.add(entryName);
				}
			}
		} catch (IOException e) {
			throw e;
		}
		
		return files;
	}
}
