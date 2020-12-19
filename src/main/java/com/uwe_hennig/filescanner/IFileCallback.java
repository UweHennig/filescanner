/**
 * @(#)IFileCallback.java
 * Copyright (c) 2018 Uwe Hennig
 * All rights reserved.
 */
package com.uwe_hennig.filescanner;

/**
 * IFileCallback
 * @author Uwe Hennig
 */
@FunctionalInterface
public interface IFileCallback {
	void callback(File file); 
}
