/**
 * @(#)FileScanner.java
 * Copyright (c) 2020 Uwe Hennig
 * All rights reserved.
 */
package com.uwe_hennig.filescanner;

import java.io.File;
import java.io.FileFilter;
import java.security.InvalidParameterException;

/**
 * FileScanner
 * @author Uwe Hennig
 */
public class FileScanner {
	private File startDir;
	
	public FileScanner(File startDir) {
		this.startDir = startDir;
		if (startDir == null || !startDir.isDirectory()) {
			throw new InvalidParameterException("Start file is not a directory or null!");
		}
	}
	
	public void scan(IFileCallback callback, FileFilter filter) {
		
	}
	
	public File getStartDir() {
		return startDir;
	}
}
