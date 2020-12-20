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
import java.io.FileFilter;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		System.out.println("callback testJavaFiles file: " + f.getAbsolutePath());
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
	
	@Test
	public void testScanJavaNonRecursive() {
		File file = new File(getPath());
		FileScanner scanner = new FileScanner(file);
		scanner.setRecursive(false);
		scanner.scan(f -> testJavaFiles(f), f -> f.getAbsolutePath().endsWith(".java"));
		assertEquals("Invalid numer of java files!", 0, counter);
	}
	
	public void testDirectory(File f) {
		System.out.println("callback testDirectory file: " + f.getAbsolutePath());
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
		System.out.println("callback testEmpty file: " + f.getAbsolutePath());
		fail("Callback called! file " + f.getName());
	}
	
	@Test
	public void testEmptyScan() {
		File file = new File(getPath());
		FileScanner scanner = new FileScanner(file);
		scanner.scan(f -> testEmpty(f), f -> false);
	}
	
	public void testMulti(File f) {
		System.out.println("callback testMulti file: " + f.getAbsolutePath());
		if(f.getAbsolutePath().contains("main")) {
			if (!f.getAbsolutePath().contains("java")) {
				fail("No java file in main!");
			}
		} else {
			if (!f.isDirectory()) {
				fail("File is not a directory in test");
			}
		}
	}
	
	@Test
	public void multiScan() {
		try {
			File fileStartJava = new File(getPath("/src/main"));
			File fileStartTest  = new File(getPath("/src/test"));
			
			FileScanner scannerJava = new FileScanner(fileStartJava);
			FileScanner scannerTest = new FileScanner(fileStartTest);
			
			ExecutorService executorService = Executors.newCachedThreadPool();
			Callable<Object> runnableJava = Executors.callable(new Runnable() {
				@Override
				public void run() {
					scannerJava.scan(f -> testMulti(f), f -> f.getAbsolutePath().endsWith(".java"));
				}
			});
			
			Callable<Object> runnableTest = Executors.callable(new Runnable() {
				@Override
				public void run() {
					scannerTest.scan(f -> testMulti(f), f -> f.isDirectory());
				}
			});
			executorService.invokeAll(Arrays.asList(runnableJava, runnableTest));
		} catch (InterruptedException e) {
			fail("exception thrown in methoe 'multiScan' " + e.getMessage());
		}
	}

	public void testNotRecursiveDirAccepted(File f) {
		System.out.println("callback testNotRecursiveDirAccepted file: " + f.getAbsolutePath());
		counter++;
	}
	
	@Test
	public void notRecuriveDirAccepted() {
		FileFilter fdir = f -> f.isDirectory();
		
		File file = new File(getPath());
		FileScanner scanner = new FileScanner(file);
		scanner.setRecursive(false);
		scanner.scan(f -> testNotRecursiveDirAccepted(f), fdir);
		assertTrue("Invalid numer of files!", counter > 0);
	}
	
	public void fileInCurrentDirectory(File f) {
		System.out.println("callback fileInCurrentDirectory file: " + f.getAbsolutePath());
		counter++;
	}
	
	@Test
	public void currentDirOnly() {
		FileFilter fdir = f -> !f.isDirectory();
		File file = new File(getPath("/src/main/java/com/uwe_hennig/filescanner"));
		
		FileScanner scanner = new FileScanner(file);
		scanner.setRecursive(false);
		scanner.scan(f -> fileInCurrentDirectory(f), fdir);
		assertEquals("Invalid numer of files!", 2, counter);
		
		scanner.setRecursive(true);
		scanner.scan(f -> fileInCurrentDirectory(f), fdir);
		assertEquals("Invalid numer of files!", 2, counter);
	}
	
	private String getPath() {
		return getPath(null);
	}
	
	private String getPath(String sub) {
		String path = TestFileScanner.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		int pos = path.lastIndexOf("filescanner") + "filescanner".length();
		path = path.substring(0, pos);
		if (sub != null) {
			path = path + sub;
		}
		System.out.println("start path: " + path);
		return path;
	}
}
