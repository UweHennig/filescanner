/**
 * @(#)TestFileScanner.java
 * Copyright (c) 2018 Uwe Hennig
 * All rights reserved.
 */
package com.uwe_hennig.filescanner;

import java.io.File;

import org.junit.Test;

/**
 * TestFileScanner
 * @author Uwe Hennig
 */
public class TestFileScanner {
	
	public void print(File f) {
		System.out.println("file: " + f.getAbsolutePath());
	}
	
	@Test
	public void testScan() {
		File file = new File("D:\\dev\\eclipse-workspace\\github\\filescanner");
		FileScanner scanner = new FileScanner(file);
		scanner.scan(f -> print(f), f -> f.getAbsolutePath().endsWith(".java"));
	}
}
