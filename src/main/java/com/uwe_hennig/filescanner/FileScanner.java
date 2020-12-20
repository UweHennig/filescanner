/**
 * @(#)FileScanner.java
 * Copyright (c) 2020 Uwe Hennig
 * All rights reserved.
 */
package com.uwe_hennig.filescanner;

import java.io.File;
import java.io.FileFilter;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Stack;

/**
 * FileScanner
 * @author Uwe Hennig
 */
public class FileScanner {
	private Stack<File> fileStack = new Stack<>();
	
	public FileScanner(File startDir) {
		if (startDir == null || !startDir.isDirectory()) {
			throw new InvalidParameterException("Start file is not a directory or null!");
		}
		fileStack.push(startDir);
	}
	
	public void scan(final IFileCallback callback) {
		scan(callback, f -> true);
	}
	
	public void scan(final IFileCallback callback, final FileFilter filter) {
		if (callback == null) {
			throw new InvalidParameterException("Callback parameter is null!");
		}
		
		if (filter == null) {
			throw new InvalidParameterException("Callback filter is null!");
		}
		
		FileFilter combinedFilter = f -> f.isDirectory() || filter.accept(f); 
		
		while(!fileStack.isEmpty()) {
			File currentFile = fileStack.pop();
			if (filter.accept(currentFile)) {
				callback.callback(currentFile);
			}
			if (currentFile.isDirectory()) {
				File[] files = currentFile.listFiles(combinedFilter);
				Arrays.asList(files).stream().forEach(f -> fileStack.push(f));
			}
		}
	}	
}
