/**
 * @(#)TestFileScanner.java
 * Copyright (c) 2018 Uwe Hennig
 * All rights reserved.
 */
package com.uwe_hennig.filescanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/**
 * TestFileScanner
 * @author Uwe Hennig
 */
public class TestFileScanner {
	private int counter;
	
	@Before
	public void init() {
		counter = 0;
	}
	
	public void testJavaFiles(File f) {
		System.out.println("file: " + f.getAbsolutePath());
		assertTrue("Project path not found!", f.getAbsolutePath().contains("filescanner"));
		assertTrue("Not java!", f.getAbsolutePath().contains(".java"));
		counter++;
	}
	
	@Test
	public void testScanJava() {
		File file = new File(getPath());
		FileScanner scanner = new FileScanner(file);
		scanner.scan(f -> testJavaFiles(f), f -> f.getAbsolutePath().endsWith(".java"));
		assertEquals("Invalid numer of java files!", 3, counter);
	}
	
	public void testDirectory(File f) {
		System.out.println("file: " + f.getAbsolutePath());
		assertTrue("File is not a directory!", f.isDirectory());
		counter++;
	}
	
	@Test
	public void testScanDirectory() {
		File file = new File(getPath());
		FileScanner scanner = new FileScanner(file);
		scanner.scan(f -> testDirectory(f), f -> f.isDirectory());
		assertTrue("Invalid numer of directories!", counter >= 4);
	}
	
	public void testEmpty(File f) {
		fail("found file " + f.getName());
	}
	
	@Test
	public void testEmptyScan() {
		File file = new File(getPath());
		FileScanner scanner = new FileScanner(file);
		scanner.scan(f -> testEmpty(f), f -> false);
	}
	
	private String getPath() {
		String path = TestFileScanner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		int pos = path.lastIndexOf("filescanner") + "filescanner".length();
		path = path.substring(0, pos);
		System.out.println("start path: " + path);
		return path;
	}
}
